package com.dezenbit.habitos.ui.screens.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dezenbit.habitos.data.Habit
import com.dezenbit.habitos.data.HabitRepository
import com.dezenbit.habitos.notifications.ReminderScheduler
import com.dezenbit.habitos.util.StreakCalculator
import com.dezenbit.habitos.util.StreakInfo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

data class HabitDetailUiState(
    val habit: Habit? = null,
    val completedDays: Set<LocalDate> = emptySet(),
    val notesByDay: Map<LocalDate, String> = emptyMap(),
    val streak: StreakInfo = StreakInfo(0, 0),
    val totalCompletions: Int = 0
)

class HabitDetailViewModel(
    application: Application,
    private val repository: HabitRepository,
    private val habitId: Long
) : AndroidViewModel(application) {

    val uiState = combine(
        repository.habitById(habitId),
        repository.completionsForHabit(habitId)
    ) { habit, completions ->
        val days = completions.map { LocalDate.ofEpochDay(it.epochDay) }.toSet()
        val notes = completions
            .filter { !it.note.isNullOrBlank() }
            .associate { LocalDate.ofEpochDay(it.epochDay) to it.note!! }
        HabitDetailUiState(
            habit = habit,
            completedDays = days,
            notesByDay = notes,
            streak = habit?.let { StreakCalculator.calculate(it, days) } ?: StreakInfo(0, 0),
            totalCompletions = days.size
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HabitDetailUiState())

    fun toggleDay(date: LocalDate) {
        viewModelScope.launch {
            repository.toggleCompletion(habitId, date)
        }
    }

    fun saveNote(date: LocalDate, note: String) {
        viewModelScope.launch {
            repository.setNote(habitId, date, note)
        }
    }

    fun setVacationUntil(until: LocalDate?) {
        viewModelScope.launch {
            uiState.value.habit?.let { habit ->
                repository.setVacation(habit, until)
            }
        }
    }

    fun deleteHabit() {
        viewModelScope.launch {
            uiState.value.habit?.let { habit ->
                repository.deleteHabit(habit)
                ReminderScheduler.cancel(getApplication(), habit.id)
            }
        }
    }
}
