package com.example.healthmeasureapp.presentation

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.healthmeasureapp.R
import com.example.healthmeasureapp.SensorHandler

class MainActivity : ComponentActivity(), SensorHandler.SensorCallback {

    private lateinit var viewModel: ExerciseViewModel
    private lateinit var sensorHandler: SensorHandler

    private lateinit var timeText: TextView
    private lateinit var heartRateText: TextView
    private lateinit var stepsText: TextView
    private lateinit var distanceText: TextView
    private lateinit var caloriesText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timeText = findViewById(R.id.timeText)
        heartRateText = findViewById(R.id.heartRateText)
        stepsText = findViewById(R.id.stepsText)
        distanceText = findViewById(R.id.distanceText)
        caloriesText = findViewById(R.id.caloriesText)

        val startButton = findViewById<Button>(R.id.startButton)
        val pauseButton = findViewById<Button>(R.id.pauseButton)
        
        viewModel = ViewModelProvider(this)[ExerciseViewModel::class.java]
        sensorHandler = SensorHandler(this, this)
        sensorHandler.register()


        startButton.setOnClickListener {
            viewModel.start()
        }

        pauseButton.setOnClickListener {
            viewModel.pause()
        }

        viewModel.time.observe(this) {
            timeText.text = it
        }

        viewModel.heartRate.observe(this) {
            heartRateText.text = "❤️ $it bpm"
        }

        viewModel.steps.observe(this) {
            stepsText.text = "👟 $it"
        }

        viewModel.distance.observe(this) {
            distanceText.text = "📏 $it"
        }

        viewModel.calories.observe(this) {
            caloriesText.text = "🔥 $it cal"
        }
    }

    override fun onHeartRateChanged(bpm: Float) {
        viewModel.updateHeartRate(bpm)
    }

    override fun onStepCountChanged(steps: Int) {
        viewModel.updateSteps(steps)
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorHandler.unregister()
    }
}
