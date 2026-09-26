package com.myau.finni

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DreamScreen(
    onBack: () -> Unit = {},
    onDone: () -> Unit = {}
) {

    var selectedDream by remember {
        mutableIntStateOf(0)
    }

    val dreams = listOf(
        Triple("🛝", "Игровая площадка", "200 🪙"),
        Triple("🚲", "Велосипед", "350 🪙"),
        Triple("🔭", "Телескоп", "500 🪙")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Bg)
    ) {

        // ---------------------------------------------------------
        // ШАПКА
        // ---------------------------------------------------------

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 18.dp,
                    bottom = 8.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(17.dp))
                    .background(Surface)
                    .clickable {
                        onBack()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "←",
                    fontSize = 25.sp,
                    color = Ink
                )
            }

            Text(
                text = "Выбери мечту",
                modifier = Modifier.weight(1f),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Ink,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.size(52.dp)
            )
        }

        // ---------------------------------------------------------
        // ОСНОВНОЕ СОДЕРЖИМОЕ
        // ---------------------------------------------------------

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "На что будем копить?",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Ink,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Выбери цель, которая тебе нравится.\n"
                        + "Потом Финни поможет тебе к ней прийти!",
                fontSize = 17.sp,
                lineHeight = 24.sp,
                color = Muted,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(22.dp))

            // -----------------------------------------------------
            // КАРТОЧКИ МЕЧТ
            // -----------------------------------------------------

            dreams.forEachIndexed { index, dream ->

                DreamCard(
                    icon = dream.first,
                    title = dream.second,
                    price = dream.third,
                    selected = selectedDream == index,
                    onClick = {
                        selectedDream = index
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Информация о выборе
            FinniCard(
                bg = PrimarySoft
            ) {

                Text(
                    text = "Твоя мечта",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = dreams[selectedDream].second,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    color = Ink
                )

                Text(
                    text = "Стоимость: ${dreams[selectedDream].third}",
                    fontSize = 15.sp,
                    color = Muted
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // ---------------------------------------------------------
        // КНОПКА
        // ---------------------------------------------------------

        Button(
            onClick = {
                onDone()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 16.dp
                )
                .height(58.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary
            )
        ) {

            Text(
                text = "Выбрать мечту →",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


// ================================================================
// КАРТОЧКА МЕЧТЫ
// ================================================================

@Composable
private fun DreamCard(
    icon: String,
    title: String,
    price: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    val borderColor =
        if (selected) Primary else Line

    val backgroundColor =
        if (selected) PrimarySoft else Surface

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(backgroundColor)
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(22.dp)
            )
            .clickable {
                onClick()
            }
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = icon,
            fontSize = 45.sp
        )

        Spacer(modifier = Modifier.size(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = title,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = Ink
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = price,
                fontSize = 15.sp,
                color = Muted
            )
        }

        // Галочка выбранной цели
        if (selected) {

            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Primary),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "✓",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = Surface
                )
            }
        }
    }
}