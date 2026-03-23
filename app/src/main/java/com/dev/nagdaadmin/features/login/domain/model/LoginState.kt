package com.dev.nagdaadmin.features.login.domain.model

import com.dev.nagdaadmin.data.model.UserModel


sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val user: UserModel?) : LoginState()
    data class Error(val message: String) : LoginState()
}

sealed class BiometricLoginState {
    object Idle : BiometricLoginState()
    object Loading : BiometricLoginState()
    data class Success(val message: String) : BiometricLoginState()
    data class Error(val message: String) : BiometricLoginState()
}