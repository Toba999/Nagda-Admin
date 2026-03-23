package com.dev.nagdaadmin.utils

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment

object LocationPermissionUtil {

    const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    private const val PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION

    fun isGranted(context: Context): Boolean =
        ContextCompat.checkSelfPermission(context, PERMISSION) == PackageManager.PERMISSION_GRANTED

    fun isGPSEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun requestPermission(fragment: Fragment) {
        fragment.requestPermissions(arrayOf(PERMISSION), LOCATION_PERMISSION_REQUEST_CODE)
    }

    fun showRationaleDialog(fragment: Fragment) {
        AlertDialog.Builder(fragment.requireContext())
            .setTitle("إذن الموقع مطلوب")
            .setMessage("يرجى منح إذن الموقع لاختيار الموقع.")
            .setPositiveButton("حسنًا") { _, _ -> requestPermission(fragment) }
            .setNegativeButton("إلغاء", null)
            .show()
    }

    fun showPermissionDeniedDialog(fragment: Fragment) {
        AlertDialog.Builder(fragment.requireContext())
            .setTitle("إذن الموقع مرفوض")
            .setMessage("لا يمكن اختيار الموقع بدون الإذن. يرجى منحه من الإعدادات.")
            .setPositiveButton("فتح الإعدادات") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = ("package:" + fragment.requireContext().packageName).toUri()
                fragment.startActivity(intent)
            }
            .setNegativeButton("إلغاء", null)
            .show()
    }

    fun showEnableGPSDialog(fragment: Fragment, onDismiss: () -> Unit) {
        AlertDialog.Builder(fragment.requireContext())
            .setTitle("تشغيل الـ GPS")
            .setMessage("يرجى تفعيل الـ GPS لاختيار الموقع.")
            .setPositiveButton("فتح الإعدادات") { _, _ ->
                fragment.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .setNegativeButton("إلغاء", null)
            .setOnDismissListener { onDismiss() }
            .show()
    }
}