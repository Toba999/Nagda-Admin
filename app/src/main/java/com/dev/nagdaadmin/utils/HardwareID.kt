package com.dev.nagdaadmin.utils

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings

object HardwareID {
    @SuppressLint("HardwareIds")
    fun getPersistentDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

}