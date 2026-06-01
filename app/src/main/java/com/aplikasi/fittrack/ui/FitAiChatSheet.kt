package com.aplikasi.fittrack.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aplikasi.fittrack.model.AiChatMessage
import com.aplikasi.fittrack.model.AiChatRequest
import com.aplikasi.fittrack.network.RetrofitClient
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitAiChatSheet(
    token: String,
    onDismiss: () -> Unit // Fungsi untuk menutup pop-up
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    // State manajemen chat
    var chatMessages by remember { mutableStateOf<List<AiChatMessage>>(emptyList()) }
    var inputText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var isTyping by remember { mutableStateOf(false) } // Indikator AI lagi mikir

    val listState = rememberLazyListState()

    // Load histori chat saat pop-up dibuka
    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.instance.getAiChatHistory(token)
            if (response.status) {
                // Laravel ngirim latest() (urutan terbaru di atas),
                // kita reverse biar yang terbaru ada di bawah layar
                chatMessages = response.data.reversed()
            }
        } catch (e: Exception) {
            // Handle error
        } finally {
            isLoading = false
        }
    }

    // Auto-scroll ke bawah saat ada pesan baru
    LaunchedEffect(chatMessages.size) {
        if (chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(chatMessages.size - 1)
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        modifier = Modifier.fillMaxHeight(0.85f) // Pop-up mengisi 85% layar
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
        ) {
            // Header FitAI
            Text(
                text = "FitAI Assistant",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            HorizontalDivider(color = Color.LightGray)

            // Area Pesan
            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color(0xFFFFB200))
                } else if (chatMessages.isEmpty()) {
                    Text(
                        text = "Belum ada obrolan. Tanya seputar fitness bro!",
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(chatMessages) { chat ->
                            ChatBubble(chat)
                        }

                        if (isTyping) {
                            item {
                                Text("FitAI sedang mengetik...", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(start = 8.dp))
                            }
                        }
                    }
                }
            }

            // Input Area
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("Tanya ke FitAI...") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFFFB200),
                        unfocusedBorderColor = Color.LightGray
                    ),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            val userMsg = inputText
                            inputText = "" // Kosongkan input
                            isTyping = true

                            // Tambahkan pesan user ke UI sementara biar kerasa responsif
                            val tempMsg = AiChatMessage(id = 0, role = "user", message = userMsg)
                            chatMessages = chatMessages + tempMsg

                            // Tembak API
                            coroutineScope.launch {
                                try {
                                    val response = RetrofitClient.instance.sendAiChatMessage(
                                        token = token,
                                        request = AiChatRequest(message = userMsg)
                                    )
                                    if (response.status) {
                                        // Update UI dengan pesan asli dari DB + Balasan AI
                                        // Kita hapus pesan sementara tadi, ganti dengan yang dari server
                                        chatMessages = chatMessages.dropLast(1) + response.data.user_message + response.data.ai_reply
                                    }
                                } catch (e: Exception) {
                                    android.util.Log.e("FITAI_ERROR", "Gagal manggil AI: ${e.message}")
                                } finally {
                                    isTyping = false
                                }
                            }
                        }
                    },
                    modifier = Modifier.size(48.dp).background(Color(0xFFFFB200), CircleShape)
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White)
                }
            }
        }
    }
}

@Composable
fun ChatBubble(chat: AiChatMessage) {
    val isUser = chat.role == "user"

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .background(
                    color = if (isUser) Color(0xFFFFB200) else Color(0xFFF0F0F0),
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isUser) 16.dp else 4.dp,
                        bottomEnd = if (isUser) 4.dp else 16.dp
                    )
                )
                .padding(12.dp)
        ) {
            Text(
                text = chat.message,
                color = if (isUser) Color.White else Color.Black,
                fontSize = 15.sp
            )
        }
    }
}