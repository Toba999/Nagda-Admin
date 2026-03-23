package com.dev.nagdaadmin.features.register.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.nagdaadmin.data.model.UserModel
import com.dev.nagdaadmin.features.register.models.RegisterState
import com.dev.nagdaadmin.domain.repo.FireBaseRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repo: FireBaseRepo
) : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState.asStateFlow()

    fun register(
        fullName: String,
        phone: String,
        mail: String,
        address: String,
        familySize: Int,
        notes: String,
        password: String
    ) {
        viewModelScope.launch {
            _registerState.value = RegisterState.Loading
            val user = UserModel(
                fullName = fullName,
                phone = phone,
                mail = mail,
                address = address,
                familySize = familySize,
                notes = notes
            )
            repo.register(user, password)
                .onSuccess { _registerState.value = RegisterState.Success }
                .onFailure { _registerState.value = RegisterState.Error(
                    it.message ?: "حدث خطأ أثناء إنشاء الحساب") }
        }
    }
}
