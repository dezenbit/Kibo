package com.dezenbit.habitos.ui.screens.addedit

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.dezenbit.habitos.data.HabitType
import com.dezenbit.habitos.ui.theme.HabitColorPalette
import com.dezenbit.habitos.util.DateUtils
import java.time.DayOfWeek

private val commonEmojis = listOf(
    "\u2B50", "\uD83D\uDCAA", "\uD83D\uDCDA", "\uD83D\uDCA7", "\uD83C\uDFC3", "\uD83E\uDDD8",
    "\uD83D\uDECC", "\uD83E\uDD57", "\uD83D\uDEB0", "\uD83C\uDFA8", "\uD83D\uDCB0", "\uD83D\uDD58",
    "\uD83C\uDFC6", "\uD83E\uDDE0", "\uD83C\uDFB8", "\uD83C\uDF31"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditHabitScreen(
    viewModel: AddEditHabitViewModel,
    onBack: () -> Unit,
    onSaved: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(state.saved) {
        if (state.saved) onSaved()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (state.isEditing) "Editar hábito" else "Nuevo hábito") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        if (state.isLoading) return@Scaffold

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            OutlinedTextField(
                value = state.name,
                onValueChange = viewModel::onNameChange,
                label = { Text("Nombre del hábito") },
                placeholder = { Text("Ej. Beber agua") },
                modifier = Modifier.fillMaxWidth()
            )

            Column {
                Text("Ícono", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(commonEmojis) { emoji ->
                        EmojiOption(
                            emoji = emoji,
                            selected = emoji == state.emoji,
                            onClick = { viewModel.onEmojiChange(emoji) }
                        )
                    }
                }
            }

            Column {
                Text("Color", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(HabitColorPalette) { hex ->
                        ColorOption(
                            hex = hex,
                            selected = hex == state.colorHex,
                            onClick = { viewModel.onColorChange(hex) }
                        )
                    }
                }
            }

            Column {
                Text("¿Qué días?", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    DayOfWeek.entries.forEach { day ->
                        val isoValue = day.value
                        FilterChip(
                            selected = state.selectedDays.contains(isoValue),
                            onClick = { viewModel.toggleDay(isoValue) },
                            label = { Text(DateUtils.isoWeekdayLabel(day).take(1)) }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = state.category,
                onValueChange = viewModel::onCategoryChange,
                label = { Text("Categoría (opcional)") },
                placeholder = { Text("Ej. Salud, Trabajo, Aprendizaje") },
                modifier = Modifier.fillMaxWidth()
            )

            Column {
                Text("Tipo de meta", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = state.type == HabitType.BOOLEAN,
                        onClick = { viewModel.onTypeChange(HabitType.BOOLEAN) },
                        label = { Text("Sí / No") }
                    )
                    FilterChip(
                        selected = state.type == HabitType.NUMERIC,
                        onClick = { viewModel.onTypeChange(HabitType.NUMERIC) },
                        label = { Text("Cantidad") }
                    )
                }
                if (state.type == HabitType.NUMERIC) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = state.targetValue,
                            onValueChange = viewModel::onTargetValueChange,
                            label = { Text("Meta diaria") },
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                keyboardType = androidx.compose.ui.text.input.KeyboardType.Decimal
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = state.unit,
                            onValueChange = viewModel::onUnitChange,
                            label = { Text("Unidad") },
                            placeholder = { Text("vasos, km...") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Card(shape = RoundedCornerShape(16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Recordatorio diario", style = MaterialTheme.typography.titleMedium)
                        Switch(
                            checked = state.reminderEnabled,
                            onCheckedChange = viewModel::onReminderEnabledChange
                        )
                    }
                    if (state.reminderEnabled) {
                        Spacer(modifier = Modifier.height(8.dp))
                        val label = String.format("%02d:%02d", state.reminderHour, state.reminderMinute)
                        Button(onClick = {
                            TimePickerDialog(
                                context,
                                { _, hour, minute -> viewModel.onReminderTimeChange(hour, minute) },
                                state.reminderHour,
                                state.reminderMinute,
                                true
                            ).show()
                        }) {
                            Text("Hora: $label")
                        }
                    }
                }
            }

            state.formError?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
            }

            Button(
                onClick = viewModel::save,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (state.isEditing) "Guardar cambios" else "Crear hábito")
            }
        }
    }
}

@Composable
private fun EmojiOption(emoji: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .size(48.dp)
            .clickable(onClick = onClick)
            .background(
                if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                CircleShape
            ),
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = emoji, style = MaterialTheme.typography.titleLarge)
        }
    }
}

@Composable
private fun ColorOption(hex: String, selected: Boolean, onClick: () -> Unit) {
    val color = Color(android.graphics.Color.parseColor(hex))
    Column(
        modifier = Modifier
            .size(40.dp)
            .background(color, CircleShape)
            .border(
                width = if (selected) 3.dp else 0.dp,
                color = MaterialTheme.colorScheme.onBackground,
                shape = CircleShape
            )
            .clickable(onClick = onClick)
    ) {}
}
