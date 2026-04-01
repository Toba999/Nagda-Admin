package com.dev.nagdaadmin.data.model

import com.dev.nagdaadmin.R


enum class RequestStatus(val label: String, val colorRes: Int) {
    SENT        ("جديد",    R.color.status_sent),
    RECEIVED    ("تم الاستلام",   R.color.status_received),
    IN_PROGRESS ("جاري التعامل", R.color.status_in_progress),
    ON_THE_WAY  ("في الطريق",    R.color.status_on_the_way),
    DONE        ("تم التعامل",   R.color.status_done),
    CANCELLED   ("تم الالغاء",   R.color.status_cancelled);

    fun next(): RequestStatus? = when (this) {
        SENT        -> RECEIVED
        RECEIVED    -> IN_PROGRESS
        IN_PROGRESS -> ON_THE_WAY
        ON_THE_WAY  -> DONE
        DONE        -> null
        CANCELLED   -> null
    }

    fun hasNext() = next() != null
}