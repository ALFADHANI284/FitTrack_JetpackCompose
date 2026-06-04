package com.aplikasi.fittrack.ui.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.* import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aplikasi.fittrack.model.UserData
import com.aplikasi.fittrack.network.RetrofitClient
import com.aplikasi.fittrack.viewmodel.AchievementViewModel
import com.aplikasi.fittrack.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onLogoutClick: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToSchedule: () -> Unit // 🆕 Tambah parameter rute navigasi untuk temen lu
) {
    // 🔒 LOGIC UTAS-ATIL AMAN: Data & state asli bawaan temen lu tetap utuh
    val user by viewModel.profileData
    val isLoading by viewModel.isLoading
    val isGuest by viewModel.isGuest

    val context = LocalContext.current
    val apiService = RetrofitClient.instance
    val achievementViewModel = remember {
        AchievementViewModel(
            apiService = apiService,
            context = context.applicationContext
        )
    }

    LaunchedEffect(isGuest) {
        if (isGuest) {
            onNavigateToLogin()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchProfile()
        achievementViewModel.loadAchievementData()
    }

    if (!isGuest) {
        ProfileContent(
            user = user,
            isLoading = isLoading,
            achievementViewModel = achievementViewModel,
            onLogoutClick = onLogoutClick,
            onNavigateToSchedule = onNavigateToSchedule // 🆕 Oper ke konten utama
        )
    }
}

@Composable
fun ProfileContent(
    user: UserData?,
    isLoading: Boolean,
    achievementViewModel: AchievementViewModel,
    onLogoutClick: () -> Unit,
    onNavigateToSchedule: () -> Unit // 🆕 Terima parameter di sini
) {
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color(0xFFFFB200))
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            // 🟡 HEADER KUNING MELENGKUNG
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(
                        color = Color(0xFFFFB300),
                        shape = RoundedCornerShape(bottomStart = 120.dp)
                    )
                    .padding(top = 24.dp, start = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { /* Handle Back jika ada */ }
                )
            }

            // 📜 KONTEN UTAMA (SCROLLABLE)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(110.dp))

                // ⚪ FOTO PROFIL BULAT OVERLAP
                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE0E0E0), shape = CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile Picture",
                        tint = Color.DarkGray,
                        modifier = Modifier
                            .size(115.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEFEFEF))
                            .padding(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 🏷️ NAMA & TIER
                Text(
                    text = user?.name ?: "No Name",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = user?.tier ?: "Bronze",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFB300)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // 📊 INFORMASI USER
                StatItem(label = "E-mail", value = user?.email ?: "No Email", modifier = Modifier.fillMaxWidth())

                val formattedGoal = user?.goal?.replace("_", " ")?.replaceFirstChar { it.uppercase() } ?: "-"
                StatItem(label = "Fitness Goal", value = formattedGoal, modifier = Modifier.fillMaxWidth())

                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp)) {
                    StatItem(label = "Berat Badan", value = "${user?.weight ?: 0} kg", modifier = Modifier.weight(1f))
                    StatItem(label = "Tinggi Badan", value = "${user?.height ?: 0} cm", modifier = Modifier.weight(1f))
                }

                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp)) {
                    StatItem(label = "Target Kalori", value = "${user?.dailyCaloriesTarget ?: 0} kcal", modifier = Modifier.weight(1f))
                    StatItem(label = "Referral Code", value = user?.referralCode ?: "-", modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 🏆 SECTION ACHIEVEMENT
                Box(modifier = Modifier.padding(horizontal = 24.dp)) {
                    AchievementSection(viewModel = achievementViewModel)
                }

                Spacer(modifier = Modifier.height(44.dp))

                // 🆕 7. TOMBOL WORKOUT SCHEDULE (Outlined Pill Style)
                OutlinedButton(
                    onClick = onNavigateToSchedule, // 🔒 Panggil aksi navigasi pesanan temen lu
                    border = BorderStroke(2.dp, Color(0xFFFFB300)),
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFFB300)),
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .height(55.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "Schedule Icon",
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Workout Schedule",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp)) // Jarak antar tombol biar gak dempet

                // 🛑 8. TOMBOL LOGOUT (Solid Yellow Pill)
                Button(
                    onClick = onLogoutClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFB300),
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .height(55.dp)
                ) {
                    Text(
                        text = "Logout",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(vertical = 10.dp)
    ) {
        Text(
            text = label,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFFB200)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 19.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}

@Composable
fun AchievementSection(viewModel: AchievementViewModel) {
    val badgeList by viewModel.achievements
    val points by viewModel.totalPoints

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFF9E6), shape = RoundedCornerShape(12.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🏆 Total Poin Kamu: $points PTS",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFFFFB200)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Badges & Achievements",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            items(badgeList) { badge ->
                val statusAlpha = if (badge.isUnlocked) 1f else 0.4f

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .width(85.dp)
                        .alpha(statusAlpha)
                ) {
                    Box(
                        modifier = Modifier
                            .size(65.dp)
                            .background(Color(0xFFFFF9E6), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🏅", fontSize = 30.sp)
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = badge.name,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )
                    Text(
                        text = "+${badge.points} Pts",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )

                    if (badge.isUnlocked && !badge.isClaimed) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Button(
                            onClick = { viewModel.claimBadge(badge.id) },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFFB200),
                                contentColor = Color.Black
                            ),
                            modifier = Modifier.height(26.dp)
                        ) {
                            Text("Claim", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreviewSuccess() {
    // Kosong untuk keperluan preview manual project
}