package com.aplikasi.fittrack.ui.setup

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GenderAge(
    onNextClick: (String, Int) -> Unit,
    onBackClick: () -> Unit
) {
    val PrimaryColor = Color(0xFFFFB200)
    var selectedGender by remember { mutableStateOf<String?>(null) }
    var ageInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text("Ceritain sedikit tentang dirimu", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(modifier = Modifier.height(32.dp))

        // Pilihan Gender (Pakai Row biar sebelahan)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // Card Laki-laki
            Card(
                modifier = Modifier.weight(1f),
                onClick = { selectedGender = "male" },
                colors = CardDefaults.cardColors(
                    containerColor = if (selectedGender == "male") Color(0xFFFFF8E1) else Color(0xFFF5F5F5)
                ),
                border = if (selectedGender == "male") BorderStroke(2.dp, PrimaryColor) else null
            ) {
                Text("Laki-laki", modifier = Modifier.padding(24.dp).fillMaxWidth(), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
            }

            // Card Perempuan
            Card(
                modifier = Modifier.weight(1f),
                onClick = { selectedGender = "female" },
                colors = CardDefaults.cardColors(
                    containerColor = if (selectedGender == "female") Color(0xFFFFF8E1) else Color(0xFFF5F5F5)
                ),
                border = if (selectedGender == "female") BorderStroke(2.dp, PrimaryColor) else null
            ) {
                Text("Perempuan", modifier = Modifier.padding(24.dp).fillMaxWidth(), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Input Umur
        OutlinedTextField(
            value = ageInput,
            onValueChange = { if (it.length <= 3) ageInput = it }, // Maksimal 3 digit
            label = { Text("Umur (Tahun)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        // Tombol Navigasi
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier.weight(1f).height(54.dp),
                shape = RoundedCornerShape(28.dp)
            ) { Text("Back", color = Color.Gray, fontWeight = FontWeight.Bold) }

            Button(
                onClick = {
                    val age = ageInput.toIntOrNull() ?: 0
                    onNextClick(selectedGender!!, age)
                },
                enabled = selectedGender != null && ageInput.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor, disabledContainerColor = Color(0xFFE0E0E0)),
                modifier = Modifier.weight(1f).height(54.dp)
            ) { Text("Next", color = Color.White, fontWeight = FontWeight.Bold) }
        }
    }
}