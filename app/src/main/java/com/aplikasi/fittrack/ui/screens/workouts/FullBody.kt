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
fun FullBodyScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (String) -> Unit
) {
    // Data Dummy khusus Full Body (Gabungan otot atas dan bawah)
    val workoutList = listOf(
        WorkoutData("9", "Burpee Blast", "7:00 am", "30 min", "400 kcal", "Today, 03 March"),
        WorkoutData("10", "Deadlift Power", "8:30 am", "50 min", "350 kcal", "Today, 03 March"),
        WorkoutData("11", "Thruster King", "5:30 pm", "40 min", "300 kcal", "Thursday, 09 March"),
        WorkoutData("12", "Farmer's Walk", "6:00 pm", "20 min", "200 kcal", "Thursday, 09 March")
    )

    // Mengelompokkan data berdasarkan tanggal
    val groupedWorkouts = workoutList.groupBy { it.dateGroup }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Ultimate Full Body", // Judul disesuaikan
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
                    onClick = { /* Aksi Reserve */ },
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
                    // Memanggil fungsi WorkoutItemCard yang sudah didefinisikan sebelumnya
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
fun FullBodyScreenPreview() {
    FullBodyScreen(
        onNavigateBack = {},
        onNavigateToDetail = {}
    )
}