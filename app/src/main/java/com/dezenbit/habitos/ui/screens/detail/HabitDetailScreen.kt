package com.dezenbit.habitos.ui.screens.detail

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BeachAccess
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.dezenbit.habitos.ui.components.HeatmapView
import com.dezenbit.habitos.ui.components.WeekCalendarView
import com.dezenbit.habitos.util.DateUtils
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitDetailScreen(
    viewModel: HabitDetailViewModel,
    onBack: () -> Unit,
    onEdit: (Long) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf(false) }
    var habitExisted by remember { mutableStateOf(false) }
    val today = remember { LocalDate.now() }
    var noteText by remember(state.notesByDay[today]) { mutableStateOf(state.notesByDay[today] ?: "") }

    LaunchedEffect(state.habit) {
        if (state.habit != null) habitExisted = true
        if (habitExisted && state.habit == null) onBack()
    }

    val habit = state.habit ?: return

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(habit.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { onEdit(habit.id) }) {
                        Icon(Icons.Filled.Edit, contentDescription = "Editar")
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
                    }
                }
            )
        }
    ) { padding ->
        val habitColor = runCatching { Color(android.graphics.Color.parseColor(habit.colorHex)) }
            .getOrDefault(MaterialTheme.colorScheme.primary)
        val onVacationToday = habit.isOnVacationOn(today.toEpochDay())

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard(
                    label = "Racha actual",
                    value = "${state.streak.current}",
                    emoji = "\uD83D\uDD25",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "Mejor racha",
                    value = "${state.streak.best}",
                    emoji = "\uD83C\uDFC6",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "Total",
                    value = "${state.totalCompletions}",
                    emoji = "\u2705",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Últimos 7 días", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(12.dp))
            WeekCalendarView(
                days = DateUtils.lastDays(7),
                completedDays = state.completedDays,
                habitColor = habitColor,
                onDayClick = {
                    viewModel.toggleDay(it)
                    com.dezenbit.habitos.widget.HabitWidgetProvider.updateAllWidgets(context)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
            Text("Historial", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(12.dp))
            HeatmapView(completedDays = state.completedDays, habitColor = habitColor)

            Spacer(modifier = Modifier.height(24.dp))
            Text("Nota de hoy", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = noteText,
                onValueChange = {
                    noteText = it
                    viewModel.saveNote(today, it)
                },
                placeholder = { Text("Ej. corrí 5km, me costó pero lo logré") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))
            Card(shape = RoundedCornerShape(16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.BeachAccess, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Modo vacaciones", style = MaterialTheme.typography.titleMedium)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        if (onVacationToday) {
                            "En pausa hasta ${DateUtils.friendlyDate(LocalDate.ofEpochDay(habit.vacationUntilEpochDay))}. No se romperá tu racha."
                        } else {
                            "Pausa este hábito unos días sin perder tu racha (ej. viajes, enfermedad)."
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    if (onVacationToday) {
                        OutlinedButton(onClick = { viewModel.setVacationUntil(null) }) {
                            Text("Reanudar hábito ahora")
                        }
                    } else {
                        OutlinedButton(onClick = {
                            val initial = today.plusDays(3)
                            DatePickerDialog(
                                context,
                                { _, year, month, dayOfMonth ->
                                    viewModel.setVacationUntil(LocalDate.of(year, month + 1, dayOfMonth))
                                },
                                initial.year,
                                initial.monthValue - 1,
                                initial.dayOfMonth
                            ).apply {
                                datePicker.minDate = System.currentTimeMillis()
                            }.show()
                        }) {
                            Text("Poner en pausa")
                        }
                    }
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("¿Eliminar \"${habit.name}\"?") },
            text = { Text("Se borrará todo su historial. Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    viewModel.deleteHabit()
                }) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
private fun StatCard(label: String, value: String, emoji: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier, shape = RoundedCornerShape(16.dp)) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = emoji, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, style = MaterialTheme.typography.titleLarge)
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
