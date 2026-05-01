package com.aplikasi.fittrack.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WorkoutDetailScreen() {
    val yellowTheme = Color(0xFFFFB300)

    // Scaffold mengatur layout utama: ada konten yang bisa di-scroll, dan bottom bar yang fix
    Scaffold(
        bottomBar = { WorkoutBottomBar(yellowTheme) },
        containerColor = Color.White
    ) { paddingValues ->

        // Column Utama yang bisa di-scroll
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Mencegah konten tertutup bottom bar
                .verticalScroll(rememberScrollState())
        ) {
            // HEADER BOX (Gambar + Tombol Back)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
            ) {
                // Placeholder Gambar Header (Ganti id-nya dengan gambar aslimu)
                Image(
                    // Pakai icon gallery bawaan Android sebagai placeholder sementara
                    painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                    contentDescription = "Workout Header",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.LightGray) // Kasih background abu-abu biar batasnya jelas
                )

                // Tombol Back di Kiri Atas
                IconButton(
                    onClick = { /* TODO: Navigasi Back */ },
                    modifier = Modifier
                        .padding(top = 40.dp, start = 16.dp)
                        .size(48.dp)
                        .background(Color.Black.copy(alpha = 0.5f), shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            }

            // BAGIAN KONTEN BAWAH (Padding 24dp)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // 1. Rating & Reviews
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = yellowTheme,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "4.9", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "(900 reviews)", fontSize = 14.sp, color = Color.Gray)
                }

                // 2. Judul Program
                Text(
                    text = "Dash Strength",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 8.dp)
                )

                // 3. Tab Kategori (Strength & Cardio)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Tab Active (Strength)
                    Column {
                        Text(text = "Strength", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        Box(
                            modifier = Modifier
                                .width(60.dp)
                                .height(4.dp)
                                .padding(top = 4.dp)
                                .background(yellowTheme)
                        )
                    }
                    // Tab Inactive (Cardio)
                    Column {
                        Text(text = "Cardio", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.Gray)
                    }
                }

                // Garis Pemisah (Divider)
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 24.dp),
                    thickness = 1.dp,
                    color = Color(0xFFE0E0E0)
                )

                // 4. Deskripsi
                Text(
                    text = "Lose belly fat, get ripped abs in just 4 weeks with this efficient plan. It also helps pump your arms, strengthen your back...",
                    fontSize = 15.sp,
                    color = Color(0xFF424242),
                    lineHeight = 22.sp
                )
                Text(
                    text = "Read more",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = yellowTheme,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .clickable { /* TODO: Expand description */ }
                )

                // 5. About The Program
                Text(
                    text = "About The Program",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 32.dp, bottom = 16.dp)
                )

                // Tag Label
                Box(
                    modifier = Modifier
                        .border(2.dp, Color.Black, RoundedCornerShape(8.dp))
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Home/Outdoors",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

// Komponen dipisah agar rapi (Mewakili bottomLayout di XML)
@Composable
fun WorkoutBottomBar(themeColor: Color) {
    Surface(
        shadowElevation = 16.dp, // Memberikan efek bayangan ke atas
        color = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tombol Favorite
            IconButton(
                onClick = { /* TODO: Toggle Favorite */ },
                modifier = Modifier
                    .size(56.dp)
                    .border(2.dp, Color.LightGray, RoundedCornerShape(16.dp))
            ) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = Color.Gray,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Tombol View Schedule
            Button(
                onClick = { /* TODO: Buka Jadwal */ },
                modifier = Modifier
                    .weight(1f) // layout_weight="1"
                    .height(56.dp)
                    .border(2.dp, Color.Black, RoundedCornerShape(28.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = themeColor),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = "View Schedule",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutDetailPreview() {
    WorkoutDetailScreen()
}