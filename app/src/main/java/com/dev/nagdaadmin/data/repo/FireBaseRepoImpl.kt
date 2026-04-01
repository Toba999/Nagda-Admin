package com.dev.nagdaadmin.data.repo

import com.dev.nagdaadmin.data.model.AdminModel
import com.dev.nagdaadmin.data.model.LoginResult
import com.dev.nagdaadmin.data.model.RequestModel
import com.dev.nagdaadmin.data.model.RequestStatus
import com.dev.nagdaadmin.data.model.UserModel
import com.dev.nagdaadmin.domain.repo.FireBaseRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.jvm.java

class FireBaseRepoImpl @Inject constructor(
    firestore: FirebaseFirestore,
) : FireBaseRepo {

    private val auth = FirebaseAuth.getInstance()
    private val usersCollection = firestore.collection("users")
    private val requestsCollection = firestore.collection("requests")
    private val adminsCollection = firestore.collection("admins")

    private fun phoneToEmail(phone: String): String {
        val sanitized = phone.trim().replace(" ", "").replace("+", "")
        return "${sanitized}@nagdaAdmin.com"
    }

    override suspend fun register(user: AdminModel, password: String): Result<Unit> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(
                phoneToEmail(user.phone), password
            ).await()

            val uid = authResult.user?.uid ?: return Result.failure(Exception("UID is null"))
            adminsCollection.document(uid).set(user.copy(uid = uid)).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun login(phone: String, password: String): Result<LoginResult> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(
                phoneToEmail(phone), password
            ).await()
            val uid = authResult.user?.uid ?: return Result.failure(Exception("UID is null"))
            val adminSnapshot = adminsCollection.document(uid).get().await()
            val admin = adminSnapshot.toObject(AdminModel::class.java)
                ?: return Result.failure(Exception("بيانات المشرف غير موجودة"))
            return Result.success(LoginResult.Admin(admin))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProfile(): Result<AdminModel> {
        return try {
            val uid = auth.currentUser?.uid
                ?: return Result.failure(Exception("المستخدم غير مسجل الدخول"))

            val user = adminsCollection.document(uid).get().await()
                .toObject(AdminModel::class.java)
                ?: return Result.failure(Exception("المستخدم غير موجود"))

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserDetails(uid: String): Result<UserModel> {
        return try {
            val user = usersCollection.document(uid).get().await()
                .toObject(UserModel::class.java)
                ?: return Result.failure(Exception("المستخدم غير موجود"))
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProfile(user: AdminModel): Result<Unit> {
        return try {
            val uid = auth.currentUser?.uid
                ?: return Result.failure(Exception("المستخدم غير مسجل الدخول"))

            adminsCollection.document(uid).update(
                mapOf(
                    "fullName"   to user.fullName,
                    "phone"      to user.phone,
                    "role"    to user.role,
                    "mail"       to user.email
                )
            ).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAllRequests(): Result<List<RequestModel>> {
        return try {
            val snapshot = requestsCollection
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()

            val requests = snapshot.documents.mapNotNull { doc ->
                doc.toObject(RequestModel::class.java)
            }
            Result.success(requests)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRequestDetails(requestId: String): Result<RequestModel> {
        return try {
            val snapshot = requestsCollection.document(requestId).get().await()
            val request = snapshot.toObject(RequestModel::class.java)
                ?: return Result.failure(Exception("البلاغ غير موجود"))
            Result.success(request)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun cancelRequest(requestId: String): Result<Unit> {
        return try {
            requestsCollection.document(requestId)
                .update("status", RequestStatus.CANCELLED.name)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateRequestStatus(requestId: String, status: RequestStatus): Result<Unit> {
        return try {
            requestsCollection.document(requestId)
                .update("status", status.name)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
