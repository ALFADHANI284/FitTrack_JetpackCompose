package com.alfa.fittrack.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen() {
    // STATE: Ini seperti ref() di Vue. Menyimpan apa yang diketik user.
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Box Utama (Mewakili Constraint Layout)
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

        // 2. Logo Aplikasi (Posisinya di atas tengah)
        // Ganti R.drawable.ic_launcher_foreground dengan logo_ft_bg aslimu nanti
        Image(
            painter = painterResource(id = android.R.drawable.ic_menu_camera), // Placeholder logo
            contentDescription = "App Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(width = 220.dp, height = 100.dp)
                .align(Alignment.TopCenter)
                .padding(top = 40.dp)
        )

        // 3. Card Putih Melengkung (Mulai dari 180dp dari atas supaya menumpuk kuningnya)
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
                // Judul Login
                Text(
                    text = "Login",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 20.dp, bottom = 30.dp)
                )

                // Input Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it }, // Update state saat ngetik
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
                    onValueChange = { password = it }, // Update state saat ngetik
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(), // Menyamarkan teks jadi titik-titik
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 30.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    shape = RoundedCornerShape(12.dp)
                )

                // Tombol Login Biasa
                Button(
                    onClick = { /* TODO: Proses Login */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .padding(bottom = 25.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Login", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
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
                    // Gunakan Icon + Spacer + Text supaya rapi
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_dialog_email), // Placeholder icon
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
                        .clickable { /* TODO: Pindah ke halaman Register */ }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}