package com.dezenbit.habitos.ui.screens.addedit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dezenbit.habitos.data.Habit
import com.dezenbit.habitos.data.HabitRepository
import com.dezenbit.habitos.data.HabitType
import com.dezenbit.habitos.notifications.ReminderScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

data class AddEditFormState(
    val habitId: Long = -1L,
    val name: String = "",
    val emoji: String = "\u2B50",
    val colorHex: String = "#6750A4",
    val selectedDays: Set<Int> = (1..7).toSet(),
    val reminderEnabled: Boolean = false,
    val reminderHour: Int = 8,
    val reminderMinute: Int = 0,
    val category: String = "",
    val type: HabitType = HabitType.BOOLEAN,
    val targetValue: String = "8",
    val unit: String = "",
    val isEditing: Boolean = false,
    val isLoading: Boolean = true,
    val formError: String? = null,
    val saved: Boolean = false
)

class AddEditHabitViewModel(
    application: Application,
    private val repository: HabitRepository,
    private val habitId: Long
) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(AddEditFormState(isEditing = habitId > 0))
    val state: StateFlow<AddEditFormState> = _state.asStateFlow()

    init {
        if (habitId > 0) {
            viewModelScope.launch {
                repository.getHabitOnce(habitId)?.let { habit ->
                    _state.value = AddEditFormState(
                        habitId = habit.id,
                        name = habit.name,
                        emoji = habit.emoji,
                        colorHex = habit.colorHex,
                        selectedDays = habit.daysSet(),
                        reminderEnabled = habit.reminderEnabled,
                        reminderHour = if (habit.reminderHour >= 0) habit.reminderHour else 8,
                        reminderMinute = if (habit.reminderMinute >= 0) habit.reminderMinute else 0,
                        category = habit.category,
                        type = habit.type,
                        targetValue = if (habit.targetValue == habit.targetValue.toLong().toDouble()) {
                            habit.targetValue.toLong().toString()
                        } else habit.targetValue.toString(),
                        unit = habit.unit,
                        isEditing = true,
                        isLoading = false
                    )
                } ?: run { _state.value = _state.value.copy(isLoading = false) }
            }
        } else {
            _state.value = _state.value.copy(isLoading = false)
        }
    }

    fun onNameChange(value: String) {
        _state.value = _state.value.copy(name = value, formError = null)
    }

    fun onEmojiChange(value: String) {
        _state.value = _state.value.copy(emoji = value)
    }

    fun onColorChange(hex: String) {
        _state.value = _state.value.copy(colorHex = hex)
    }

    fun onCategoryChange(value: String) {
        _state.value = _state.value.copy(category = value)
    }

    fun onTypeChange(type: HabitType) {
        _state.value = _state.value.copy(type = type)
    }

    fun onTargetValueChange(value: String) {
        _state.value = _state.value.copy(targetValue = value)
    }

    fun onUnitChange(value: String) {
        _state.value = _state.value.copy(unit = value)
    }

    fun toggleDay(isoDay: Int) {
        val current = _state.value.selectedDays
        val updated = if (current.contains(isoDay)) current - isoDay else current + isoDay
        _state.value = _state.value.copy(selectedDays = updated)
    }

    fun onReminderEnabledChange(enabled: Boolean) {
        _state.value = _state.value.copy(reminderEnabled = enabled)
    }

    fun onReminderTimeChange(hour: Int, minute: Int) {
        _state.value = _state.value.copy(reminderHour = hour, reminderMinute = minute)
    }

    fun save() {
        val current = _state.value
        if (current.name.isBlank()) {
            _state.value = current.copy(formError = "Ponle un nombre a tu hábito")
            return
        }
        if (current.selectedDays.isEmpty()) {
            _state.value = current.copy(formError = "Elige al menos un día")
            return
        }
        val target = current.targetValue.toDoubleOrNull()
        if (current.type == HabitType.NUMERIC && (target == null || target <= 0.0)) {
            _state.value = current.copy(formError = "Pon una meta numérica válida")
            return
        }

        viewModelScope.launch {
            val existing = if (current.isEditing) repository.getHabitOnce(current.habitId) else null
            val habit = Habit(
                id = if (current.isEditing) current.habitId else 0,
                name = current.name.trim(),
                emoji = current.emoji.ifBlank { "\u2B50" },
                colorHex = current.colorHex,
                frequencyDays = current.selectedDays.sorted().joinToString(","),
                reminderHour = if (current.reminderEnabled) current.reminderHour else -1,
                reminderMinute = if (current.reminderEnabled) current.reminderMinute else -1,
                reminderEnabled = current.reminderEnabled,
                createdAtEpochDay = existing?.createdAtEpochDay ?: LocalDate.now().toEpochDay(),
                type = current.type,
                targetValue = if (current.type == HabitType.NUMERIC) (target ?: 1.0) else 1.0,
                unit = current.unit.trim(),
                category = current.category.trim(),
                vacationUntilEpochDay = existing?.vacationUntilEpochDay ?: -1,
                sortOrder = existing?.sortOrder ?: 0
            )

            val savedId = if (current.isEditing) {
                repository.updateHabit(habit)
                habit.id
            } else {
                repository.createHabit(habit)
            }

            val context = getApplication<Application>()
            if (habit.reminderEnabled) {
                ReminderScheduler.schedule(context, habit.copy(id = savedId))
            } else {
                ReminderScheduler.cancel(context, savedId)
            }

            _state.value = _state.value.copy(saved = true)
        }
    }
}
