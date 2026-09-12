package com.dezenbit.habitos.ui.screens.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dezenbit.habitos.data.Habit
import com.dezenbit.habitos.data.HabitRepository
import com.dezenbit.habitos.util.DateUtils
import com.dezenbit.habitos.util.StreakCalculator
import com.dezenbit.habitos.util.StreakInfo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

data class DayStat(val date: LocalDate, val completed: Int, val applicable: Int) {
    val rate: Float get() = if (applicable == 0) 0f else completed / applicable.toFloat()
}

data class HabitStat(val habit: Habit, val streak: StreakInfo, val totalCompletions: Int)

data class StatisticsUiState(
    val weekly: List<DayStat> = emptyList(),
    val habitStats: List<HabitStat> = emptyList(),
    val overallRateThisWeek: Float = 0f,
    val totalHabits: Int = 0,
    val bestWeekdayLabel: String? = null
)

class StatisticsViewModel(repository: HabitRepository) : ViewModel() {

    val uiState = combine(
        repository.activeHabits,
        repository.allCompletions
    ) { habits, completions ->
        val completionsByHabit = completions.groupBy { it.habitId }
        val days = DateUtils.lastDays(7)

        val weekly = days.map { date ->
            var applicable = 0
            var completed = 0
            habits.forEach { habit ->
                if (habit.appliesOnIsoDayOfWeek(date.dayOfWeek.value)) {
                    applicable++
                    val habitDays = completionsByHabit[habit.id]?.map { LocalDate.ofEpochDay(it.epochDay) } ?: emptyList()
                    if (habitDays.contains(date)) completed++
                }
            }
            DayStat(date, completed, applicable)
        }

        val habitStats = habits.map { habit ->
            val habitDays = completionsByHabit[habit.id]?.map { LocalDate.ofEpochDay(it.epochDay) }?.toSet() ?: emptySet()
            HabitStat(
                habit = habit,
                streak = StreakCalculator.calculate(habit, habitDays),
                totalCompletions = habitDays.size
            )
        }.sortedByDescending { it.streak.current }

        val totalApplicable = weekly.sumOf { it.applicable }
        val totalCompleted = weekly.sumOf { it.completed }

        val bestWeekday = bestWeekdayLabel(habits, completions)

        StatisticsUiState(
            weekly = weekly,
            habitStats = habitStats,
            overallRateThisWeek = if (totalApplicable == 0) 0f else totalCompleted / totalApplicable.toFloat(),
            totalHabits = habits.size,
            bestWeekdayLabel = bestWeekday
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StatisticsUiState())

    /** Compara, de las últimas 8 semanas, en qué día de la semana se completa más. */
    private fun bestWeekdayLabel(habits: List<Habit>, completions: List<com.dezenbit.habitos.data.HabitCompletion>): String? {
        if (habits.isEmpty() || completions.isEmpty()) return null
        val today = LocalDate.now()
        val windowStart = today.minusWeeks(8)

        val applicableByWeekday = IntArray(8) // índice 1..7
        val completedByWeekday = IntArray(8)
        val completedDates = completions.map { LocalDate.ofEpochDay(it.epochDay) to it.habitId }.toSet()

        var cursor = windowStart
        while (!cursor.isAfter(today)) {
            val iso = cursor.dayOfWeek.value
            habits.forEach { habit ->
                if (habit.appliesOnIsoDayOfWeek(iso) && cursor.toEpochDay() >= habit.createdAtEpochDay) {
                    applicableByWeekday[iso]++
                    if (completedDates.contains(cursor to habit.id)) completedByWeekday[iso]++
                }
            }
            cursor = cursor.plusDays(1)
        }

        val bestIso = (1..7).maxByOrNull { iso ->
            if (applicableByWeekday[iso] == 0) -1.0
            else completedByWeekday[iso].toDouble() / applicableByWeekday[iso]
        } ?: return null

        if (applicableByWeekday[bestIso] == 0) return null
        return DateUtils.isoWeekdayLabel(java.time.DayOfWeek.of(bestIso))
    }
}
