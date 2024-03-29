package com.example.demoproject

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import com.example.baseclasses.toast
import com.example.demoproject.databinding.ActivityAcitivityNewReminderBinding
import com.example.geofance.BaseGeofanceActivity
import com.example.geofance.Reminder
import com.example.geofance.UtilsGeofance
import com.example.geofance.UtilsGeofance.requestFocusWithKeyboard
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import kotlin.math.roundToInt

class AcitivityNewReminder : BaseGeofanceActivity(), OnMapReadyCallback {
    private var reminder = Reminder(latLng = null, radius = null, message = null)
    private var binding: ActivityAcitivityNewReminderBinding? = null
    private lateinit var map: GoogleMap
    private val radiusBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
        override fun onStartTrackingTouch(seekBar: SeekBar?) {}

        override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            updateRadiusWithProgress(progress)
            showReminderUpdate()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAcitivityNewReminderBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        binding!!.instructionTitle.visibility = View.GONE
        binding!!.instructionSubtitle.visibility = View.GONE
        binding!!.radiusBar.visibility = View.GONE
        binding!!.radiusDescription.visibility = View.GONE
        binding!!.message.visibility = View.GONE

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isMapToolbarEnabled = false

        centerCamera()

        showConfigureLocationStep()
    }

    private fun updateRadiusWithProgress(progress: Int) {
        val radius = getRadius(progress)
        reminder.radius = radius
        binding!!.radiusDescription.text = getString(R.string.radius_description, radius.roundToInt().toString())
    }

    private fun centerCamera() {
        val latLng = intent.extras!!.get(EXTRA_LAT_LNG) as LatLng
        val zoom = intent.extras!!.get(EXTRA_ZOOM) as Float
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }

    private fun showConfigureLocationStep() {
        binding!!.marker.visibility = View.VISIBLE
        binding!!.instructionTitle.visibility = View.VISIBLE
        binding!!.instructionSubtitle.visibility = View.VISIBLE
        binding!!.radiusBar.visibility = View.GONE
        binding!!.radiusDescription.visibility = View.GONE
        binding!!.message.visibility = View.GONE
        binding!!.instructionTitle.text = getString(R.string.instruction_where_description)
        binding!!.next.setOnClickListener {
            reminder.latLng = map.cameraPosition.target
            showConfigureRadiusStep()
        }

        showReminderUpdate()
    }

    private fun showConfigureRadiusStep() {
        binding!!.marker.visibility = View.GONE
        binding!!.instructionTitle.visibility = View.VISIBLE
        binding!!.instructionSubtitle.visibility = View.GONE
        binding!!.radiusBar.visibility = View.VISIBLE
        binding!!.radiusDescription.visibility = View.VISIBLE
        binding!!.message.visibility = View.GONE
        binding!!.instructionTitle.text = getString(R.string.instruction_radius_description)
        binding!!.next.setOnClickListener {
            showConfigureMessageStep()
        }
        binding!!.radiusBar.setOnSeekBarChangeListener(radiusBarChangeListener)
        updateRadiusWithProgress(binding!!.radiusBar.progress)

        map.animateCamera(CameraUpdateFactory.zoomTo(15f))

        showReminderUpdate()
    }

    private fun getRadius(progress: Int) = 100 + (2 * progress.toDouble() + 1) * 100

    private fun showConfigureMessageStep() {
        binding!!.marker.visibility = View.GONE
        binding!!.instructionTitle.visibility = View.VISIBLE
        binding!!.instructionSubtitle.visibility = View.GONE
        binding!!.radiusBar.visibility = View.GONE
        binding!!.radiusDescription.visibility = View.GONE
        binding!!.message.visibility = View.VISIBLE
        binding!!.instructionTitle.text = getString(R.string.instruction_message_description)
        binding!!.next.setOnClickListener {
            UtilsGeofance.hideKeyboard(this, binding!!.message)

            reminder.message = binding!!.message.text.toString()

            if (reminder.message.isNullOrEmpty()) {
                binding!!.message.error = getString(R.string.error_required)
            } else {
                addReminder(reminder)
            }
        }
        binding!!.message.requestFocusWithKeyboard()

        showReminderUpdate()
    }

    private fun addReminder(reminder: Reminder) {
        getRepository().add(reminder,
            success = {
                setResult(Activity.RESULT_OK)
                finish()
            },
            failure = {
                toast(it)
            })
    }

    private fun showReminderUpdate() {
        map.clear()
        UtilsGeofance.showReminderInMap(this, map, reminder)
    }


    companion object {
        private const val EXTRA_LAT_LNG = "EXTRA_LAT_LNG"
        private const val EXTRA_ZOOM = "EXTRA_ZOOM"

        fun newIntent(context: Context, latLng: LatLng, zoom: Float): Intent {
            val intent = Intent(context, AcitivityNewReminder::class.java)
            intent
                .putExtra(EXTRA_LAT_LNG, latLng)
                .putExtra(EXTRA_ZOOM, zoom)
            return intent
        }
    }
}