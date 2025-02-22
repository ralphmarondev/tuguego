package com.tuguego.app.features.home.presentation.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.tuguego.app.R
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun MapScreen() {
    val userLocation = GeoPoint(17.6584574, 121.7548505)  // User's fixed location
    val initialDriverLocation = GeoPoint(17.6300, 121.7200)  // Driver's starting point

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    MapView(context).apply {
                        setTileSource(TileSourceFactory.MAPNIK)
                        setMultiTouchControls(true)
                        isTilesScaledToDpi = true
                        controller.setZoom(12.0)
                        controller.setCenter(userLocation)

                        // Add User Marker (Fixed)
                        val userMarker = Marker(this).apply {
                            position = userLocation
                            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                            icon = context.resources.getDrawable(R.drawable.user_icon, null)
                            title = "You"
                        }
                        overlays.add(userMarker)

                        // Add Driver Marker (Moving)
                        val driverMarker = Marker(this).apply {
                            position = initialDriverLocation
                            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                            icon = context.resources.getDrawable(R.drawable.car_icon, null)
                            title = "Driver"
                        }
                        overlays.add(driverMarker)

                        // Start Real-Time Movement
                        startRealTimeRouting(this, userLocation, driverMarker)
                    }
                }
            )
        }
    }
}
