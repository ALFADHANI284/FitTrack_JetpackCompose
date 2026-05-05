package com.aplikasi.fittrack.ui.screens.admin

import android.widget.Toast
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aplikasi.fittrack.model.WorkoutRequest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWorkoutScreen(
    onNavigateBack: () -> Unit // Fungsi buat kembali ke Dashboard Admin
) {
    // Warna sesuai tema Admin lu
    val primaryColor = Color(0xFFF5A300)
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // 1. STATE FORM INPUT (Menyimpan ketikan Admin)
    var name: String by remember { mutableStateOf("") }
    var categoryId by remember { mutableStateOf("") } // Pake String dulu biar gampang di TextField
    var duration by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()) // Wajib ada biar form bisa di-scroll
    ) {
        // Tombol Back & Judul
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Kembali",
                tint = Color.Black,
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onNavigateBack() }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Tambah Gerakan",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Text(
            text = "Masukkan detail workout baru ke database",
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 32.dp, start = 44.dp) // Digeser sejajar dengan judul
        )

        // Custom warna Outline biar senada sama tombol
        val textFieldColors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = primaryColor,
            unfocusedBorderColor = Color.Gray,
            focusedLabelColor = primaryColor,
            cursorColor = primaryColor
        )

        // --- FORM INPUTS ---

        // 1. Nama Workout (Text)
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nama Gerakan (Contoh: Push Up)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = textFieldColors,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 2. Category ID (Number)
            OutlinedTextField(
                value = categoryId,
                onValueChange = { categoryId = it },
                label = { Text("ID Kategori") },
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = textFieldColors,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            // 3. Durasi (Number)
            OutlinedTextField(
                value = duration,
                onValueChange = { duration = it },
                label = { Text("Durasi (Menit)") },
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = textFieldColors,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        // 4. Kalori (Number)
        OutlinedTextField(
            value = calories,
            onValueChange = { calories = it },
            label = { Text("Kalori Terbakar (Kcal)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = textFieldColors,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        // 5. Deskripsi (Text - Multiline)
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Deskripsi / Cara Melakukan") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp) // Dibikin lebih tinggi karena deskripsi
                .padding(bottom = 32.dp),
            shape = RoundedCornerShape(12.dp),
            colors = textFieldColors,
            maxLines = 4
        )

        // Spacer dorong tombol ke bawah (Opsional kalau layarnya panjang)
        Spacer(modifier = Modifier.weight(1f, fill = false))

        // --- TOMBOL SIMPAN ---
        Button(
            onClick = {
                // Validasi Kosong
                if (name.isBlank() || categoryId.isBlank() || duration.isBlank() || calories.isBlank() || description.isBlank()) {
                    Toast.makeText(context, "Semua kolom wajib diisi!", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                isLoading = true

                coroutineScope.launch {
                    try {
                        // Convert inputan string ke Int biar sesuai sama WorkoutRequest
                        val request = WorkoutRequest(
                            category_id = categoryId.toIntOrNull() ?: 1,
                            name = name,
                            duration_minutes = duration.toIntOrNull() ?: 0,
                            calories_burned = calories.toIntOrNull() ?: 0,
                            description = description
                        )

                        // TODO: Ambil token asli lu dari SharedPreferences / DataStore
                        val dummyToken = "Bearer TOKEN_ADMIN_LU_DI_SINI"

                        // Tembak API
                        // val response = RetrofitClient.instance.createWorkout(dummyToken, request)

                        // Simulasi Sukses
                        Toast.makeText(context, "Workout berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                        onNavigateBack() // Otomatis balik ke dashboard admin setelah sukses

                    } catch (e: Exception) {
                        Toast.makeText(context, "Gagal: ${e.message}", Toast.LENGTH_LONG).show()
                    } finally {
                        isLoading = false
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text(
                    text = "Simpan Data Workout",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddWorkoutPreview() {
    AddWorkoutScreen(onNavigateBack = {})

}