package com.myau.finni

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(
    vm: GameViewModel,
    onNavigate: (String) -> Unit
) {
    val s = vm.state

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Bg)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Spacer(Modifier.height(16.dp))
        Text("День ${s.day} из 5", fontSize = 16.sp, color = Muted, fontWeight = FontWeight.Bold)
        Text("Дом ${s.petName}", fontSize = 28.sp, color = Ink, fontWeight = FontWeight.Bold)

        // --- кошелёк и копилка ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FinniCard(bg = YellowSoft, modifier = Modifier.weight(1f)) {
                Text("В кошельке", fontSize = 16.sp, color = Muted, fontWeight = FontWeight.Bold)
                Text("${s.coins} 🪙", fontSize = 26.sp, color = Ink, fontWeight = FontWeight.Bold)
                Text("Доход дня +100", fontSize = 15.sp, color = Muted)
            }
            FinniCard(bg = MintSoft, modifier = Modifier.weight(1f)) {
                Text("В копилке", fontSize = 16.sp, color = Muted, fontWeight = FontWeight.Bold)
                Text("${s.savings} 🪙", fontSize = 26.sp, color = Ink, fontWeight = FontWeight.Bold)
                Text("Цель: ${s.goalPrice}", fontSize = 15.sp, color = Muted)
            }
        }

        // --- мечта ---
        FinniCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Моя мечта", fontSize = 16.sp, color = Muted, fontWeight = FontWeight.Bold)
                    Text(
                        if (s.goalTitle.isBlank()) "Цель не выбрана" else s.goalTitle,
                        fontSize = 20.sp,
                        color = Ink,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    "${s.savings} / ${s.goalPrice}",
                    fontSize = 16.sp,
                    color = Primary,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.height(10.dp))
            LinearProgressIndicator(
                progress = {
                    if (s.goalPrice == 0) 0f
                    else (s.savings.toFloat() / s.goalPrice).coerceIn(0f, 1f)
                },
                modifier = Modifier.fillMaxWidth().height(10.dp),
                color = Primary,
                trackColor = PrimarySoft
            )
        }

        // --- питомец ---
        FinniCard(bg = PrimarySoft) {
            Text(
                "Стадия ${s.petStage} · ${stageName(s.petStage)}",
                fontSize = 16.sp,
                color = Primary,
                fontWeight = FontWeight.Bold
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(petFace(s.mood), fontSize = 110.sp)
            }
        }

        // --- показатели состояния ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatusCard("🍲", "Сытость", levelText(s.satiety), Modifier.weight(1f))
            StatusCard("🧴", "Уход", levelText(s.care), Modifier.weight(1f))
            StatusCard("🙂", "Настроение", levelText(s.mood), Modifier.weight(1f))
        }

        // --- план на день ---
        FinniCard(bg = Surface2) {
            if (s.planConfirmed) {
                Text("План на день", fontSize = 15.sp, color = Muted, fontWeight = FontWeight.Bold)
                Text(
                    "Нужно ${s.planNeed} · Хочется ${s.planWant} · Копилка ${s.planSave}",
                    fontSize = 16.sp,
                    color = Ink
                )
                Text(
                    "Потрачено: ${s.factNeed} · ${s.factWant} · ${s.factSave}",
                    fontSize = 15.sp,
                    color = Muted
                )
            } else {
                Text("План не составлен", fontSize = 17.sp, color = Ink, fontWeight = FontWeight.Bold)
                Text("Реши заранее, сколько потратишь и сколько отложишь", fontSize = 15.sp, color = Muted)
                Spacer(Modifier.height(10.dp))
                FinniButton("Составить план") { onNavigate("plan") }
            }
        }

        Spacer(Modifier.height(8.dp))
        FinniButton("Завершить день ${s.day}") { onNavigate("summary") }

        Spacer(Modifier.height(16.dp))

        // --- нижняя навигация ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TabItem("🏠", "Дом", true) { }
            TabItem("📋", "План", false) { onNavigate("plan") }
            TabItem("🛒", "Магазин", false) { onNavigate("shop") }
            TabItem("🧩", "Задания", false) { onNavigate("tasks") }
            TabItem("👨‍👩‍👧", "Взрослым", false) { onNavigate("adult") }
        }
        Spacer(Modifier.height(24.dp))
    }
}

// ---------- помощники ----------

private fun stageName(stage: Int): String = when (stage) {
    3 -> "взрослый"
    2 -> "подросток"
    else -> "малыш"
}

private fun petFace(mood: Int): String = when {
    mood >= 66 -> "😺"
    mood >= 33 -> "🐱"
    else -> "🙀"
}

private fun levelText(value: Int): String = when {
    value >= 66 -> "Хорошо"
    value >= 33 -> "Средне"
    else -> "Нужно внимание"
}

@Composable
private fun StatusCard(icon: String, title: String, value: String, modifier: Modifier = Modifier) {
    FinniCard(modifier = modifier) {
        Text(icon, fontSize = 24.sp)
        Text(title, fontSize = 15.sp, color = Muted, fontWeight = FontWeight.Bold)
        Text(value, fontSize = 16.sp, color = Ink, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun TabItem(
    icon: String,
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .background(
                color = if (selected) PrimarySoft else Bg,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 10.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(icon, fontSize = 22.sp)
        Text(
            title,
            fontSize = 13.sp,
            color = if (selected) Primary else Muted,
            fontWeight = FontWeight.Bold
        )
    }
}