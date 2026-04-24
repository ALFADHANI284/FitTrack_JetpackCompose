package com.alfa.fittrack.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WorkoutScheduleScreen() {
    val yellowTheme = Color(0xFFFFB300)

    // STATE: Untuk menyimpan minggu mana yang sedang dipilih
    var selectedWeek by remember { mutableStateOf("Week 1") }
    val weeks = listOf("Week 1", "Week 2", "Week 3", "Week 4", "Week 5")

    Scaffold(
        topBar = {
            // HEADER DENGAN JUDUL DI TENGAH
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { /* TODO: Navigasi Back */ }
                )

                Text(
                    text = "Massive Upper Body",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                // Spacer ini untuk menyeimbangkan posisi tombol Back agar teks benar-benar di tengah
                Spacer(modifier = Modifier.size(28.dp))
            }
        },
        bottomBar = {
            // TOMBOL RESERVE DI BAWAH
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(24.dp)
            ) {
                Button(
                    onClick = { /* TODO: Proses Reservasi */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = yellowTheme),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Reserve",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        },
        containerColor = Color.White
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // TABS MINGGU (Bisa di-scroll ke samping)
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(weeks) { week ->
                    val isSelected = selectedWeek == week
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) Color.Black else Color(0xFFE0E0E0))
                            .clickable { selectedWeek = week }
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = week,
                            color = if (isSelected) Color.White else Color.Gray,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // LIST JADWAL: HARI PERTAMA
            DateHeader("Today, 03 March")
            ScheduleCard(title = "Dash Strength", time = "7:30 am • 45 min", calories = "250 kcal")
            ScheduleCard(title = "High 45", time = "8:30 am • 45 min", calories = "200 kcal")

            Spacer(modifier = Modifier.height(16.dp))

            // LIST JADWAL: HARI KEDUA
            DateHeader("Monday, 06 March")
            ScheduleCard(title = "High 45", time = "5:30 pm • 45 min", calories = "250 kcal")
            ScheduleCard(title = "Mobility", time = "6:00 pm • 45 min", calories = "250 kcal")

            // Tambahan ruang kosong di bawah agar tidak terlalu mepet dengan tombol Reserve
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// --- KOMPONEN BANTUAN ---

@Composable
fun DateHeader(dateText: String) {
    Text(
        text = dateText,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = Color(0xFF424242),
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
    )
}

@Composable
fun ScheduleCard(title: String, time: String, calories: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)), // Warna Light Gray
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Gambar Workout
            Image(
                painter = painterResource(id = android.R.drawable.ic_menu_gallery), // Placeholder
                contentDescription = "Workout Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.LightGray)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Detail Teks
            Column {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Ikon Waktu
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = "Time",
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = time, fontSize = 12.sp, color = Color.Gray)

                    Spacer(modifier = Modifier.width(16.dp))

                    // Ikon Kalori
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = "Calories",
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = calories, fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutSchedulePreview() {
    WorkoutScheduleScreen()
}