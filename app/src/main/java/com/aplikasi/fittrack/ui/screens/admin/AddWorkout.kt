package com.aplikasi.fittrack.ui.screens.admin

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.aplikasi.fittrack.network.RetrofitClient
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWorkoutScreen(
    onNavigateBack: () -> Unit
) {
    // 🟡 Tema Warna Admin
    val primaryColor = Color(0xFFF5A300)
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // 🔒 LOGIC TEMEN LU (TIDAK DIUBAH SAMA SEKALI)
    var name: String by remember { mutableStateOf("") }
    var categoryId by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var linkYt by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA)) // Diubah jadi abu-abu soft agar layout punya depth / dimensi
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // --- HEADER SECTION ---
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White, shape = RoundedCornerShape(10.dp))
                        .clickable { onNavigateBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Kembali",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Tambah Gerakan",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Masukkan detail workout baru ke database",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(start = 56.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            // --- FORM CONTAINER CARD ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                    .padding(20.dp)
            ) {
                // Custom warna Outline + Isian Box biar Premium
                // Custom warna Outline + Isian Box biar Premium
                val textFieldColors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryColor,
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedLabelColor = primaryColor,
                    unfocusedLabelColor = Color.Gray,
                    cursorColor = primaryColor,
                    // 🔥 PERBAIKAN: Di Material 3, containerColor harus dipecah jadi dua ini:
                    focusedContainerColor = Color(0xFFFAFAFA),
                    unfocusedContainerColor = Color(0xFFFAFAFA)
                )

                // 1. Nama Workout
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

                // Row untuk Kategori dan Durasi
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 2. Category ID
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

                    // 3. Durasi
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

                // 4. Kalori Terbakar
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

                // 5. Link Youtube
                OutlinedTextField(
                    value = linkYt,
                    onValueChange = { linkYt = it },
                    label = { Text("Link YouTube") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldColors,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
                )

                // 6. Deskripsi (Multiline)
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Deskripsi / Cara Melakukan") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .padding(bottom = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldColors,
                    maxLines = 5
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- TOMBOL SIMPAN ---
            Button(
                onClick = {
                    // 🔒 LOGIC TOMBOL & API (Sama persis bawaan temen lu)
                    if (name.isBlank() || categoryId.isBlank() || duration.isBlank() || calories.isBlank() || description.isBlank()) {
                        Toast.makeText(context, "Semua kolom wajib diisi!", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    isLoading = true

                    coroutineScope.launch {
                        try {
                            val request = WorkoutRequest(
                                category_id = categoryId.toIntOrNull() ?: 1,
                                name = name,
                                duration_minutes = duration.toIntOrNull() ?: 0,
                                calories_burned = calories.toIntOrNull() ?: 0,
                                description = description,
                                link_yt = linkYt.takeIf { it.isNotBlank() }
                            )

                            val dummyToken = "35|kDaczeVuwRtCvNngRTkjYtpNlP1VtrP0BSZ7QvD3024878f5"
                            val response = RetrofitClient.instance.createWorkout(dummyToken, request)

                            if (response.status) {
                                Toast.makeText(context, "Workout berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                                onNavigateBack()
                            } else {
                                Toast.makeText(context, "Gagal: ${response.message}", Toast.LENGTH_LONG).show()
                            }

                        } catch (e: Exception) {
                            Toast.makeText(context, "Error Jaringan: ${e.message}", Toast.LENGTH_LONG).show()
                        } finally {
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(14.dp), // Sedikit lebih melengkung modern
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryColor,
                    disabledContainerColor = primaryColor.copy(alpha = 0.6f)
                ),
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

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddWorkoutPreview() {
    AddWorkoutScreen(onNavigateBack = {})
}