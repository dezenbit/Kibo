package com.dezenbit.habitos.ui.screens.settings

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dezenbit.habitos.data.AppSettings
import com.dezenbit.habitos.data.BackupManager
import com.dezenbit.habitos.data.HabitRepository
import com.dezenbit.habitos.data.SettingsRepository
import com.dezenbit.habitos.data.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class BackupResult { SUCCESS, ERROR }

class SettingsViewModel(
    application: Application,
    private val repository: HabitRepository,
    private val settingsRepository: SettingsRepository
) : AndroidViewModel(application) {

    val settings: StateFlow<AppSettings> = settingsRepository.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppSettings())

    private val _backupResult = MutableStateFlow<BackupResult?>(null)
    val backupResult: StateFlow<BackupResult?> = _backupResult.asStateFlow()

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch { settingsRepository.setThemeMode(mode) }
    }

    fun setDynamicColor(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.setDynamicColor(enabled) }
    }

    fun setHapticsEnabled(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.setHapticsEnabled(enabled) }
    }

    fun exportBackup(uri: Uri) {
        viewModelScope.launch {
            runCatching {
                val backup = repository.exportBackup()
                BackupManager.writeToUri(getApplication(), uri, backup)
            }.onSuccess {
                _backupResult.value = BackupResult.SUCCESS
            }.onFailure {
                _backupResult.value = BackupResult.ERROR
            }
        }
    }

    fun importBackup(uri: Uri) {
        viewModelScope.launch {
            runCatching {
                val backup = BackupManager.readFromUri(getApplication(), uri)
                repository.importBackup(backup)
            }.onSuccess {
                _backupResult.value = BackupResult.SUCCESS
            }.onFailure {
                _backupResult.value = BackupResult.ERROR
            }
        }
    }

    fun clearBackupResult() {
        _backupResult.value = null
    }
}
