package com.aplikasi.fittrack.ui.screens.admin

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aplikasi.fittrack.model.WorkoutResponse
import com.aplikasi.fittrack.network.RetrofitClient
import androidx.compose.foundation.lazy.items

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutListScreen(
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (Int) -> Unit // Nanti buat lempar ID ke halaman Edit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val primaryColor = Color(0xFFF5A300)

    // State buat nampung data dari API
    var workoutList by remember { mutableStateOf<List<WorkoutResponse>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }

    // Otomatis jalanin API pas layar pertama kali dibuka
    LaunchedEffect(Unit) {
        try {
            // TODO: Nanti ganti pakai token asli dari SharedPreferences
            val dummyToken = "Bearer TOKEN_ADMIN_LU_DI_SINI"

            val response = RetrofitClient.instance.getWorkouts(dummyToken)
            workoutList = response
        } catch (e: Exception) {
            errorMessage = e.message.toString()
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // --- HEADER ---
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Kembali",
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onNavigateBack() }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text("Daftar Gerakan", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }

        // --- KONDISI LOADING, ERROR, & LIST ---
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = primaryColor)
            }
        } else if (errorMessage.isNotEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Gagal mengambil data: $errorMessage", color = Color.Red)
            }
        } else if (workoutList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Belum ada data workout.", color = Color.Gray)
            }
        } else {
            // INI DIA PENGGANTI ADAPTER! (LazyColumn)
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(workoutList) { workout ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Info Workout (Kiri)
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = workout.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Durasi: ${workout.duration_minutes} Menit | Kalori: ${workout.calories_burned} kcal", fontSize = 14.sp, color = Color.Gray)
                            }

                            // Ikon Aksi (Kanan)
                            Row {
                                // Tombol Edit
                                IconButton(onClick = { onNavigateToEdit(workout.id) }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = primaryColor)
                                }
                                // Tombol Hapus (Sementara logikanya kosong dulu)
                                IconButton(onClick = { /* TODO: Bikin fungsi hapus */ }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = Color.Red)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}