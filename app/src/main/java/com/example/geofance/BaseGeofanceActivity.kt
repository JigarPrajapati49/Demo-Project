package com.example.geofance

import androidx.appcompat.app.AppCompatActivity
import com.example.DemoProjectApplication

abstract class BaseGeofanceActivity : AppCompatActivity() {
    fun getRepository() = (application as DemoProjectApplication).getRepository()
}