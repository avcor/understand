package com.example.understand

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class StateViewModel(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    val countFlow = savedStateHandle.getStateFlow("count", 0)
    val countLiveData = savedStateHandle.getLiveData<Int>("count")

    init {
        Log.d("abcd", ": ${countLiveData.value} ")
    }
}