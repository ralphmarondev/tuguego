package com.tuguego.app.features.home.presentation.map

import kotlinx.coroutines.*
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import kotlin.math.sqrt

var routeJob: Job? = null

fun startRealTimeRouting(mapView: MapView, user: GeoPoint, driverMarker: Marker) {
    routeJob?.cancel()
    routeJob = CoroutineScope(Dispatchers.Main).launch {
        var driver = driverMarker.position
        while (isActive) {
            driver = moveDriverTowardsUser(driver, user) // Move driver gradually
            driverMarker.position = driver
            driverMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            mapView.invalidate() // Refresh map UI

            fetchAndDrawRoute(mapView, user, driver) // Update route

            delay(3000) // Repeat every 3 sec
        }
    }
}

// Moves the driver a little closer to the user
fun moveDriverTowardsUser(driver: GeoPoint, user: GeoPoint, speed: Double = 0.0005): GeoPoint {
    val latDiff = user.latitude - driver.latitude
    val lonDiff = user.longitude - driver.longitude
    val distance = sqrt(latDiff * latDiff + lonDiff * lonDiff)

    return if (distance < speed) {
        user  // Stop moving if close enough
    } else {
        GeoPoint(
            driver.latitude + (latDiff / distance) * speed,
            driver.longitude + (lonDiff / distance) * speed
        )
    }
}