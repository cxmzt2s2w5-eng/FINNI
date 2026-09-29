package com.myau.finni

import androidx.compose.foundation.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Главный экран — ТЗ 2.5.3.
 * Игровой вид: комната на весь экран, питомец в центре,
 * показатели компактными плашками поверх фона.
 */
@Composable
fun HomeScreen(
    vm: GameViewModel,
    onNavigate: (String) -> Unit
) {
    val s = vm.state

    Box(modifier = Modifier.fillMaxSize()) {

        // --- фон комнаты ---
        Image(
            painter = painterResource(R.drawable.back),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // светлая вуаль сверху и снизу, середина остаётся яркой
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.75f),
                            Color.White.copy(alpha = 0.05f),
                            Color.White.copy(alpha = 0.05f),
                            Color.White.copy(alpha = 0.85f)
                        )
                    )
                )
        )

        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {

            Spacer(Modifier.height(14.dp))

            // --- верх: день и круглые кнопки ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "День ${s.day} из 5",
                        fontSize = 14.sp,
                        color = Muted,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Дом ${s.petName}",
                        fontSize = 26.sp,
                        color = Ink,
                        fontWeight = FontWeight.Bold
                    )
                }

                RoundButton("📈") { onNavigate("progress") }
                Spacer(Modifier.width(8.dp))
                RoundButton("👨‍👩‍👧") { onNavigate("adult") }
            }

            Spacer(Modifier.height(12.dp))

            // --- деньги ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MoneyPill(
                    icon = "🪙",
                    label = "Кошелёк",
                    value = s.coins,
                    accent = Yellow,
                    modifier = Modifier.weight(1f)
                )
                MoneyPill(
                    icon = "🐷",
                    label = "Копилка",
                    value = s.savings,
                    accent = Mint,
                    modifier = Modifier.weight(1f)
                ) { onNavigate("savings") }
            }

            Spacer(Modifier.height(10.dp))

            // --- цель ---
            GoalStrip(
                title = if (s.goalTitle.isBlank()) "Выбери мечту" else s.goalTitle,
                saved = s.savings,
                price = s.goalPrice
            ) { onNavigate("savings") }

            // --- питомец: главный герой экрана ---
            Box(
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(Primary)
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            "Стадия ${s.petStage} · ${stageName(s.petStage)}",
                            fontSize = 14.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(Modifier.height(6.dp))

                    // ЗДЕСЬ БУДЕТ КАРТИНКА ПИТОМЦА ОТ ДИЗАЙНЕРА
                    Text(petFace(s.mood), fontSize = 150.sp)
                }
            }

            // --- состояние ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatChip("🍲", "Сытость", s.satiety, Peach, Modifier.weight(1f))
                StatChip("🧴", "Уход", s.care, Blue, Modifier.weight(1f))
                StatChip("😊", "Настроение", s.mood, Yellow, Modifier.weight(1f))
            }

            Spacer(Modifier.height(10.dp))

            // --- главное действие дня ---
            if (!s.planConfirmed) {
                BigActionButton("📋  Составить план на день", Primary) { onNavigate("plan") }
            } else {
                BigActionButton("🌙  Завершить день ${s.day}", Peach) { onNavigate("summary") }
            }

            Spacer(Modifier.height(10.dp))

            // --- нижняя навигация, один ряд ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White.copy(alpha = 0.92f))
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                NavItem(
                    R.drawable.icon_home,
                    "Дом",
                    true
                ) {}

                NavItem(
                    R.drawable.icon_plan,
                    "План",
                    false
                ) { onNavigate("plan") }


                NavItem(
                    R.drawable.icon_shop,
                    "Магазин",
                    false
                ) { onNavigate("shop") }


                NavItem(
                    R.drawable.icon_tasks,
                    "Задания",
                    false
                ) { onNavigate("tasks") }


                NavItem(
                    R.drawable.icon_savings,
                    "Копилка",
                    false
                ) { onNavigate("savings") }
            }

            Spacer(Modifier.height(14.dp))
        }
    }
}

// ---------------------------------------------------------------------------
// Элементы интерфейса
// ---------------------------------------------------------------------------

@Composable
private fun MoneyPill(
    icon: String,
    label: String,
    value: Int,
    accent: Color,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.92f))
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(accent.copy(alpha = 0.30f)),
            contentAlignment = Alignment.Center
        ) {
            Text(icon, fontSize = 20.sp)
        }

        Spacer(Modifier.width(10.dp))

        Column {
            Text(label, fontSize = 13.sp, color = Muted, fontWeight = FontWeight.Bold)
            Text("$value", fontSize = 22.sp, color = Ink, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun GoalStrip(
    title: String,
    saved: Int,
    price: Int,
    onClick: () -> Unit
) {
    val progress = if (price == 0) 0f else (saved.toFloat() / price).coerceIn(0f, 1f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.92f))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "🎯  $title",
                fontSize = 15.sp,
                color = Ink,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Text("$saved / $price", fontSize = 14.sp, color = Primary, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(50))
                .background(PrimarySoft)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .height(10.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Primary)
            )
        }
    }
}

@Composable
private fun StatChip(
    icon: String,
    label: String,
    value: Int,
    accent: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White.copy(alpha = 0.92f))
            .padding(horizontal = 10.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(icon, fontSize = 20.sp)
        Text(label, fontSize = 12.sp, color = Muted, fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(5.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(50))
                .background(accent.copy(alpha = 0.25f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(value / 100f)
                    .height(8.dp)
                    .clip(RoundedCornerShape(50))
                    .background(accent)
            )
        }

        Spacer(Modifier.height(3.dp))

        // состояние дублируется словом: цвет не единственный носитель смысла (ТЗ 3.6)
        Text(levelText(value), fontSize = 11.sp, color = Ink, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun BigActionButton(text: String, color: Color, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(color)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text, fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun RoundButton(icon: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(46.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.92f))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(icon, fontSize = 20.sp)
    }
}


@Composable
private fun NavItem(
    icon: Int,
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val iconSize = if(selected) 42.dp else 36.dp
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (selected) PrimarySoft
                else Color.Transparent,
                RoundedCornerShape(18.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(icon),
            contentDescription = title,
            modifier = Modifier.size(iconSize)

        )
        if(selected){
        Box(
            modifier = Modifier
                .padding(top = 2.dp)
                .size(width = 22.dp, height = 4.dp)
                .clip(RoundedCornerShape(50))
                .background(Primary)
        )
    }

        Text(
            title,
            fontSize = 12.sp,
            color = if(selected) Primary else Muted,
            fontWeight = FontWeight.Bold
        )
    }
}

// ---------------------------------------------------------------------------
// Вспомогательные функции
// ---------------------------------------------------------------------------

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
    else -> "Мало"
}