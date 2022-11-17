package com.example.bmicalcapp

expect class LocationDetails {

    fun getLocationAddress (locationAddress: LocationAddress)
}

interface LocationAddress { // just to get async data
    fun onReceiveLocationAddress(address: String) : String
}