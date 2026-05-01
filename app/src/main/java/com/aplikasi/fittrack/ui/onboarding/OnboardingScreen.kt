package com.aplikasi.fittrack.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aplikasi.fittrack.ui.theme.PrimaryColor
import com.aplikasi.fittrack.R


@Composable
fun OnboardingScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Ganti kalau ada warna background spesifik
    ) {
        // Bagian Gambar (Hero Image)
        Image(
            painter = painterResource(id = R.drawable.img_welcome_hero),
            contentDescription = "Welcome Hero",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Mengambil sisa ruang di atas
        )

        // Bagian Teks
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 80.dp, top = 16.dp, bottom = 32.dp)
        ) {
            Text(
                text = "Let’s",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "Get Started",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "everything start from here",
                fontSize = 20.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // Bagian Tombol
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 44.dp)
        ) {
            Button(
                onClick = onNavigateToLogin,
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryColor,
                ),
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier
                    .width(253.dp)
                    .height(50.dp)
            ) {
                Text(text = "Login", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onNavigateToRegister,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier
                    .width(253.dp)
                    .height(50.dp)
            ) {
                Text(text = "Register", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OnboardingScreenPreview() {
    OnboardingScreen(
        onNavigateToLogin = {},
        onNavigateToRegister = {}
    )
}