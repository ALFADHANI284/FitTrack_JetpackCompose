package com.aplikasi.fittrack.network

import com.aplikasi.fittrack.model.AuthResponse
import com.aplikasi.fittrack.model.BaseResponse
import com.aplikasi.fittrack.model.CategoryResponse
import com.aplikasi.fittrack.model.DefaultResponse
import com.aplikasi.fittrack.model.LoginRequest
import com.aplikasi.fittrack.model.ProfileResponse
import com.aplikasi.fittrack.model.RegisterRequest
import com.aplikasi.fittrack.model.WorkoutRequest
import com.aplikasi.fittrack.model.WorkoutResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    // Ambil Daftar Workout (Admin & User)
    @GET("workouts")
    suspend fun getWorkouts(
        @Header("Authorization") token: String
    ): BaseResponse<List<WorkoutResponse>>

    // Endpoint untuk Login
    @POST("login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): AuthResponse

    // Endpoint untuk Register
    @POST("register")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): AuthResponse

    @GET("profile")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): ProfileResponse

    // Tambah Data Workout (Hanya Admin)
    @POST("workouts")
    suspend fun createWorkout(
        @Header("Authorization") token: String, // <-- Wajib ada buat ngirim Bearer Token
        @Body request: WorkoutRequest
    ): DefaultResponse
    // Untuk Categories
    @GET("categories")
    suspend fun getCategories(
        @Header("Authorization") token: String
    ): BaseResponse<List<CategoryResponse>>


}
