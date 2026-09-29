package com.myau.finni

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.ceil

/**
 * Накопления и финансовая цель — ТЗ 2.5.7.
 * Снятие возможно только после отдельного подтверждения,
 * в котором заранее показано, как уменьшится сумма и как изменится срок.
 */

// --- цвета экрана ---
private val SavPaper = CardCream
private val SavGold = Color(0xFFF2B51F)
private val SavGoldSoft = Color(0xFFFFF1C4)
private val SavPink = Color(0xFFF49A9A)
private val SavGreen = Color(0xFF3FBF8A)

@Composable
fun SavingsScreen(vm: GameViewModel, onBack: () -> Unit) {
    val s = vm.state

    var amount by remember { mutableIntStateOf(10) }
    var withdrawAmount by remember { mutableIntStateOf(10) }
    var confirmWithdraw by remember { mutableStateOf(false) }

    val left = (s.goalPrice - s.savings).coerceAtLeast(0)
    val perDay = if (s.day > 1) s.savings / (s.day - 1) else s.savings
    val daysLeft = if (perDay > 0) ceil(left.toFloat() / perDay).toInt() else null
    val progress = if (s.goalPrice == 0) 0f
    else (s.savings.toFloat() / s.goalPrice).coerceIn(0f, 1f)

    Box(modifier = Modifier.fillMaxSize()) {

        // --- фон: банк ---
        Image(
            painter = painterResource(R.drawable.savings_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(14.dp))

            // --- шапка: общая для всех экранов ---
            ScreenHeader("Копилка", onBack = onBack)

            Spacer(Modifier.height(10.dp))

            // --- свинка и сколько накоплено ---
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
                // карточка начинается под свинкой, свинка «сидит» на ней
                Column(
                    modifier = Modifier
                        .padding(top = 120.dp)
                        .fillMaxWidth()
                        .shadow(6.dp, RoundedCornerShape(26.dp))
                        .clip(RoundedCornerShape(26.dp))
                        .background(SavGoldSoft)
                        .border(2.dp, SavGold.copy(alpha = 0.45f), RoundedCornerShape(26.dp))
                        .padding(top = 44.dp, bottom = 14.dp, start = 12.dp, end = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("В копилке", fontSize = 16.sp, color = Muted, fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("${s.savings}", fontSize = 44.sp, color = Ink, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.width(10.dp))
                        Coin(40.dp)
                    }
                    CoinText(
                        "В кошельке ${s.coins} 🪙",
                        fontSize = 15.sp,
                        color = Muted,
                        fontWeight = FontWeight.Bold
                    )
                }

                Image(
                    painter = painterResource(R.drawable.piggy),
                    contentDescription = "Копилка-свинка",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth(0.58f)
                        .aspectRatio(1.26f)
                )
            }

            Spacer(Modifier.height(14.dp))

            // --- мечта ---
            SavCard {
                if (s.goalTitle.isBlank()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                    AppIcon("icon_target", "🎯", 36.dp)
                    Spacer(Modifier.width(8.dp))
                    Text("Цель ещё не выбрана", fontSize = 18.sp, color = Ink, fontWeight = FontWeight.Bold)
                }
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Выбери мечту, чтобы копилка показывала прогресс",
                        fontSize = 15.sp,
                        color = Muted
                    )
                } else {
                    Text("Моя мечта", fontSize = 14.sp, color = Muted, fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppIcon(dreamIconName(s.goalTitle), "🎯", 44.dp)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            s.goalTitle,
                            fontSize = 20.sp,
                            color = Ink,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            "${s.savings} / ${s.goalPrice}",
                            fontSize = 16.sp,
                            color = Primary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(Modifier.height(10.dp))
                    GoldProgress(progress)
                    Spacer(Modifier.height(10.dp))

                    CoinText(
                        when {
                            left == 0 -> "🎉 Цель достигнута!"
                            daysLeft == null -> "Осталось $left 🪙. Срок появится после первого пополнения"
                            else -> "Осталось $left 🪙. Если откладывать по $perDay 🪙 в день — примерно $daysLeft дн."
                        },
                        fontSize = 14.sp,
                        color = Muted
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // --- отложить ---
            SavCard(accent = SavGreen) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AppIcon("icon_savings", "🐷", 36.dp)
                    Spacer(Modifier.width(8.dp))
                    Text("Отложить в копилку", fontSize = 18.sp, color = Ink, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(10.dp))
                SavCounter(amount, 5, 5, s.coins.coerceAtLeast(5), SavGreen) { amount = it }
                Spacer(Modifier.height(12.dp))
                SavButton(
                    text = "Отложить $amount 🪙",
                    colors = listOf(Color(0xFF6FE0B0), SavGreen),
                    enabled = s.coins >= amount
                ) { vm.addToSavings(amount) }
                if (s.coins < amount) {
                    Spacer(Modifier.height(6.dp))
                    CoinText(
                        "В кошельке только ${s.coins} 🪙",
                        fontSize = 14.sp,
                        color = Danger,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // --- взять ---
            SavCard(accent = SavPink) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AppIcon("icon_budget", "💰", 36.dp)
                    Spacer(Modifier.width(8.dp))
                    Text("Взять из копилки", fontSize = 18.sp, color = Ink, fontWeight = FontWeight.Bold)
                }
                Text(
                    "Снятие отодвигает мечту. Перед подтверждением покажем, насколько.",
                    fontSize = 14.sp,
                    color = Muted
                )
                Spacer(Modifier.height(10.dp))
                SavCounter(withdrawAmount, 5, 5, s.savings.coerceAtLeast(5), SavPink) { withdrawAmount = it }
                Spacer(Modifier.height(12.dp))
                SavButton(
                    text = "Взять $withdrawAmount 🪙",
                    colors = listOf(Color(0xFFFFB8B8), Color(0xFFE77C7C)),
                    enabled = s.savings >= withdrawAmount
                ) { confirmWithdraw = true }
            }

            BottomBarSpacer()
        }
    }

    // ---------------------------------------------------------------------
    // Подтверждение снятия
    // ---------------------------------------------------------------------
    if (confirmWithdraw) {
        val after = (s.savings - withdrawAmount).coerceAtLeast(0)
        val leftAfter = (s.goalPrice - after).coerceAtLeast(0)
        val daysAfter = if (perDay > 0) ceil(leftAfter.toFloat() / perDay).toInt() else null

        AlertDialog(
            onDismissRequest = { confirmWithdraw = false },
            containerColor = SavPaper,
            shape = RoundedCornerShape(28.dp),
            title = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.piggy),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.height(90.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    CoinText(
                        "Взять $withdrawAmount 🪙 из копилки?",
                        fontSize = 20.sp,
                        color = Ink,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CoinText(
                        "В копилке: ${s.savings} → $after 🪙",
                        fontSize = 17.sp,
                        color = Ink,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(6.dp))
                    if (daysLeft != null && daysAfter != null) {
                        Text(
                            "До мечты: $daysLeft → $daysAfter дн.",
                            fontSize = 17.sp,
                            color = Danger,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Text(
                            "Срок появится после регулярных пополнений",
                            fontSize = 15.sp,
                            color = Muted,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    vm.withdrawFromSavings(withdrawAmount)
                    confirmWithdraw = false
                }) {
                    Text("Взять", fontSize = 17.sp, color = Danger, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmWithdraw = false }) {
                    Text("Оставить", fontSize = 17.sp, color = Primary, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

// ---------------------------------------------------------------------------
// Элементы экрана
// ---------------------------------------------------------------------------

/** Кремовая карточка с цветной полоской-акцентом слева (если задан accent). */
@Composable
private fun SavCard(
    accent: Color? = null,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(22.dp))
            .clip(RoundedCornerShape(22.dp))
            .background(SavPaper)
            .drawBehind {
                // цветная полоска слева на всю высоту карточки
                if (accent != null) {
                    drawRect(color = accent, size = Size(6.dp.toPx(), size.height))
                }
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) { content() }
    }
}

/** Золотая полоска прогресса с монеткой на конце. */
@Composable
private fun GoldProgress(progress: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(22.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(14.dp)
                .clip(RoundedCornerShape(50))
                .background(SavGoldSoft)
                .border(1.5.dp, SavGold.copy(alpha = 0.5f), RoundedCornerShape(50))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .height(14.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Brush.horizontalGradient(listOf(Color(0xFFFFD75E), SavGold)))
            )
        }
    }
}

@Composable
private fun SavCounter(
    value: Int,
    step: Int,
    min: Int,
    max: Int,
    color: Color,
    onChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RoundStep("−", color) { onChange((value - step).coerceAtLeast(min)) }

        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .width(110.dp)
                .clip(RoundedCornerShape(50))
                .background(Color.White)
                .border(2.dp, color.copy(alpha = 0.4f), RoundedCornerShape(50))
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("$value", fontSize = 24.sp, color = Ink, fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(6.dp))
            Coin(26.dp)
        }

        RoundStep("+", color) { onChange((value + step).coerceAtMost(max)) }
    }
}

@Composable
private fun RoundStep(symbol: String, color: Color, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(52.dp)
            .shadow(3.dp, CircleShape)
            .clip(CircleShape)
            .background(color)
            .border(3.dp, Color.White, CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            symbol,
            fontSize = 26.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.offset(y = (-1).dp)
        )
    }
}

@Composable
private fun SavButton(
    text: String,
    colors: List<Color>,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(if (enabled) 6.dp else 0.dp, RoundedCornerShape(28.dp))
            .clip(RoundedCornerShape(28.dp))
            .background(
                if (enabled) Brush.verticalGradient(colors)
                else Brush.verticalGradient(listOf(Muted.copy(alpha = 0.45f), Muted.copy(alpha = 0.55f)))
            )
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        CoinText(text, fontSize = 19.sp, color = Color.White, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun SavBackPill(modifier: Modifier = Modifier, onClick: () -> Unit) {
    BackArrowButton(modifier, onClick)   // общая кнопка из NavButtons.kt
}