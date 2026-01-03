package com.example.understand

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class StateViewModel(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    val cursorPosState = savedStateHandle.getStateFlow("cursor_pos", 0)

    init {
        viewModelScope.launch {
            cursorPosState.collect { posValue ->
                Log.d(TAG, "savedStateHandle collected value $posValue")
            }
        }
    }

    fun setCursorPos(pos: Int) {
        savedStateHandle["cursor_pos"] = pos + 1
    }
}