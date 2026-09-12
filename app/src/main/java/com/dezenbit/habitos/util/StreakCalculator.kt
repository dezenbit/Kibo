package com.dezenbit.habitos.util

import com.dezenbit.habitos.data.Habit
import java.time.LocalDate

data class StreakInfo(val current: Int, val best: Int)

/**
 * Calcula la racha actual y la mejor racha de un hábito.
 *
 * Solo cuenta los días en los que el hábito "aplica" según su frecuencia
 * ([Habit.appliesOnIsoDayOfWeek]), de forma que un hábito de "solo fines de
 * semana" no se rompe entre lunes y viernes.
 */
object StreakCalculator {

    fun calculate(
        habit: Habit,
        completedDays: Set<LocalDate>,
        today: LocalDate = LocalDate.now()
    ): StreakInfo {
        if (completedDays.isEmpty()) return StreakInfo(0, 0)

        val applicableDays = generateApplicableDaysDescending(habit, today, completedDays)
        if (applicableDays.isEmpty()) return StreakInfo(0, 0)

        // --- Racha actual: desde hoy (o el último día aplicable) hacia atrás ---
        var current = 0
        var streakBroken = false
        for (day in applicableDays) {
            if (streakBroken) break
            if (completedDays.contains(day)) {
                current++
            } else if (day == today) {
                // Hoy aún no se marcó: no rompe la racha, solo no la incrementa todavía.
                continue
            } else {
                streakBroken = true
            }
        }

        // --- Mejor racha histórica ---
        var best = 0
        var running = 0
        // Recorremos en orden ascendente para acumular rachas consecutivas.
        for (day in applicableDays.sorted()) {
            if (completedDays.contains(day)) {
                running++
                if (running > best) best = running
            } else {
                running = 0
            }
        }

        return StreakInfo(current = current, best = maxOf(best, current))
    }

    /**
     * Genera los días aplicables desde [today] hacia atrás, hasta el día completado
     * más antiguo (para no iterar indefinidamente en hábitos muy viejos, se limita a 3 años).
     */
    private fun generateApplicableDaysDescending(
        habit: Habit,
        today: LocalDate,
        completedDays: Set<LocalDate>
    ): List<LocalDate> {
        val earliest = completedDays.minOrNull() ?: today
        val limit = maxOf(earliest, today.minusYears(3))
        val result = mutableListOf<LocalDate>()
        var cursor = today
        while (!cursor.isBefore(limit)) {
            if (habit.appliesOnIsoDayOfWeek(cursor.dayOfWeek.value) &&
                !habit.isOnVacationOn(cursor.toEpochDay())
            ) {
                result.add(cursor)
            }
            cursor = cursor.minusDays(1)
        }
        return result
    }
}
