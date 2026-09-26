package com.myau.finni

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.myau.finni.ui.theme.FINNITheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            FINNITheme {

                var currentScreen by remember {
                    mutableStateOf("tutorial")
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { _ ->

                    when (currentScreen) {

                        // =================================================
                        // 1. ОБУЧЕНИЕ
                        // =================================================

                        "tutorial" -> {

                            TutoScreen(
                                onFinish = {
                                    currentScreen = "custom"
                                }
                            )
                        }


                        // =================================================
                        // 2. СОЗДАНИЕ ФИННИ
                        // =================================================

                        "custom" -> {

                            FinniCustomScreen(
                                onBack = {
                                    currentScreen = "tutorial"
                                },

                                onDone = {
                                    currentScreen = "dream"
                                }
                            )
                        }


                        // =================================================
                        // 3. ВЫБОР МЕЧТЫ
                        // =================================================

                        "dream" -> {

                            DreamScreen(
                                onBack = {
                                    currentScreen = "custom"
                                },

                                onDone = {
                                    currentScreen = "home"
                                }
                            )
                        }


                        // =================================================
                        // 4. ГЛАВНЫЙ ЭКРАН
                        // =================================================

                        "home" -> {

                            HomeScreen()
                        }
                    }
                }
            }
        }
    }
}