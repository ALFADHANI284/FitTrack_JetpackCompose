package com.aplikasi.fittrack.ui.screens.workouts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aplikasi.fittrack.model.WorkoutResponse
import com.aplikasi.fittrack.viewmodel.SearchViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onNavigateToProfile: () -> Unit,
    onNavigateToCategories: () -> Unit,
    onNavigateToDetail: (Int) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    // Tarik state dari ViewModel
    val searchResults by viewModel.searchResults
    val isLoading by viewModel.isLoading

    // Pemicu otomatis tiap kali text berubah
    LaunchedEffect(searchQuery) {
        viewModel.performSearch(searchQuery)
    }

    Scaffold(
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // 1. SEARCH BAR
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .border(1.dp, Color.Black, RoundedCornerShape(25.dp)) // Tambahan border neo-brutalist
                    .background(Color.White, shape = RoundedCornerShape(25.dp))
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.Black)
                Spacer(modifier = Modifier.width(8.dp))

                Box(modifier = Modifier.weight(1f)) {
                    if (searchQuery.isEmpty()) {
                        Text("Search workouts...", color = Color.Gray, fontSize = 16.sp)
                    }
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                if (searchQuery.isNotEmpty()) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Clear",
                        tint = Color.Black,
                        modifier = Modifier.clickable { searchQuery = "" }
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // 2. LOGIKA TAMPILAN DINAMIS
            if (searchQuery.isNotEmpty()) {
                // --- MODE PENCARIAN AKTIF ---
                if (isLoading) {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFFFFB200))
                    }
                } else if (searchResults.isEmpty()) {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        Text("Pencarian tidak ditemukan", color = Color.Gray)
                    }
                } else {
                    Text("Search Results", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Spacer(modifier = Modifier.height(16.dp))

                    // Looping daftar hasil pencarian
                    searchResults.forEach { workout ->
                        SearchResultItem(workout = workout, onClick = { onNavigateToDetail(workout.id) })
                    }
                }
            } else {
                // --- MODE DEFAULT (KOSONG) ---

                // RECENT SEARCH SECTION
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Recent search", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text("See all", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.clickable { })
                }

                Spacer(modifier = Modifier.height(16.dp))

                RecentSearchItem("Pilates")
                RecentSearchItem("Cardio")
                RecentSearchItem("Strength Training")

                Spacer(modifier = Modifier.height(30.dp))

                // BROWSE ALL SECTION
                Text("Browse all", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)

                Spacer(modifier = Modifier.height(16.dp))

                BrowseCard(
                    title = "Full Body",
                    subtitle = "24 Workouts Progress",
                    backgroundColor = Color(0xFFA1CFFB) // Biru Muda
                )
                BrowseCard(
                    title = "Upper Body",
                    subtitle = "18 Workouts Progress",
                    backgroundColor = Color(0xFFCDB4F3) // Ungu Muda
                )
                BrowseCard(
                    title = "Lower Body",
                    subtitle = "15 Workouts Progress",
                    backgroundColor = Color(0xFFF3B4D4) // Pink Muda
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// --- KOMPONEN BANTUAN ---
// (RecentSearchItem dan BrowseCard biarkan sama persis seperti kodingan asli lu)

// Komponen baru untuk nampilin item hasil pencarian
@Composable
fun SearchResultItem(workout: WorkoutResponse, onClick: () -> Unit) {

    val categoryName = when (workout.category_id) {
        1 -> "Full Body"
        2 -> "Upper Body"
        3 -> "Lower Body"
        else -> "General Workout"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .clickable { onClick() }
            .border(1.dp, Color.Black, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = workout.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = categoryName, fontSize = 12.sp, color = Color.DarkGray)
            }
            Icon(Icons.Default.Search, contentDescription = "Go", tint = Color.Gray)
        }
    }
}
@Composable
fun RecentSearchItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.History, contentDescription = "History", tint = Color.Gray, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))

        Text(text = text, fontSize = 14.sp, color = Color.Black, modifier = Modifier.weight(1f))

        Icon(Icons.Default.Close, contentDescription = "Remove", tint = Color.Black, modifier = Modifier.size(20.dp).clickable { })
    }
}

@Composable
fun BrowseCard(title: String, subtitle: String, backgroundColor: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .padding(bottom = 16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .clickable { /* Aksi saat diklik */ }
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = subtitle, fontSize = 12.sp, color = Color.DarkGray)
            }

            Image(
                painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                contentDescription = title,
                modifier = Modifier.size(100.dp)
            )
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_5")
@Composable
fun SearchScreenPreview() {
    // Kamu bisa membungkusnya dengan Tema aplikasimu jika ada,
    // misal: FitTrackTheme { ... }
    SearchScreen(
        // 1. Tambahkan parameter viewModel yang diminta
        viewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
        onNavigateToProfile = {  },
        onNavigateToCategories = {  },
        // 2. Tambahkan parameter onNavigateToDetail yang diminta
        onNavigateToDetail = {  }
    )
}