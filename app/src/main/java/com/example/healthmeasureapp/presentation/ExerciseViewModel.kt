package com.example.healthmeasureapp.presentation

import androidx.lifecycle.*
import kotlinx.coroutines.*
import java.util.*

class ExerciseViewModel : ViewModel() {
    private val _time = MutableLiveData("00:00")
    val time: LiveData<String> = _time

    val steps = MutableLiveData("0")
    val distance = MutableLiveData("0 km")
    val calories = MutableLiveData("0")

    private var elapsedSeconds = 0
    private var timerJob: Job? = null

    fun start() {
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000)
                elapsedSeconds++
                _time.value = String.format("%02d:%02d", elapsedSeconds / 60, elapsedSeconds % 60)
            }
        }
    }

    fun pause() {
        timerJob?.cancel()
    }

    fun updateSteps(stepsCount: Int) {
        steps.value = stepsCount.toString()
        distance.value = String.format("%.2f km", stepsCount * 0.0008)
    }

    fun updateCalories(caloriesDouble: Double) {
        calories.value = caloriesDouble.toInt().toString()
    }
}

