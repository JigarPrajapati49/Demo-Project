package com.example.demoproject

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.example.demoproject.databinding.ActivityCustomRadiusBinding
import kotlin.math.roundToInt

class ActivityCustomRadius : AppCompatActivity() {
    private var binding: ActivityCustomRadiusBinding? = null
    var progress: Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomRadiusBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        binding!!.radiusBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seek: SeekBar,
                progress: Int, fromUser: Boolean,
            ) {
                // write custom code for progress is changed
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                // write custom code for progress is stopped
                progress = updateRadius(seek.progress)

            }
        })

        binding!!.next.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putExtra("progress", progress)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }

    private fun updateRadius(progress: Int): Double {
        val radius = getRadius(progress)

        binding!!.radiusDescription.text = getString(R.string.radius_description, radius.roundToInt().toString())
        return radius
    }

    private fun getRadius(progress: Int) = 100 + (2 * progress.toDouble() + 1) * 100
}