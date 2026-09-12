package com.dezenbit.habitos.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.temporal.WeekFields

/**
 * Mapa de calor tipo GitHub: una columna por semana, una fila por día (lunes-domingo),
 * mostrando las últimas [weeks] semanas hasta hoy.
 */
@Composable
fun HeatmapView(
    completedDays: Set<LocalDate>,
    habitColor: Color,
    weeks: Int = 18,
    modifier: Modifier = Modifier
) {
    val today = LocalDate.now()
    val currentWeekMonday = today.with(WeekFields.ISO.dayOfWeek(), 1L)
    val firstMonday = currentWeekMonday.minusWeeks((weeks - 1).toLong())
    val emptyColor = habitColor.copy(alpha = 0.10f)
    val filledColor = habitColor

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(110.dp)
    ) {
        val cellSpacing = 3.dp.toPx()
        val cellSize = ((size.width - cellSpacing * (weeks - 1)) / weeks).coerceAtMost(
            (size.height - cellSpacing * 6) / 7
        )
        val gridWidth = cellSize * weeks + cellSpacing * (weeks - 1)
        val startX = (size.width - gridWidth) / 2f
        val corner = CornerRadius(2.dp.toPx(), 2.dp.toPx())

        for (week in 0 until weeks) {
            val weekMonday = firstMonday.plusWeeks(week.toLong())
            for (dayOffset in 0..6) {
                val day = weekMonday.plusDays(dayOffset.toLong())
                if (day.isAfter(today)) continue
                val x = startX + week * (cellSize + cellSpacing)
                val y = dayOffset * (cellSize + cellSpacing)
                drawRoundRect(
                    color = if (completedDays.contains(day)) filledColor else emptyColor,
                    topLeft = androidx.compose.ui.geometry.Offset(x, y),
                    size = Size(cellSize, cellSize),
                    cornerRadius = corner
                )
            }
        }
    }
}
