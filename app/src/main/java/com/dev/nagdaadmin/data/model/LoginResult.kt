package com.dev.nagdaadmin.data.model

sealed class LoginResult {
    data class User(val user: UserModel)   : LoginResult()
    data class Admin(val admin: AdminModel) : LoginResult()
}