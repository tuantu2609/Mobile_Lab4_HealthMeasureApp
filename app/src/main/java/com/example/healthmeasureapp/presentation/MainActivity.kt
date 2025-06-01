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
    private lateinit var heartRateText: TextView
    private lateinit var stepsText: TextView
    private lateinit var distanceText: TextView
    private lateinit var caloriesText: TextView

    private lateinit var viewModel: ExerciseViewModel

    private val dataReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val steps = intent?.getIntExtra("steps", 0) ?: 0
            val caloriesDouble = intent?.getDoubleExtra("calories", 0.0) ?: 0.0
            val points = intent?.getIntExtra("points", 0) ?: 0

            Log.d("MainActivity", "Received broadcast with steps=$steps, calories=$caloriesDouble, points=$points")

            viewModel.updateSteps(steps)
            viewModel.calories.value = caloriesDouble.toInt().toString()
            // Nếu có dùng points thì update luôn (nếu viewModel có support)
            // viewModel.points.value = points.toString()
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

        // Lắng nghe dữ liệu cập nhật
        registerReceiver(
            dataReceiver,
            IntentFilter("com.example.healthmeasureapp.STEP_DATA_UPDATE"),
            Context.RECEIVER_EXPORTED
        )

        // Quan sát LiveData để cập nhật UI
        viewModel.time.observe(this) { timeText.text = it }
        viewModel.steps.observe(this) { stepsText.text = "👟 $it" }
        viewModel.distance.observe(this) { distanceText.text = "📏 $it" }
        viewModel.calories.observe(this) { caloriesText.text = "🔥 $it cal" }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(dataReceiver)
    }
}
