package com.dev.nagdaadmin.data.model

data class AdminModel(
    val uid: String = "",
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val role: String = "admin",
    val createdAt: Long = System.currentTimeMillis()
)