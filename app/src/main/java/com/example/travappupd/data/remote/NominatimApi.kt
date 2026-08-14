package com.example.travappupd.data.remote

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import com.example.travappupd.data.remote.NominatimResult

interface NominatimApi {
    @GET("search")
    suspend fun search(
        @Query("q") query: String,
        @Query("format") format: String = "json",
        @Query("limit") limit: Int = 5,
        @Header("User-Agent") userAgent: String = "TravAppUpd/1.0 (student project)"
    ): List<NominatimResult>
}