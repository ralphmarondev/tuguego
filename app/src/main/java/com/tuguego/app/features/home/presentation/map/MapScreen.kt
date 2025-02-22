package com.tuguego.app.features.home.presentation.map

import android.graphics.drawable.Drawable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.tuguego.app.R
import com.tuguego.app.features.home.domain.model.User
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun MapScreen() {
    val users = listOf(
        User(
            name = "Driver 1",
            latitude = 14.5995,
            longitude = 120.9842,
            iconRes = R.drawable.car_icon
        ),
        User(
            name = "Passenger 1",
            latitude = 14.6050,
            longitude = 121.9860,
            iconRes = R.drawable.user_icon
        ),
        User(
            name = "Passenger 2",
            latitude = 14.6300,
            longitude = 121.7200,
            iconRes = R.drawable.user_icon
        )
    )

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
                        // Tile source
                        setTileSource(TileSourceFactory.MAPNIK)
                        setMultiTouchControls(true)
                        isTilesScaledToDpi = true

                        // Center the map on Tuguegarao city
                        val mapController = controller
                        mapController.setZoom(12.0)
                        val startPoint = GeoPoint(14.6260, 121.7244) // Tuguegarao city coordinates
                        mapController.setCenter(startPoint)

                        users.forEach { user ->
                            val marker = Marker(this)
                            val userLocation = GeoPoint(user.latitude, user.longitude)
                            marker.position = userLocation
                            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

                            val icon: Drawable = context.resources.getDrawable(user.iconRes, null)
                            marker.icon = icon
                            marker.title = user.name

                            // Display popup with the name of the user when the marker is clicked
                            marker.setOnMarkerClickListener { marker, _ ->
                                // Show name as a popup
                                marker.showInfoWindow()
                                true
                            }
                            overlays.add(marker)
                        }
                    }
                }
            )
        }
    }
}
