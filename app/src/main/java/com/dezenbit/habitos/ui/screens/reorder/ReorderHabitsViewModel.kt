package com.dezenbit.habitos.ui.screens.reorder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dezenbit.habitos.data.Habit
import com.dezenbit.habitos.data.HabitRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ReorderHabitsViewModel(private val repository: HabitRepository) : ViewModel() {

    private val _habits = MutableStateFlow<List<Habit>>(emptyList())
    val habits: StateFlow<List<Habit>> = _habits.asStateFlow()

    init {
        viewModelScope.launch {
            _habits.value = repository.activeHabits.first()
        }
    }

    fun moveUp(index: Int) = swap(index, index - 1)

    fun moveDown(index: Int) = swap(index, index + 1)

    private fun swap(from: Int, to: Int) {
        val current = _habits.value
        if (from !in current.indices || to !in current.indices) return
        val updated = current.toMutableList().apply {
            val tmp = this[from]
            this[from] = this[to]
            this[to] = tmp
        }
        _habits.value = updated
        viewModelScope.launch {
            repository.reorderHabits(updated)
        }
    }
}
