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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PlanScreen(vm: GameViewModel, onBack: () -> Unit) {
    val s = vm.state
    val available = s.coins

    var need by remember { mutableIntStateOf(s.planNeed) }
    var want by remember { mutableIntStateOf(s.planWant) }
    var save by remember { mutableIntStateOf(s.planSave) }

    val total = need + want + save
    val left = available - total
    val fits = left >= 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Bg)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(16.dp))
        TopBar("План на день ${s.day}") { onBack() }

        FinniCard(bg = YellowSoft) {
            Text("Можно распределить", fontSize = 16.sp, color = Muted, fontWeight = FontWeight.Bold)
            Text("$available 🪙", fontSize = 28.sp, color = Ink, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            Text(
                if (fits) "Свободно ещё $left 🪙"
                else "Не хватает ${-left} 🪙 — уменьши одну из частей",
                fontSize = 16.sp,
                color = if (fits) Muted else Danger,
                fontWeight = FontWeight.Bold
            )
        }

        FinniCard {
            Text("Разложи деньги по трём коробкам", fontSize = 17.sp, color = Ink, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))

            PlanSlider("🍲 Нужно", need, available) { need = it }
            PlanSlider("🎈 Хочется", want, available) { want = it }
            PlanSlider("🐷 В копилку", save, available) { save = it }

            Spacer(Modifier.height(8.dp))
            Text(
                "Нужно — это еда и уход, без них Финни грустит. " +
                        "Хочется можно перенести на завтра, это не ошибка.",
                fontSize = 15.sp,
                color = Muted
            )
        }

        Spacer(Modifier.height(8.dp))
        FinniButton(
            if (s.planConfirmed) "Сохранить изменения" else "Подтвердить план"
        ) {
            if (fits) {
                vm.confirmPlan(need, want, save)
                onBack()
            }
        }

        if (s.planConfirmed) {
            FinniCard(bg = Surface2) {
                Text("План и факт", fontSize = 17.sp, color = Ink, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                CompareRow("Нужно", s.planNeed, s.factNeed)
                CompareRow("Хочется", s.planWant, s.factWant)
                CompareRow("В копилку", s.planSave, s.factSave)
                Spacer(Modifier.height(8.dp))
                Text(
                    "Точность плана: ${vm.planAccuracy()}%",
                    fontSize = 16.sp,
                    color = Primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun PlanSlider(label: String, value: Int, max: Int, onChange: (Int) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(label, fontSize = 17.sp, color = Ink, modifier = Modifier.weight(1f))
            Text("$value 🪙", fontSize = 17.sp, color = Ink, fontWeight = FontWeight.Bold)
        }
        Slider(
            value = value.toFloat(),
            onValueChange = { onChange(it.toInt()) },
            valueRange = 0f..max.coerceAtLeast(1).toFloat(),
            steps = (max / 5 - 1).coerceAtLeast(0)
        )
    }
}

@Composable
private fun CompareRow(label: String, plan: Int, fact: Int) {
    val diff = fact - plan
    val comment = when {
        diff == 0 -> "как в плане"
        diff > 0 -> "больше на $diff"
        else -> "меньше на ${-diff}"
    }
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 16.sp, color = Ink, modifier = Modifier.weight(1f))
        Text("$plan → $fact", fontSize = 16.sp, color = Ink, fontWeight = FontWeight.Bold)
        Text("  ($comment)", fontSize = 14.sp, color = Muted)
    }
}