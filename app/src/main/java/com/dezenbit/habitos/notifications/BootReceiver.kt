package com.dezenbit.habitos.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dezenbit.habitos.HabitApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        val pendingResult = goAsync()
        val app = context.applicationContext as HabitApplication

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val habits = app.repository.activeHabits.first()
                habits.filter { it.reminderEnabled }.forEach { habit ->
                    ReminderScheduler.schedule(context, habit)
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
