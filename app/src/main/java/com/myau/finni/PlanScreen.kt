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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.roundToInt
import kotlin.math.cos
import kotlin.math.sin

/**
 * План на день — ТЗ 2.5.5.
 * Ребёнок раскладывает монеты по трём коробкам: нужно / хочется / в копилку.
 * Сумма не может быть больше, чем есть в кошельке.
 */

// --- цвета коробок ---
private val BoxRed = Color(0xFFE85A5A)
private val BoxBlue = Color(0xFF4A8BEA)
private val BoxYellow = Color(0xFFF2B51F)
private val Paper = CardCream       // кремовые плашки как на референсе
private val PaperYellow = Color(0xFFFFF1C4)

@Composable
fun PlanScreen(vm: GameViewModel, onBack: () -> Unit) {
    val s = vm.state
    val available = s.coins

    var need by remember { mutableIntStateOf(s.planNeed) }
    var want by remember { mutableIntStateOf(s.planWant) }
    var save by remember { mutableIntStateOf(s.planSave) }

    val left = available - (need + want + save)
    val fits = left >= 0

    Box(modifier = Modifier.fillMaxSize()) {

        // --- фон ---
        Image(
            painter = painterResource(R.drawable.plan_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(14.dp))

            // --- шапка: общая для всех экранов ---
            ScreenHeader("План на день ${s.day}", onBack = onBack)

            Spacer(Modifier.height(12.dp))

            // --- сколько можно распределить ---
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.82f)
                    .shadow(6.dp, RoundedCornerShape(26.dp))
                    .clip(RoundedCornerShape(26.dp))
                    .background(PaperYellow)
                    .border(2.dp, BoxYellow.copy(alpha = 0.45f), RoundedCornerShape(26.dp))
                    .padding(vertical = 12.dp, horizontal = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Можно распределить", fontSize = 16.sp, color = Muted, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("$available", fontSize = 44.sp, color = Ink, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(10.dp))
                    Coin(42.dp)
                }
                CoinText(
                    if (fits) "Свободно ещё $left 🪙"
                    else "Не хватает ${-left} 🪙 — уменьши одну коробку",
                    fontSize = 15.sp,
                    color = if (fits) Muted else Danger,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(14.dp))

            // --- объяснение задания ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(22.dp))
                    .clip(RoundedCornerShape(22.dp))
                    .background(Paper)
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Разложи деньги по трём коробкам",
                    fontSize = 19.sp,
                    color = Ink,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Распредели монеты между тем, что тебе нужно, хочется и что пойдёт в копилку.",
                    fontSize = 14.sp,
                    color = Ink.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(16.dp))

            // --- три коробки ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MoneyBox(
                    title = "Нужно",
                    iconName = "need_icon",
                    fallback = "🍲",
                    value = need,
                    max = available,
                    color = BoxRed,
                    hue = -50f,
                    brightness = 0.82f,
                    modifier = Modifier.weight(1f)
                ) { need = it }

                MoneyBox(
                    title = "Хочется",
                    iconName = "want_icon",
                    fallback = "🎈",
                    value = want,
                    max = available,
                    color = BoxBlue,
                    hue = 175f,
                    brightness = 0.78f,
                    modifier = Modifier.weight(1f)
                ) { want = it }

                MoneyBox(
                    title = "В копилку",
                    iconName = "icon_savings",
                    fallback = "🐷",
                    value = save,
                    max = available,
                    color = BoxYellow,
                    hue = 0f,             // коробка и так жёлтая
                    brightness = 1f,
                    modifier = Modifier.weight(1f)
                ) { save = it }
            }

            Spacer(Modifier.height(14.dp))

            // --- подсказка от Финни: стикер сверху, текст под ним ---
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.finni_plan),
                    contentDescription = "Финни думает о еде и уходе",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth(0.78f)
                        .aspectRatio(1.676f)
                )

                Text(
                    "Нужно — это еда и уход, без них Финни грустит. " +
                            "Хочется можно перенести на завтра, это не ошибка.",
                    fontSize = 14.sp,
                    color = Ink,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(22.dp))
                        .clip(RoundedCornerShape(22.dp))
                        .background(Paper)
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            // --- подтвердить ---
            ConfirmButton(
                text = if (s.planConfirmed) "Сохранить изменения" else "Подтвердить план",
                enabled = fits
            ) {
                vm.confirmPlan(need, want, save)
                onBack()
            }

            // --- план и факт (когда план уже есть) ---
            if (s.planConfirmed) {
                Spacer(Modifier.height(14.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(22.dp))
                        .background(Paper)
                        .padding(14.dp)
                ) {
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

            BottomBarSpacer()
        }
    }
}

// ---------------------------------------------------------------------------
// Коробка: пузырь с суммой, коробка, иконка, подпись, ползунок
// ---------------------------------------------------------------------------

@Composable
private fun MoneyBox(
    title: String,
    iconName: String,
    fallback: String,
    value: Int,
    max: Int,
    color: Color,
    hue: Float,
    brightness: Float,
    modifier: Modifier = Modifier,
    onChange: (Int) -> Unit
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {

        // пузырь с суммой и «хвостиком» вниз
        Box(contentAlignment = Alignment.BottomCenter) {
            Box(
                modifier = Modifier
                    .offset(y = 5.dp)
                    .size(12.dp)
                    .rotate(45f)
                    .background(Color.White)
            )
            Row(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color.White)
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("$value", fontSize = 20.sp, color = Ink, fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(5.dp))
                Coin(22.dp)
            }
        }

        Spacer(Modifier.height(14.dp))

        // коробка с тенью, иконкой и подписью.
        // BOX_SCALE — насколько коробка больше своей колонки (растёт вверх от низа)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.19f)
                .graphicsLayer {
                    scaleX = BOX_SCALE
                    scaleY = BOX_SCALE
                    transformOrigin = TransformOrigin(0.5f, 1f)
                }
        ) {
            // мягкая овальная тень под дном
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 7.dp)
                    .fillMaxWidth(0.95f)
                    .height(18.dp)
                    .drawBehind {
                        scale(scaleX = size.width / size.height, scaleY = 1f) {
                            drawCircle(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.35f),
                                        Color.Transparent
                                    ),
                                    center = center,
                                    radius = size.height / 2
                                ),
                                radius = size.height / 2
                            )
                        }
                    }
            )

            Image(
                painter = painterResource(R.drawable.box),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                colorFilter = if (hue == 0f && brightness == 1f) null
                else ColorFilter.colorMatrix(hueMatrix(hue, brightness)),
                modifier = Modifier.fillMaxSize()
            )

            Box(modifier = Modifier.align(BiasAlignment(0f, 0.15f))) {
                PlanDrawable(iconName, fallback, 42.dp)
            }

            Text(
                title,
                fontSize = 13.sp,
                color = Ink,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                modifier = Modifier
                    .align(BiasAlignment(0f, 0.8f))
                    .clip(RoundedCornerShape(10.dp))
                    .background(Paper)
                    .border(1.5.dp, color.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            )
        }

        Spacer(Modifier.height(18.dp))

        BoxSlider(value, max, color, onChange)
    }
}

private const val BOX_SCALE = 1.05f

// ---------------------------------------------------------------------------
// Свой ползунок: белая капсула, цветная заливка, крупный кругляш.
// Можно тянуть пальцем или просто ткнуть в нужное место. Шаг 5 монет.
// ---------------------------------------------------------------------------

@Composable
private fun BoxSlider(value: Int, max: Int, color: Color, onChange: (Int) -> Unit) {
    val step = 5
    val latestOnChange by rememberUpdatedState(onChange)
    val fraction = if (max <= 0) 0f else (value.toFloat() / max).coerceIn(0f, 1f)

    fun snap(x: Float, width: Int): Int {
        if (max <= 0 || width <= 0) return 0
        val f = (x / width).coerceIn(0f, 1f)
        return ((f * max / step).roundToInt() * step).coerceIn(0, max)
    }

    val thumb = 28.dp
    val pad = 3.dp

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(thumb + pad * 2)
            .shadow(3.dp, RoundedCornerShape(50))
            .clip(RoundedCornerShape(50))
            .background(Color.White)
            .border(2.dp, color.copy(alpha = 0.55f), RoundedCornerShape(50))
            .pointerInput(max) {
                detectTapGestures { pos -> latestOnChange(snap(pos.x, size.width)) }
            }
            .pointerInput(max) {
                detectHorizontalDragGestures { change, _ ->
                    change.consume()
                    latestOnChange(snap(change.position.x, size.width))
                }
            }
    ) {
        val travel = maxWidth - thumb - pad * 2

        // цветная заливка до кругляша
        Box(
            modifier = Modifier
                .padding(pad)
                .width(thumb + travel * fraction)
                .fillMaxHeight()
                .clip(RoundedCornerShape(50))
                .background(color.copy(alpha = 0.35f))
        )

        // кругляш
        Box(
            modifier = Modifier
                .padding(pad)
                .offset(x = travel * fraction)
                .size(thumb)
                .shadow(2.dp, CircleShape)
                .clip(CircleShape)
                .background(color)
                .border(3.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Мелкие элементы
// ---------------------------------------------------------------------------

@Composable
private fun BackPill(modifier: Modifier = Modifier, onClick: () -> Unit) {
    BackArrowButton(modifier, onClick)   // общая кнопка из NavButtons.kt
}

@Composable
private fun ConfirmButton(text: String, enabled: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.88f)
            .height(62.dp)
            .shadow(if (enabled) 8.dp else 0.dp, RoundedCornerShape(31.dp))
            .clip(RoundedCornerShape(31.dp))
            .background(
                if (enabled) Brush.verticalGradient(listOf(Color(0xFF9A7BFF), Primary))
                else Brush.verticalGradient(listOf(Muted.copy(alpha = 0.5f), Muted.copy(alpha = 0.6f)))
            )
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text, fontSize = 20.sp, color = Color.White, fontWeight = FontWeight.Bold)
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

/** Картинка из drawable по имени; если файла нет — эмодзи. */
@Composable
private fun PlanDrawable(name: String, fallback: String, size: Dp) {
    val context = LocalContext.current
    val id = remember(name) {
        context.resources.getIdentifier(name, "drawable", context.packageName)
    }
    if (id != 0) {
        Image(
            painter = painterResource(id),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(size)
        )
    } else {
        Text(fallback, fontSize = (size.value * 0.7f).sp)
    }
}

/**
 * Перекрашивание картинки: поворот оттенка + яркость.
 * Как CSS filter: hue-rotate(Xdeg) brightness(Y).
 * Блики и обводка сохраняются, меняется только цвет.
 */
private fun hueMatrix(degrees: Float, brightness: Float): ColorMatrix {
    val a = Math.toRadians(degrees.toDouble())
    val c = cos(a).toFloat()
    val s = sin(a).toFloat()
    val b = brightness
    return ColorMatrix(
        floatArrayOf(
            (0.213f + c * 0.787f - s * 0.213f) * b, (0.715f - c * 0.715f - s * 0.715f) * b, (0.072f - c * 0.072f + s * 0.928f) * b, 0f, 0f,
            (0.213f - c * 0.213f + s * 0.143f) * b, (0.715f + c * 0.285f + s * 0.140f) * b, (0.072f - c * 0.072f - s * 0.283f) * b, 0f, 0f,
            (0.213f - c * 0.213f - s * 0.787f) * b, (0.715f - c * 0.715f + s * 0.715f) * b, (0.072f + c * 0.928f + s * 0.072f) * b, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
    )
}