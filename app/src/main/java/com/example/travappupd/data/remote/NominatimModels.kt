package com.example.travappupd.data.remote

import com.google.gson.annotations.SerializedName

data class NominatimResult(
    @SerializedName("display_name") val displayName: String,
    @SerializedName("lat") val lat: String,
    @SerializedName("lon") val lon: String,
    @SerializedName("name") val name: String? = null,
    @SerializedName("address") val address: NominatimAddress? = null
)

data class NominatimAddress(
    @SerializedName("country") val country: String? = null
)


data class GeocodingResult(
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val country: String? = null
)