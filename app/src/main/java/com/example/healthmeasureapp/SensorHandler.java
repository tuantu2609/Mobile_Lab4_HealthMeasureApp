package com.example.healthmeasureapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorHandler implements SensorEventListener {

    private final SensorManager sensorManager;
    private final Sensor heartSensor;
    private final Sensor stepSensor;
    private final SensorCallback callback;

    public interface SensorCallback {
        void onHeartRateChanged(float bpm);
        void onStepCountChanged(int steps);
    }

    public SensorHandler(Context context, SensorCallback callback) {
        this.callback = callback;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        heartSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
    }

    public void register() {
        if (heartSensor != null)
            sensorManager.registerListener(this, heartSensor, SensorManager.SENSOR_DELAY_NORMAL);
        if (stepSensor != null)
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregister() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_HEART_RATE) {
            callback.onHeartRateChanged(event.values[0]);
        } else if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            callback.onStepCountChanged((int) event.values[0]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
