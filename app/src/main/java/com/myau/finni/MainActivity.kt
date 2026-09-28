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
import androidx.compose.ui.platform.LocalContext
import com.myau.finni.ui.theme.FINNITheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            FINNITheme {

                val context = LocalContext.current
                val vm = remember { GameViewModel(ProgressManager(context)) }

                var screen by remember {
                    mutableStateOf(if (vm.hasProfile) "home" else "tuto")
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->

                    when (screen) {

                        "tuto" -> TutoScreen(
                            onFinish = { screen = "custom" }
                        )

                        "custom" -> FinniCustomScreen(
                            vm = vm,
                            onDone = {
                                screen = "dream"
                            }
                        )

                        "dream" -> DreamScreen(
                            vm = vm,
                            onBack = { screen = "custom" },
                            onDone = { screen = "home" }
                        )

                        "home" -> HomeScreen(
                            vm = vm,
                            onNavigate = { target -> screen = target }
                        )

                        "plan" -> PlanScreen(
                            vm = vm,
                            onBack = { screen = "home" }
                        )

                        "shop" -> ShopScreen(
                            vm = vm,
                            onBack = { screen = "home" }
                        )

                        "tasks" -> TasksScreen(
                            vm = vm,
                            onBack = { screen = "home" }
                        )

                        "savings" -> SavingsScreen(
                            vm = vm,
                            onBack = { screen = "home" }
                        )

                        "summary" -> SummaryScreen(
                            vm = vm,
                            onBack = { screen = "home" }
                        )

                        "progress" -> ProgressScreen(
                            vm = vm,
                            onBack = { screen = "home" }
                        )

                        "adult" -> AdultScreen(
                            vm = vm,
                            onBack = { screen = "home" }
                        )

                        else -> HomeScreen(
                            vm = vm,
                            onNavigate = { target -> screen = target }
                        )
                    }
                }
            }
        }
    }
}