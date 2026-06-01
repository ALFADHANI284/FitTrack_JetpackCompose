package com.aplikasi.fittrack.ui.setup

import OnboardingViewModel
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aplikasi.fittrack.network.RetrofitClient
import kotlinx.coroutines.delay

@Composable
fun Calculation(
    viewModel: OnboardingViewModel,
    onSuccess: () -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Ambil token dari SharedPreferences (sesuaikan dengan caramu simpan token saat login)
    val sharedPref = context.getSharedPreferences("FitTrackPrefs", Context.MODE_PRIVATE)
    val token = sharedPref.getString("ACCESS_TOKEN", "") ?: ""

    // LaunchedEffect jalan otomatis saat layar ini dibuka
    LaunchedEffect(Unit) {
        try {
            // 1. Ambil semua data dari ViewModel
            val requestData = viewModel.buildRequest()

            // 2. Tembak API Laravel
            // (Pastikan RetrofitClient.instance udah terhubung ke method saveOnboarding yang kita bikin)
            val response = RetrofitClient.instance.saveOnboarding("Bearer $token", requestData)

            if (response.status) {
                // Biar user sempet liat animasi loading bentar (nggak terlalu cepet)
                delay(1000)
                isLoading = false
                onSuccess() // Langsung lempar ke Home
            } else {
                isLoading = false
                errorMessage = response.message
            }
        } catch (e: Exception) {
            isLoading = false
            errorMessage = "Gagal terhubung ke server: ${e.message}"
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = Color(0xFFFFB200))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Sedang meracik program terbaik untukmu...", fontWeight = FontWeight.Bold)
        } else if (errorMessage != null) {
            Text("Oops, ada masalah!", color = Color.Red, fontWeight = FontWeight.Bold)
            Text(errorMessage ?: "", textAlign = TextAlign.Center, modifier = Modifier.padding(top = 8.dp))
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onBackClick, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB200))) {
                Text("Coba Lagi")
            }
        }
    }
}