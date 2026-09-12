package com.dezenbit.habitos.widget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dezenbit.habitos.HabitApplication
import com.dezenbit.habitos.data.HabitType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * Al tocar el check de un hábito en el widget: para hábitos Sí/No alterna el estado;
 * para hábitos de meta numérica, marca la meta completa o la reinicia a 0.
 */
class WidgetToggleReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_TOGGLE = "com.dezenbit.habitos.widget.ACTION_TOGGLE"
        const val EXTRA_HABIT_ID = "habitId"
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != ACTION_TOGGLE) return
        val habitId = intent.getLongExtra(EXTRA_HABIT_ID, -1L)
        if (habitId < 0) return

        val pendingResult = goAsync()
        val app = context.applicationContext as HabitApplication

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val habit = app.repository.getHabitOnce(habitId)
                if (habit != null) {
                    val today = LocalDate.now()
                    when (habit.type) {
                        HabitType.BOOLEAN -> app.repository.toggleCompletion(habitId, today)
                        HabitType.NUMERIC -> {
                            val current = app.repository.getCompletionOnce(habitId, today)?.value ?: 0.0
                            val newValue = if (current >= habit.targetValue) 0.0 else habit.targetValue
                            app.repository.setNumericValue(habitId, today, newValue)
                        }
                    }
                }
                HabitWidgetProvider.updateAllWidgets(context)
            } finally {
                pendingResult.finish()
            }
        }
    }
}
