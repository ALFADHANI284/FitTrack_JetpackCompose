package com.aplikasi.fittrack

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainScreen() {
    // Scaffold menggantikan RelativeLayout untuk mengunci Bottom Navigation di bawah
    Scaffold(
        bottomBar = { CustomBottomNavigation() },
        containerColor = Color.White
    ) { paddingValues ->
        // Column ini menggantikan ScrollView & LinearLayout utama
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Mencegah konten tertutup Bottom Nav
                .verticalScroll(rememberScrollState()) // Bikin bisa di-scroll
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
                text = "Good Morning Alfa",
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
                // Pakai Box sebagai placeholder ImageView
                Box(modifier = Modifier.size(150.dp).clip(RoundedCornerShape(8.dp)).background(Color.LightGray))
                Box(modifier = Modifier.size(150.dp).clip(RoundedCornerShape(8.dp)).background(Color.LightGray))
                Box(modifier = Modifier.size(150.dp).clip(RoundedCornerShape(8.dp)).background(Color.LightGray))
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

// --- KOMPONEN BANTUAN --- (Supaya kode di atas tidak terlalu berantakan)

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
fun CustomBottomNavigation() {
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
        NavItem(Icons.Default.Home, "Home", Color(0xFFF5A300))
        NavItem(Icons.Default.Search, "Search", Color.Black)
        NavItem(Icons.Default.Category, "Categories", Color.Black)
        NavItem(Icons.Default.Person, "Account", Color.Black)
    }
}

@Composable
fun NavItem(icon: ImageVector, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = label, tint = color, modifier = Modifier.size(28.dp))
        Text(text = label, fontSize = 14.sp, color = color)
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}