package com.example.rickandmorty.data.remote.apiservices

import com.example.rickandmorty.data.models.Location
import com.example.rickandmorty.data.models.RickAndMortyResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LocationApi {

    @GET("api/location")
    suspend fun getLocation(
        @Query("page") page: Int
    ): RickAndMortyResponse<Location>

    @GET("api/location/{id}")
    suspend fun getId(@Path("id") id: Int
    ): Location

}