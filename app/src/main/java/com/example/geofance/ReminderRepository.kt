package com.example.geofance

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson

class ReminderRepository(val context: Context) {
    companion object {
        private const val PREFS_NAME = "ReminderRepository"
        private const val REMINDERS = "REMINDERS"
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()
    private val geofencingClient = LocationServices.getGeofencingClient(context)

    fun add(
        reminder: Reminder,
        success: () -> Unit,
        failure: (error: String) -> Unit,
    ) {
        val geofence = buildGeofence(reminder)

        if (geofence != null && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // step 3
            geofencingClient.addGeofences(buildGeofencingRequest(geofence), geofencePendingIntent).addOnSuccessListener {
                saveAll(getAll() + reminder)
                success()
            }
                .addOnFailureListener {
                    failure(GeofenceErrorMessages.getErrorString(context, it))
                }

        }
    }

    // for buildGeofence 1 Step...
    fun buildGeofence(reminders: Reminder): Geofence? {
        val lati = reminders.latLng?.latitude
        val long = reminders.latLng?.longitude
        val radius = reminders.radius

        if (lati != null && long != null && radius != null) {
            return Geofence.Builder()
                .setRequestId(reminders.id)
                .setCircularRegion(lati, long, radius.toFloat())
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build()
        }
        return null
    }

    // for build the request Step 2
    private fun buildGeofencingRequest(geofence: Geofence): GeofencingRequest {
        return GeofencingRequest.Builder()
            .setInitialTrigger(/*0*/GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofences(listOf(geofence))
            .build()
    }

    // step 3
    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(context, GeofenceBroadcastReceivers::class.java)
        PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    fun remove(reminders: Reminder, success: () -> Unit, failure: (error: String) -> Unit) {
        geofencingClient.removeGeofences(listOf(reminders.id))
            .addOnSuccessListener {
                saveAll(getAll() - reminders)
                success()
            }
            .addOnFailureListener {
                failure(GeofenceErrorMessages.getErrorString(context, it))
            }
    }

    private fun saveAll(list: List<Reminder>) {
        preferences
            .edit()
            .putString(REMINDERS, gson.toJson(list))
            .apply()
    }

    fun getAll(): List<Reminder> {
        if (preferences.contains(REMINDERS)) {
            val remindersString = preferences.getString(REMINDERS, null)
            val arrayOfReminders = gson.fromJson(
                remindersString,
                Array<Reminder>::class.java
            )
            if (arrayOfReminders != null) {
                return arrayOfReminders.toList()
            }
        }
        return listOf()
    }

    fun get(requestId: String?) = getAll().firstOrNull { it.id == requestId }

    fun getLast() = getAll().lastOrNull()
}
