package com.aplikasi.fittrack.ui.screens.workouts

import android.content.Context
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.aplikasi.fittrack.model.WorkoutResponse
import com.aplikasi.fittrack.network.RetrofitClient
import com.aplikasi.fittrack.viewmodel.WorkoutDetailViewModel
import java.util.Calendar
import android.graphics.Color as AndroidColor

@Composable
fun WorkoutDetailScreen(
    workoutId: Int, // Menerima ID dari halaman sebelumnya
    onNavigateBack: () -> Unit // Fungsi kembali
) {
    val yellowTheme = Color(0xFFFFB300)

    val context = LocalContext.current

    val apiService = RetrofitClient.instance
    val viewModel = remember { WorkoutDetailViewModel(apiService, context) }

    // State untuk menampung 1 data detail saja (makanya pakai tanda tanya '?' / bisa null saat loading)
    var workoutDetail by remember { mutableStateOf<WorkoutResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    // Ambil data waktu sekarang untuk default picker
    val calendar = Calendar.getInstance()
    val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
    val currentMinute = calendar.get(Calendar.MINUTE)

    // Setup Dialog Jam bawaan Android
    val timePickerDialog = android.app.TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            // Ketika user klik "OK" di pop-up jam, tembak API!
            viewModel.addWorkoutSchedule(workoutId, hourOfDay, minute)
        },
        currentHour,
        currentMinute,
        true // Menggunakan format 24 jam
    )

    // Ambil data saat layar dibuka
    LaunchedEffect(workoutId) {
        // 👇 1. KITA PRINT DULU ID-NYA BIAR TAU NYANGKUT ATAU NGGAK
        println("DEBUG_DETAIL: ID Workout yang diklik = $workoutId")

        try {
            // 👇 2. AMBIL TOKEN ASLI LAGI DARI BRANKAS
            val sharedPref = context.getSharedPreferences("FitTrackPrefs", Context.MODE_PRIVATE)
            val savedToken = sharedPref.getString("ACCESS_TOKEN", "") ?: ""
            val userToken = "Bearer $savedToken"

            println("DEBUG_DETAIL: Token dipake = $userToken")

            // Tembak API Get All Workouts
            val response = apiService.getWorkouts(userToken)

            if (response.status) {
                // Cari HANYA 1 data yang ID-nya sama dengan workoutId yang diklik
                val detail = response.data.find { it.id == workoutId }
                workoutDetail = detail

                // Set status tombol love
                viewModel.setInitialFavoriteStatus(detail?.isFavorite ?: false)

                println("DEBUG_DETAIL: Data ketemu = ${detail?.name}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            println("DEBUG_DETAIL: ERROR API -> ${e.message}")
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        bottomBar = {
            // Kita kirim link YT-nya ke bottom bar siapa tau mau dipakai
            WorkoutBottomBar(
                workoutId = workoutDetail?.id ?: 0,
                viewModel = viewModel,
                themeColor = yellowTheme,
                youtubeLink = workoutDetail?.link_yt,
                onWatchTutorialClick = {
                    timePickerDialog.show()
                },
                onViewScheduleClick = {
                    timePickerDialog.show()
                }
            )
        },
        containerColor = Color.White
    ) { paddingValues ->

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = yellowTheme)
            }
        } else if (workoutDetail == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Data latihan tidak ditemukan", color = Color.Gray)
            }
        } else {
            // Data berhasil didapat, mari kita ekstrak
            val detail = workoutDetail!!

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                // HEADER BOX
                Box(modifier = Modifier.fillMaxWidth().height(350.dp).background(Color.Black)) {

                    // Cek apakah ada link YouTube-nya
                    if (!detail.link_yt.isNullOrEmpty()) {
                        YoutubeVideoPlayer(
                            youtubeUrl = detail.link_yt,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        // Kalau nggak ada link, tampilkan gambar abu-abu statis
                        Image(
                            painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                            contentDescription = "Workout Header",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize().background(Color.LightGray)
                        )
                    }

                    // Tombol Back di Kiri Atas
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .padding(top = 40.dp, start = 16.dp)
                            .size(48.dp)
                            .background(Color.Black.copy(alpha = 0.5f), shape = CircleShape)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                }

                // BAGIAN KONTEN BAWAH
                Column(
                    modifier = Modifier.fillMaxWidth().padding(24.dp)
                ) {
                    // 1. Rating (Statis dulu karena belum ada di DB)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = "Rating", tint = yellowTheme, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("4.9", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }

                    // 2. Judul Program dari Database
                    Text(
                        text = detail.name,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    // 3. Info Durasi & Kalori dari Database
                    Row(
                        modifier = Modifier.padding(top = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Schedule, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "${detail.duration_minutes ?: 0} min", fontSize = 14.sp, color = Color.Gray)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "${detail.calories_burned ?: 0} kcal", fontSize = 14.sp, color = Color.Gray)
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp), thickness = 1.dp, color = Color(0xFFE0E0E0))

                    // 4. Deskripsi dari Database
                    Text(
                        text = "About The Program",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Text(
                        text = detail.description ?: "Tidak ada deskripsi untuk latihan ini.",
                        fontSize = 15.sp,
                        color = Color(0xFF424242),
                        lineHeight = 22.sp
                    )
                }
            }
        }
    }
}

// --- KOMPONEN BOTTOM BAR ---
@Composable
fun WorkoutBottomBar(
    workoutId: Int,
    viewModel: WorkoutDetailViewModel,
    themeColor: Color,
    youtubeLink: String?,
    onWatchTutorialClick: () -> Unit,
    onViewScheduleClick: () -> Unit
) {
    val isFavorite by viewModel.isFavorite
    val isLoadingFavorite by viewModel.isLoadingFavorite

    Surface(
        shadowElevation = 16.dp,
        color = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. TOMBOL FAVORITE (LOVE)
            IconButton(
                onClick = { viewModel.toggleFavorite(workoutId) },
                enabled = !isLoadingFavorite, // Di-disable sementara kalau API lagi loading
                modifier = Modifier
                    .size(56.dp)
                    .border(
                        width = 2.dp,
                        // Berubah warna border kalau aktif
                        color = if (isFavorite) Color(0xFFFF4A4A) else Color.LightGray,
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Icon(
                    // Berganti ikon berdasarkan state isFavorite
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    // Warna icon berubah merah solid saat ter-favorit
                    tint = if (isFavorite) Color(0xFFFF4A4A) else Color.Gray,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 2. TOMBOL AKSI UTAMA
            Button(
                onClick = {
                    if (!youtubeLink.isNullOrEmpty()) {
                        onWatchTutorialClick()
                    } else {
                        onViewScheduleClick()
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = themeColor),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = "Set Workout Schedule",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )
            }
        }
    }
}

// Fungsi untuk mengambil ID unik dari link YouTube
fun getYoutubeVideoId(url: String): String {
    return when {
        url.contains("v=") -> url.substringAfter("v=").substringBefore("&")
        url.contains("youtu.be/") -> url.substringAfter("youtu.be/").substringBefore("?")
        url.contains("embed/") -> url.substringAfter("embed/").substringBefore("?")
        url.contains("shorts/") -> url.substringAfter("shorts/").substringBefore("?").substringBefore("&")
        else -> ""
    }
}

@Composable
fun YoutubeVideoPlayer(youtubeUrl: String, modifier: Modifier = Modifier) {
    val videoId = getYoutubeVideoId(youtubeUrl)

    if (videoId.isEmpty()) {
        Box(modifier = modifier.background(Color.Black), contentAlignment = Alignment.Center) {
            Text("Format link YouTube tidak valid", color = Color.White)
        }
        return
    }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                // Paksa WebView biar ukurannya memenuhi kotak header
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                settings.javaScriptEnabled = true
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                settings.mediaPlaybackRequiresUserGesture = false
                settings.domStorageEnabled = true

                // Set background WebView jadi hitam agar pas loading ngga putih
                setBackgroundColor(AndroidColor.BLACK)

                // 👇 INI JANTUNGNYA "ULTRA CLEAN MODE" 👇
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)

                        // Script ini akan berjalan terus menerus setiap 300 milidetik
                        // untuk memastikan video selalu menutupi seluruh layar
                        val jsCode = """
                            javascript:(function() {
                                function nukeUI() {
                                    // 1. Sembunyikan elemen sampah yang sering ngalangin
                                    var garbage = document.querySelectorAll('ytm-mobile-topbar-renderer, ytm-single-column-watch-next-results-renderer, .ytp-chrome-top, .ytp-chrome-bottom, ytm-promoted-app-install-renderer');
                                    garbage.forEach(function(el) { 
                                        if(el) el.style.setProperty('display', 'none', 'important'); 
                                    });

                                    // 2. Culik tag <video> aslinya dan paksa jadi Full Screen di atas segalanya
                                    var video = document.querySelector('video');
                                    if (video) {
                                        video.style.setProperty('position', 'fixed', 'important');
                                        video.style.setProperty('top', '0', 'important');
                                        video.style.setProperty('left', '0', 'important');
                                        video.style.setProperty('width', '100vw', 'important');
                                        video.style.setProperty('height', '100vh', 'important');
                                        video.style.setProperty('object-fit', 'contain', 'important'); // Biar rasio video tetep proporsional
                                        video.style.setProperty('z-index', '999999', 'important');
                                        video.style.setProperty('background', 'black', 'important');
                                    }
                                }

                                // Eksekusi fungsi nukeUI setiap 300ms 
                                // (Karena YouTube meload UI secara bertahap)
                                setInterval(nukeUI, 300);
                                
                                // Paksa body background jadi hitam
                                document.body.style.setProperty('background', 'black', 'important');
                            })();
                        """.trimIndent()

                        // Tembakkan script ke WebView
                        view?.evaluateJavascript(jsCode, null)
                    }
                }
                webChromeClient = WebChromeClient()
            }
        },
        update = { webView ->
            webView.loadUrl("https://m.youtube.com/watch?v=$videoId")
        }
    )
}


//@Preview(showBackground = true)
//@Composable
//fun WorkoutDetailPreview() {
//    WorkoutDetailScreen(
//        workoutId = 1, // Ganti dengan ID latihan yang sesuai
//        onNavigateBack = {}
//    )
//}