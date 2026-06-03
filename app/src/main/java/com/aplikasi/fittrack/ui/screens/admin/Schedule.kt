//package com.aplikasi.fittrack.ui.screens.admin
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.DateRange
//import androidx.compose.material.icons.filled.Person
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun AdminScheduleScreen(
//    schedules: List<WorkoutSchedule> // Nantinya di-passing dari ViewModel
//) {
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Admin: Jadwal Fit Track", fontWeight = FontWeight.Bold) },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = MaterialTheme.colorScheme.primaryContainer,
//                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
//                )
//            )
//        }
//    ) { paddingValues ->
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .padding(horizontal = 16.dp),
//            contentPadding = PaddingValues(vertical = 16.dp),
//            verticalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            items(schedules) { schedule ->
//                ScheduleCard(schedule = schedule)
//            }
//        }
//    }
//}
//
//@Composable
//fun ScheduleCard(schedule: WorkoutSchedule) {
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        shape = RoundedCornerShape(12.dp),
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
//        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
//    ) {
//        Column(
//            modifier = Modifier.padding(16.dp)
//        ) {
//            // Baris Atas: Info User dan Status
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    // Avatar Placeholder
//                    Box(
//                        modifier = Modifier
//                            .size(40.dp)
//                            .clip(CircleShape)
//                            .background(MaterialTheme.colorScheme.secondaryContainer),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.Person,
//                            contentDescription = "User Avatar",
//                            tint = MaterialTheme.colorScheme.onSecondaryContainer
//                        )
//                    }
//                    Spacer(modifier = Modifier.width(12.dp))
//                    Column {
//                        Text(
//                            text = schedule.user.name,
//                            fontWeight = FontWeight.Bold,
//                            fontSize = 16.sp
//                        )
//                        Text(
//                            text = schedule.user.email,
//                            fontSize = 12.sp,
//                            color = MaterialTheme.colorScheme.onSurfaceVariant
//                        )
//                    }
//                }
//
//                // Badge Status
//                Surface(
//                    shape = RoundedCornerShape(16.dp),
//                    color = schedule.status.color.copy(alpha = 0.1f)
//                ) {
//                    Text(
//                        text = schedule.status.label,
//                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
//                        color = schedule.status.color,
//                        fontSize = 12.sp,
//                        fontWeight = FontWeight.Medium
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//            Divider(color = MaterialTheme.colorScheme.surfaceVariant)
//            Spacer(modifier = Modifier.height(12.dp))
//
//            // Baris Bawah: Detail Jadwal (Workout & Waktu)
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Column {
//                    Text(
//                        text = "Jenis Latihan",
//                        fontSize = 12.sp,
//                        color = MaterialTheme.colorScheme.onSurfaceVariant
//                    )
//                    Text(
//                        text = schedule.workoutName,
//                        fontWeight = FontWeight.SemiBold,
//                        fontSize = 14.sp
//                    )
//                }
//
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Icon(
//                        imageVector = Icons.Default.DateRange,
//                        contentDescription = "Time",
//                        modifier = Modifier.size(16.dp),
//                        tint = MaterialTheme.colorScheme.onSurfaceVariant
//                    )
//                    Spacer(modifier = Modifier.width(4.dp))
//                    Text(
//                        text = "${schedule.date} • ${schedule.time}",
//                        fontSize = 14.sp,
//                        fontWeight = FontWeight.Medium,
//                        color = MaterialTheme.colorScheme.onSurfaceVariant
//                    )
//                }
//            }
//        }
//    }
//}