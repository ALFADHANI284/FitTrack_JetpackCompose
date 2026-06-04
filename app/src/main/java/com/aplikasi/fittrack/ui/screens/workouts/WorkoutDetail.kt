package com.aplikasi.fittrack.ui.screens.workouts

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.view.ViewGroup
import android.webkit.JavascriptInterface
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
    workoutId: Int,
    onNavigateBack: () -> Unit
) {
    val yellowTheme = Color(0xFFFFB300)
    val context = LocalContext.current
    val apiService = RetrofitClient.instance
    val viewModel = remember { WorkoutDetailViewModel(apiService, context) }

    var workoutDetail by remember { mutableStateOf<WorkoutResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // 💡 1. STATE RAHASIA BUAT MUNCULIN TOMBOL
    var isVideoFinished by remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()
    val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
    val currentMinute = calendar.get(Calendar.MINUTE)

    val timePickerDialog = android.app.TimePickerDialog(
        context,
        { _, hourOfDay, minute -> viewModel.addWorkoutSchedule(workoutId, hourOfDay, minute) },
        currentHour, currentMinute, true
    )

    LaunchedEffect(workoutId) {
        try {
            val sharedPref = context.getSharedPreferences("FitTrackPrefs", Context.MODE_PRIVATE)
            val savedToken = sharedPref.getString("ACCESS_TOKEN", "") ?: ""
            val userToken = "Bearer $savedToken"

            val response = apiService.getWorkouts(userToken)
            if (response.status) {
                val detail = response.data.find { it.id == workoutId }
                workoutDetail = detail
                viewModel.setInitialFavoriteStatus(detail?.isFavorite ?: false)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        bottomBar = {
            WorkoutBottomBar(
                workoutId = workoutDetail?.id ?: 0,
                viewModel = viewModel,
                themeColor = yellowTheme,
                youtubeLink = workoutDetail?.link_yt,
                onWatchTutorialClick = { timePickerDialog.show() },
                onViewScheduleClick = { timePickerDialog.show() }
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
            val detail = workoutDetail!!

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                // HEADER BOX
                Box(modifier = Modifier.fillMaxWidth().height(350.dp).background(Color.Black)) {

                    if (!detail.link_yt.isNullOrEmpty()) {

                        // 💡 Panggil komponen WebView andalan lu, dan tangkap sinyal selesainya!
                        YoutubeVideoPlayer(
                            youtubeUrl = detail.link_yt,
                            modifier = Modifier.fillMaxSize(),
                            onVideoEnded = {
                                isVideoFinished = true // TOMBOL TRIGGERED CO! BOOM! 🚀
                            }
                        )

                    } else {
                        Image(
                            painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                            contentDescription = "Workout Header",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize().background(Color.LightGray)
                        )
                    }

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
                Column(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = "Rating", tint = yellowTheme, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("4.9", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }

                    Text(text = detail.name, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black, modifier = Modifier.padding(top = 8.dp))

                    Row(modifier = Modifier.padding(top = 16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
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

                    Text(text = "About The Program", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.padding(bottom = 12.dp))
                    Text(text = detail.description ?: "Tidak ada deskripsi untuk latihan ini.", fontSize = 15.sp, color = Color(0xFF424242), lineHeight = 22.sp)

                    Spacer(modifier = Modifier.height(32.dp))

                    // 💡 3. TOMBOL AJAIB MUNCUL DI BAWAH SINI
                    if (isVideoFinished) {
                        Button(
                            onClick = {
                                // Tembak API dan simpan history!
                                viewModel.finishWorkout(
                                    workoutId = detail.id,
                                    duration = detail.duration_minutes ?: 0,
                                    calories = detail.calories_burned ?: 0
                                )
                                // Tendang balik ke Home biar bisa liat bar chart-nya naik
                                onNavigateBack()
                            },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = yellowTheme),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text("Selesai & Simpan Progress", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
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

@SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
@Composable
fun YoutubeVideoPlayer(
    youtubeUrl: String,
    modifier: Modifier = Modifier,
    onVideoEnded: () -> Unit
) {
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
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                settings.javaScriptEnabled = true
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                settings.mediaPlaybackRequiresUserGesture = false
                settings.domStorageEnabled = true
                setBackgroundColor(AndroidColor.BLACK)

                // 💡 2. INI JEMBATANNYA (ANDROID BRIDGE)
                // Objek ini ngizinin Javascript di dalam WebView manggil fungsi Kotlin
                addJavascriptInterface(object {
                    @JavascriptInterface
                    fun triggerEnded() {
                        // Harus dilempar ke Main Thread biar UI Compose aman pas diubah
                        android.os.Handler(Looper.getMainLooper()).post {
                            onVideoEnded()
                        }
                    }
                }, "AndroidBridge")

                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)

                        val jsCode = """
                            javascript:(function() {
                                function nukeUI() {
                                    var garbage = document.querySelectorAll('ytm-mobile-topbar-renderer, ytm-single-column-watch-next-results-renderer, .ytp-chrome-top, .ytp-chrome-bottom, ytm-promoted-app-install-renderer');
                                    garbage.forEach(function(el) { 
                                        if(el) el.style.setProperty('display', 'none', 'important'); 
                                    });

                                    var video = document.querySelector('video');
                                    if (video) {
                                        video.style.setProperty('position', 'fixed', 'important');
                                        video.style.setProperty('top', '0', 'important');
                                        video.style.setProperty('left', '0', 'important');
                                        video.style.setProperty('width', '100vw', 'important');
                                        video.style.setProperty('height', '100vh', 'important');
                                        video.style.setProperty('object-fit', 'contain', 'important');
                                        video.style.setProperty('z-index', '999999', 'important');
                                        video.style.setProperty('background', 'black', 'important');
                                        
                                        // 💡 3. SENSOR DETEKSI VIDEO TAMAT
                                        if (!video.hasEndedListener) {
                                            video.addEventListener('ended', function() {
                                                // Kalau video kelar, panggil Jembatan Kotlin tadi!
                                                AndroidBridge.triggerEnded(); 
                                            });
                                            video.hasEndedListener = true;
                                        }
                                    }
                                }
                                setInterval(nukeUI, 300);
                                document.body.style.setProperty('background', 'black', 'important');
                            })();
                        """.trimIndent()

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