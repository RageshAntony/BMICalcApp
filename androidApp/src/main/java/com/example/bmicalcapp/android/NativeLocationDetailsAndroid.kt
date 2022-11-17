package com.example.bmicalcapp.android

import android.annotation.SuppressLint
import android.app.Activity
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import com.example.bmicalcapp.LocationAddress
import com.example.bmicalcapp.NativeLocationDetails
import com.google.android.gms.location.*
import java.util.*

class NativeLocationDetailsAndroid(var activity: Activity) : NativeLocationDetails {

   // lateinit var activity: Activity // non - actual field member

    @SuppressLint("MissingPermission")
    override fun getLocationAddress(locationAddress: LocationAddress) {

        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
        lateinit var locationCallback: LocationCallback

        val locationRequest: LocationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1500)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(1000)
            .setMaxUpdateDelayMillis(2000)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationAddress.onReceiveLocationAddress(
                    locationResult.lastLocation?.let {
                        getAddress(it)
                    } ?: "Location error"
                ) } }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()!!
        ) }

    fun getAddress(latLng: Location): String? {
        val geocoder = Geocoder(activity, Locale.getDefault())
        val addresses: List<Address>? =
            geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        return if (addresses?.isNotEmpty() == true) {
            val address = addresses[0]
            address.getAddressLine(0)
        } else {
            "Location not found"
        }
    }
}