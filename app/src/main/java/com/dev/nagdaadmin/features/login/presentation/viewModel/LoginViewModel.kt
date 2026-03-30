package com.dev.nagdaadmin.features.login.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.nagdaadmin.data.model.LoginResult
import com.dev.nagdaadmin.domain.repo.FireBaseRepo
import com.dev.nagdaadmin.features.login.domain.model.LoginState
import com.dev.nagdaadmin.utils.SharedPrefManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseRepository: FireBaseRepo,
    private val sharedPrefManager: SharedPrefManager
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(phone: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            firebaseRepository.login(phone, password)
                .onSuccess { result ->
                    when (result) {
                        is LoginResult.Admin -> {
                            sharedPrefManager.putString(KEY_PHONE, phone)
                            sharedPrefManager.putString(KEY_PASSWORD, password)
                            sharedPrefManager.putBoolean(KEY_BIOMETRIC_ENABLED, true)
                            _loginState.value = LoginState.Success(result.admin)
                        }
                        is LoginResult.User -> {
                            _loginState.value = LoginState.Error("هذا الحساب ليس حساب مشرف")
                        }
                    }
                }
                .onFailure {
                    _loginState.value = LoginState.Error(
                        it.message ?: "حدث خطأ أثناء تسجيل الدخول")
                }
        }
    }

    fun loginWithBiometric() {
        val phone    = sharedPrefManager.getString(KEY_PHONE)
        val password = sharedPrefManager.getString(KEY_PASSWORD)
        if (phone.isBlank() || password.isBlank()) {
            _loginState.value = LoginState.Error("لا توجد بيانات محفوظة")
            return
        }
        login(phone, password)
    }

    fun isBiometricEnabled(): Boolean =
        sharedPrefManager.getBoolean(KEY_BIOMETRIC_ENABLED, false)

    fun resetState() { _loginState.value = LoginState.Idle }

    companion object {
        private const val KEY_PHONE             = "login_phone"
        private const val KEY_PASSWORD          = "login_password"
        private const val KEY_BIOMETRIC_ENABLED = "biometric_enabled"
    }
}