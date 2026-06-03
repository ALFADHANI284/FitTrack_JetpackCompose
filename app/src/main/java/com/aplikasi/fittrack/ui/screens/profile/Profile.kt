package com.aplikasi.fittrack.ui.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
    onNavigateToLogin: () -> Unit
) {
    val user by viewModel.profileData
    val isLoading by viewModel.isLoading
    val isGuest by viewModel.isGuest

    // 👇 TRIK AMAN H-1: Init AchievementViewModel di dalam sini biar GAK MERAH di MainScreen
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
            achievementViewModel = achievementViewModel, //  Lempar ke konten
            onLogoutClick = onLogoutClick
        )
    }
}

@Composable
fun ProfileContent(
    user: UserData?,
    isLoading: Boolean,
    achievementViewModel: AchievementViewModel, // Tambah parameter di sini
    onLogoutClick: () -> Unit
) {
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color(0xFFFFB200))
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                // PENTING: Kasih scroll biar bisa di-skrol ke bawah pas badge-nya banyak
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- Header Profil ---
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile Picture",
                tint = Color.Gray,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEEEEEE))
                    .padding(16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = user?.name ?: "No Name", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(text = user?.email ?: "No Email", fontSize = 14.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(24.dp))

            // --- Stats Metrik Fisik ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
                border = BorderStroke(1.dp, Color(0xFFFFB200))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatItem(label = "Berat", value = "${user?.weight ?: 0} kg")
                    StatItem(label = "Tinggi", value = "${user?.height ?: 0} cm")

                    val formattedGoal = user?.goal?.replace("_", " ")?.replaceFirstChar { it.uppercase() } ?: "-"
                    StatItem(label = "Goal", value = formattedGoal)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Stats Kalori & Akun ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatItem(label = "Target Kalori", value = "${user?.dailyCaloriesTarget ?: 0} kcal")
                    StatItem(label = "Points", value = "${user?.points ?: 0}")
                    StatItem(label = "Tier", value = user?.tier ?: "Bronze")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ======================================================
            // NGEGABUNGIN SECTION ACHIEVEMENT LU KE SINI 🔥
            // ======================================================
            AchievementSection(viewModel = achievementViewModel)

            // Ganti Spacer weight(1f) jadi ukuran tetap biar gak ngerusak layout scrollable
            Spacer(modifier = Modifier.height(32.dp))

            // --- Tombol Logout ---
            Button(
                onClick = onLogoutClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text(text = "Logout", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Text(text = label, fontSize = 14.sp, color = Color.Gray)
    }
}

@Composable
fun AchievementSection(viewModel: AchievementViewModel) {
    val badgeList by viewModel.achievements
    val points by viewModel.totalPoints

    Column(modifier = Modifier.fillMaxWidth()) {
        // Tampilan Total Poin Berkelir Kuning Hitam ala Neo-Brutalist
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFB200), shape = RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Text(
                text = "🏆 Total Poin Kamu: $points PTS",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Badges & Achievements", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))

        // List Badge Horizontal
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(badgeList) { badge ->
                val statusAlpha = if (badge.isUnlocked) 1f else 0.4f

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(80.dp).alpha(statusAlpha)
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(Color(0xFF4A4A4A), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🏅", fontSize = 28.sp)
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = badge.name, fontSize = 12.sp, fontWeight = FontWeight.Medium, maxLines = 1, textAlign = TextAlign.Center)
                    Text(text = "+${badge.points} Pts", fontSize = 10.sp, color = Color.Gray)

                    if (badge.isUnlocked && !badge.isClaimed) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Button(
                            onClick = { viewModel.claimBadge(badge.id) },
                            contentPadding = PaddingValues(2.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB200), contentColor = Color.Black),
                            modifier = Modifier.height(24.dp)
                        ) {
                            Text("Claim", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}


// --- Komponen Bantuan ---
// (StatItem, NeoBrutalistStreakBox, AchievementBadge tetap sama seperti kodemu, tidak perlu diubah)
// 3. Preview sekarang mengarah ke ProfileContent yang murni UI!
//@Preview(showBackground = true)
//@Composable
//fun ProfileScreenPreviewLoading() {
//    ProfileContent(
//        user = null,
//        isLoading = true, // Ubah jadi false kalau mau ngetes tampilan tanpa loading
//        onLogoutClick = {},
//        achievementViewModel = remember { AchievementViewModel(RetrofitClient.instance, LocalContext.current) }
//    )
//}

