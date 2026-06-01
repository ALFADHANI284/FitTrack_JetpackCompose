package com.aplikasi.fittrack.ui.setup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ActivityLevel(
    onNextClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val PrimaryColor = Color(0xFFFFB200)

    // Mapping label UI ke value database Laravel
    val activities = listOf(
        Pair("Jarang Olahraga (Rebahan)", "sedentary"),
        Pair("Aktif Ringan (1-3x seminggu)", "lightly_active"),
        Pair("Aktif Sedang (3-5x seminggu)", "moderately_active"),
        Pair("Sangat Aktif (Tiap Hari)", "very_active")
    )

    var selectedActivity by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxSize().background(Color.White).padding(24.dp)) {
        Spacer(modifier = Modifier.height(32.dp))
        Text("Seberapa aktif keseharianmu?", fontSize = 24.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(32.dp))

        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            activities.forEach { (uiText, apiValue) ->
                // Pastikan GoalOptionItem bisa diakses dari file ini
                GoalOptionItem(
                    text = uiText,
                    isSelected = selectedActivity == apiValue,
                    onClick = { selectedActivity = apiValue }
                )
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedButton(onClick = onBackClick, modifier = Modifier.weight(1f).height(54.dp)) { Text("Back", color = Color.Gray) }
            Button(
                onClick = { selectedActivity?.let { onNextClick(it) } },
                enabled = selectedActivity != null,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                modifier = Modifier.weight(1f).height(54.dp)
            ) { Text("Calculate", color = Color.White) }
        }
    }
}