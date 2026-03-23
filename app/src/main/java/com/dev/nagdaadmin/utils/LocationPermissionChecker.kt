package com.dev.nagdaadmin.utils

import android.Manifest
import android.R
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class LocationPermissionChecker @Inject constructor(
    private val context: Context
) {

    fun isPermissionGranted(): Boolean {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    fun isLocationEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun navigateToSettings(activity: Activity) {
        Snackbar.make(
            activity.findViewById(R.id.content),
            "مطلوب إذن الوصول إلى الموقع. انتقل إلى الإعدادات لتمكينه.",
            Snackbar.LENGTH_LONG
        ).setAction("Settings") {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
            }
            context.startActivity(intent)
        }.show()
    }
}
