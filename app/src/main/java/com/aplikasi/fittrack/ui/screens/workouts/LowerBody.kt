package com.aplikasi.fittrack.ui.screens.workouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LowerBodyScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (String) -> Unit
) {
    // Data Dummy khusus Lower Body
    val workoutList = listOf(
        WorkoutData("5", "Squat Strength", "7:00 am", "50 min", "320 kcal", "Today, 03 March"),
        WorkoutData("6", "Leg Press Power", "8:30 am", "45 min", "280 kcal", "Today, 03 March"),
        WorkoutData("7", "Glute & Hamstrings", "5:30 pm", "40 min", "220 kcal", "Wednesday, 08 March"),
        WorkoutData("8", "Calf Raises & Core", "6:00 pm", "30 min", "150 kcal", "Wednesday, 08 March")
    )

    // Mengelompokkan data berdasarkan tanggal
    val groupedWorkouts = workoutList.groupBy { it.dateGroup }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Intense Lower Body", // Judul disesuaikan
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Button(
                    onClick = { /* Aksi saat tombol bawah diklik */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB200)),
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
                    // Memanggil fungsi WorkoutItemCard yang sudah ada sebelumnya
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LowerBodyScreenPreview() {
    LowerBodyScreen(
        onNavigateBack = {},
        onNavigateToDetail = {}
    )
}