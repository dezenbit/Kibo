package com.dezenbit.habitos.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.dezenbit.habitos.HabitApplication
import com.dezenbit.habitos.MainActivity
import com.dezenbit.habitos.R
import com.dezenbit.habitos.data.HabitType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate

private const val MAX_ROWS = 5

class HabitWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        updateAllWidgets(context)
    }

    companion object {
        private val rowIds = listOf(
            R.id.widget_row_0 to (R.id.widget_row_0_text to R.id.widget_row_0_check),
            R.id.widget_row_1 to (R.id.widget_row_1_text to R.id.widget_row_1_check),
            R.id.widget_row_2 to (R.id.widget_row_2_text to R.id.widget_row_2_check),
            R.id.widget_row_3 to (R.id.widget_row_3_text to R.id.widget_row_3_check),
            R.id.widget_row_4 to (R.id.widget_row_4_text to R.id.widget_row_4_check)
        )

        fun updateAllWidgets(context: Context) {
            val manager = AppWidgetManager.getInstance(context)
            val ids = manager.getAppWidgetIds(ComponentName(context, HabitWidgetProvider::class.java))
            if (ids.isEmpty()) return

            CoroutineScope(Dispatchers.IO).launch {
                val views = buildRemoteViews(context)
                ids.forEach { id -> manager.updateAppWidget(id, views) }
            }
        }

        private suspend fun buildRemoteViews(context: Context): RemoteViews {
            val app = context.applicationContext as HabitApplication
            val today = LocalDate.now()
            val allHabits = app.repository.activeHabits.first()
            val completions = app.repository.allCompletions.first()

            val todayHabits = allHabits
                .filter { it.appliesOnIsoDayOfWeek(today.dayOfWeek.value) && !it.isOnVacationOn(today.toEpochDay()) }
                .take(MAX_ROWS)

            val views = RemoteViews(context.packageName, com.dezenbit.habitos.R.layout.widget_habits)

            val openAppIntent = Intent(context, MainActivity::class.java)
            val openAppPending = android.app.PendingIntent.getActivity(
                context, 0, openAppIntent,
                android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_title, openAppPending)

            var completedCount = 0

            rowIds.forEachIndexed { index, (rowId, textCheck) ->
                val (textId, checkId) = textCheck
                if (index < todayHabits.size) {
                    val habit = todayHabits[index]
                    val habitCompletions = completions.filter { it.habitId == habit.id && it.epochDay == today.toEpochDay() }
                    val record = habitCompletions.firstOrNull()
                    val completed = when (habit.type) {
                        HabitType.BOOLEAN -> record != null
                        HabitType.NUMERIC -> (record?.value ?: 0.0) >= habit.targetValue && habit.targetValue > 0
                    }
                    if (completed) completedCount++

                    views.setViewVisibility(rowId, android.view.View.VISIBLE)
                    views.setTextViewText(textId, "${habit.emoji} ${habit.name}")
                    views.setImageViewResource(
                        checkId,
                        if (completed) R.drawable.ic_widget_check_on else R.drawable.ic_widget_check_off
                    )

                    val toggleIntent = Intent(context, WidgetToggleReceiver::class.java).apply {
                        action = WidgetToggleReceiver.ACTION_TOGGLE
                        putExtra(WidgetToggleReceiver.EXTRA_HABIT_ID, habit.id)
                    }
                    val togglePending = android.app.PendingIntent.getBroadcast(
                        context,
                        habit.id.toInt(),
                        toggleIntent,
                        android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
                    )
                    views.setOnClickPendingIntent(checkId, togglePending)
                    views.setOnClickPendingIntent(textId, togglePending)
                } else {
                    views.setViewVisibility(rowId, android.view.View.GONE)
                }
            }

            views.setViewVisibility(
                R.id.widget_empty,
                if (todayHabits.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
            )
            views.setTextViewText(R.id.widget_progress, "$completedCount / ${todayHabits.size} completados hoy")

            return views
        }
    }
}
