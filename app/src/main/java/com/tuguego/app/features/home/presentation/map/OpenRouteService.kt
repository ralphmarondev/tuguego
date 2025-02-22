package com.tuguego.app.features.home.presentation.map

import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY = "5b3ce3597851110001cf6248b32e2196717145b3b010f06d172b2fd2"

interface OpenRouteService {
    @GET("v2/directions/driving-car")
    suspend fun getRoute(
        @Query("api_key") apiKey: String,
        @Query("start") start: String,
        @Query("end") end: String
    ): RouteResponse
}

data class RouteResponse(
    val routes: List<Route>
)

data class Route(
    val geometry: String
)