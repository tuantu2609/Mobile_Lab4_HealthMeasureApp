package com.example.healthmeasureapp.sync

import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.wearable.*

class WearDataListenerService : WearableListenerService() {
    override fun onDataChanged(dataEvents: DataEventBuffer) {
        Log.d("WearDataListener", "onDataChanged called with ${dataEvents.count} events")
        for (event in dataEvents) {
            if (event.type == DataEvent.TYPE_CHANGED) {
                val path = event.dataItem.uri.path
                if (path == "/fitness_data") {
                    val dataMap = DataMapItem.fromDataItem(event.dataItem).dataMap
                    val steps = dataMap.getInt("steps")
                    val calories = dataMap.getDouble("calories")
                    val points = dataMap.getInt("points")

                    Log.d("WearDataListener", "Received steps=$steps, calories=$calories, points=$points")

                    val intent = Intent("com.example.healthmeasureapp.STEP_DATA_UPDATE").apply {
                        putExtra("steps", steps)
                        putExtra("calories", calories)
                        putExtra("points", points)
                    }

                    sendBroadcast(intent)
                    Log.d("WearDataListener", "Broadcast sent with step data")
                }
            }
        }
    }
}
