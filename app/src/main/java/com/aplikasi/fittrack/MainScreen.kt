package com.aplikasi.fittrack

import UserCategoryScreen
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Favorite
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
import com.aplikasi.fittrack.model.ReviewUIModel
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
import com.aplikasi.fittrack.viewmodel.HomeViewModel
import com.aplikasi.fittrack.viewmodel.ProfileViewModel
import com.aplikasi.fittrack.viewmodel.SearchViewModel

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

            val homeViewModel = remember {
                HomeViewModel(
                    apiService = apiService,
                    context = context.applicationContext
                )
            }

            val searchViewModel = remember {
                SearchViewModel(
                    apiService = apiService,
                    context = context.applicationContext
                )
            }

            // SCAFFOLD UTAMA (BINGKAI APLIKASI)
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

                //  LAYAR TV (KONTEN YANG BERUBAH-UBAH)
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

                        //  UPDATE: Cuma nambahin parameter homeViewModel ke HomeScreen
                        "home" -> {
                            HomeScreen(
                                profileViewModel = profileViewModel, // Ganti nama dikit menyesuaikan HomeScreen baru
                                homeViewModel = homeViewModel,       // Masukin viewmodel streak & analytics
                                onBodyFocusClick = { id, name ->
                                    selectedCategoryId = id
                                    selectedCategoryName = name
                                    currentScreen = "category_workouts"
                                }
                            )
                        }

                        "search" -> {
                            SearchScreen(
                                viewModel = searchViewModel,
                                onNavigateToProfile = { currentScreen = "profile" },
                                onNavigateToCategories = { currentScreen = "categories" },
                                onNavigateToDetail = { workoutId ->
                                    selectedWorkoutId = workoutId
                                    currentScreen = "workout_detail"
                                }
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
    profileViewModel: ProfileViewModel,
    homeViewModel: HomeViewModel, // Tambahan parameter
    onBodyFocusClick: (Int, String) -> Unit,
) {
    var showAiChat by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("FitTrackPrefs", Context.MODE_PRIVATE)
    val savedToken = sharedPreferences.getString("ACCESS_TOKEN", "") ?: ""

    // State dari Profile
    val user by profileViewModel.profileData
    val namaUser = user?.name ?: "User"

    // Tarik State dari HomeViewModel
    val streakDays by homeViewModel.streakDays
    val isWorkoutToday by homeViewModel.isWorkoutToday
    val analyticsData by homeViewModel.analyticsData


    val weeklyHistory by homeViewModel.weeklyWorkoutCounts
    val days = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

    val favoritesList by homeViewModel.favoriteWorkouts

    val currentWeight by homeViewModel.currentWeight
    val weightDiff by homeViewModel.weightDifferenceText

    val reviewsList by homeViewModel.userReviews


    // Panggil API pas layar dibuka
    LaunchedEffect(Unit) {
        profileViewModel.fetchProfile()
        homeViewModel.loadAllHomeData() // Muat streak & analytics
    }

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
            // HEADER DENGAN STREAK
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "HOME",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                // UI Streak Dinamis
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val fireColor = if (isWorkoutToday) Color(0xFFFF4500) else Color(0xFFD3D3D3)
                    Icon(Icons.Default.LocalFireDepartment, contentDescription = "Streak", tint = fireColor, modifier = Modifier.size(24.dp))
                    Text(text = "$streakDays Days", fontWeight = FontWeight.Bold, color = fireColor, modifier = Modifier.padding(start = 4.dp))
                }
            }

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

                    // --- Header Grafik ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = "Workout History",
                                tint = Color.Red,
                                modifier = Modifier.size(22.dp)
                            )
                            Text(
                                text = "Workout History",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                        Text(text = "Week ▾", fontSize = 14.sp, color = Color.Gray)
                    }

                    // --- Grafik Batang ---
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp)
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        // Sekarang days dan weeklyHistory udah kebaca di sini!
                        days.forEachIndexed { index, day ->
                            val workoutCount = weeklyHistory.getOrNull(index) ?: 0
                            val barValue = (workoutCount * 12).coerceAtMost(70)
                            val barColor = if (workoutCount > 0) Color(0xFFFFB200) else Color(0xFFE6E6E6)

                            // Pilih salah satu (tergantung BarItem lu nerima Int atau Dp)
                            BarItem(
                                day = day,
                                height = if (barValue == 0) 5 else barValue, // Ganti jadi 5.dp dan barValue.dp kalau merah
                                color = barColor
                            )
                        }
                    }
                }
            }

            // FEATURED PLAN (Body Focus)
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

            // Favorites
            SectionTitle(title = "My Favorites", modifier = Modifier.padding(top = 20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                favoritesList.forEach { favorite ->
                    // Panggil FavoriteCard lu dengan isi dari loop API
                    FavoriteCard(
                        title = favorite.first,       // Mengambil judul workout
                        duration = favorite.second    // Mengambil durasi workout
                    )
                }
            }

            // PROGRESS
            SectionTitle(title = "Your Progress", modifier = Modifier.padding(top = 22.dp))
            Card(
                modifier = Modifier.fillMaxWidth().height(130.dp).padding(top = 12.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFB200))
            ) {
                Column(modifier = Modifier.padding(18.dp).fillMaxSize()) {

                    // Panggil state angkanya di sini
                    Text(
                        text = currentWeight,
                        color = Color(0xFF0F0B0B),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold
                    )

                    // Panggil state naik/turunnya di sini
                    Text(
                        text = weightDiff,
                        fontSize = 14.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(top = 4.dp)
                    )

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
        // Tampilkan bagian ini HANYA kalau datanya ada (nggak kosong)
        if (reviewsList.isNotEmpty()) {
            // Header Section
            SectionTitle(title = "Recent App Reviews", modifier = Modifier.padding(top = 24.dp))

            // List Review
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Ambil maksimal 3 data terbaru pakai .take(3)
                reviewsList.take(3).forEach { review ->
                    ReviewItemCard(review = review)
                }
            }
        }

        if (showAiChat) {
            FitAiChatSheet(
                token = "Bearer $savedToken",
                onDismiss = { showAiChat = false }
            )
        }
    }
}
@Composable
fun ReviewItemCard(review: ReviewUIModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)), // Abu-abu super muda
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFFEEEEEE)) // Garis tepi tipis biar tegas
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Deretan Bintang Rating
                Row {
                    repeat(review.rating) {
                        Text(text = "⭐", fontSize = 14.sp)
                    }
                }

                // Label kecil penanda ini review dari dia sendiri
                Box(
                    modifier = Modifier
                        .background(Color(0xFFE3F2FD), shape = RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(text = "My Review", fontSize = 10.sp, color = Color(0xFF1976D2), fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Teks Ulasan
            Text(
                text = "\"${review.reviewText}\"",
                fontSize = 14.sp,
                color = Color.DarkGray,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                lineHeight = 20.sp
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
fun FavoriteCard(title: String, duration: String) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(110.dp)
            .clickable { /* TODO: Arahin ke Workout Detail */ },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Default.Favorite,
                contentDescription = "Favorite",
                tint = Color.Red,
                modifier = Modifier.size(24.dp).padding(bottom = 8.dp)
            )
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = duration,
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}