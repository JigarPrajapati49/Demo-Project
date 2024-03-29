package com.example.geofance

import android.content.Context
import com.example.demoproject.R
import com.google.android.gms.location.GeofenceStatusCodes

object GeofenceErrorMessages {

    fun getErrorString(context: Context, e: Exception): String {
        return if (e is com.google.android.gms.common.api.ApiException) {
            getErrorString(context, 506)
        } else {
            context.resources.getString(R.string.geofence_unknown_error)
        }
    }

    fun getErrorString(context: Context, errorCodes: Int): String {
        val resources = context.resources

        return when (errorCodes) {
            GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE -> {
                resources.getString(R.string.geofence_not_available)
            }

            GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES ->
                resources.getString(R.string.geofence_too_many_geofences)

            GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS ->
                resources.getString(R.string.geofence_too_many_pending_intents)

            else -> {
                resources.getString(R.string.geofence_unknown_error)
            }
        }
    }

}