package com.dezenbit.habitos

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.dezenbit.habitos.data.AppSettings
import com.dezenbit.habitos.data.ThemeMode
import com.dezenbit.habitos.ui.navigation.HabitosNavGraph
import com.dezenbit.habitos.ui.theme.HabitosTheme

class MainActivity : ComponentActivity() {

    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { /* no-op */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            )
            if (granted != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        val app = application as HabitApplication

        setContent {
            val settings by app.settingsRepository.settings.collectAsState(initial = AppSettings())
            val systemDark = isSystemInDarkTheme()
            val darkTheme = when (settings.themeMode) {
                ThemeMode.SYSTEM -> systemDark
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
            }

            HabitosTheme(darkTheme = darkTheme, dynamicColor = settings.dynamicColor) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    HabitosNavGraph(
                        application = app,
                        repository = app.repository,
                        settingsRepository = app.settingsRepository
                    )
                }
            }
        }
    }
}
