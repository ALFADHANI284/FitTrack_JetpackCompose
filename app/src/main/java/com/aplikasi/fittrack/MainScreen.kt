package com.aplikasi.fittrack

import UserCategoryScreen
import android.content.Context
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aplikasi.fittrack.network.RetrofitClient
import com.aplikasi.fittrack.ui.FitAiChatSheet
import com.aplikasi.fittrack.ui.screens.admin.AddWorkoutScreen
import com.aplikasi.fittrack.ui.screens.admin.AdminDashboardScreen
import com.aplikasi.fittrack.ui.screens.admin.WorkoutListScreen
import com.aplikasi.fittrack.ui.screens.auth.LoginScreen
import com.aplikasi.fittrack.ui.screens.auth.RegisterScreen
import com.aplikasi.fittrack.ui.screens.onboarding.OnboardingScreen
import com.aplikasi.fittrack.ui.screens.profile.ProfileScreen
import com.aplikasi.fittrack.ui.screens.workouts.CategoryWorkoutsScreen
import com.aplikasi.fittrack.ui.screens.workouts.FullBodyScreen
import com.aplikasi.fittrack.ui.screens.workouts.LowerBodyScreen
import com.aplikasi.fittrack.ui.screens.workouts.SearchScreen
import com.aplikasi.fittrack.ui.screens.workouts.UpperBodyScreen
import com.aplikasi.fittrack.ui.screens.workouts.WorkoutDetailScreen
import com.aplikasi.fittrack.ui.setup.OnboardingHostScreen
import com.aplikasi.fittrack.viewmodel.ProfileViewModel

class MainScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current

            // 1. Cek Token
            val sharedPref = context.getSharedPreferences("FitTrackPrefs", Context.MODE_PRIVATE)
            val savedToken = sharedPref.getString("ACCESS_TOKEN", "")

            // 2. Logika start screen
            val startDestination = if (!savedToken.isNullOrEmpty()) "home" else "onboarding"

            var currentScreen by remember { mutableStateOf(startDestination) }
            var selectedCategoryId by remember { mutableStateOf(0) }
            var selectedCategoryName by remember { mutableStateOf("") }
            var selectedWorkoutId by remember { mutableStateOf(0) }

            val apiService = RetrofitClient.instance
            val profileViewModel = remember {
                ProfileViewModel(
                    apiService = apiService,
                    context = context.applicationContext
                )
            }

            // 👇 SCAFFOLD UTAMA (BINGKAI APLIKASI)
            Scaffold(
                bottomBar = {
                    // Navbar cuma muncul di layar utama
                    if (currentScreen in listOf("home", "search", "categories", "profile")) {
                        CustomBottomNavigation(
                            currentScreen = currentScreen,
                            onNavigateToHome = { currentScreen = "home" },
                            onNavigateToSearch = { currentScreen = "search" },
                            onNavigateToCategories = { currentScreen = "categories" },
                            onNavigateToProfile = { currentScreen = "profile" }
                        )
                    }
                }
            ) { innerPadding ->

                // 👇 LAYAR TV (KONTEN YANG BERUBAH-UBAH)
                Box(modifier = Modifier.padding(innerPadding)) {
                    when (currentScreen) {
                        "onboarding" -> {
                            OnboardingScreen(
                                onNavigateToLogin = { currentScreen = "login" },
                                onNavigateToRegister = { currentScreen = "register" },
                                onNavigateToHome = { currentScreen = "home" }
                            )
                        }
                        "login" -> {
                            LoginScreen(
                                onNavigateToHome = { currentScreen = "home" },
                                onNavigateToRegister = { currentScreen = "register" },
                                onNavigateToAdmin = { currentScreen = "admin" },
                                onNavigateToSetupGoal = { currentScreen = "setup_goal" }
                            )
                        }
                        "register" -> {
                            RegisterScreen(
                                onNavigateToLogin = { currentScreen = "login" }
                            )
                        }
                        "setup_goal" -> {
                            OnboardingHostScreen(
                                onFinishOnboarding = { currentScreen = "home" }
                            )
                        }
                        "home" -> {
                            HomeScreen(
                                viewModel = profileViewModel,
                                onBodyFocusClick = { id, name ->
                                    selectedCategoryId = id
                                    selectedCategoryName = name
                                    currentScreen = "category_workouts"
                                }
                            )
                        }
                        "search" -> {
                            SearchScreen(
                                onNavigateToProfile = { currentScreen = "profile" },
                                onNavigateToCategories = { currentScreen = "categories" }
                            )
                        }
                        "categories" -> {
                            UserCategoryScreen(
                                onNavigateBack = { currentScreen = "home" },
                                onCategoryClick = { id ->
                                    selectedCategoryId = id
                                    selectedCategoryName = "Daftar Latihan"
                                    currentScreen = "category_workouts"
                                }
                            )
                        }
                        "category_workouts" -> {
                            CategoryWorkoutsScreen(
                                categoryId = selectedCategoryId,
                                categoryName = selectedCategoryName,
                                onNavigateBack = { currentScreen = "categories" },
                                onWorkoutDetailClick = { workoutId ->
                                    selectedWorkoutId = workoutId
                                    currentScreen = "workout_detail"
                                }
                            )
                        }
                        "workout_detail" -> {
                            WorkoutDetailScreen(
                                workoutId = selectedWorkoutId,
                                onNavigateBack = { currentScreen = "category_workouts" }
                            )
                        }
                        "profile" -> {
                            ProfileScreen(
                                viewModel = profileViewModel,
                                onLogoutClick = {
                                    sharedPref.edit().remove("ACCESS_TOKEN").apply()
                                    currentScreen = "login"
                                },
                                onNavigateToLogin = {
                                    currentScreen = "login"
                                }
                            )
                        }
                        "upper_body" -> {
                            UpperBodyScreen(
                                onNavigateBack = { currentScreen = "home" },
                                onNavigateToDetail = { workoutId ->
                                    Toast.makeText(this@MainScreen, "Buka detail ID: $workoutId", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                        "lower_body" -> {
                            LowerBodyScreen(
                                onNavigateBack = { currentScreen = "home" },
                                onNavigateToDetail = { workoutId ->
                                    Toast.makeText(this@MainScreen, "Buka detail ID: $workoutId", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                        "full_body" -> {
                            FullBodyScreen(
                                onNavigateBack = { currentScreen = "home" },
                                onNavigateToDetail = { workoutId ->
                                    Toast.makeText(this@MainScreen, "Detail Workout: $workoutId", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
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
                                    Toast.makeText(this@MainScreen, "Mau edit ID: $id", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

// --- HOME SCREEN ---
@Composable
fun HomeScreen(
    viewModel: ProfileViewModel,
    onBodyFocusClick: (Int, String) -> Unit,
) {
    var showAiChat by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("FitTrackPrefs", Context.MODE_PRIVATE)
    val savedToken = sharedPreferences.getString("ACCESS_TOKEN", "") ?: ""

    val user by viewModel.profileData
    val namaUser = user?.name ?: "User"

    LaunchedEffect(Unit) {
        viewModel.fetchProfile()
    }

    // Scaffold di sini cuma buat tombol Floating AI
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAiChat = true },
                containerColor = Color.Black,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.SmartToy, contentDescription = "Tanya FitAI")
            }
        },
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

                    Row(
                        modifier = Modifier.fillMaxWidth().height(70.dp).padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        BarItem("Sun", 20, Color(0xFFE6E6E6))
                        BarItem("Mon", 28, Color(0xFFE6E6E6))
                        BarItem("Tue", 42, Color(0xFFFFB200))
                        BarItem("Wed", 30, Color(0xFFE6E6E6))
                        BarItem("Thu", 26, Color(0xFFE6E6E6))
                        BarItem("Fri", 24, Color(0xFFE6E6E6))
                        BarItem("Sat", 18, Color(0xFFE6E6E6))
                    }
                }
            }

            // FEATURED PLAN
            SectionTitle(title = "Body Focus", modifier = Modifier.padding(top = 20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier.size(150.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFE0E0E0))
                        .clickable { onBodyFocusClick(1, "Upper Body") },
                    contentAlignment = Alignment.Center
                ) { Text("Upper Body", fontWeight = FontWeight.Bold) }

                Box(
                    modifier = Modifier.size(150.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFE0E0E0))
                        .clickable { onBodyFocusClick(2, "Lower Body") },
                    contentAlignment = Alignment.Center
                ) { Text("Lower Body", fontWeight = FontWeight.Bold) }

                Box(
                    modifier = Modifier.size(150.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFE0E0E0))
                        .clickable { onBodyFocusClick(3, "Full Body") },
                    contentAlignment = Alignment.Center
                ) { Text("Full Body", fontWeight = FontWeight.Bold) }
            }

            // CHALLENGES
            SectionTitle(title = "Challenges", modifier = Modifier.padding(top = 20.dp))
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

        // POP-UP FIT AI
        if (showAiChat) {
            FitAiChatSheet(
                token = "Bearer $savedToken",
                onDismiss = { showAiChat = false }
            )
        }
    }
}

// --- KOMPONEN NAVIGASI & BANTUAN UTAMA ---
@Composable
fun CustomBottomNavigation(
    currentScreen: String,
    onNavigateToHome: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToCategories: () -> Unit,
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
        NavItem(
            icon = Icons.Default.Home,
            label = "Home",
            color = if (currentScreen == "home") Color(0xFFF5A300) else Color.Gray,
            onClick = onNavigateToHome
        )
        NavItem(
            icon = Icons.Default.Search,
            label = "Search",
            color = if (currentScreen == "search") Color(0xFFF5A300) else Color.Gray,
            onClick = onNavigateToSearch
        )
        NavItem(
            icon = Icons.Default.Category,
            label = "Categories",
            color = if (currentScreen == "categories") Color(0xFFF5A300) else Color.Gray,
            onClick = onNavigateToCategories
        )
        NavItem(
            icon = Icons.Default.Person,
            label = "Account",
            color = if (currentScreen == "profile") Color(0xFFF5A300) else Color.Gray,
            onClick = onNavigateToProfile
        )
    }
}

@Composable
fun NavItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, color: Color, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Icon(icon, contentDescription = label, tint = color, modifier = Modifier.size(28.dp))
        Text(text = label, fontSize = 14.sp, color = color)
    }
}

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