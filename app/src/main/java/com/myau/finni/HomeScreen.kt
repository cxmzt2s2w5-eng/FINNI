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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen() {
    val day = 1
    val petName = "Финни"
    var coins by remember { mutableStateOf(100) }
    var savings by remember { mutableStateOf(0) }
    val goalTitle = "Игровая площадка"
    val goalPrice = 200

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Bg)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {

        // --- шапка ---
        Spacer(Modifier.height(16.dp))
        Text("День $day из 5", fontSize = 16.sp, color = Muted, fontWeight = FontWeight.Bold)
        Text("Дом $petName", fontSize = 28.sp, color = Ink, fontWeight = FontWeight.Bold)

        // --- плашка демо-режима ---
        FinniCard(bg = Ink) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "⚡ Деморежим: все этапы доступны",
                    fontSize = 16.sp,
                    color = Surface,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text("Сброс", fontSize = 16.sp, color = Surface)
            }
        }

        // --- кошелёк и копилка ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FinniCard(bg = YellowSoft, modifier = Modifier.weight(1f)) {
                Text("В кошельке", fontSize = 16.sp, color = Muted, fontWeight = FontWeight.Bold)
                Text("$coins 🪙", fontSize = 26.sp, color = Ink, fontWeight = FontWeight.Bold)
                Text("Доход дня +100", fontSize = 15.sp, color = Muted)
            }
            FinniCard(bg = MintSoft, modifier = Modifier.weight(1f)) {
                Text("В копилке", fontSize = 16.sp, color = Muted, fontWeight = FontWeight.Bold)
                Text("$savings 🪙", fontSize = 26.sp, color = Ink, fontWeight = FontWeight.Bold)
                Text("Цель: $goalPrice", fontSize = 15.sp, color = Muted)
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
                    Text("🛝 $goalTitle", fontSize = 20.sp, color = Ink, fontWeight = FontWeight.Bold)
                }
                Text(
                    "$savings / $goalPrice",
                    fontSize = 16.sp,
                    color = Primary,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.height(10.dp))
            LinearProgressIndicator(
                progress = { savings.toFloat() / goalPrice },
                modifier = Modifier.fillMaxWidth().height(10.dp),
                color = Primary,
                trackColor = PrimarySoft
            )
        }

        // --- питомец ---
        FinniCard(bg = PrimarySoft) {
            Text("Стадия 1 · малыш", fontSize = 16.sp, color = Primary, fontWeight = FontWeight.Bold)
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("🐻", fontSize = 110.sp)
            }
        }

        // --- показатели состояния ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatusCard("🍲", "Сытость", "Нужно поесть", Modifier.weight(1f))
            StatusCard("🧴", "Уход", "Не выполнен", Modifier.weight(1f))
            StatusCard("🙂", "Настроение", "Спокойное", Modifier.weight(1f))
        }

        // --- активное задание ---
        FinniCard(bg = Surface2) {
            Text("Активное задание · +10 🪙", fontSize = 15.sp, color = Muted, fontWeight = FontWeight.Bold)
            Text("Три конверта", fontSize = 20.sp, color = Ink, fontWeight = FontWeight.Bold)
            Text("Распредели 100 монет", fontSize = 16.sp, color = Muted)
            Spacer(Modifier.height(12.dp))
            FinniButton("Выполнить") { }
        }

        Spacer(Modifier.height(16.dp))

        // --- нижняя навигация ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TabItem("🏠", "Дом", true)
            TabItem("✉️", "План", false)
            TabItem("🛒", "Магазин", false)
            TabItem("🎯", "Задания", false)
            TabItem("🐷", "Копилка", false)
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun StatusCard(icon: String, title: String, value: String, modifier: Modifier = Modifier) {
    FinniCard(modifier = modifier) {
        Text(icon, fontSize = 24.sp)
        Text(title, fontSize = 15.sp, color = Muted, fontWeight = FontWeight.Bold)
        Text(value, fontSize = 17.sp, color = Ink, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun TabItem(icon: String, title: String, selected: Boolean) {
    Column(
        modifier = Modifier
            .background(
                color = if (selected) PrimarySoft else Bg,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(icon, fontSize = 22.sp)
        Text(
            title,
            fontSize = 14.sp,
            color = if (selected) Primary else Muted,
            fontWeight = FontWeight.Bold
        )
    }
}