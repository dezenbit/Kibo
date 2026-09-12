package com.dezenbit.habitos.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dezenbit.habitos.HabitApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val habitId = intent.getLongExtra("habitId", -1L)
        val habitName = intent.getStringExtra("habitName") ?: return
        val habitEmoji = intent.getStringExtra("habitEmoji") ?: "\u2B50"
        if (habitId < 0) return

        val pendingResult = goAsync()
        val app = context.applicationContext as HabitApplication

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val habit = app.repository.getHabitOnce(habitId)
                if (habit != null && !habit.archived) {
                    val today = LocalDate.now()
                    if (habit.appliesOnIsoDayOfWeek(today.dayOfWeek.value)) {
                        NotificationHelper.showReminder(context, habitId, habitName, habitEmoji)
                    }
                    // Reprograma para la misma hora del día siguiente.
                    ReminderScheduler.schedule(context, habit)
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
