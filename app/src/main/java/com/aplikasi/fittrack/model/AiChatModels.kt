package com.aplikasi.fittrack.model

data class YoutubeResult(
    val video_id: String,
    val title: String,
    val channel: String,
    val thumbnail: String,
    val published_at: String,
    val url: String
)

data class MapsLocation(
    val lat: Double,
    val lng: Double
)

data class MapsResult(
    val name: String,
    val address: String?,
    val distance_m: Int,
    val location: MapsLocation,
    val maps_url: String,
    val google_search_url: String
)

data class AiChatMessage(
    val id: Int = 0,
    val role: String,
    val message: String,
    val youtube_results: List<YoutubeResult>? = null,
    val maps_result: List<MapsResult>? = null
)

data class AiChatHistoryResponse(
    val status: Boolean,
    val message: String,
    val data: List<AiChatMessage>
)

data class AiChatStoreData(
    val user_message: AiChatMessage,
    val ai_reply: AiChatMessage,
    val youtube_results: List<YoutubeResult>? = null,
    val maps_result: List<MapsResult>? = null
)

data class AiChatStoreResponse(
    val status: Boolean,
    val message: String,
    val data: AiChatStoreData
)

data class AiChatRequest(
    val message: String,
    val role: String = "user"
)