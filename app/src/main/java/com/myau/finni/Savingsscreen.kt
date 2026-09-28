package com.myau.finni

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.ceil

/**
 * Накопления и финансовая цель — ТЗ 2.5.7.
 * Снятие возможно только после отдельного подтверждения,
 * в котором заранее показано, как уменьшится сумма и как изменится срок.
 */
@Composable
fun SavingsScreen(vm: GameViewModel, onBack: () -> Unit) {
    val s = vm.state

    var amount by remember { mutableIntStateOf(10) }
    var withdrawAmount by remember { mutableIntStateOf(10) }
    var confirmWithdraw by remember { mutableStateOf(false) }

    val left = (s.goalPrice - s.savings).coerceAtLeast(0)
    val perDay = if (s.day > 1) s.savings / (s.day - 1) else s.savings
    val daysLeft = if (perDay > 0) ceil(left.toFloat() / perDay).toInt() else null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Bg)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(16.dp))
        TopBar("Копилка") { onBack() }

        FinniCard(bg = MintSoft) {
            Text("В копилке", fontSize = 16.sp, color = Muted, fontWeight = FontWeight.Bold)
            Text("${s.savings} 🪙", fontSize = 30.sp, color = Ink, fontWeight = FontWeight.Bold)
            Text("В кошельке ${s.coins} 🪙", fontSize = 15.sp, color = Muted)
        }

        if (s.goalTitle.isBlank()) {
            FinniCard {
                Text("Цель ещё не выбрана", fontSize = 18.sp, color = Ink, fontWeight = FontWeight.Bold)
                Text("Выбери мечту, чтобы копилка показывала прогресс", fontSize = 16.sp, color = Muted)
            }
        } else {
            FinniCard {
                Text("Моя мечта", fontSize = 16.sp, color = Muted, fontWeight = FontWeight.Bold)
                Text(s.goalTitle, fontSize = 22.sp, color = Ink, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text(
                    "Накоплено ${s.savings} из ${s.goalPrice}. Осталось $left",
                    fontSize = 16.sp,
                    color = Ink
                )
                Spacer(Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = {
                        if (s.goalPrice == 0) 0f
                        else (s.savings.toFloat() / s.goalPrice).coerceIn(0f, 1f)
                    },
                    modifier = Modifier.fillMaxWidth().height(12.dp),
                    color = Primary,
                    trackColor = PrimarySoft
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    when {
                        left == 0 -> "Цель достигнута!"
                        daysLeft == null -> "Срок появится после первого пополнения"
                        else -> "В среднем откладываешь $perDay 🪙 в день → примерно $daysLeft дней до цели"
                    },
                    fontSize = 15.sp,
                    color = Muted
                )
            }
        }

        FinniCard {
            Text("Отложить в копилку", fontSize = 18.sp, color = Ink, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            Counter(amount, 5, 5, s.coins.coerceAtLeast(5)) { amount = it }
            Spacer(Modifier.height(12.dp))
            FinniButton("Отложить $amount 🪙") {
                vm.addToSavings(amount)
            }
            if (s.coins < amount) {
                Spacer(Modifier.height(6.dp))
                Text(
                    "В кошельке только ${s.coins} 🪙",
                    fontSize = 15.sp,
                    color = Danger,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        FinniCard(bg = Surface2) {
            Text("Взять из копилки", fontSize = 18.sp, color = Ink, fontWeight = FontWeight.Bold)
            Text(
                "Снятие отодвигает мечту. Перед подтверждением покажем, насколько.",
                fontSize = 15.sp,
                color = Muted
            )
            Spacer(Modifier.height(12.dp))
            Counter(withdrawAmount, 5, 5, s.savings.coerceAtLeast(5)) { withdrawAmount = it }
            Spacer(Modifier.height(12.dp))
            FinniButton("Взять $withdrawAmount 🪙") { confirmWithdraw = true }
        }

        Spacer(Modifier.height(24.dp))
    }

    if (confirmWithdraw) {
        val after = (s.savings - withdrawAmount).coerceAtLeast(0)
        val leftAfter = (s.goalPrice - after).coerceAtLeast(0)
        val daysAfter = if (perDay > 0) ceil(leftAfter.toFloat() / perDay).toInt() else null

        AlertDialog(
            onDismissRequest = { confirmWithdraw = false },
            containerColor = Surface,
            title = {
                Text(
                    "Взять $withdrawAmount 🪙 из копилки?",
                    fontSize = 20.sp,
                    color = Ink,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text("В копилке: ${s.savings} → $after 🪙", fontSize = 17.sp, color = Ink)
                    Spacer(Modifier.height(6.dp))
                    if (daysLeft != null && daysAfter != null) {
                        Text("До мечты: $daysLeft → $daysAfter дней", fontSize = 17.sp, color = Danger)
                    } else {
                        Text("Срок появится после регулярных пополнений", fontSize = 16.sp, color = Muted)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    vm.withdrawFromSavings(withdrawAmount)
                    confirmWithdraw = false
                }) {
                    Text("Взять", fontSize = 17.sp, color = Primary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmWithdraw = false }) {
                    Text("Отмена", fontSize = 17.sp, color = Muted)
                }
            }
        )
    }
}

@Composable
private fun Counter(value: Int, step: Int, min: Int, max: Int, onChange: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedButton(
            onClick = { onChange((value - step).coerceAtLeast(min)) },
            modifier = Modifier.size(56.dp)
        ) { Text("−", fontSize = 22.sp) }

        Spacer(Modifier.width(20.dp))
        Text("$value 🪙", fontSize = 24.sp, color = Ink, fontWeight = FontWeight.Bold)
        Spacer(Modifier.width(20.dp))

        OutlinedButton(
            onClick = { onChange((value + step).coerceAtMost(max)) },
            modifier = Modifier.size(56.dp)
        ) { Text("+", fontSize = 22.sp) }
    }
}