package com.dev.nagdaadmin.di

import android.content.Context
import com.dev.nagdaadmin.data.geofence.GeoFenceChecker
import com.dev.nagdaadmin.data.repo.FireBaseRepoImpl
import com.dev.nagdaadmin.domain.repo.FireBaseRepo
import com.dev.nagdaadmin.utils.LocationPermissionChecker
import com.dev.nagdaadmin.utils.SharedPrefManager
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideSharedPrefManager(@ApplicationContext context: Context): SharedPrefManager =
        SharedPrefManager(context)

    @Provides
    @Singleton
    fun provideFireBaseRepo(
        firestore: FirebaseFirestore,
    ): FireBaseRepo {
        return FireBaseRepoImpl(firestore)
    }

    @Provides
    @Singleton
    fun provideFirebaseFireStore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideLocationPermissionChecker(@ApplicationContext context: Context): LocationPermissionChecker {
        return LocationPermissionChecker(context)
    }

    @Provides
    @Singleton
    fun provideGeoFenceChecker(): GeoFenceChecker {
        return GeoFenceChecker()
    }

}