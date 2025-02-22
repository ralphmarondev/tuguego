package com.tuguego.app.features.home.presentation.map

import android.graphics.Color
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polyline
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


private const val BASE_URL = "https://api.openrouteservice.org/"
suspend fun fetchAndDrawRoute(mapView: MapView, user: GeoPoint, driver: GeoPoint) {
    val start = "${driver.longitude},${driver.latitude}"
    val end = "${user.longitude},${user.latitude}"

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service =
        retrofit.create(OpenRouteService::class.java)

    try {
        // Request to OpenRouteService API with the driving-car profile for road-following route
        val response = withContext(Dispatchers.IO) {
            service.getRoute(API_KEY, start, end, "driving-car")
        }

        // Check if the response contains routes
        if (response.routes.isNotEmpty()) {
            val route = decodePolyline(response.routes[0].geometry)
            drawRoute(mapView, route)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}


private fun decodePolyline(encoded: String): List<GeoPoint> {
    val poly = mutableListOf<GeoPoint>()
    var index = 0
    val len = encoded.length
    var lat = 0
    var lng = 0

    while (index < len) {
        var b: Int
        var shift = 0
        var result = 0
        do {
            b = encoded[index++].code - 63
            result = result or (b and 0x1F shl shift)
            shift += 5
        } while (b >= 0x20)
        val deltaLat = if ((result and 1) != 0) (result shr 1).inv() else (result shr 1)
        lat += deltaLat

        shift = 0
        result = 0
        do {
            b = encoded[index++].code - 63
            result = result or (b and 0x1F shl shift)
            shift += 5
        } while (b >= 0x20)
        val deltaLng = if ((result and 1) != 0) (result shr 1).inv() else (result shr 1)
        lng += deltaLng

        val geoPoint = GeoPoint(lat / 1E5, lng / 1E5)
        poly.add(geoPoint)
        // Debug log to verify the decoded route points
        Log.d("Decoded Route", "Lat: ${geoPoint.latitude}, Lng: ${geoPoint.longitude}")
    }
    return poly
}

private fun drawRoute(mapView: MapView, route: List<GeoPoint>) {
    val polyline = Polyline().apply {
        setPoints(route)  // Set the decoded polyline
        color = Color.BLUE
        width = 8f
    }
    mapView.overlays.add(polyline)  // Add polyline to the map
    mapView.invalidate()  // Redraw the map to ensure the polyline is rendered
}
