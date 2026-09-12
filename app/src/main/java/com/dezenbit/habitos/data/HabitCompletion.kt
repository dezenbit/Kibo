package com.dezenbit.habitos.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Registro de que [habitId] se completó en el día [epochDay] (LocalDate.toEpochDay()).
 * Un habitId+epochDay solo puede existir una vez (ver OnConflictStrategy en el DAO).
 *
 * @param note nota corta opcional que el usuario dejó ese día (ej. "corrí 5km").
 * @param value cantidad registrada ese día para hábitos de tipo NUMERIC (ej. 6.0 vasos de agua).
 */
@Entity(tableName = "habit_completions")
data class HabitCompletion(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val habitId: Long,
    val epochDay: Long,
    val note: String? = null,
    val value: Double? = null
)
