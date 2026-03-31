package com.dev.nagdaadmin.features.requestDetails.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.nagdaadmin.domain.repo.FireBaseRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RequestDetailsViewModel @Inject constructor(
    private val repo: FireBaseRepo
) : ViewModel() {

    private val _detailsState = MutableStateFlow<RequestDetailsState>(RequestDetailsState.Idle)
    val detailsState: StateFlow<RequestDetailsState> = _detailsState.asStateFlow()

    fun getRequestDetails(requestId: String) {
        viewModelScope.launch {
            _detailsState.value = RequestDetailsState.Loading
            repo.getRequestDetails(requestId)
                .onSuccess { _detailsState.value = RequestDetailsState.Success(it) }
                .onFailure { _detailsState.value = RequestDetailsState.Error(
                    it.message ?: "حدث خطأ") }
        }
    }

    fun cancelRequest(requestId: String) {
        viewModelScope.launch {
            repo.cancelRequest(requestId)
                .onSuccess { getRequestDetails(requestId) }
                .onFailure { _detailsState.value = RequestDetailsState.Error(
                    it.message ?: "حدث خطأ") }
        }
    }
}
