package com.dev.nagdaadmin.data.geofence

import com.google.firebase.firestore.GeoPoint
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class GeoFenceChecker {

    private fun calculateDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
        val earthRadius = 6371e3
        val dLat = Math.toRadians(lat2 - lat1)
        val dLng = Math.toRadians(lng2 - lng1)

        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLng / 2) * sin(dLng / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadius * c
    }

    private fun calculateRadiusFromArea(area: Double): Double {
        return sqrt((area) / Math.PI)
    }

    fun isWithinGeofence(userLocation: GeoPoint, companyLocation: GeoPoint?, area: Double): Boolean {
        if (companyLocation == null) return false

        val radius = calculateRadiusFromArea(area)
        val distance = calculateDistance(
            userLocation.latitude, userLocation.longitude,
            companyLocation.latitude, companyLocation.longitude
        )

        return distance <= 2*radius
    }
}