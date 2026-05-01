package com.aplikasi.fittrack.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aplikasi.fittrack.model.RegisterRequest
import com.aplikasi.fittrack.network.RetrofitClient
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(onNavigateToLogin: () -> Unit) {
    // 1. STATE INPUT: Menyimpan ketikan user
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // 2. STATE UI: Untuk toggle password dan loading API
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) } // State animasi loading
    var registerMessage by remember { mutableStateOf<String?>(null) } // Pesan sukses/gagal

    // 3. COROUTINE SCOPE: Wajib ada buat manggil API (suspend function)
    val coroutineScope = rememberCoroutineScope()

    val yellowTheme = Color(0xFFFFB300)
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // --- HEADER KUNING ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(yellowTheme)
                .padding(top = 40.dp, start = 16.dp, end = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { /* TODO: Navigasi Back ke Login */ }
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Register",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )
            }
        }

        // --- CARD PUTIH UTAMA ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = 120.dp),
            shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Create Your Account",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(bottom = 16.dp) // Dikurangi dikit jaraknya biar pas buat pesan error
                        .align(Alignment.Start)
                )

                // 4. PESAN ERROR/SUKSES: Tampil di bawah judul
                registerMessage?.let { message ->
                    Text(
                        text = message,
                        color = if (message.startsWith("Gagal")) Color.Red else Color(0xFF4CAF50),
                        fontSize = 14.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )
                }

                val textFieldColors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color.Black,
                    cursorColor = yellowTheme
                )

                // --- FORM INPUT ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = firstName,
                        onValueChange = { firstName = it },
                        label = { Text("First Name") },
                        modifier = Modifier.weight(1f),
                        colors = textFieldColors,
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        label = { Text("Last Name") },
                        modifier = Modifier.weight(1f),
                        colors = textFieldColors,
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    colors = textFieldColors,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, contentDescription = "Toggle Password")
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = textFieldColors,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = textFieldColors,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // 5. TOMBOL REGISTER DENGAN LOGIKA API
                Button(
                    onClick = {
                        // A. Validasi Confirm Password dulu di Android
                        if (password != confirmPassword) {
                            registerMessage = "Gagal: Password dan Confirm Password tidak sama!"
                            return@Button // Stop proses
                        }

                        // B. Validasi inputan kosong
                        if (firstName.isBlank() || email.isBlank() || password.isBlank()) {
                            registerMessage = "Gagal: First Name, Email, dan Password wajib diisi!"
                            return@Button // Stop proses
                        }

                        // C. Gabungkan First Name dan Last Name jadi satu String
                        val fullName = "$firstName $lastName".trim()

                        // D. Mulai proses tembak API
                        isLoading = true
                        registerMessage = null // Reset pesan error sebelumnya

                        coroutineScope.launch {
                            try {
                                val request = RegisterRequest(
                                    name = fullName,
                                    email = email,
                                    password = password
                                )
                                val response = RetrofitClient.instance.registerUser(request)

                                // MUNCULIN NOTIF TOAST
                                Toast.makeText(context, "Registrasi berhasil! Silakan login.", Toast.LENGTH_LONG).show()

                                // PINDAH KE HALAMAN LOGIN
                                onNavigateToLogin()

                            } catch (e: retrofit2.HttpException) {
                                // 1. Ambil body errornya
                                val errorBody = e.response()?.errorBody()?.string()

                                // 2. Coba bongkar JSON-nya
                                val displayMessage = try {
                                    val jsonObject = org.json.JSONObject(errorBody)
                                    jsonObject.getString("message")
                                } catch (parseException: Exception) {
                                    "Terjadi kesalahan server (Kode: ${e.code()})"
                                }

                                registerMessage = "Gagal: $displayMessage"

                            } catch (e: Exception) {
                                registerMessage = "Gagal: Koneksi terputus atau server mati."
                            } finally {
                                isLoading = false
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .border(2.dp, Color.Black, RoundedCornerShape(28.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = yellowTheme),
                    shape = RoundedCornerShape(28.dp),
                    enabled = !isLoading // Cegah user spam klik tombol
                ) {
                    // 6. GANTI TEKS JADI LOADING SPINNER KALAU LAGI PROSES
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Register", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Already have an account? ", color = Color.Gray)
                    Text(
                        text = "Login",
                        color = yellowTheme,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            // PINDAH KE HALAMAN LOGIN SAAT DIKLIK
                            onNavigateToLogin()
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(onNavigateToLogin = {})
}