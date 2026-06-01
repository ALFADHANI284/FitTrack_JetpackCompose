package com.aplikasi.fittrack.ui.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aplikasi.fittrack.model.UserData
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

    LaunchedEffect(isGuest) {
        if (isGuest) {
            onNavigateToLogin()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchProfile()
    }

    if (!isGuest) {
        ProfileContent(
            user = user,
            isLoading = isLoading,
            onLogoutClick = onLogoutClick
        )
    }
}

@Composable
fun ProfileContent(
    user: UserData?,
    isLoading: Boolean,
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

            // --- Stats Metrik Fisik (BARU) ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)), // Kasih warna beda dikit (Kuning muda)
                border = BorderStroke(1.dp, Color(0xFFFFB200))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatItem(label = "Berat", value = "${user?.weight ?: 0} kg")
                    StatItem(label = "Tinggi", value = "${user?.height ?: 0} cm")

                    // Ngerapihin tulisan goal (lose_weight jadi Lose Weight)
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

            Spacer(modifier = Modifier.weight(1f))

            // --- Tombol Logout ---
            Button(
                onClick = onLogoutClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier.fillMaxWidth().height(50.dp).padding(bottom = 16.dp)
            ) {
                Text(text = "Logout", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
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


// --- Komponen Bantuan ---
// (StatItem, NeoBrutalistStreakBox, AchievementBadge tetap sama seperti kodemu, tidak perlu diubah)
// 3. Preview sekarang mengarah ke ProfileContent yang murni UI!
@Preview(showBackground = true)
@Composable
fun ProfileScreenPreviewLoading() {
    ProfileContent(
        user = null,
        isLoading = true, // Ubah jadi false kalau mau ngetes tampilan tanpa loading
        onLogoutClick = {}
    )
}

