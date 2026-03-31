package com.dev.nagdaadmin.domain.repo

import com.dev.nagdaadmin.data.model.AdminModel
import com.dev.nagdaadmin.data.model.LoginResult
import com.dev.nagdaadmin.data.model.RequestModel
import com.dev.nagdaadmin.data.model.UserModel


interface FireBaseRepo {
    suspend fun register(user: AdminModel, password: String): Result<Unit>
    suspend fun login(phone: String, password: String):Result<LoginResult>
    suspend fun getProfile(): Result<AdminModel>
    suspend fun updateProfile(user: AdminModel): Result<Unit>
    suspend fun getUserDetails(uid: String): Result<UserModel>
    suspend fun getAllRequests(): Result<List<RequestModel>>
    suspend fun getRequestDetails(requestId: String): Result<RequestModel>
    suspend fun cancelRequest(requestId: String): Result<Unit>
}