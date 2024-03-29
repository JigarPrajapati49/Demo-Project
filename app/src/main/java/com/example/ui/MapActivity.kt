package com.example.ui

import android.Manifest
import android.R.attr.x
import android.R.attr.y
import android.content.pm.PackageManager
import android.graphics.Point
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.example.baseclasses.BaseActivity
import com.example.baseclasses.BaseViewModel
import com.example.demoproject.R
import com.example.demoproject.databinding.ActivityMapBinding
import com.example.utils.PreferenceManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.*
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MapActivity : BaseActivity<BaseViewModel>(), OnMapReadyCallback, MapPresenter {
    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val permissionCode = 101
    private val TAG = MapActivity::class.java.simpleName

    private var mMap: GoogleMap? = null
    override val viewModel: BaseViewModel by viewModels()
    private lateinit var binding: ActivityMapBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = setDataBindingView(R.layout.activity_map)

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this@MapActivity)

        binding.mapPresenter = this

        fetchLocation()

    }

    override fun getLayoutId(): Int = R.layout.activity_map

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                permissionCode
            )
            return
        }
        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener {
            if (it != null) {
                currentLocation = it
                Toast.makeText(
                    applicationContext,
                    currentLocation.latitude.toString() + "" + currentLocation.longitude,
                    Toast.LENGTH_SHORT
                ).show()
                val supportMapFragment =
                    (supportFragmentManager.findFragmentById(R.id.myMap) as SupportMapFragment?)!!
                supportMapFragment.getMapAsync(this@MapActivity)
            } else {
                Log.e("TAG", "fetchLocation: -------------null")
            }
        }


    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
        val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
        val markerOptions = MarkerOptions().position(latLng).title("My Current Location")
        googleMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f))
        googleMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap?.addMarker(markerOptions)
        val cameraPosition = CameraPosition.Builder()
            .target(latLng)
            .zoom(24f)
            .bearing(-30f)
            .tilt(75f)
            .build()

        googleMap?.addMarker(MarkerOptions().position(latLng))

        googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

        googleMap?.isMyLocationEnabled = true
        googleMap?.uiSettings?.isScrollGesturesEnabled = true

        googleMap?.uiSettings?.isMapToolbarEnabled = true
        val projection = googleMap!!.projection
        Log.e("PROJECTION", projection.fromScreenLocation(Point(x, y)).toString())
        googleMap.isTrafficEnabled = true
        googleMap.isIndoorEnabled = true
        googleMap.isBuildingsEnabled = true

        setMapLongClick(googleMap)

        googleMap.mapType = PreferenceManager.getPref("SELECTED_MAP_TYPE", MAP_TYPE_NORMAL)

        val mapStyleOptions = MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style)
        googleMap.setMapStyle(mapStyleOptions)

    }


    private fun setMapLongClick(map: GoogleMap) {

        map.setOnMapLongClickListener { latLng ->
            val snippet = String.format(
                Locale.getDefault(),
                "Lat: %1$.5f, Long: %2$.5f",
                latLng.latitude,
                latLng.longitude
            )

            val addresses: List<Address>?
            val geocoder = Geocoder(this, Locale.getDefault())

            addresses = geocoder.getFromLocation(
                latLng.latitude,
                latLng.longitude,
                1
            )
            val address = addresses!![0].getAddressLine(0)
            val city = addresses[0].locality
            val state = addresses[0].adminArea
            val country = addresses[0].countryName
            val postalCode = addresses[0].postalCode
            val knownName = addresses[0].featureName

            Log.e(
                TAG,
                "setMapLongClick: ------------Location Details -----------city -- > $city ----> state --->$state----->country $country----postalCode-->$postalCode-----KnownName---$knownName",
            )

            map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(address)
                    .draggable(true)
                    .snippet(snippet)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            )

            val overlaySize = 10f
            val androidOverlay = GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.map))
                .position(latLng, overlaySize)
            map.addGroundOverlay(androidOverlay)

        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            permissionCode -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation()
            }
        }
    }

    override fun onHybridClick() {
        mMap?.mapType = MAP_TYPE_HYBRID
        PreferenceManager.savePref("SELECTED_MAP_TYPE", MAP_TYPE_HYBRID)
        Log.e(TAG, "onHybridClick: -------------------onHybridClick--------------")
    }

    override fun onSatelliteClick() {
        mMap?.mapType = MAP_TYPE_SATELLITE
        PreferenceManager.savePref("SELECTED_MAP_TYPE", MAP_TYPE_SATELLITE)
        Log.e(TAG, "onHybridClick: -------------------onSatelliteClick--------------")
    }

    override fun onTerrainClick() {
        mMap?.mapType = MAP_TYPE_TERRAIN
        PreferenceManager.savePref("SELECTED_MAP_TYPE", MAP_TYPE_TERRAIN)
        Log.e(TAG, "onHybridClick: -------------------onTerrainClick--------------")
    }


}