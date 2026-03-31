package com.dev.nagdaadmin.features.requests.view

import com.dev.nagdaadmin.data.model.RequestModel

sealed class RequestsState {
    object Idle    : RequestsState()
    object Loading : RequestsState()
    object Empty   : RequestsState()
    data class Success(val requests: List<RequestModel>) : RequestsState()
    data class Error(val message: String) : RequestsState()
}