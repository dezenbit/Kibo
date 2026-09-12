package com.dezenbit.habitos.util

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

object DateUtils {

    val spanishLocale: Locale = Locale("es", "ES")

    /** Devuelve los últimos [count] días terminando en [today], en orden ascendente. */
    fun lastDays(count: Int, today: LocalDate = LocalDate.now()): List<LocalDate> =
        (count - 1 downTo 0).map { today.minusDays(it.toLong()) }

    fun shortDayLabel(date: LocalDate): String =
        date.dayOfWeek.getDisplayName(TextStyle.SHORT, spanishLocale)
            .replaceFirstChar { it.uppercase() }

    fun isoWeekdayLabel(day: DayOfWeek): String =
        day.getDisplayName(TextStyle.SHORT, spanishLocale)
            .replaceFirstChar { it.uppercase() }

    fun friendlyDate(date: LocalDate): String {
        val today = LocalDate.now()
        return when {
            date == today -> "Hoy"
            date == today.minusDays(1) -> "Ayer"
            else -> "${date.dayOfMonth} de ${
                date.month.getDisplayName(TextStyle.FULL, spanishLocale)
            }"
        }
    }
}
