package com.example.bmicalcapp


import kotlinx.cinterop.useContents
import platform.CoreLocation.*
import platform.Foundation.NSError

actual class LocationDetails {
    actual fun getLocationAddress(locationAddress: LocationAddress) {

        val locationManager = CLLocationManager()
        val currentLoc: CLLocation

        locationManager.requestWhenInUseAuthorization()

        if (locationManager.authorizationStatus() in listOf(kCLAuthorizationStatusAuthorizedWhenInUse,
                kCLAuthorizationStatusAuthorizedAlways,kCLAuthorizationStatusAuthorized)
        ) {
            currentLoc = locationManager.location!!
            val locationCord = currentLoc.coordinate.useContents { this }
            print("THE CORDS ARE ${locationCord.latitude} , ${locationCord.longitude}")

            val clDecoder = CLGeocoder()

            clDecoder.reverseGeocodeLocation(currentLoc, object : CLGeocodeCompletionHandler {
                override fun invoke(p1: List<*>?, p2: NSError?) {
                    val placmarks = p1 as List<CLPlacemark>?
                    val res = placmarks?.get(0)?.let {
                        it.name + ", " + it.locality + ", " +
                                it.administrativeArea + ", " + it.country
                    }
                    locationAddress.onReceiveLocationAddress("The location is $res ")
                }
            })
        } else {
            locationAddress.onReceiveLocationAddress("The location  is failed ")
        }
    }
}