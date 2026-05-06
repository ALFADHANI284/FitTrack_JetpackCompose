package com.aplikasi.fittrack.ui.screens.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    onLogout: () -> Unit
) {
    // Warna primary_color (kamu bisa sesuaikan dengan hex warna aslimu)
    val primaryColor = Color(0xFFF5A300)

    // Column = LinearLayout (vertical)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Pengganti @color/background_color
            .padding(24.dp)
    ) {

        // Judul
        Text(
            text = "Admin Dashboard",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black, // Pengganti @color/text_color_primary
            modifier = Modifier.padding(top = 32.dp, bottom = 8.dp)
        )

        // Subjudul
        Text(
            text = "Kelola data FitTrack dari sini",
            fontSize = 16.sp,
            color = Color.Gray, // Pengganti @color/text_color_secondary
            modifier = Modifier.padding(bottom = 40.dp)
        )

        // 1. TOMBOL TAMBAH GERAKAN
        Button(
            onClick = { onNavigateToAddWorkout() }, // <--- PANGGIL DI SINI
            modifier = Modifier.fillMaxWidth().height(60.dp).padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
        ) {
            Text("Tambah Gerakan Latihan", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        // Tombol Daftar Gerakan (Outlined Button)
        OutlinedButton(
            onClick = { onNavigateToWorkoutList() },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(2.dp, primaryColor),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = primaryColor)
        ) {
            Text(
                text = "Daftar Gerakan (Database)",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Ini pengganti <Space layout_weight="1"/>
        // Fungsinya untuk mendorong elemen di bawahnya sampai ke dasar layar
        Spacer(modifier = Modifier.weight(1f))

        // 2. TOMBOL LOGOUT
        Button(
            onClick = { onLogout() }, // <--- PANGGIL DI SINI
            modifier = Modifier.fillMaxWidth().height(55.dp).padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEBEE))
        ) {
            Text("Keluar dari Mode Admin", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD32F2F))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminDashboardPreview() {
    AdminDashboardScreen(onNavigateToAddWorkout = {}, onLogout = {}, onNavigateToWorkoutList = {})
}