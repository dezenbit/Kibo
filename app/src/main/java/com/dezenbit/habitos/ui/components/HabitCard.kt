package com.dezenbit.habitos.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BeachAccess
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.dezenbit.habitos.data.Habit
import com.dezenbit.habitos.data.HabitType
import com.dezenbit.habitos.ui.theme.SuccessGreen

@Composable
fun HabitCard(
    habit: Habit,
    completedToday: Boolean,
    todayValue: Double,
    hasNoteToday: Boolean,
    onVacation: Boolean,
    currentStreak: Int,
    onToggle: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val defaultColor = MaterialTheme.colorScheme.primary
    val habitColor = remember(habit.colorHex, defaultColor) {
        runCatching { Color(android.graphics.Color.parseColor(habit.colorHex)) }.getOrDefault(defaultColor)
    }
    val alpha by animateFloatAsState(if (completedToday || onVacation) 0.6f else 1f, label = "cardAlpha")

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .alpha(alpha),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(habitColor.copy(alpha = 0.18f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = habit.emoji, style = MaterialTheme.typography.titleLarge)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = habit.name,
                        style = MaterialTheme.typography.titleMedium,
                        textDecoration = if (completedToday) TextDecoration.LineThrough else null
                    )
                    if (hasNoteToday) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Filled.EditNote,
                            contentDescription = "Tiene nota",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                if (onVacation) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.BeachAccess,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "En pausa",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else if (habit.type == HabitType.NUMERIC) {
                    Text(
                        text = "${formatAmount(todayValue)}/${formatAmount(habit.targetValue)} ${habit.unit}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else if (currentStreak > 0) {
                    Spacer(modifier = Modifier.padding(top = 2.dp))
                    StreakBadge(streak = currentStreak)
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            if (!onVacation) {
                CompletionCircle(
                    completed = completedToday,
                    isNumeric = habit.type == HabitType.NUMERIC,
                    color = habitColor,
                    onClick = onToggle
                )
            }
        }
    }
}

private fun formatAmount(value: Double): String =
    if (value == value.toLong().toDouble()) value.toLong().toString() else value.toString()

@Composable
private fun CompletionCircle(completed: Boolean, isNumeric: Boolean, color: Color, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clickable(onClick = onClick)
            .background(
                if (completed) SuccessGreen else color.copy(alpha = 0.15f),
                CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        when {
            completed -> Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "Completado",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
            isNumeric -> Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Sumar",
                tint = color,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
