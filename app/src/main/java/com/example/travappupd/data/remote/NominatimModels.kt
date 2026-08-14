package com.example.travappupd.data.remote

import com.google.gson.annotations.SerializedName

data class NominatimResult(
    @SerializedName("display_name") val displayName: String,
    @SerializedName("lat") val lat: String,
    @SerializedName("lon") val lon: String,
    @SerializedName("name") val name: String? = null
)

