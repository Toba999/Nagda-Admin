package com.dev.nagdaadmin.data.model

import com.dev.nagdaadmin.R

enum class RequestType(val label: String, val iconRes: Int) {
    ACCIDENT("حادث",        R.drawable.ic_accident),
    FIRE("حريق",            R.drawable.ic_fire),
    MEDICAL("مساعدة طبية", R.drawable.ic_medical),
    FLOOD("فيضان",          R.drawable.ic_flood),
    ASSAULT("تعدي",         R.drawable.ic_shoot),
    THEFT("سرقة",           R.drawable.ic_steal),
    EARTHQUAKE("زلزال",     R.drawable.ic_earthquake),
    OTHER("اخري",           R.drawable.ic_more)
}