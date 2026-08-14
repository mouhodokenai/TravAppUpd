package com.example.travappupd.data.repositories

import com.example.travappupd.data.remote.NominatimApi
import javax.inject.Inject

data class GeocodingResult(
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double
)

interface GeocodingRepository {
    suspend fun search(query: String): List<GeocodingResult>
}

class GeocodingRepositoryImpl @Inject constructor(
    private val api: NominatimApi
) : GeocodingRepository {

    override suspend fun search(query: String): List<GeocodingResult> {
        if (query.isBlank()) return emptyList()
        return try {
            api.search(query = query).map { result ->
                GeocodingResult(
                    name = result.name ?: result.displayName.substringBefore(","),
                    address = result.displayName,
                    latitude = result.lat.toDoubleOrNull() ?: 0.0,
                    longitude = result.lon.toDoubleOrNull() ?: 0.0
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}