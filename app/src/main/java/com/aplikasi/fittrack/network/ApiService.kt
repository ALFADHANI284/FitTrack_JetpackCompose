package com.aplikasi.fittrack.network

import com.aplikasi.fittrack.model.AuthResponse
import com.aplikasi.fittrack.model.BaseResponse
import com.aplikasi.fittrack.model.LoginRequest
import com.aplikasi.fittrack.model.RegisterRequest
import com.aplikasi.fittrack.model.WorkoutResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    // Endpoint ini nyambung ke local laravel : http://10.0.2.2:8000/api/workout
    @GET("workouts")
    suspend fun getWorkouts(): BaseResponse

    // Endpoint untuk Login
    @POST("login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): AuthResponse

    // Endpoint untuk Register
    @POST("register")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): AuthResponse
}
