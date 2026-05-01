package com.aplikasi.fittrack.ui.setup

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Menggunakan warna Primary yang udah kita bahas (Warna Kuning/Orange Sporter)
val PrimaryColor = Color(0xFFFFB200)

@Composable
fun SetupGoalScreen(
    onNextClick: (String) -> Unit // Fungsi ini bakal bawa data goal yang dipilih ke halaman berikutnya
) {
    // List pilihan alasan join (Aku ganti kata Sporter jadi FitTrack ya)
    val goals = listOf(
        "Add variety to my fitness routine",
        "Establish healthier habits",
        "Find a motivating workout time",
        "Work out with friends",
        "Others"
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
            text = "What’s your primary reason for joining FitTrack?",
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

// --- Preview (Biar bisa langsung dilihat hasilnya) ---
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SetupGoalScreenPreview() {
    SetupGoalScreen(onNextClick = {})
}