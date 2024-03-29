package com.example.demoproject


import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.baseclasses.toast
import com.example.demoproject.databinding.ActivityGeofancMapBinding
import com.example.geofance.BaseGeofanceActivity
import com.example.geofance.Reminder
import com.example.geofance.UtilsGeofance
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker


class GeofancMapActivity : BaseGeofanceActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private val TAG = "GeoMapsActivity"
    private var binding: ActivityGeofancMapBinding? = null
    private var map: GoogleMap? = null
    private lateinit var locationManager: LocationManager

    companion object {
        private const val MY_LOCATION_REQUEST_CODE = 329
        private const val NEW_REMINDER_REQUEST_CODE = 330
        private const val EXTRA_LAT_LNG = "EXTRA_LAT_LNG"

        fun newIntent(context: Context, latLng: LatLng): Intent {
            val intent = Intent(context, GeofancMapActivity::class.java)
            intent.putExtra(EXTRA_LAT_LNG, latLng)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityGeofancMapBinding.inflate(layoutInflater);
        setContentView(binding!!.root)
        val supportMapFragment =
            (supportFragmentManager.findFragmentById(R.id.geofenceMap) as SupportMapFragment?)!!
        supportMapFragment.getMapAsync(this)
        binding!!.newReminder.visibility = View.GONE
        binding!!.currentLocation.visibility = View.GONE
        binding!!.newReminder.setOnClickListener {
            map?.run {
                val intent = AcitivityNewReminder.newIntent(
                    this@GeofancMapActivity,
                    cameraPosition.target,
                    cameraPosition.zoom
                )
                startActivityForResult(intent, NEW_REMINDER_REQUEST_CODE)
            }
        }
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                MY_LOCATION_REQUEST_CODE
            )
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map?.run {
            uiSettings.isMyLocationButtonEnabled = false
            uiSettings.isMapToolbarEnabled = false
            setOnMarkerClickListener(this@GeofancMapActivity)
        }
        onMapAndPermissionReady()
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val reminder = getRepository().get(marker.tag as String)

        if (reminder != null) {
            showReminderRemoveAlert(reminder)
        }

        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == NEW_REMINDER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            showReminders()

            val reminder = getRepository().getLast()
            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(reminder?.latLng, 15f))
            toast(this.getString(R.string.reminder_added_success))
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            onMapAndPermissionReady()
        }
    }

    private fun onMapAndPermissionReady() {
        if (map != null
            && ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            map?.isMyLocationEnabled = true
            binding!!.newReminder.visibility = View.VISIBLE
            binding!!.currentLocation.visibility = View.VISIBLE

            binding!!.currentLocation.setOnClickListener {
                val bestProvider = locationManager.getBestProvider(Criteria(), false)
                val location = locationManager.getLastKnownLocation(bestProvider!!)
                if (location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    map?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                }
            }

            showReminders()

            centerCamera()
        }
    }

    private fun showReminders() {
        map?.run {
            clear()
            for (reminder in getRepository().getAll()) {
                UtilsGeofance.showReminderInMap(this@GeofancMapActivity, this, reminder)
            }
        }
    }

    private fun centerCamera() {
        if (intent.extras != null && intent.extras!!.containsKey(EXTRA_LAT_LNG)) {
            val latLng = intent.extras!!.get(EXTRA_LAT_LNG) as LatLng
            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        }
    }

    private fun showReminderRemoveAlert(reminder: Reminder) {
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.run {
            setMessage(getString(R.string.reminder_removal_alert))
            setButton(
                AlertDialog.BUTTON_POSITIVE,
                getString(R.string.reminder_removal_alert_positive)
            ) { dialog, _ ->
                removeReminder(reminder)
                dialog.dismiss()
            }
            setButton(
                AlertDialog.BUTTON_NEGATIVE,
                getString(R.string.reminder_removal_alert_negative)
            ) { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }

    private fun removeReminder(reminder: Reminder) {
        getRepository().remove(
            reminder,
            success = {
                showReminders()
                toast(this.getString(R.string.reminder_removed_success))
            },
            failure = {

                toast(it)
            })
    }

}