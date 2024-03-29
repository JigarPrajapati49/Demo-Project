package com.example.geofance

import com.google.android.gms.maps.model.LatLng
import java.util.UUID

data class Reminder(
    val id: String = UUID.randomUUID().toString(),
    var latLng: LatLng?,
    var radius: Double?,
    var message: String?,
)
