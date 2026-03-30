package com.dev.nagdaadmin.features.profile.view

import com.dev.nagdaadmin.data.model.AdminModel


sealed class ProfileState {
    object Idle : ProfileState()
    object Loading : ProfileState()
    data class Success(val user: AdminModel?) : ProfileState()
    data class Error(val message: String) : ProfileState()
}

sealed class UpdateState {
    object Idle    : UpdateState()
    object Loading : UpdateState()
    object Success : UpdateState()
    data class Error(val message: String) : UpdateState()
}

sealed class SignOutState {
    object Idle    : SignOutState()
    object Loading : SignOutState()
    object Success : SignOutState()
    data class Error(val message: String) : SignOutState()
}