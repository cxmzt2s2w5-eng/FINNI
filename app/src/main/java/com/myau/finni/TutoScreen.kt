package com.myau.finni

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun TutoScreen(
    onFinish: () -> Unit
) {
    var page by remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Bg)
    ) {

        // Основное содержимое
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    top = 20.dp,
                    bottom = 110.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Точки
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) { index ->
                    TutorialDot(active = index == page)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Текущий слайд
            when (page) {
                0 -> TutorialWelcome()
                1 -> TutorialMoney()
                2 -> TutorialDay()
            }
        }

        // Нижняя кнопка
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Bg)
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    bottom = 20.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(
                onClick = {
                    if (page < 2) {
                        page++
                    } else {
                        onFinish()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary
                )
            ) {
                Text(
                    text = if (page < 2) {
                        "Дальше →"
                    } else {
                        "Начать игру →"
                    },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${page + 1} из 3",
                fontSize = 14.sp,
                color = Muted
            )
        }
    }
}


// ================================================================
// ЭКРАН 1 — ПРИВЕТСТВИЕ
// ================================================================

@Composable
private fun TutorialWelcome() {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Привет! Я Финни",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Ink,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Будем заботиться обо мне и вместе\nкопить на мечту.",
            fontSize = 17.sp,
            lineHeight = 24.sp,
            color = Muted,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Большая область с Финни
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(330.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(PrimarySoft),
            contentAlignment = Alignment.Center
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🐻",
                    fontSize = 145.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        TutorialInfoCard(
            icon = "👋",
            title = "Приветствие",
            text = "Финни будет рядом, пока ты учишься обращаться с деньгами."
        )
    }
}


// ================================================================
// ЭКРАН 2 — НУЖНОЕ / ЖЕЛАЕМОЕ / МЕЧТА
// ================================================================

@Composable
private fun TutorialMoney() {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Как будем тратить деньги?",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Ink,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "У каждой монетки есть своё дело.",
            fontSize = 17.sp,
            color = Muted,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(22.dp))

        TutorialMoneyCard(
            icon = "🍲",
            title = "Сначала — нужное",
            description = "Еда и уход за питомцем.",
            background = YellowSoft
        )

        TutorialMoneyCard(
            icon = "🪁",
            title = "Потом — желаемое",
            description = "Игрушки и украшения можно перенести.",
            background = Surface
        )

        TutorialMoneyCard(
            icon = "🐷",
            title = "И часть — на мечту",
            description = "Небольшие накопления приближают цель.",
            background = MintSoft
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Главное — найти баланс 💜",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = Primary
        )
    }
}


// ================================================================
// ЭКРАН 3 — ИГРОВОЙ ДЕНЬ
// ================================================================

@Composable
private fun TutorialDay() {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Как проходит день?",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Ink,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Каждый день ты принимаешь решения\nи видишь их результат.",
            fontSize = 17.sp,
            lineHeight = 23.sp,
            color = Muted,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(22.dp))

        TutorialStep(
            number = "1",
            icon = "🪙",
            title = "Получаешь деньги",
            description = "В начале дня у тебя появляется новый бюджет."
        )

        TutorialArrow()

        TutorialStep(
            number = "2",
            icon = "💰",
            title = "Принимаешь решения",
            description = "Решай, что потратить, а что сохранить."
        )

        TutorialArrow()

        TutorialStep(
            number = "3",
            icon = "🎯",
            title = "Копишь на мечту",
            description = "Каждая отложенная монетка приближает тебя к цели."
        )

        TutorialArrow()

        TutorialStep(
            number = "4",
            icon = "🌙",
            title = "Заканчиваешь день",
            description = "Смотришь результат и переходишь к следующему дню."
        )
    }
}


// ================================================================
// ВСПОМОГАТЕЛЬНЫЕ КОМПОНЕНТЫ
// ================================================================

@Composable
private fun TutorialDot(active: Boolean) {

    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .size(
                if (active) 10.dp else 8.dp
            )
            .clip(CircleShape)
            .background(
                if (active) Primary else Line
            )
    )
}


@Composable
private fun TutorialInfoCard(
    icon: String,
    title: String,
    text: String
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Surface)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = icon,
            fontSize = 30.sp
        )

        Spacer(modifier = Modifier.size(12.dp))

        Column {

            Text(
                text = title,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Ink
            )

            Text(
                text = text,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = Muted
            )
        }
    }
}


@Composable
private fun TutorialMoneyCard(
    icon: String,
    title: String,
    description: String,
    background: androidx.compose.ui.graphics.Color
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(background)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = icon,
            fontSize = 34.sp
        )

        Spacer(modifier = Modifier.size(14.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Ink
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = description,
                fontSize = 15.sp,
                lineHeight = 20.sp,
                color = Muted
            )
        }
    }
}


@Composable
private fun TutorialStep(
    number: String,
    icon: String,
    title: String,
    description: String
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Surface)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(PrimarySoft),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = number,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Primary
            )
        }

        Spacer(modifier = Modifier.size(12.dp))

        Text(
            text = icon,
            fontSize = 30.sp
        )

        Spacer(modifier = Modifier.size(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = title,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Ink
            )

            Text(
                text = description,
                fontSize = 14.sp,
                lineHeight = 19.sp,
                color = Muted
            )
        }
    }
}


@Composable
private fun TutorialArrow() {

    Text(
        text = "↓",
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = Primary,
        modifier = Modifier.padding(vertical = 2.dp)
    )
}