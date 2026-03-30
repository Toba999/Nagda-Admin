package com.dev.nagdaadmin.domain.repo

import com.dev.nagdaadmin.data.model.AdminModel
import com.dev.nagdaadmin.data.model.LoginResult
import com.dev.nagdaadmin.data.model.RequestModel
import com.dev.nagdaadmin.data.model.UserModel


interface FireBaseRepo {
    suspend fun register(user: AdminModel, password: String): Result<Unit>
    suspend fun login(phone: String, password: String):Result<LoginResult>
    suspend fun getProfile(): Result<UserModel>
    suspend fun updateProfile(user: UserModel): Result<Unit>
    suspend fun sendRequest(request: RequestModel): Result<Unit>
    suspend fun getUserRequests(): Result<List<RequestModel>>
    suspend fun getRequestDetails(requestId: String): Result<RequestModel>
    suspend fun cancelRequest(requestId: String): Result<Unit>
}