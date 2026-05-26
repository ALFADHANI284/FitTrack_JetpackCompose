package com.aplikasi.fittrack.ui.screens.auth

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aplikasi.fittrack.model.LoginRequest
import com.aplikasi.fittrack.network.RetrofitClient
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToRegister: () -> Unit, // Buat tombol Sign Up di bawah
    onNavigateToAdmin: () -> Unit //
) {
    // 1. STATE: Untuk API
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) } // State loading
    var loginResult by remember { mutableStateOf<String?>(null) } // Pesan sukses/error

    // 2. SCOPE: Buat ngejalanin fungsi suspend (asynchronous)
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 1. Background Kuning Atas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .background(Color(0xFFFFC107))
        )

        // 2. Logo Aplikasi
        Image(
            painter = painterResource(id = android.R.drawable.ic_menu_camera),
            contentDescription = "App Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(width = 220.dp, height = 100.dp)
                .align(Alignment.TopCenter)
                .padding(top = 40.dp)
        )

        // 3. Card Putih Melengkung
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = 180.dp),
            shape = RoundedCornerShape(topStart = 80.dp, topEnd = 80.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(25.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Teks Judul
                Text(
                    text = "Login",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 20.dp, bottom = 10.dp) // Bottom dikurangi dikit
                )

                // 3. Pesan error/sukses di bawah judul
                loginResult?.let { message ->
                    Text(
                        text = message,
                        color = if (message.startsWith("Gagal")) Color.Red else Color(0xFF4CAF50),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                // Input Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    shape = RoundedCornerShape(12.dp)
                )

                // Input Password
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 30.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    shape = RoundedCornerShape(12.dp)
                )

                // 4. TOMBOL LOGIN
                Button(
                    // Logika ke API Laravel
                    onClick = {
                        if (email.isNotBlank() && password.isNotBlank()) {
                            isLoading = true
                            loginResult = null
                            coroutineScope.launch {
                                try {
                                    val request = LoginRequest(email, password)
                                    val response = RetrofitClient.instance.loginUser(request)

                                    // 1. Simpan Token ke HP
                                    val sharedPref = context.getSharedPreferences("FitTrackPrefs", Context.MODE_PRIVATE)
                                    sharedPref.edit().putString("ACCESS_TOKEN", response.token).apply()

                                    Toast.makeText(context, "Login Berhasil!", Toast.LENGTH_SHORT).show()

                                    // 2. LOGIKA CABANG BERDASARKAN ROLE
                                    if (response.role == "admin") {
                                        onNavigateToAdmin() // ke Dashboard Admin
                                    } else {
                                        onNavigateToHome() // ke Home biasa
                                    }

                                } catch (e: Exception) {
                                    e.printStackTrace()

                                    // Cek apakah error dari HTTP (Server) atau dari Gagal Baca JSON
                                    if (e is retrofit2.HttpException) {
                                        loginResult = "Gagal: Email/Password salah!"
                                    } else {
                                        loginResult = "Error Sistem: ${e.localizedMessage}"
                                    }
                                } finally {
                                    isLoading = false
                                }
                            }
                        } else {
                            loginResult = "Gagal: Email dan Password wajib diisi!"
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .padding(bottom = 25.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107)),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isLoading
                ) {
                    // Ganti teks jadi loading spinner kalau lagi request API
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Login", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Tombol Login Google
                Button(
                    onClick = { /* TODO: Google Sign-In */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .padding(bottom = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF0000)),
                    shape = RoundedCornerShape(80.dp)
                ) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_dialog_email),
                        contentDescription = "Google Icon",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Login with Google", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                // Teks Sign Up
                Text(
                    text = "Don’t have any account ? Sign Up",
                    color = Color(0xFF555555),
                    fontSize = 13.sp,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .clickable { onNavigateToRegister() }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        onNavigateToHome = {},
        onNavigateToRegister = {},
        onNavigateToAdmin = {}
    )
}