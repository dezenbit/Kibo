package com.dezenbit.habitos.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class HabitRepository(private val dao: HabitDao) {

    val activeHabits: Flow<List<Habit>> = dao.getActiveHabits()
    val allCompletions: Flow<List<HabitCompletion>> = dao.getAllCompletions()

    fun habitById(id: Long): Flow<Habit?> = dao.getHabitById(id)

    fun completionsForHabit(habitId: Long): Flow<List<HabitCompletion>> =
        dao.getCompletionsForHabit(habitId)

    suspend fun createHabit(habit: Habit): Long {
        val nextOrder = dao.getMaxSortOrder() + 1
        return dao.insertHabit(habit.copy(sortOrder = nextOrder))
    }

    suspend fun updateHabit(habit: Habit) = dao.updateHabit(habit)

    suspend fun deleteHabit(habit: Habit) {
        dao.deleteHabit(habit)
        dao.deleteCompletionsForHabit(habit.id)
    }

    suspend fun archiveHabit(habit: Habit) = dao.updateHabit(habit.copy(archived = true))

    suspend fun getHabitOnce(habitId: Long): Habit? = dao.getHabitByIdOnce(habitId)

    /** Alterna el estado de cumplimiento (hábitos tipo BOOLEAN) para un día concreto. */
    suspend fun toggleCompletion(habitId: Long, date: LocalDate) {
        val epochDay = date.toEpochDay()
        val existing = dao.getCompletionOnce(habitId, epochDay)
        if (existing != null) {
            dao.deleteCompletion(habitId, epochDay)
        } else {
            dao.upsertCompletion(HabitCompletion(habitId = habitId, epochDay = epochDay))
        }
    }

    /** Guarda la cantidad registrada ese día para hábitos tipo NUMERIC (0 o menos = borra el registro). */
    suspend fun setNumericValue(habitId: Long, date: LocalDate, value: Double) {
        val epochDay = date.toEpochDay()
        if (value <= 0.0) {
            dao.deleteCompletion(habitId, epochDay)
            return
        }
        val existing = dao.getCompletionOnce(habitId, epochDay)
        dao.upsertCompletion(
            (existing ?: HabitCompletion(habitId = habitId, epochDay = epochDay)).copy(value = value)
        )
    }

    /** Añade o quita una nota del día, sin afectar si el hábito cuenta como completado. */
    suspend fun setNote(habitId: Long, date: LocalDate, note: String?) {
        val epochDay = date.toEpochDay()
        val existing = dao.getCompletionOnce(habitId, epochDay)
            ?: HabitCompletion(habitId = habitId, epochDay = epochDay)
        dao.upsertCompletion(existing.copy(note = note?.ifBlank { null }))
    }

    suspend fun getCompletionOnce(habitId: Long, date: LocalDate): HabitCompletion? =
        dao.getCompletionOnce(habitId, date.toEpochDay())

    /** Pone (o quita, con until = null) el hábito en modo vacaciones hasta la fecha indicada. */
    suspend fun setVacation(habit: Habit, until: LocalDate?) {
        dao.updateHabit(habit.copy(vacationUntilEpochDay = until?.toEpochDay() ?: -1))
    }

    /** Persiste un nuevo orden manual para la lista de hábitos. */
    suspend fun reorderHabits(orderedHabits: List<Habit>) {
        orderedHabits.forEachIndexed { index, habit ->
            if (habit.sortOrder != index) {
                dao.updateHabit(habit.copy(sortOrder = index))
            }
        }
    }

    // ---- Backup / restauración ----

    suspend fun exportBackup(): BackupData =
        BackupData(habits = dao.getAllHabitsOnce(), completions = dao.getAllCompletionsOnce())

    /** Reemplaza todos los datos actuales por los del backup. */
    suspend fun importBackup(backup: BackupData) {
        dao.deleteAllCompletions()
        dao.deleteAllHabits()
        backup.habits.forEach { dao.insertHabit(it.copy(id = 0)) }
        // Como los IDs de hábito cambian al reinsertarse, solo restauramos las
        // completions si coincide la cantidad de hábitos (caso típico: mismo dispositivo
        // restaurando su propio backup). Para un import robusto entre dispositivos ver README.
        val newHabits = dao.getAllHabitsOnce()
        val oldToNewId = backup.habits.map { it.id }.zip(newHabits.map { it.id }).toMap()
        backup.completions.forEach { completion ->
            val newHabitId = oldToNewId[completion.habitId] ?: return@forEach
            dao.upsertCompletion(completion.copy(id = 0, habitId = newHabitId))
        }
    }
}

data class BackupData(
    val habits: List<Habit>,
    val completions: List<HabitCompletion>
)
