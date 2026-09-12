package com.dezenbit.habitos

import android.app.Application
import com.dezenbit.habitos.data.AppDatabase
import com.dezenbit.habitos.data.HabitRepository
import com.dezenbit.habitos.data.SettingsRepository
import com.dezenbit.habitos.notifications.NotificationHelper

class HabitApplication : Application() {

    val database: AppDatabase by lazy { AppDatabase.getInstance(this) }
    val repository: HabitRepository by lazy { HabitRepository(database.habitDao()) }
    val settingsRepository: SettingsRepository by lazy { SettingsRepository(this) }

    override fun onCreate() {
        super.onCreate()
        NotificationHelper.createChannel(this)
    }
}
