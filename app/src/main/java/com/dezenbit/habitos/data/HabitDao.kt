package com.dezenbit.habitos.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    // ---- Hábitos ----

    @Insert
    suspend fun insertHabit(habit: Habit): Long

    @Update
    suspend fun updateHabit(habit: Habit)

    @Delete
    suspend fun deleteHabit(habit: Habit)

    @Query("SELECT * FROM habits WHERE archived = 0 ORDER BY sortOrder ASC, createdAtEpochDay DESC")
    fun getActiveHabits(): Flow<List<Habit>>

    @Query("SELECT * FROM habits WHERE id = :habitId")
    fun getHabitById(habitId: Long): Flow<Habit?>

    @Query("SELECT * FROM habits WHERE id = :habitId")
    suspend fun getHabitByIdOnce(habitId: Long): Habit?

    @Query("SELECT * FROM habits WHERE archived = 0 ORDER BY sortOrder ASC")
    suspend fun getActiveHabitsOnce(): List<Habit>

    @Query("SELECT COALESCE(MAX(sortOrder), -1) FROM habits")
    suspend fun getMaxSortOrder(): Int

    // ---- Registros de cumplimiento ----

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCompletion(completion: HabitCompletion)

    @Query("DELETE FROM habit_completions WHERE habitId = :habitId AND epochDay = :epochDay")
    suspend fun deleteCompletion(habitId: Long, epochDay: Long)

    @Query("DELETE FROM habit_completions WHERE habitId = :habitId")
    suspend fun deleteCompletionsForHabit(habitId: Long)

    @Query("SELECT * FROM habit_completions WHERE habitId = :habitId ORDER BY epochDay DESC")
    fun getCompletionsForHabit(habitId: Long): Flow<List<HabitCompletion>>

    @Query("SELECT * FROM habit_completions")
    fun getAllCompletions(): Flow<List<HabitCompletion>>

    @Query("SELECT * FROM habit_completions WHERE habitId = :habitId AND epochDay = :epochDay LIMIT 1")
    suspend fun getCompletionOnce(habitId: Long, epochDay: Long): HabitCompletion?

    // ---- Backup / restauración ----

    @Query("SELECT * FROM habits")
    suspend fun getAllHabitsOnce(): List<Habit>

    @Query("SELECT * FROM habit_completions")
    suspend fun getAllCompletionsOnce(): List<HabitCompletion>

    @Query("DELETE FROM habits")
    suspend fun deleteAllHabits()

    @Query("DELETE FROM habit_completions")
    suspend fun deleteAllCompletions()
}
