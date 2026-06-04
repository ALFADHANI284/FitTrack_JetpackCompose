package com.aplikasi.fittrack.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 🗄️ DATA MODEL YANG DISESUAIKAN DENGAN STRUKTUR TABEL DATABASE LU
data class UserDbInfo(val id: Int, val name: String, val email: String)
data class AdminWorkoutSchedule(
    val id: Int,
    val userId: Int,
    val workoutId: Int,
    val user: UserDbInfo,          // Hasil relasi ke tabel User
    val title: String,             // Dari kolom 'title'
    val description: String,       // Dari kolom 'description'
    val scheduleTime: String,      // Dari kolom 'schedule_time'
    val statusLabel: String,       // Tambahan untuk display status (e.g., "Selesai", "Upcoming")
    val statusColor: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScheduleScreen(
    onBackClick: () -> Unit
) {
    val primaryColor = Color(0xFFF5A300) // Kuning Khas FitTrack
    var selectedFilter by remember { mutableStateOf("Semua") }
    val filterOptions = listOf("Semua", "Upcoming", "Selesai", "Batal")

    // 🎯 DATA DUMMY CONTOH HISTORY YANG SESUAI STRUKTUR DB LU
    val dummySchedules = remember {
        listOf(
            AdminWorkoutSchedule(
                id = 1, userId = 101, workoutId = 12,
                user = UserDbInfo(101, "Ahmad Rifai", "ahmad@gmail.com"),
                title = "Latihan Dada (Chest Day)",
                description = "Fokus pada Bench Press dan Incline Dumbbell Fly untuk pembentukan otot dada maksimal.",
                scheduleTime = "05 Juni 2026 • 08:00 WIB",
                statusLabel = "Upcoming", statusColor = Color(0xFF2196F3) // Biru
            ),
            AdminWorkoutSchedule(
                id = 2, userId = 102, workoutId = 15,
                user = UserDbInfo(102, "Budi Santoso", "budi.s@yahoo.com"),
                title = "Cardio & Fat Burning",
                description = "Sesi treadmill intensitas tinggi dicampur dengan HIIT selama 45 menit.",
                scheduleTime = "04 Juni 2026 • 16:30 WIB",
                statusLabel = "Selesai", statusColor = Color(0xFF4CAF50) // Hijau
            ),
            AdminWorkoutSchedule(
                id = 3, userId = 103, workoutId = 9,
                user = UserDbInfo(103, "Siti Rahma", "siti@outlook.com"),
                title = "Leg Day Workout",
                description = "Squat berat, lunges, dan leg press untuk melatih kekuatan otot paha.",
                scheduleTime = "03 Juni 2026 • 19:00 WIB",
                statusLabel = "Batal", statusColor = Color(0xFFE53935) // Merah
            )
        )
    }

    // Logika Filter List
    val filteredSchedules = if (selectedFilter == "Semua") {
        dummySchedules
    } else {
        dummySchedules.filter { it.statusLabel == selectedFilter }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Log Jadwal User", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Kembali", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = primaryColor)
            )
        },
        containerColor = Color(0xFFF8F9FA)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            // 🏷️ SECTION 1: FILTER CHIPS (Biar UI Terlihat Interaktif & Premium)
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filterOptions) { filter ->
                    val isSelected = selectedFilter == filter
                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = if (isSelected) primaryColor else Color.White,
                        tonalElevation = 2.dp,
                        modifier = Modifier.clickable { selectedFilter = filter }
                    ) {
                        Text(
                            text = filter,
                            color = if (isSelected) Color.White else Color.DarkGray,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            // 📋 SECTION 2: LIST HISTORY SCHEDULE DENGAN LAZYCOLUMN
            if (filteredSchedules.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Tidak ada riwayat jadwal untuk kategori ini.", color = Color.Gray, fontSize = 14.sp)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(filteredSchedules) { schedule ->
                        HistoryScheduleCard(schedule = schedule, primaryColor = primaryColor)
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryScheduleCard(schedule: AdminWorkoutSchedule, primaryColor: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // 👤 BARIS 1: IDENTITAS USER & STATUS BADGE
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(primaryColor.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Person, contentDescription = "User", tint = primaryColor)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = schedule.user.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF222222))
                        Text(text = "UID: ${schedule.userId} • ${schedule.user.email}", fontSize = 11.sp, color = Color.Gray)
                    }
                }

                // Status Badge Dinamis
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = schedule.statusColor.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = schedule.statusLabel,
                        color = schedule.statusColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF1F1F1))

            // 🏋️ BARIS 2: DETAIL DATA JADWAL (Sesuai kolom title & description DB lu)
            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Workout",
                    tint = Color.Gray,
                    modifier = Modifier.size(18.dp).padding(top = 2.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = schedule.title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF333333))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = schedule.description, fontSize = 13.sp, color = Color.Gray, lineHeight = 18.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 📅 BARIS 3: WAKTU PELAKSANAAN (Kolom schedule_time)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8F9FA), shape = RoundedCornerShape(8.dp))
                    .padding(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.DateRange, contentDescription = "Waktu", tint = primaryColor, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = schedule.scheduleTime,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF444444)
                    )
                }
            }
        }
    }
}