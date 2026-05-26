package com.aplikasi.fittrack

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aplikasi.fittrack.network.RetrofitClient
import com.aplikasi.fittrack.ui.screens.admin.AddWorkoutScreen
import com.aplikasi.fittrack.ui.screens.admin.AdminDashboardScreen
import com.aplikasi.fittrack.ui.screens.admin.WorkoutListScreen
import com.aplikasi.fittrack.ui.screens.auth.LoginScreen
import com.aplikasi.fittrack.ui.screens.auth.RegisterScreen
import com.aplikasi.fittrack.ui.screens.onboarding.OnboardingScreen
import com.aplikasi.fittrack.ui.screens.profile.ProfileScreen
import com.aplikasi.fittrack.ui.screens.workouts.FullBodyScreen
import com.aplikasi.fittrack.ui.screens.workouts.LowerBodyScreen
import com.aplikasi.fittrack.ui.screens.workouts.UpperBodyScreen
import com.aplikasi.fittrack.viewmodel.ProfileViewModel


class MainScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current

            // 1. CEK TOKEN: Buka brankas SharedPreferences saat aplikasi baru loading
            val sharedPref = context.getSharedPreferences("FitTrackPrefs", Context.MODE_PRIVATE)
            val savedToken = sharedPref.getString("ACCESS_TOKEN", "")

            // 2. LOGIKA CERDAS: Tentukan halaman awal berdasarkan token
            // Kalau tokennya ada isinya -> langsung masuk "home"
            // Kalau tokennya kosong -> mulai dari "onboarding"
            val startDestination = if (!savedToken.isNullOrEmpty()) "home" else "onboarding"

            // 3. Masukkan hasil penentuan tadi ke dalam state
            var currentScreen by remember { mutableStateOf(startDestination) }

            // --- Panggil API & ViewModel seperti biasa ---
            val apiService = RetrofitClient.instance
            val profileViewModel = remember {
                ProfileViewModel(
                    apiService = apiService,
                    context = context.applicationContext
                )
            }

            when (currentScreen) {
                "onboarding" -> {
                    OnboardingScreen(
                        onNavigateToLogin = { currentScreen = "login" },
                        onNavigateToRegister = { currentScreen = "register" },
                        onNavigateToHome = { currentScreen = "home" }
                    )
                }

                // Login / masuk
                "login" -> {
                    LoginScreen(
                        onNavigateToHome = { currentScreen = "home" },
                        onNavigateToRegister = { currentScreen = "register" },
                        onNavigateToAdmin = { currentScreen = "admin" }
                    )
                }
                "register" -> {
                    RegisterScreen(
                        onNavigateToLogin = { currentScreen = "login" }
                        // Apakah di dalam file RegisterScreen.kt dia butuh ViewModel atau parameter lain?
                        // Kalau iya, harus ditambahkan di sini juga.
                    )
                }

                // ... (Kode Register dan Home tetap sama seperti punyamu) ...

                "home" -> {
                    HomeScreen(
                        viewModel = profileViewModel,
                        onNavigateToProfile = { currentScreen = "profile" },
                        onNavigateToUpperBody = { currentScreen = "upper_body" },
                        onNavigateToLowerBody = { currentScreen = "lower_body" },
                        onNavigateToFullBody = { currentScreen = "full_body" }
                    )
                }
                // Profile
                "profile" -> {
                    ProfileScreen(
                        viewModel = profileViewModel, // Sekarang ini otomatis terhubung dan tidak merah!
                        onLogoutClick = {
                            val sharedPref = context.getSharedPreferences("FitTrackPrefs", Context.MODE_PRIVATE)
                            sharedPref.edit().remove("ACCESS_TOKEN").apply()

                            currentScreen = "login"
                        }
                    )
                }

                // Workouts
                "upper_body" -> {
                    UpperBodyScreen(
                        onNavigateBack = { currentScreen = "home" }, // Kembali ke Home
                        onNavigateToDetail = { workoutId ->
                            // Nanti ubah ke halaman detail, bawa parameternya
                            android.widget.Toast.makeText(this@MainScreen, "Buka detail ID: $workoutId", android.widget.Toast.LENGTH_SHORT).show()
                        }
                    )
                }
                "lower_body" -> {
                    LowerBodyScreen(
                        onNavigateBack = { currentScreen = "home" },
                        onNavigateToDetail = { workoutId ->
                            // Menuju halaman detail
                            android.widget.Toast.makeText(this@MainScreen, "Buka detail ID: $workoutId", android.widget.Toast.LENGTH_SHORT).show()
                        }
                    )
                }
                "full_body" -> {
                    FullBodyScreen(
                        onNavigateBack = { currentScreen = "home" },
                        onNavigateToDetail = { workoutId ->
                            android.widget.Toast.makeText(this@MainScreen, "Detail Workout: $workoutId", android.widget.Toast.LENGTH_SHORT).show()
                        }
                    )
                }

                // Admin
                "admin" -> {
                    AdminDashboardScreen(
                        onNavigateToAddWorkout = { currentScreen = "add_workout" },
                        onNavigateToWorkoutList = { currentScreen = "workout_list" },
                        onLogout = { currentScreen = "login" }
                    )
                }
                "add_workout" -> {
                    AddWorkoutScreen(
                        onNavigateBack = { currentScreen = "admin" }
                    )
                }
                "workout_list" -> {
                    WorkoutListScreen(
                        onNavigateBack = { currentScreen = "admin" },
                        onNavigateToEdit = { id ->
                            android.widget.Toast.makeText(this@MainScreen, "Mau edit ID: $id", android.widget.Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun HomeScreen(
    viewModel: ProfileViewModel,
    onNavigateToProfile: () -> Unit, // Ke Profile
    onNavigateToUpperBody: () -> Unit, // Ke Upper Body
    onNavigateToLowerBody: () -> Unit, // Ke Lower Body
    onNavigateToFullBody: () -> Unit   // Ke Full Body
) {

    val user by viewModel.profileData
    val namaUser = user?.name ?: "User"

    LaunchedEffect(Unit) {
        // Panggil fungsi untuk mengambil data dari Laravel
        // (Pastikan nama fungsinya sesuai dengan yang ada di ProfileViewModel kamu)
        viewModel.fetchProfile()
    }

    Scaffold(
        bottomBar = { CustomBottomNavigation(
            onNavigateToProfile = onNavigateToProfile
        ) },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // HEADER
            Text(
                text = "HOME",
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                textAlign = TextAlign.Center,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(text = "Tue 04 Nov", color = Color(0xFFA3A3A3), fontSize = 12.sp)
            Text(
                text = "Good Morning $namaUser",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp)
            )

            // CALORIES CARD
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(top = 16.dp),
                shape = RoundedCornerShape(14.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocalFireDepartment, contentDescription = "Calorie", tint = Color.Red, modifier = Modifier.size(22.dp))
                            Text("Calories", fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 8.dp))
                        }
                        Text("Week ▾", fontSize = 14.sp, color = Color.Gray)
                    }

                    // BAR CHART (Dummy)
                    Row(
                        modifier = Modifier.fillMaxWidth().height(70.dp).padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        BarItem("Sun", 20, Color(0xFFE6E6E6))
                        BarItem("Mon", 28, Color(0xFFE6E6E6))
                        BarItem("Tue", 42, Color(0xFFFFB200)) // Active
                        BarItem("Wed", 30, Color(0xFFE6E6E6))
                        BarItem("Thu", 26, Color(0xFFE6E6E6))
                        BarItem("Fri", 24, Color(0xFFE6E6E6))
                        BarItem("Sat", 18, Color(0xFFE6E6E6))
                    }
                }
            }

            // FEATURED PLAN
            SectionTitle(title = "Body Focus", modifier = Modifier.padding(top = 20.dp))

            // Horizontal ScrollView 1
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Kotak 1: Upper Body
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFE0E0E0))
                        .clickable { onNavigateToUpperBody() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("Upper Body", fontWeight = FontWeight.Bold)
                }

                // Kotak 2: Lower Body
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFE0E0E0))
                        .clickable { onNavigateToLowerBody() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("Lower Body", fontWeight = FontWeight.Bold)
                }

                // Kotak 3: Full Body
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFE0E0E0))
                        .clickable { onNavigateToFullBody() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("Full Body", fontWeight = FontWeight.Bold)
                }
            }

            // CHALLENGES
            SectionTitle(title = "Challenges", modifier = Modifier.padding(top = 20.dp))

            // Horizontal ScrollView 2
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ChallengeCard("5K Run Challenge - Join Now!")
                ChallengeCard("30 Day Plank – Start Today")
            }

            // PROGRESS
            SectionTitle(title = "Your Progress", modifier = Modifier.padding(top = 22.dp))

            Card(
                modifier = Modifier.fillMaxWidth().height(130.dp).padding(top = 12.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFB200))
            ) {
                Column(modifier = Modifier.padding(18.dp).fillMaxSize()) {
                    Text("67.5KG", color = Color(0xFF0F0B0B), fontSize = 36.sp, fontWeight = FontWeight.Bold)
                    Text("↓ 2.5kg this week", fontSize = 14.sp, color = Color.Black, modifier = Modifier.padding(top = 4.dp))
                    Text(
                        text = "View History",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0048FF),
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}

// --- KOMPONEN BANTUAN ---
@Composable
fun SectionTitle(title: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(text = "See all", fontSize = 14.sp, color = Color(0xFFA3A3A3))
    }
}

@Composable
fun BarItem(day: String, height: Int, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom) {
        Box(modifier = Modifier.width(14.dp).height(height.dp).background(color, RoundedCornerShape(2.dp)))
        Text(text = day, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
    }
}

@Composable
fun ChallengeCard(title: String) {
    Column(modifier = Modifier.width(180.dp)) {
        Box(modifier = Modifier.fillMaxWidth().height(110.dp).clip(RoundedCornerShape(8.dp)).background(Color.Gray))
        Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.padding(top = 8.dp))
    }
}

@Composable
fun CustomBottomNavigation(
    onNavigateToProfile: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .shadow(8.dp)
            .background(Color.White)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavItem(Icons.Default.Home, "Home", Color(0xFFF5A300), onClick = {
            // Biarkan kosong dulu
        })

        NavItem(Icons.Default.Search, "Search", Color.Black, onClick = {
            // Biarkan kosong dulu
        })

        NavItem(Icons.Default.Category, "Categories", Color.Black, onClick = {
            // Biarkan kosong dulu
        })

        NavItem(Icons.Default.Person, "Account", Color.Black, onClick = {
            // 2. Hapus Intent, ganti dengan memanggil fungsi ini
            onNavigateToProfile()
        })
    }
}
@Composable
fun NavItem(icon: ImageVector, label: String, color: Color, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        // Tambahkan modifier clickable di sini
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp) // Opsional: tambah padding agar area klik lebih nyaman
    ) {
        Icon(icon, contentDescription = label, tint = color, modifier = Modifier.size(28.dp))
        Text(text = label, fontSize = 14.sp, color = color)
    }
}

// Preview
//@Preview(showBackground = true)
//@Composable
//fun HomeScreenPreview() {
//    HomeScreen(
//        onNavigateToProfile = {},
//        onNavigateToUpperBody = {},
//        onNavigateToLowerBody = {},
//        onNavigateToFullBody = {},
//        viewModel = profileViewModel
//    )
//}