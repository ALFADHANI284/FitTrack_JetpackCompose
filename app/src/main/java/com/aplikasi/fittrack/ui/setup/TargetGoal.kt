package com.aplikasi.fittrack.ui.setup

import androidx.compose.foundation.background
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
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TargetGoal(
    onNextClick: (String) -> Unit, // Mengirim data ke ViewModel
    onBackClick: () -> Unit        // Fungsi untuk kembali ke slide sebelumnya
) {
    val PrimaryColor = Color(0xFFFFB200)

    // Mapping pilihan UI ke Value API Laravel lu
    val goals = listOf(
        Pair("Turunkan Berat Badan", "lose_weight"),
        Pair("Jaga Berat Badan", "maintain_weight"),
        Pair("Naikkan Massa/Berat", "gain_weight")
    )

    var selectedGoalValue by remember { mutableStateOf<String?>(null) }

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
            text = "Apa target fisik utamamu?",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(32.dp))

        // --- List Pilihan ---
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            goals.forEach { (uiText, apiValue) ->
                // Menggunakan GoalOptionItem yang sama dengan Slide 1
                GoalOptionItem(
                    text = uiText,
                    isSelected = selectedGoalValue == apiValue,
                    onClick = { selectedGoalValue = apiValue }
                )
            }
        }

        // --- Tombol Navigasi Bawah ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Tombol Back
            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text("Back", color = Color.Gray, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            // Tombol Next
            Button(
                onClick = { selectedGoalValue?.let { onNextClick(it) } },
                enabled = selectedGoalValue != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryColor,
                    disabledContainerColor = Color(0xFFE0E0E0)
                ),
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp)
            ) {
                Text(
                    text = "Next",
                    color = if (selectedGoalValue != null) Color.White else Color.Gray,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}