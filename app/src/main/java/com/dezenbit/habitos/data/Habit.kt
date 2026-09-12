package com.dezenbit.habitos.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class HabitType { BOOLEAN, NUMERIC }

/**
 * Un hábito que el usuario quiere seguir.
 *
 * @param frequencyDays días de la semana en los que aplica (1=Lunes ... 7=Domingo),
 *   separados por coma. Vacío o "1,2,3,4,5,6,7" significa todos los días.
 * @param reminderHour / reminderMinute hora del recordatorio diario, -1 si no hay recordatorio.
 * @param type BOOLEAN = check simple; NUMERIC = se registra una cantidad (ej. "8 vasos de agua").
 * @param targetValue meta diaria cuando [type] es NUMERIC (ej. 8.0).
 * @param unit unidad de la meta numérica (ej. "vasos", "km", "páginas").
 * @param category etiqueta libre para agrupar/filtrar hábitos (vacío = sin categoría).
 * @param vacationUntilEpochDay si hoy es <= a este valor, el hábito está "en pausa":
 *   no cuenta para romper ni sumar racha. -1 significa que no está en pausa.
 * @param sortOrder posición manual del hábito en la lista (menor = más arriba).
 */
@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val emoji: String = "\u2B50",
    val colorHex: String = "#6750A4",
    val frequencyDays: String = "1,2,3,4,5,6,7",
    val reminderHour: Int = -1,
    val reminderMinute: Int = -1,
    val reminderEnabled: Boolean = false,
    val createdAtEpochDay: Long,
    val archived: Boolean = false,
    val type: HabitType = HabitType.BOOLEAN,
    val targetValue: Double = 1.0,
    val unit: String = "",
    val category: String = "",
    val vacationUntilEpochDay: Long = -1,
    val sortOrder: Int = 0
) {
    fun daysSet(): Set<Int> =
        if (frequencyDays.isBlank()) (1..7).toSet()
        else frequencyDays.split(",").mapNotNull { it.trim().toIntOrNull() }.toSet()

    fun appliesOnIsoDayOfWeek(isoDayOfWeek: Int): Boolean = daysSet().contains(isoDayOfWeek)

    fun isOnVacationOn(epochDay: Long): Boolean =
        vacationUntilEpochDay >= 0 && epochDay <= vacationUntilEpochDay
}
