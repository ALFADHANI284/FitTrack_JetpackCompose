package com.aplikasi.fittrack.network

import com.aplikasi.fittrack.model.BaseResponse
import com.aplikasi.fittrack.model.WorkoutResponse
import retrofit2.http.GET

interface ApiService {
    // Endpoint ini nyambung ke local laravel : http://10.0.2.2:8000/api/workout
    @GET("workouts")
    suspend fun getWorkouts(): BaseResponse
}