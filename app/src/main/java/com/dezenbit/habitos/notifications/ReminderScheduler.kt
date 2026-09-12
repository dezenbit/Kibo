package com.dezenbit.habitos.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.dezenbit.habitos.data.Habit
import java.time.LocalDateTime
import java.time.ZoneId

object ReminderScheduler {

    private const val EXTRA_HABIT_ID = "habitId"
    private const val EXTRA_HABIT_NAME = "habitName"
    private const val EXTRA_HABIT_EMOJI = "habitEmoji"

    fun schedule(context: Context, habit: Habit) {
        if (!habit.reminderEnabled || habit.reminderHour < 0 || habit.reminderMinute < 0) {
            cancel(context, habit.id)
            return
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val triggerMillis = nextTriggerMillis(habit.reminderHour, habit.reminderMinute)
        val pendingIntent = buildPendingIntent(context, habit)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            // Sin permiso de alarmas exactas: usamos una alarma inexacta como respaldo.
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerMillis, pendingIntent)
            return
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerMillis,
            pendingIntent
        )
    }

    fun cancel(context: Context, habitId: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            habitId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    private fun buildPendingIntent(context: Context, habit: Habit): PendingIntent {
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra(EXTRA_HABIT_ID, habit.id)
            putExtra(EXTRA_HABIT_NAME, habit.name)
            putExtra(EXTRA_HABIT_EMOJI, habit.emoji)
        }
        return PendingIntent.getBroadcast(
            context,
            habit.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    /** Próxima ocurrencia de hour:minute; si ya pasó hoy, se programa para mañana. */
    private fun nextTriggerMillis(hour: Int, minute: Int): Long {
        val zone = ZoneId.systemDefault()
        val now = LocalDateTime.now(zone)
        var trigger = now.withHour(hour).withMinute(minute).withSecond(0).withNano(0)
        if (!trigger.isAfter(now)) {
            trigger = trigger.plusDays(1)
        }
        return trigger.atZone(zone).toInstant().toEpochMilli()
    }
}
