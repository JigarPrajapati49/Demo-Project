package com.example.geofance

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class GeofenceBroadcastReceivers : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, "Broadcast Received", Toast.LENGTH_SHORT).show()
        Log.e("TAG", "onReceive: ============================BroadCast", )
        GeofenceTransitionsJobIntentService.enqueueWork(context, intent)
    }
}