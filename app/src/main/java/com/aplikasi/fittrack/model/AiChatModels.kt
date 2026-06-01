package com.aplikasi.fittrack.model

data class AiChatMessage(
    val id: Int = 0,
    val role: String,
    val message: String
)

data class AiChatHistoryResponse(
    val status: Boolean,
    val message: String,
    val data: List<AiChatMessage>
)

data class AiChatStoreData(
    val user_message: AiChatMessage,
    val ai_reply: AiChatMessage
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