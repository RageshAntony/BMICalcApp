package com.example.bmicalcapp

interface NativeLocationDetails {

    fun getLocationAddress (locationAddress: LocationAddress)
}

interface NativeLocationAddress {
    fun onReceiveLocationAddress(address: String) : String
}