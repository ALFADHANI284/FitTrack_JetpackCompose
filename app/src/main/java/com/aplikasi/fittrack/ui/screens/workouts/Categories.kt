
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aplikasi.fittrack.model.CategoryResponse
import com.aplikasi.fittrack.network.RetrofitClient

@Composable
fun UserCategoryScreen(
    onNavigateBack: () -> Unit,
    onCategoryClick: (Int) -> Unit // Fungsi buat mindahin user ke halaman detail sesuai ID
) {
    val context = LocalContext.current

    // State penampung data dari Database
    var categories by remember { mutableStateOf<List<CategoryResponse>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Daftar warna pastel untuk background Card (akan di-loop otomatis)
    val cardColors = listOf(
        Color(0xFFA1CFFB), // Biru Muda
        Color(0xFFCDB4F3), // Ungu Muda
        Color(0xFFF3B4D4), // Pink Muda
        Color(0xFFFDE0A6), // Oranye/Kuning Muda
        Color(0xFFA6EBC9)  // Hijau Muda
    )

    // Tembak API saat halaman dibuka
    LaunchedEffect(Unit) {
        try {
            // TODO: Ganti pakai token user lu yang login
            val userToken = "Bearer TOKEN_USER_LU_DI_SINI"

            val response = RetrofitClient.instance.getCategories(userToken)
            if (response.status) {
                categories = response.data
            }
        } catch (e: Exception) {
            // Handle error kalau koneksi gagal
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp)
    ) {
        // --- HEADER ---
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 24.dp, bottom = 24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black,
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onNavigateBack() }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Browse all",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        // --- KONTEN LIST ---
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFFA1CFFB))
            }
        } else if (categories.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Kategori belum tersedia.", color = Color.Gray)
            }
        } else {
            // Pakai LazyColumn biar bisa di-scroll
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                // Pakai itemsIndexed biar kita bisa ngambil index (urutan ke-berapa)
                // Index ini dipakai buat nentuin warna Card-nya
                itemsIndexed(categories) { index, category ->

                    // Ambil warna secara bergantian dari list cardColors
                    val color = cardColors[index % cardColors.size]

                    UserCategoryCard(
                        category = category,
                        backgroundColor = color,
                        onClick = { onCategoryClick(category.id) }
                    )
                }
            }
        }
    }
}

// --- KOMPONEN BANTUAN UNTUK CARD KATEGORI ---
@Composable
fun UserCategoryCard(
    category: CategoryResponse,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // Nama Kategori dari Database (Misal: Full Body)
                Text(
                    text = category.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Deskripsi dari Database
                Text(
                    text = category.description ?: "0 Workouts Progress",
                    fontSize = 12.sp,
                    color = Color.DarkGray,
                    maxLines = 2
                )
            }

            Image(
                painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                contentDescription = category.name,
                modifier = Modifier.size(80.dp)
            )
        }
    }
}