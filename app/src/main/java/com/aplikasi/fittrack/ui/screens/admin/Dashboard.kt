package com.aplikasi.fittrack.ui.screens.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AdminDashboardScreen(
    onNavigateToAddWorkout: () -> Unit,
    onNavigateToWorkoutList: () -> Unit,
    onNavigateToSchedule: () -> Unit,
    onNavigateToCategories: () -> Unit, // 🎯 TAMBAHAN PARAMETER BARU UNTUK KELAYAR CATEGORIES
    onLogout: () -> Unit
) {
    // 🟡 Warna Tema Utama Admin
    val primaryColor = Color(0xFFF5A300)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA)) // Diubah ke abu-abu lembut agar layout memiliki dimensi kedalaman
    ) {
        // 🟡 1. AKSEN HEADER MELENGKUNG (Konsisten dengan gaya Profile)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(
                    color = primaryColor,
                    shape = RoundedCornerShape(bottomEnd = 60.dp)
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            // --- HEADER TEXT SECTION ---
            Spacer(modifier = Modifier.height(36.dp))
            Text(
                text = "Admin Dashboard",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Kelola data FitTrack dari sini",
                fontSize = 15.sp,
                color = Color.White.copy(alpha = 0.85f)
            )

            Spacer(modifier = Modifier.height(54.dp))

            // 🟢 2. KARTU STATUS KOSMETIK (Mengisi ruang kosong agar terlihat profesional)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(Color(0xFF4CAF50), shape = CircleShape) // Indikator Hijau Aktif
                    )
                    Text(
                        text = "Mode Admin: Sistem Basis Data Aktif",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Menu Manajemen Data",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray,
                modifier = Modifier.padding(bottom = 12.dp, start = 4.dp)
            )

            // --- PILIHAN MENU UTAMA ---

            // 1. TOMBOL TAMBAH GERAKAN
            Button(
                onClick = { onNavigateToAddWorkout() }, // 🔒 LOGIC TETAP SAMA
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    text = "➕  Tambah Gerakan Latihan",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // 2. TOMBOL DAFTAR GERAKAN
            OutlinedButton(
                onClick = { onNavigateToWorkoutList() }, // 🔒 LOGIC TETAP SAMA
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(2.dp, primaryColor),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = primaryColor,
                    containerColor = Color.White // Latar belakang putih bersih di atas base abu-abu
                )
            ) {
                Text(
                    text = "📋  Daftar Gerakan (Database)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // 📅 3. TOMBOL BARU: MENUJU KE SCHEDULE ACTIVITY
            OutlinedButton(
                onClick = { onNavigateToSchedule() }, // 🔒 LOGIKAA BARU UNTUK NAVIGASI JADWAL
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(2.dp, primaryColor),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = primaryColor,
                    containerColor = Color.White
                )
            ) {
                Text(
                    text = "📅  Lihat Jadwal Latihan (Schedule)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // 🗂️ 4. TOMBOL BARU: MENUJU KE CATEGORIES ACTIVITY
            OutlinedButton(
                onClick = { onNavigateToCategories() }, // 🔒 LOGIKAA BARU UNTUK NAVIGASI KATEGORI
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(2.dp, primaryColor),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = primaryColor,
                    containerColor = Color.White
                )
            ) {
                Text(
                    text = "🗂️  Lihat Kategori Latihan (Categories)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Mendorong tombol logout tetap berada tepat di bagian paling bawah layar
            Spacer(modifier = Modifier.weight(1f))

            // 5. TOMBOL LOGOUT (Dibuat melonjong menyerupai desain tombol logout user)
            Button(
                onClick = { onLogout() }, // 🔒 LOGIC TETAP SAMA
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(50.dp), // Pill-shaped lonjong sempurna
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFEBEE), // Background soft-red
                    contentColor = Color(0xFFD32F2F)     // Teks merah tegas
                )
            ) {
                Text(
                    text = "Keluar dari Mode Admin",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminDashboardPreview() {
    AdminDashboardScreen(
        onNavigateToAddWorkout = {},
        onLogout = {},
        onNavigateToWorkoutList = {},
        onNavigateToSchedule = {},
        onNavigateToCategories = {} // Tambahan untuk preview
    )
}