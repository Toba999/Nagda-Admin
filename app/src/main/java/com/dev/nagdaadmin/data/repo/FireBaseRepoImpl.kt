package com.dev.nagdaadmin.data.repo

import com.dev.nagdaadmin.data.model.RequestModel
import com.dev.nagdaadmin.data.model.RequestStatus
import com.dev.nagdaadmin.data.model.UserModel
import com.dev.nagdaadmin.domain.repo.FireBaseRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireBaseRepoImpl @Inject constructor(
    firestore: FirebaseFirestore,
) : FireBaseRepo {

    private val auth = FirebaseAuth.getInstance()
    private val usersCollection = firestore.collection("users")
    private val requestsCollection = firestore.collection("requests")

    override suspend fun register(user: UserModel, password: String): Result<Unit> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(
                "${user.phone}@nagda.com", password
            ).await()

            val uid = authResult.user?.uid ?: return Result.failure(Exception("UID is null"))

            val userWithUid = user.copy(uid = uid)
            usersCollection.document(uid).set(userWithUid).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun login(phone: String, password: String): Result<UserModel> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(
                "${phone}@nagda.com", password
            ).await()

            val uid = authResult.user?.uid ?: return Result.failure(Exception("UID is null"))

            val snapshot = usersCollection.document(uid).get().await()
            val user = snapshot.toObject(UserModel::class.java)
                ?: return Result.failure(Exception("المستخدم غير موجود"))

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProfile(): Result<UserModel> {
        return try {
            val uid = auth.currentUser?.uid
                ?: return Result.failure(Exception("المستخدم غير مسجل الدخول"))

            val user = usersCollection.document(uid).get().await()
                .toObject(UserModel::class.java)
                ?: return Result.failure(Exception("المستخدم غير موجود"))

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProfile(user: UserModel): Result<Unit> {
        return try {
            val uid = auth.currentUser?.uid
                ?: return Result.failure(Exception("المستخدم غير مسجل الدخول"))

            usersCollection.document(uid).update(
                mapOf(
                    "fullName"   to user.fullName,
                    "phone"      to user.phone,
                    "address"    to user.address,
                    "mail"       to user.mail,
                    "familySize" to user.familySize,
                    "notes"      to user.notes
                )
            ).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendRequest(request: RequestModel): Result<Unit> {
        return try {
            val uid = auth.currentUser?.uid
                ?: return Result.failure(Exception("المستخدم غير مسجل الدخول"))

            val docRef = requestsCollection.document()
            val requestWithIds = request.copy(
                id  = docRef.id,
                uid = uid
            )
            docRef.set(requestWithIds).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserRequests(): Result<List<RequestModel>> {
        return try {
            val uid = auth.currentUser?.uid
                ?: return Result.failure(Exception("المستخدم غير مسجل الدخول"))

            val snapshot = requestsCollection
                .whereEqualTo("uid", uid)
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
}