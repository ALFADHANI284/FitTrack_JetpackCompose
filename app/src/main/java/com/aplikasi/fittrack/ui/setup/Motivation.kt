package com.aplikasi.fittrack.ui.setup

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val PrimaryColor = Color(0xFFFFB200)

@Composable
fun Motivation(
    onNextClick: (String) -> Unit
) {
    // List pilihan alasan join (Aku ganti kata Sporter jadi FitTrack ya)
    val goals = listOf(
        "Sakit Hati",
        "Hidup Sehat",
        "Mengisi Waktu Luang",
        "Agar terlihat keren",
        "Lainya"
    )

    // State untuk menyimpan pilihan yang lagi aktif/diklik
    var selectedGoal by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // --- Judul Halaman ---
        Text(
            text = "Apa alsan utama untuk memulai Workout?",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(32.dp))

        // --- List Pilihan (Pakai Column + Looping) ---
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            goals.forEach { goal ->
                GoalOptionItem(
                    text = goal,
                    isSelected = selectedGoal == goal, // Akan bernilai true jika item ini yang dipilih
                    onClick = { selectedGoal = goal }  // Update state saat diklik
                )
            }
        }

        // --- Tombol Next ---
        Button(
            onClick = {
                // Pastikan user udah milih sebelum lanjut
                selectedGoal?.let { onNextClick(it) }
            },
            enabled = selectedGoal != null, // Tombol cuma bisa diklik kalau user udah milih
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryColor,
                disabledContainerColor = Color(0xFFE0E0E0) // Warna abu-abu kalau belum milih
            ),
            shape = RoundedCornerShape(28.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
        ) {
            Text(
                text = "Next",
                color = if (selectedGoal != null) Color.White else Color.Gray,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// --- Reusable Component untuk tiap baris pilihan ---
@Composable
fun GoalOptionItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    // Ganti warna background dan border kalau item ini lagi dipilih
    val backgroundColor = if (isSelected) Color(0xFFFFF8E1) else Color(0xFFF5F5F5)
    val borderColor = if (isSelected) PrimaryColor else Color.Transparent

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }, // Biar seluruh area card bisa diklik
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = text,
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )

            // Bulatan Radio Button di sebelah kanan
            RadioButton(
                selected = isSelected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = PrimaryColor,
                    unselectedColor = Color.LightGray
                )
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SetupGoalScreenPreview() {
    Motivation(onNextClick = {})
}