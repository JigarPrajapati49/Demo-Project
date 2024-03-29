package com.example.demoproject


import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.example.baseclasses.toast
import com.example.geofance.GeofenceErrorMessages
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class Map2 : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapLongClickListener {
    private val TAG = "MapsActivity"

    private var mMap: GoogleMap? = null
    private var geofencingClient: GeofencingClient? = null
    private var geofenceHelper: GeofenceHelper? = null

    private val GEOFENCE_RADIUS = 200f
    private val GEOFENCE_ID = "SOME_GEOFENCE_ID"
    val r = 1

    private val FINE_LOCATION_ACCESS_REQUEST_CODE = 10001
    private val BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map2)


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        geofencingClient = LocationServices.getGeofencingClient(this)
        geofenceHelper = GeofenceHelper(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val eiffel = LatLng(48.8589, 2.29365)
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(eiffel, 16f))
        enableUserLocation()
        mMap!!.setOnMapLongClickListener(this)
    }

    private fun enableUserLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap!!.isMyLocationEnabled = true
        } else {
            //Ask for permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                //We need to show user a dialog for displaying why the permission is needed and then ask for the permission...
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), FINE_LOCATION_ACCESS_REQUEST_CODE)
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                    FINE_LOCATION_ACCESS_REQUEST_CODE
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == r) {
            if (resultCode == RESULT_OK) {
                val result: Double = data?.getDoubleExtra("progress", 0.0)!!
                mMap?.run {
                    addCircle(cameraPosition!!.target, result.toFloat())
                }
                Log.e(TAG, "onActivityResult: ===================$result")
            }

        }
    }

    override fun onMapLongClick(latLng: LatLng) {
        if (Build.VERSION.SDK_INT >= 29) {
            //We need background permission
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                handleMapLongClick(latLng)
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                    //We show a dialog and ask for permission
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                        BACKGROUND_LOCATION_ACCESS_REQUEST_CODE
                    )
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                        BACKGROUND_LOCATION_ACCESS_REQUEST_CODE
                    )
                }
            }
        } else {
            handleMapLongClick(latLng)
        }
    }


    private fun handleMapLongClick(latLng: LatLng) {
        mMap!!.clear()
        // first set the Marker
        showDialog(latLng)
    }

    fun showDialog(latLng: LatLng) {
        val builder = AlertDialog.Builder(this@Map2)
        builder.setMessage("Do you want to set Custom Radius?")
        builder.setCancelable(false)
        builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
            // When the user click yes button then app will close
            // set custom Radious
            toast("Set radius")
            val i = Intent(applicationContext, ActivityCustomRadius::class.java)
            startActivityForResult(i, r)

        } as DialogInterface.OnClickListener)
        builder.setNegativeButton("No", DialogInterface.OnClickListener { dialog: DialogInterface, which: Int ->

            dialog.cancel()
            addMarker(latLng)
            // add Circle for geofence radius
            addCircle(latLng, GEOFENCE_RADIUS)

            addGeofence(latLng, GEOFENCE_RADIUS)
        } as DialogInterface.OnClickListener)

        val alertDialog = builder.create()

        alertDialog.show()
    }

    // Starting From Here
    private fun addGeofence(latLng: LatLng, radius: Float) {
        // Step 1 Add Geofence List According to Official Document
        val geofence = geofenceHelper!!.getGeofence(
            GEOFENCE_ID,
            latLng,
            radius,
            Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_DWELL or Geofence.GEOFENCE_TRANSITION_EXIT
        )
        // Step 2 Get Geofence List According to Official Document
        val geofencingRequest = geofenceHelper!!.getGeofencingRequest(geofence)
        // Step 3 Get Broadcast According to Official Document
        val pendingIntent = geofenceHelper!!.getPendingIntent()

        geofencingClient!!.addGeofences(geofencingRequest, pendingIntent)
            .addOnSuccessListener {
                Log.d(TAG, "onSuccess: Geofence Added...")
            }
            .addOnFailureListener { e ->
                GeofenceErrorMessages.getErrorString(this, e)
                val errorMessage = geofenceHelper!!.getErrorString(e)
                Log.d(TAG, "onFailure: $errorMessage")
            }
    }


    private fun addMarker(latLng: LatLng) {
        val markerOptions = MarkerOptions().position(latLng)
        mMap!!.addMarker(markerOptions)
    }

    private fun addCircle(latLng: LatLng, radius: Float) {
        val circleOptions = CircleOptions()
        circleOptions.center(latLng)
        circleOptions.radius(radius.toDouble())
        circleOptions.strokeColor(Color.argb(255, 255, 0, 0))
        circleOptions.fillColor(Color.argb(64, 255, 0, 0))
        circleOptions.strokeWidth(4f)
        mMap!!.addCircle(circleOptions)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == FINE_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //We have the permission
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return
                }
                mMap!!.isMyLocationEnabled = true
            } else {
                toast("Permission Denied")
                toast("Please enable location Permission")
                //We do not have the permission..
            }
        }
        if (requestCode == BACKGROUND_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //We have the permission
                Toast.makeText(this, "You can add geofences...", Toast.LENGTH_SHORT).show()
            } else {
                //We do not have the permission..
                Toast.makeText(this, "Background location access is neccessary for geofences to trigger...", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun askPermissionForBackgroundUsage() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this@Map2, Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
            AlertDialog.Builder(this)
                .setTitle("Permission Needed!")
                .setMessage("Background Location Permission Needed!, tap \"Allow all time in the next screen\"")
                .setPositiveButton(
                    "OK"
                ) { dialog, which ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        ActivityCompat.requestPermissions(
                            this@Map2,
                            arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                            400
                        )
                    }
                }
                .setNegativeButton("CANCEL") { dialog, which ->
                    // User declined for Background Location Permission.
                }
                .create().show()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                    400
                )
            }
        }
    }

    private fun askForLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this@Map2, Manifest.permission.ACCESS_FINE_LOCATION)) {
            AlertDialog.Builder(this)
                .setTitle("Permission Needed!")
                .setMessage("Location Permission Needed!")
                .setPositiveButton("OK"
                ) { dialog, which ->
                    ActivityCompat.requestPermissions(
                        this@Map2,
                        arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                        300
                    )
                }
                .setNegativeButton("CANCEL") { dialog, which ->
                    // Permission is denied by the user
                }
                .create().show()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 300)
        }
    }


}

