package com.aplikasi.fittrack

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aplikasi.fittrack.model.WorkoutResponse
import com.aplikasi.fittrack.network.RetrofitClient

@Composable
fun WorkoutTestScreen() {
    // Siapkan wadah penyimpan status
    var workouts by remember { mutableStateOf<List<WorkoutResponse>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Efek ini akan berjalan otomatis saat layar pertama kali dibuka
    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.instance.getWorkouts()
            // Tambahkan .data di belakang response
            workouts = response.data
            isLoading = false
        } catch (e: Exception) {
            errorMessage = e.message
            isLoading = false
        }
    }

    // UI Halaman Testing
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> {
                // Munculin animasi muter-muter kalau lagi loading
                CircularProgressIndicator()
            }
            errorMessage != null -> {
                // Munculin teks merah kalau error
                Text(
                    text = "Gagal nyambung API:\n$errorMessage",
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }
            workouts.isEmpty() -> {
                // Kalau sukses nembak tapi isi database Laravel-nya masih kosong
                Text("Database kosong. Belum ada data Workout.")
            }
            else -> {
                // Kalau sukses dan ada datanya, tampilkan pakai LazyColumn (seperti RecyclerView)
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(workouts) { workout ->
                        WorkoutCard(workout)
                    }
                }
            }
        }
    }
}

// Desain Card untuk setiap baris data
@Composable
fun WorkoutCard(workout: WorkoutResponse) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = workout.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Kategori ID: ${workout.category_id}")
            Text(text = "Durasi: ${workout.duration_minutes} menit")
            Text(text = "Kalori Terbakar: ${workout.calories_burned} kcal")

            // Karena description itu bisa null (?), kita cek dulu
            if (!workout.description.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Deskripsi: ${workout.description}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
    }
}