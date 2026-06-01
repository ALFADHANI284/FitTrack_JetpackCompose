package com.aplikasi.fittrack.ui.setup

import OnboardingViewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun OnboardingHostScreen(
    viewModel: OnboardingViewModel = viewModel(),
    onFinishOnboarding: () -> Unit // Dipanggil pas semua slide beres buat pindah ke Home
) {
    var currentStep by remember { mutableStateOf(1) }

    // Logika pergantian layar
    when (currentStep) {
        1 -> {
            // SLIDE 1: Alasan/Motivasi
            Motivation(
                onNextClick = { jawabanMotivasi ->
                    viewModel.updateMotivation(jawabanMotivasi)
                    currentStep = 2
                }
            )
        }
        2 -> {
            // SLIDE 2: Target Goal (Lose weight, dll)
            TargetGoal(
                onNextClick = { jawabanTarget ->
                    viewModel.updateGoal(jawabanTarget)
                    currentStep = 3 // Lanjut ke gender & umur
                },
                onBackClick = { currentStep = 1 }
            )
        }
        3 -> {
            // SLIDE 3: Gender & Umur
            GenderAge(
                onNextClick = { gender, age ->
                    viewModel.updateGender(gender)
                    viewModel.updateAge(age)
                    currentStep = 4 // Lanjut ke berat & tinggi
                },
                onBackClick = { currentStep = 2 }
            )
        }
        4 -> {
            // SLIDE 4: Berat & Tinggi Badan
            PhysicalData(
                onNextClick = { weight, height ->
                    viewModel.updateWeight(weight)
                    viewModel.updateHeight(height)
                    currentStep = 5 // Lanjut ke level aktivitas
                },
                onBackClick = { currentStep = 3 }
            )
        }
        5 -> {
            // SLIDE 5: Level Aktivitas (Jarang olahraga, aktif, dll)
            ActivityLevel(
                onNextClick = { activityLevel ->
                    viewModel.updateActivityLevel(activityLevel)
                    currentStep = 6 // Lanjut ke kalkulasi API
                },
                onBackClick = { currentStep = 4 }
            )
        }
        6 -> {
            // SLIDE 6: Loading & Tembak API
            Calculation(
                viewModel = viewModel,
                onSuccess = onFinishOnboarding, // Pindah ke HomeScreen kalau sukses
                onBackClick = { currentStep = 5 }
            )
        }
    }
}