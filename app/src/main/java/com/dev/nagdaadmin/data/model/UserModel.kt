package com.dev.nagdaadmin.data.model

data class UserModel(
    val uid: String = "",
    val fullName: String = "",
    val phone: String = "",
    val mail: String = "",
    val address: String = "",
    val familySize: Int = 0,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)