package com.dezenbit.habitos.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dezenbit.habitos.HabitApplication
import com.dezenbit.habitos.data.HabitRepository
import com.dezenbit.habitos.data.SettingsRepository
import com.dezenbit.habitos.ui.screens.addedit.AddEditHabitViewModel
import com.dezenbit.habitos.ui.screens.detail.HabitDetailViewModel
import com.dezenbit.habitos.ui.screens.home.HomeViewModel
import com.dezenbit.habitos.ui.screens.reorder.ReorderHabitsViewModel
import com.dezenbit.habitos.ui.screens.settings.SettingsViewModel
import com.dezenbit.habitos.ui.screens.stats.StatisticsViewModel

/**
 * Fábrica única para todos los ViewModels de la app. Al no usar un framework de
 * inyección de dependencias, cada ViewModel recibe sus repositorios directamente.
 */
class ViewModelFactory(
    private val app: Application,
    private val repository: HabitRepository,
    private val settingsRepository: SettingsRepository,
    private val habitId: Long = -1L
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) ->
                HomeViewModel(repository) as T

            modelClass.isAssignableFrom(AddEditHabitViewModel::class.java) ->
                AddEditHabitViewModel(app, repository, habitId) as T

            modelClass.isAssignableFrom(HabitDetailViewModel::class.java) ->
                HabitDetailViewModel(app, repository, habitId) as T

            modelClass.isAssignableFrom(StatisticsViewModel::class.java) ->
                StatisticsViewModel(repository) as T

            modelClass.isAssignableFrom(ReorderHabitsViewModel::class.java) ->
                ReorderHabitsViewModel(repository) as T

            modelClass.isAssignableFrom(SettingsViewModel::class.java) ->
                SettingsViewModel(app, repository, settingsRepository) as T

            else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
        }
    }
}

fun habitApplicationOf(context: android.content.Context): HabitApplication =
    context.applicationContext as HabitApplication
