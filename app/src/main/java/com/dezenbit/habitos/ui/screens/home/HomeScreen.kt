package com.dezenbit.habitos.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dezenbit.habitos.HabitApplication
import com.dezenbit.habitos.data.AppSettings
import com.dezenbit.habitos.ui.components.HabitCard
import com.dezenbit.habitos.util.HapticsHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onAddHabit: () -> Unit,
    onOpenHabit: (Long) -> Unit,
    onOpenStats: () -> Unit,
    onOpenSettings: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val app = context.applicationContext as HabitApplication
    val settings by app.settingsRepository.settings.collectAsState(initial = AppSettings())

    fun tickIfEnabled() {
        if (settings.hapticsEnabled) HapticsHelper.tick(context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hábitos") },
                actions = {
                    IconButton(onClick = onOpenStats) {
                        Icon(Icons.Filled.BarChart, contentDescription = "Estadísticas")
                    }
                    IconButton(onClick = onOpenSettings) {
                        Icon(Icons.Filled.Settings, contentDescription = "Ajustes")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddHabit) {
                Icon(Icons.Filled.Add, contentDescription = "Añadir hábito")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            if (state.totalCount > 0) {
                ProgressHeader(completed = state.completedCount, total = state.totalCount)
            }

            if (state.categories.isNotEmpty()) {
                CategoryFilterRow(
                    categories = state.categories,
                    selected = state.selectedCategory,
                    onSelect = viewModel::selectCategory
                )
            }

            if (state.isEmpty) {
                EmptyState(modifier = Modifier.fillMaxSize())
            } else if (state.todayHabits.isEmpty()) {
                EmptyState(
                    modifier = Modifier.fillMaxSize(),
                    title = "Nada para hoy",
                    subtitle = "Ninguno de tus hábitos aplica para el día de hoy"
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(state.todayHabits, key = { it.habit.id }) { item ->
                        HabitCard(
                            habit = item.habit,
                            completedToday = item.completedToday,
                            todayValue = item.todayValue,
                            hasNoteToday = item.hasNoteToday,
                            onVacation = item.onVacation,
                            currentStreak = item.streak.current,
                            onToggle = {
                                when (item.habit.type) {
                                    com.dezenbit.habitos.data.HabitType.BOOLEAN ->
                                        viewModel.toggleToday(item.habit.id)
                                    com.dezenbit.habitos.data.HabitType.NUMERIC ->
                                        viewModel.incrementToday(item.habit.id, item.todayValue)
                                }
                                if (!item.completedToday) tickIfEnabled()
                                com.dezenbit.habitos.widget.HabitWidgetProvider.updateAllWidgets(context)
                            },
                            onClick = { onOpenHabit(item.habit.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryFilterRow(
    categories: List<String>,
    selected: String?,
    onSelect: (String?) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(bottom = 4.dp)
    ) {
        item {
            FilterChip(selected = selected == null, onClick = { onSelect(null) }, label = { Text("Todas") })
        }
        items(categories) { category ->
            FilterChip(
                selected = selected == category,
                onClick = { onSelect(category) },
                label = { Text(category) }
            )
        }
    }
}

@Composable
private fun ProgressHeader(completed: Int, total: Int) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Progreso de hoy",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "$completed / $total",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = { if (total == 0) 0f else completed / total.toFloat() },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
        )
    }
}

@Composable
private fun EmptyState(
    modifier: Modifier = Modifier,
    title: String = "Aún no tienes hábitos",
    subtitle: String = "Toca el botón + para crear tu primer hábito"
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "\uD83C\uDF31", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = title, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}
