package com.dezenbit.habitos.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import com.dezenbit.habitos.ui.theme.StreakOrange

@Composable
fun StreakBadge(streak: Int, modifier: Modifier = Modifier) {
    if (streak <= 0) return
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.LocalFireDepartment,
            contentDescription = "Racha",
            tint = StreakOrange,
            modifier = Modifier.padding(0.dp)
        )
        Text(
            text = "$streak",
            style = MaterialTheme.typography.labelSmall,
            color = StreakOrange
        )
    }
}
