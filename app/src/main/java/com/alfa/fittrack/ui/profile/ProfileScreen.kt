package com.alfa.fittrack.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileScreen(
    onLogoutClick: () -> Unit // Parameter fungsi buat navigasi logout nanti
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Bagian Header: Foto & Nama ---
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Profile Picture",
            tint = Color.Gray,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color(0xFFEEEEEE))
                .padding(16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "User FitTrack", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = "user@fittrack.com", fontSize = 14.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(32.dp))

        // --- Bagian Stats Info Fisik ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Kamu bisa ganti data ini nanti dengan data asli dari database/API Laravel
                StatItem(label = "Umur", value = "17")
                StatItem(label = "Berat", value = "65 kg")
                StatItem(label = "Tinggi", value = "170 cm")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Bagian Aktivitas Terakhir ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Aktivitas Terakhir", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(12.dp))

                // Contoh dummy data history olahraga
                Text(text = "🏃‍♂️ Fun Run 5K Selesai", fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "🏋️‍♂️ Leg Day Workout", fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.weight(1f)) // Spacer ini buat dorong tombol logout ke paling bawah layar

        // --- Bagian Tombol Logout ---
        Button(
            onClick = onLogoutClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)), // Warna merah buat logout
            shape = RoundedCornerShape(28.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(bottom = 16.dp)
        ) {
            Text(text = "Logout", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// --- Komponen Bantuan (Reusable) buat nampilin Umur/Berat/Tinggi ---
@Composable
fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Text(text = label, fontSize = 14.sp, color = Color.Gray)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(
        onLogoutClick = {}
    )
}