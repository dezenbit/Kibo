package com.dezenbit.habitos.ui.screens.stats

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import com.dezenbit.habitos.ui.theme.SuccessGreen
import com.dezenbit.habitos.util.DateUtils
import com.dezenbit.habitos.util.StreakInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel,
    onBack: () -> Unit,
    onOpenHabit: (Long) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Estadísticas") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        if (state.totalHabits == 0) {
            Column(
                modifier = Modifier.padding(padding).fillMaxSize().padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("\uD83D\uDCCA", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Crea tu primer hábito para ver estadísticas aquí",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Card(shape = RoundedCornerShape(16.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Consistencia de la semana", style = MaterialTheme.typography.titleMedium)
                        Text(
                            "${(state.overallRateThisWeek * 100).toInt()}% de tus hábitos completados",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        WeeklyBarChart(days = state.weekly)
                        state.bestWeekdayLabel?.let { day ->
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                "\uD83D\uDCC8 Tu mejor día suele ser: $day",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            item {
                Text("Tus hábitos", style = MaterialTheme.typography.titleMedium)
            }

            items(state.habitStats, key = { it.habit.id }) { stat ->
                HabitStatRow(stat = stat, onClick = { onOpenHabit(stat.habit.id) })
            }
        }
    }
}

@Composable
private fun WeeklyBarChart(days: List<DayStat>) {
    Row(
        modifier = Modifier.fillMaxWidth().height(120.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        days.forEach { day ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                DayBar(day.rate)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = DateUtils.shortDayLabel(day.date).take(1),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun DayBar(rate: Float) {
    val barColor = MaterialTheme.colorScheme.primary
    val trackColor = MaterialTheme.colorScheme.surfaceVariant
    Canvas(
        modifier = Modifier
            .width(20.dp)
            .height(90.dp)
    ) {
        val barWidth = size.width
        val corner = CornerRadius(barWidth / 2, barWidth / 2)
        drawRoundRect(
            color = trackColor,
            size = Size(barWidth, size.height),
            cornerRadius = corner
        )
        val filledHeight = size.height * rate.coerceIn(0f, 1f)
        drawRoundRect(
            color = if (rate >= 1f) SuccessGreen else barColor,
            topLeft = androidx.compose.ui.geometry.Offset(0f, size.height - filledHeight),
            size = Size(barWidth, filledHeight),
            cornerRadius = corner
        )
    }
}

@Composable
private fun HabitStatRow(stat: HabitStat, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(14.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(14.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = stat.habit.emoji, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(text = stat.habit.name, style = MaterialTheme.typography.bodyLarge)
                    Text(
                        text = "${stat.totalCompletions} completados en total",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            StreakSummary(streak = stat.streak)
        }
    }
}

@Composable
private fun StreakSummary(streak: StreakInfo) {
    Column(horizontalAlignment = Alignment.End) {
        Text("\uD83D\uDD25 ${streak.current}", style = MaterialTheme.typography.bodyMedium)
        Text(
            "mejor: ${streak.best}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
