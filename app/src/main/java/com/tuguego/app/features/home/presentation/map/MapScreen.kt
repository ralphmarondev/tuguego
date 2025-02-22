package com.tuguego.app.features.home.presentation.map

import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.tuguego.app.R
import com.tuguego.app.features.home.domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import java.net.URL
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun MapScreen() {
    val context = LocalContext.current

    val users = listOf(
        User(
            name = "You",
            latitude = 17.6584574,
            longitude = 121.7548505,
            iconRes = R.drawable.user_icon
        ),
        User(
            name = "Driver 1",
            latitude = 17.6780,
            longitude = 121.7645,
            iconRes = R.drawable.car_icon
        ),
        User(
            name = "Driver 2",
            latitude = 17.6300,
            longitude = 121.7200,
            iconRes = R.drawable.car_icon
        )
    )

    var mapView: MapView? by remember { mutableStateOf(null) }
    val userLocation = GeoPoint(17.6584574, 121.7548505)

    val nearestDriver = findNearestDriver(userLocation, users)

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    MapView(ctx).apply {
                        setTileSource(TileSourceFactory.MAPNIK)
                        setMultiTouchControls(true)
                        isTilesScaledToDpi = true

                        val mapController = controller
                        mapController.setZoom(12.0)
                        mapController.setCenter(userLocation)

                        users.forEach { user ->
                            val marker = Marker(this)
                            val userPoint = GeoPoint(user.latitude, user.longitude)
                            marker.position = userPoint
                            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

                            val icon: Drawable = context.resources.getDrawable(user.iconRes, null)
                            marker.icon = icon
                            marker.title = user.name

                            marker.setOnMarkerClickListener { m, _ ->
                                m.showInfoWindow()
                                true
                            }

                            overlays.add(marker)
                        }

                        mapView = this // Save reference to the map
                    }
                }
            )
        }
    }

    // Launch route fetching inside a coroutine
    LaunchedEffect(nearestDriver) {
        nearestDriver?.let {
            val driverLocation = GeoPoint(it.latitude, it.longitude)
            mapView?.let { map -> fetchAndDrawRoute(map, userLocation, driverLocation) }
        }
    }
}

/**
 * Calculate the nearest driver to the user using Haversine formula
 */
fun findNearestDriver(userLocation: GeoPoint, users: List<User>): User? {
    return users.filter { it.name.contains("Driver") }
        .minByOrNull {
            distanceBetween(
                userLocation.latitude,
                userLocation.longitude,
                it.latitude,
                it.longitude
            )
        }
}

/**
 * Haversine formula to calculate distance between two GPS points
 */
fun distanceBetween(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val R = 6371e3 // Earth radius in meters
    val phi1 = Math.toRadians(lat1)
    val phi2 = Math.toRadians(lat2)
    val deltaPhi = Math.toRadians(lat2 - lat1)
    val deltaLambda = Math.toRadians(lon2 - lon1)

    val a = sin(deltaPhi / 2) * sin(deltaPhi / 2) +
            cos(phi1) * cos(phi2) * sin(deltaLambda / 2) * sin(deltaLambda / 2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return R * c // Distance in meters
}

/**
 * Fetch route from OSRM API and draw it on the map
 */
suspend fun fetchRoute(start: GeoPoint, end: GeoPoint): List<GeoPoint> {
    return withContext(Dispatchers.IO) {
        val url =
            "https://router.project-osrm.org/route/v1/driving/${start.longitude},${start.latitude};${end.longitude},${end.latitude}?overview=full&geometries=geojson"
        val response = URL(url).readText()
        val json = JSONObject(response)

        val coordinates = json.getJSONArray("routes")
            .getJSONObject(0)
            .getJSONObject("geometry")
            .getJSONArray("coordinates")

        (0 until coordinates.length()).map {
            val coord = coordinates.getJSONArray(it)
            GeoPoint(coord.getDouble(1), coord.getDouble(0))
        }
    }
}

/**
 * Draws a route on the map
 */
suspend fun fetchAndDrawRoute(map: MapView, start: GeoPoint, end: GeoPoint) {
    val routePoints = fetchRoute(start, end)

    val routeLine = Polyline()
    routeLine.setPoints(routePoints)
    routeLine.color = Color.BLUE
    routeLine.width = 5f

    map.overlays.add(routeLine)
    map.invalidate()
}
