package com.aplikasi.fittrack.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale // 🎯 WAJIB UNTUK POTONG FOTO JADI BULAT
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aplikasi.fittrack.model.CategoryResponse
import com.aplikasi.fittrack.network.RetrofitClient

@Composable
fun UserCategoryScreen(
    onNavigateBack: () -> Unit,
    onCategoryClick: (Int) -> Unit
) {
    val context = LocalContext.current

    // 🔒 LOGIC AMAN 100%
    var categories by remember { mutableStateOf<List<CategoryResponse>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // 🎨 Warna Kuning & Amber Bold yang tegas
    val boldYellowColors = listOf(
        Color(0xFFFFB200),
        Color(0xFFFFC107),
        Color(0xFFFFA000)
    )

    // 📸 DAFTAR FOTO YANG BERBEDA-BEDA:
    // Silakan ganti nama "cihuy", "gambar_upper", "gambar_lower" sesuai file asli yang ada di folder drawable lu!
    val categoryImages = listOf(
        com.aplikasi.fittrack.R.drawable.cihuy,         // Foto untuk kotak ke-1
        com.aplikasi.fittrack.R.drawable.high,  // Foto untuk kotak ke-2
        com.aplikasi.fittrack.R.drawable.dash   // Foto untuk kotak ke-3
    )

    LaunchedEffect(Unit) {
        try {
            val userToken = "Bearer TOKEN_USER_LU_DI_SINI"
            val response = RetrofitClient.instance.getCategories(userToken)
            if (response.status) {
                categories = response.data
            }
        } catch (e: Exception) {
            // Handle error
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp)
    ) {
        // --- HEADER KONSISTEN POLOS ---
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 24.dp, bottom = 24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black,
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onNavigateBack() }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Browse all",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        // --- KONTEN LIST ---
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFFFFB200), strokeWidth = 4.dp)
            }
        } else if (categories.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Kategori belum tersedia.",
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 32.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(categories) { index, category ->
                    val solidColor = boldYellowColors[index % boldYellowColors.size]

                    // 🎯 Trik otomatis memilih foto berdasarkan urutan index data
                    val imageRes = categoryImages[index % categoryImages.size]

                    UserCategoryCard(
                        category = category,
                        backgroundColor = solidColor,
                        imageRes = imageRes, // 🎯 Oper gambarnya ke card
                        onClick = { onCategoryClick(category.id) }
                    )
                }
            }
        }
    }
}

// --- 🍃 SOLID BOLD ROW CARD DESIGN WITH UNIQUE PHOTO ---
@Composable
fun UserCategoryCard(
    category: CategoryResponse,
    backgroundColor: Color,
    imageRes: Int, // 🎯 Terima parameter gambar di sini
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(115.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(backgroundColor)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Sisi Teks Kiri
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category.name,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF111111)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = category.description ?: "Start your training plan now",
                    fontSize = 12.sp,
                    color = Color(0xFF222222).copy(alpha = 0.8f),
                    maxLines = 2,
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Sisi Ikon Kanan (FOTO BULAT SEMPUNA DAN BEDA-BEDA)
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
            ) {
                Image(
                    painter = painterResource(id = imageRes), // 🎯 Gambar berubah dinamis sesuai index
                    contentDescription = category.name,
                    contentScale = ContentScale.Crop, // Potong melingkar rapi
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}