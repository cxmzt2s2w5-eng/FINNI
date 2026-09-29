package com.myau.finni

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Одна мечта: название, цена, картинка из drawable */
private data class Dream(val title: String, val price: Int, val image: Int)

@Composable
fun DreamScreen(
    vm: GameViewModel,
    onBack: () -> Unit = {},
    onDone: () -> Unit = {}
) {
    var selectedDream by remember { mutableIntStateOf(0) }

    val dreams = listOf(
        Dream("Игровая площадка", 200, R.drawable.dream_playground),
        Dream("Велосипед", 350, R.drawable.dream_bike),
        Dream("Телескоп", 500, R.drawable.dream_telescope)
    )
    val chosen = dreams[selectedDream]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Bg)
    ) {

        // --- шапка: общая для всех экранов ---
        ScreenHeader(
            "Выбери мечту",
            onBack = onBack,
            fontSize = 22.sp,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
        )

        // --- содержимое ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(10.dp))

            Text(
                "На что будем копить?",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Ink,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            Text(
                "Выбери цель, которая тебе нравится.\nПотом Финни поможет тебе к ней прийти!",
                fontSize = 16.sp,
                lineHeight = 22.sp,
                color = Muted,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(20.dp))

            dreams.forEachIndexed { index, dream ->
                DreamCard(
                    dream = dream,
                    selected = selectedDream == index
                ) { selectedDream = index }
                Spacer(Modifier.height(12.dp))
            }

            Spacer(Modifier.height(6.dp))

            // --- итог выбора ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(PrimarySoft)
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Твоя мечта", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Primary)
                    Spacer(Modifier.height(4.dp))
                    Text(chosen.title, fontSize = 21.sp, fontWeight = FontWeight.Bold, color = Ink)
                    Spacer(Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Стоимость: ${chosen.price}", fontSize = 15.sp, color = Muted)
                        Spacer(Modifier.width(4.dp))
                        Coin(18.dp)
                    }
                }
                Image(
                    painter = painterResource(chosen.image),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(64.dp)
                )
            }

            Spacer(Modifier.height(20.dp))
        }

        // --- кнопка ---
        NextButton(
            text = "Выбрать мечту",
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 16.dp)
        ) {
            vm.setGoal(chosen.title, chosen.price)
            onDone()
        }
    }
}

// ---------------------------------------------------------------------------
// Карточка мечты
// ---------------------------------------------------------------------------

@Composable
private fun DreamCard(
    dream: Dream,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(104.dp)
            .shadow(if (selected) 6.dp else 2.dp, RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .background(if (selected) PrimarySoft else Color.White)
            .border(
                width = if (selected) 2.5.dp else 1.dp,
                color = if (selected) Primary else Line,
                shape = RoundedCornerShape(24.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // картинка мечты на светлом круге
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(if (selected) Color.White else Bg),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(dream.image),
                contentDescription = dream.title,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(64.dp)
            )
        }

        Spacer(Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(dream.title, fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Ink)
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("${dream.price}", fontSize = 17.sp, color = Ink, fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(4.dp))
                Coin(20.dp)
            }
        }

        // галочка выбранной
        if (selected) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Primary),
                contentAlignment = Alignment.Center
            ) {
                Text("✓", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}