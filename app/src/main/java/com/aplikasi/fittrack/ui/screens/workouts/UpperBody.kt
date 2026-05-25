package com.aplikasi.fittrack.ui.screens.workouts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 1. Data Class Mockup (Nanti diganti data dari database/API)
data class WorkoutData(
    val id: String,
    val title: String,
    val time: String,
    val duration: String,
    val calories: String,
    val dateGroup: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpperBodyScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (String) -> Unit // Mengirim ID workout saat diklik
) {
    // Data Dummy (Simulasi data dari Admin)
    val workoutList = listOf(
        WorkoutData("1", "Dash Strength", "7:30 am", "45 min", "250 kcal", "Today, 03 March"),
        WorkoutData("2", "High 45", "8:30 am", "45 min", "200 kcal", "Today, 03 March"),
        WorkoutData("3", "High 45", "5:30 pm", "45 min", "250 kcal", "Monday, 06 March"),
        WorkoutData("4", "Mobility", "6:00 pm", "45 min", "250 kcal", "Monday, 06 March")
    )

    // Mengelompokkan data berdasarkan tanggal (dateGroup)
    val groupedWorkouts = workoutList.groupBy { it.dateGroup }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Massive Upper Body",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            // Tombol Bottom CTA (Reserve/Start)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Button(
                    onClick = { /* Aksi saat tombol bawah diklik */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB200)), // Warna kuning FitTrack
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                ) {
                    Text("Reserve", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        },
        containerColor = Color.White
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // Looping berdasarkan grup tanggal
            groupedWorkouts.forEach { (date, workouts) ->
                item {
                    Text(
                        text = date,
                        fontSize = 14.sp,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                }

                items(workouts) { workout ->
                    WorkoutItemCard(
                        workout = workout,
                        onClick = { onNavigateToDetail(workout.id) }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

// 2. Komponen Custom untuk Card List Latihannya
@Composable
fun WorkoutItemCard(workout: WorkoutData, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)), // Light Gray
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Placeholder Gambar (Nanti bisa diganti pakai AsyncImage dari Coil jika ambil dari internet)
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.DarkGray),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = Color.White)
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Info Teks
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = workout.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Ikon Waktu
                    Icon(Icons.Default.Schedule, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "${workout.time} • ${workout.duration}", fontSize = 12.sp, color = Color.Gray)

                    Spacer(modifier = Modifier.width(12.dp))

                    // Ikon Kalori
                    Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = workout.calories, fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun UpperBodyScreenPreview() {
    UpperBodyScreen(
        onNavigateBack = {},
        onNavigateToDetail = {}
    )
}