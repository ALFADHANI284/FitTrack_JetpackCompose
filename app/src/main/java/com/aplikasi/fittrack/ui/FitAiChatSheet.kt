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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.DirectionsRun
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.border
import androidx.compose.ui.graphics.Brush
import coil.compose.AsyncImage
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
                                        val aiReply = response.data.ai_reply.copy(
                                            youtube_results = response.data.youtube_results,
                                            maps_result = response.data.maps_result
                                        )
                                        chatMessages = chatMessages.dropLast(1) + response.data.user_message + aiReply
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

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
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
                text = if (isUser) androidx.compose.ui.text.AnnotatedString(chat.message) else parseMarkdownToAnnotatedString(chat.message),
                color = if (isUser) Color.White else Color.Black,
                fontSize = 15.sp,
                lineHeight = 22.sp
            )
        }
        
        if (!isUser && !chat.youtube_results.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "📺 Rekomendasi Video",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray,
                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(start = 4.dp, end = 4.dp, bottom = 12.dp) // extra padding for shadow
            ) {
                items(chat.youtube_results) { video ->
                    YoutubeRecommendationItem(video)
                }
            }
        }

        if (!isUser && !chat.maps_result.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "📍 Rekomendasi Lokasi Gym",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray,
                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(start = 4.dp, end = 4.dp, bottom = 12.dp)
            ) {
                items(chat.maps_result) { mapResult ->
                    MapsRecommendationItem(mapResult)
                }
            }
        }
    }
}

@Composable
fun YoutubeRecommendationItem(video: com.aplikasi.fittrack.model.YoutubeResult) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .width(190.dp) // Lebih kecil agar tidak terlalu memakan tempat
            .padding(bottom = 6.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = Color.Black.copy(alpha = 0.15f),
                ambientColor = Color.Black.copy(alpha = 0.1f)
            )
            .background(Color.White, RoundedCornerShape(16.dp))
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(video.url))
                context.startActivity(intent)
            }
    ) {
        // Thumbnail Container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = video.thumbnail,
                contentDescription = video.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            // Dark gradient overlay from bottom to make play button pop
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.5f)),
                            startY = 50f
                        )
                    )
            )
            // Premium Play Button (Red Youtube Style)
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFFE52D27), CircleShape)
                    .border(2.dp, Color.White.copy(alpha = 0.9f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp).padding(start = 2.dp) // visual center
                )
            }
        }
        
        // Text Information
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(
                text = video.title,
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1F1F1F),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 17.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .background(Color(0xFFF0F0F0), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Channel",
                        tint = Color.Gray,
                        modifier = Modifier.size(12.dp)
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = video.channel,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

fun parseMarkdownToAnnotatedString(text: String): androidx.compose.ui.text.AnnotatedString {
    return androidx.compose.ui.text.buildAnnotatedString {
        val lines = text.split("\n")
        for ((index, line) in lines.withIndex()) {
            var currentLine = line
            
            // Check for headers
            val isHeader = currentLine.startsWith("#")
            
            if (isHeader) {
                currentLine = currentLine.trimStart('#').trimStart()
                pushStyle(androidx.compose.ui.text.SpanStyle(fontWeight = FontWeight.Bold, fontSize = 17.sp))
            } else if (currentLine.trimStart().startsWith("- ") || currentLine.trimStart().startsWith("* ")) {
                val indent = currentLine.takeWhile { it.isWhitespace() }
                currentLine = currentLine.trimStart().substring(2)
                append(indent)
                withStyle(androidx.compose.ui.text.SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("• ")
                }
            }
            
            // Parse bold inside the line
            val boldRegex = Regex("\\*\\*(.*?)\\*\\*")
            var currentIndex = 0
            val matches = boldRegex.findAll(currentLine)
            
            for (match in matches) {
                val start = match.range.first
                val end = match.range.last + 1
                
                if (start > currentIndex) {
                    append(currentLine.substring(currentIndex, start))
                }
                
                withStyle(androidx.compose.ui.text.SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(match.groupValues[1])
                }
                currentIndex = end
            }
            
            if (currentIndex < currentLine.length) {
                append(currentLine.substring(currentIndex))
            }
            
            if (isHeader) {
                pop()
            }
            
            if (index < lines.size - 1) {
                append("\n")
            }
        }
    }
}

@Composable
fun MapsRecommendationItem(mapResult: com.aplikasi.fittrack.model.MapsResult) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .width(190.dp)
            .padding(bottom = 6.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = Color.Black.copy(alpha = 0.15f),
                ambientColor = Color.Black.copy(alpha = 0.1f)
            )
            .background(Color.White, RoundedCornerShape(16.dp))
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mapResult.google_search_url))
                context.startActivity(intent)
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
        ) {
            // Menggunakan Yandex Static Maps karena lebih reliable dan tidak memerlukan API key/User-Agent khusus
            val staticMapUrl = "https://static-maps.yandex.ru/1.x/?ll=${mapResult.location.lng},${mapResult.location.lat}&z=16&l=map&size=400,200&lang=en_US"
            
            coil.compose.AsyncImage(
                model = staticMapUrl,
                contentDescription = "Map for ${mapResult.name}",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            // Pin icon in center
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFFE52D27),
                    modifier = Modifier.size(36.dp).padding(bottom = 14.dp) // center pin bottom tip
                )
            }
        }
        
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = mapResult.name,
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1F1F1F),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 17.sp
            )
            if (!mapResult.address.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = mapResult.address,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 15.sp
                )
            }
        }
    }
}