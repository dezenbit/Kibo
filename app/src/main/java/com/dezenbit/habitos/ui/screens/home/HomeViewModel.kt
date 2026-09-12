package com.dezenbit.habitos.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dezenbit.habitos.data.Habit
import com.dezenbit.habitos.data.HabitRepository
import com.dezenbit.habitos.data.HabitType
import com.dezenbit.habitos.util.StreakCalculator
import com.dezenbit.habitos.util.StreakInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

data class HabitUiState(
    val habit: Habit,
    val completedToday: Boolean,
    val todayValue: Double,
    val hasNoteToday: Boolean,
    val onVacation: Boolean,
    val streak: StreakInfo
)

data class HomeUiState(
    val todayHabits: List<HabitUiState> = emptyList(),
    val completedCount: Int = 0,
    val totalCount: Int = 0,
    val isEmpty: Boolean = false,
    val categories: List<String> = emptyList(),
    val selectedCategory: String? = null
)

class HomeViewModel(private val repository: HabitRepository) : ViewModel() {

    private val selectedCategory = MutableStateFlow<String?>(null)

    val uiState = combine(
        repository.activeHabits,
        repository.allCompletions,
        selectedCategory
    ) { habits, completions, category ->
        val today = LocalDate.now()
        val todayEpoch = today.toEpochDay()

        val categories = habits.map { it.category }.filter { it.isNotBlank() }.distinct().sorted()

        val filteredHabits = if (category.isNullOrBlank()) habits else habits.filter { it.category == category }

        val todayHabits = filteredHabits
            .filter { it.appliesOnIsoDayOfWeek(today.dayOfWeek.value) }
            .map { habit ->
                val habitCompletions = completions.filter { it.habitId == habit.id }
                val habitCompletedDays = habitCompletions.map { LocalDate.ofEpochDay(it.epochDay) }.toSet()
                val todayRecord = habitCompletions.find { it.epochDay == todayEpoch }
                val todayValue = todayRecord?.value ?: if (todayRecord != null) 1.0 else 0.0
                val completedToday = when (habit.type) {
                    HabitType.BOOLEAN -> habitCompletedDays.contains(today)
                    HabitType.NUMERIC -> todayValue >= habit.targetValue && habit.targetValue > 0
                }
                HabitUiState(
                    habit = habit,
                    completedToday = completedToday,
                    todayValue = todayValue,
                    hasNoteToday = !todayRecord?.note.isNullOrBlank(),
                    onVacation = habit.isOnVacationOn(todayEpoch),
                    streak = StreakCalculator.calculate(habit, habitCompletedDays, today)
                )
            }

        HomeUiState(
            todayHabits = todayHabits,
            completedCount = todayHabits.count { it.completedToday },
            totalCount = todayHabits.size,
            isEmpty = habits.isEmpty(),
            categories = categories,
            selectedCategory = category
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState())

    fun selectCategory(category: String?) {
        selectedCategory.value = category
    }

    fun toggleToday(habitId: Long) {
        viewModelScope.launch {
            repository.toggleCompletion(habitId, LocalDate.now())
        }
    }

    /** Para hábitos numéricos: suma [step] al valor de hoy (creando el registro si no existía). */
    fun incrementToday(habitId: Long, currentValue: Double, step: Double = 1.0) {
        viewModelScope.launch {
            repository.setNumericValue(habitId, LocalDate.now(), currentValue + step)
        }
    }
}
