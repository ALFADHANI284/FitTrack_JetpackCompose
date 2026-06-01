package com.aplikasi.fittrack.network

import com.aplikasi.fittrack.model.AiChatHistoryResponse
import com.aplikasi.fittrack.model.AiChatRequest
import com.aplikasi.fittrack.model.AiChatStoreResponse
import com.aplikasi.fittrack.model.AuthResponse
import com.aplikasi.fittrack.model.BaseResponse
import com.aplikasi.fittrack.model.CategoryResponse
import com.aplikasi.fittrack.model.DefaultResponse
import com.aplikasi.fittrack.model.FavoriteResponse
import com.aplikasi.fittrack.model.LoginRequest
import com.aplikasi.fittrack.model.OnboardingRequest
import com.aplikasi.fittrack.model.OnboardingResponse
import com.aplikasi.fittrack.model.ProfileResponse
import com.aplikasi.fittrack.model.RegisterRequest
import com.aplikasi.fittrack.model.ScheduleListResponse
import com.aplikasi.fittrack.model.ScheduleRequest
import com.aplikasi.fittrack.model.ScheduleResponse
import com.aplikasi.fittrack.model.WorkoutRequest
import com.aplikasi.fittrack.model.WorkoutResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path


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
    @Headers("Accept: application/json")
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


    // Goals
    @Headers("Accept: application/json")
    @POST("profile/onboarding")
    suspend fun saveOnboarding(
        @Header("Authorization") token: String,
        @Body request: OnboardingRequest
    ): OnboardingResponse

    // FitAI
    // 1. Ambil Histori Chat AI
    @GET("ai/chat")
    suspend fun getAiChatHistory(
        @Header("Authorization") token: String
    ): AiChatHistoryResponse

    // 2. Kirim Pesan ke AI
    @POST("ai/chat")
    suspend fun sendAiChatMessage(
        @Header("Authorization") token: String,
        @Body request: AiChatRequest
    ): AiChatStoreResponse

    // 3. Ambil Data Personalisasi AI (Preferences)
    @GET("ai/personalization")
    suspend fun getAiPersonalization(
        @Header("Authorization") token: String
    ): DefaultResponse

    // 4. Simpan/Update Personalisasi AI
    @POST("ai/personalization")
    suspend fun saveAiPersonalization(
        @Header("Authorization") token: String,
        @Body request: Any
    ): DefaultResponse

    // 5. Hapus Personalisasi AI
    @DELETE("ai/personalization")
    suspend fun deleteAiPersonalization(
        @Header("Authorization") token: String
    ): DefaultResponse

    // Favorites
    @POST("favorites/{workoutId}")
    suspend fun addToFavorite(
        @Header("Authorization") token: String,
        @Path("workoutId") workoutId: Int
    ): Response<FavoriteResponse>

    @DELETE("favorites/{workoutId}")
    suspend fun removeFromFavorite(
        @Header("Authorization") token: String,
        @Path("workoutId") workoutId: Int
    ): Response<FavoriteResponse>

    // Schedules
    @POST("schedules")
    suspend fun createSchedule(
        @Header("Authorization") token: String,
        @Body request: ScheduleRequest
    ): Response<ScheduleResponse>
    @GET("workout-schedules")
    suspend fun getMySchedules(
        @Header("Authorization") token: String
    ): ScheduleListResponse
}

