package com.example.bmicalcapp.android

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.example.bmicalcapp.*
import com.example.bmicalcapp.android.databinding.ActivityMainBinding

fun greet(): String {
    return Greeting().greeting()
}

class MainActivity : AppCompatActivity() {
    private val requestcode: Int = 123
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.calculateBtn.setOnClickListener {
            val height = binding.etHeight.text.toString().trim().toFloat()
            val weight = binding.etWeight.text.toString().trim().toFloat()

            val bmIcalculator = BMIcalculator()
            val status = bmIcalculator.calculateBMI(height,weight)

            showAlert(status)

            if (isLocationPermissionGranted()) {
                val locationDetails  = NativeLocationDetailsAndroid(this)
                val profileDetails = ProfileDetails(locationDetails) // accessing interface type native impl
                locationDetails.apply {
                    getLocationAddress(object : LocationAddress {
                        override fun onReceiveLocationAddress(address: String): String {
                            runOnUiThread {
                                binding.address.text = address
                            }
                            return address
                        }
                    })
                }
            }

        }
    }

    fun showAlert(message: String) {
        AlertDialog.Builder(this)
            .setTitle("BMI STATUS")
            .setMessage("You are $message")
            .show()

    }

    private fun isLocationPermissionGranted(): Boolean {

        return if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                requestcode
            )
            false
        } else {
            true
        }
    }
}
