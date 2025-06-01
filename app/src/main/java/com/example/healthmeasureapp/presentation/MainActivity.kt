package com.example.healthmeasureapp.presentation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.healthmeasureapp.R

class MainActivity : ComponentActivity() {

    private lateinit var timeText: TextView
    private lateinit var stepsText: TextView
    private lateinit var distanceText: TextView
    private lateinit var caloriesText: TextView

    private lateinit var viewModel: ExerciseViewModel

    private var started = false

    private val dataReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                "com.example.healthmeasureapp.STEP_DATA_UPDATE" -> {
                    val steps = intent.getIntExtra("steps", 0)
                    val caloriesDouble = intent.getDoubleExtra("calories", 0.0)

                    Log.d("MainActivity", "Received STEP_DATA_UPDATE steps=$steps, calories=$caloriesDouble")

                    if (!started && steps > 0) {
                        viewModel.start()
                        started = true
                        Log.d("MainActivity", "Timer started automatically on first data")
                    }

                    viewModel.updateSteps(steps)
                    viewModel.updateCalories(caloriesDouble)
                }

                "com.example.healthmeasureapp.EXERCISE_PAUSE" -> {
                    Log.d("MainActivity", "Received EXERCISE_PAUSE - pause timer")
                    viewModel.pause()
                    started = false
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timeText = findViewById(R.id.timeText)
        stepsText = findViewById(R.id.stepsText)
        distanceText = findViewById(R.id.distanceText)
        caloriesText = findViewById(R.id.caloriesText)

        viewModel = ViewModelProvider(this)[ExerciseViewModel::class.java]

        val filter = IntentFilter().apply {
            addAction("com.example.healthmeasureapp.STEP_DATA_UPDATE")
            addAction("com.example.healthmeasureapp.EXERCISE_PAUSE")
        }
        registerReceiver(dataReceiver, filter, Context.RECEIVER_EXPORTED)

        viewModel.time.observe(this) { timeText.text = it }
        viewModel.steps.observe(this) { stepsText.text = "👟 $it" }
        viewModel.distance.observe(this) { distanceText.text = "📏 $it" }
        viewModel.calories.observe(this) { caloriesText.text = "🔥 $it cal" }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(dataReceiver)
        viewModel.pause()
    }
}
