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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ---------------------------------------------------------------------------
// Товары магазина (ТЗ 2.5.6, 2.6 — минимум 8 позиций двух типов)
// ---------------------------------------------------------------------------

data class Product(
    val id: String,
    val title: String,
    val emoji: String,
    val price: Int,
    val required: Boolean,
    val effect: String,
    val effectIcon: String = "🙂",
    val requires: String? = null
)

val products = listOf(
    // обязательные расходы
    Product("borsch", "Борщ", "🍲", 30, true, "Финни станет сытым", "🍽"),
    Product("rice", "Рис с брокколи", "🍚", 25, true, "Полезный обед", "🍽"),
    Product("water", "Вода", "💧", 10, true, "Не будет хотеть пить", "🍽"),
    Product("care", "Уход на день", "🧼", 20, true, "Финни станет ухоженным", "🧼"),

    // необязательные расходы
    Product("chips", "Чипсы", "🍟", 15, false, "Вкусно, но не насыщает", "😋"),
    Product("bun", "Булочка", "🥐", 15, false, "Маленькая радость", "😋"),
    Product("juice", "Сок", "🧃", 20, false, "Любимый напиток", "😋"),
    Product("ball", "Мяч", "⚽", 20, false, "Можно играть", "😊"),
    Product("flower", "Цветок", "🌼", 40, false, "Украшение комнаты", "🌈"),
    Product("curtains", "Шторы", "🪟", 40, false, "В комнате уютнее", "🌈"),
    Product("chair", "Кресло", "🪑", 60, false, "Есть где отдохнуть", "😊"),
    Product("tv", "Телевизор", "📺", 80, false, "Можно смотреть мультики", "😊"),
    Product("console", "Приставка", "🎮", 100, false, "Игры на телевизоре", "😊", requires = "tv")
)

// ---------------------------------------------------------------------------
// Экран магазина
// ---------------------------------------------------------------------------

@Composable
fun ShopScreen(vm: GameViewModel, onBack: () -> Unit) {
    val s = vm.state

    var showRequired by remember { mutableStateOf(true) }
    var selected by remember { mutableStateOf<Product?>(null) }

    val visible = products.filter { it.required == showRequired }
    val boughtToday = s.purchased.mapNotNull { id -> products.firstOrNull { it.id == id } }

    Box(modifier = Modifier.fillMaxSize()) {

        // --- фон лавки ---
        Image(
            painter = painterResource(R.drawable.shop_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // тёплая дымка только под шапкой, чтобы заголовок читался.
        // Цвет и прозрачность можно менять здесь.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFFE9C7).copy(alpha = 0.55f),
                            Color.Transparent
                        )
                    )
                )
        )

        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 14.dp)) {

            Spacer(Modifier.height(14.dp))

            // --- шапка: общая для всех экранов, справа баланс ---
            ScreenHeader("Магазин", onBack = onBack) { BalancePill(s.coins) }

            Spacer(Modifier.height(14.dp))

            // --- переключатель категорий ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(CardCream.copy(alpha = CardAlpha))
                    .padding(4.dp)
            ) {
                SegmentTab("need_icon", "Нужно", showRequired, Modifier.weight(1f)) {
                    showRequired = true
                }
                SegmentTab("want_icon", "Хочется", !showRequired, Modifier.weight(1f)) {
                    showRequired = false
                }
            }

            Spacer(Modifier.height(14.dp))

            // --- витрина: прокручивается только она ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                visible.chunked(3).forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        row.forEach { product ->
                            ProductTile(
                                product = product,
                                enough = vm.canBuy(product.price),
                                locked = product.requires != null &&
                                        !s.purchased.contains(product.requires),
                                bought = s.purchased.contains(product.id),
                                modifier = Modifier.weight(1f)
                            ) { selected = product }
                        }
                        repeat(3 - row.size) { Spacer(Modifier.weight(1f)) }
                    }
                }

                Spacer(Modifier.height(24.dp))
            }

            // --- корзина: всегда на месте, не прокручивается ---
            BasketView(boughtToday)

            BottomBarSpacer()
        }
    }

    // ---------------------------------------------------------------------
    // Подтверждение покупки (ТЗ 2.5.6)
    // ---------------------------------------------------------------------
    val product = selected
    if (product != null) {
        val enough = vm.canBuy(product.price)

        AlertDialog(
            onDismissRequest = { selected = null },
            containerColor = Surface,
            shape = RoundedCornerShape(28.dp),
            title = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(104.dp)
                            .clip(CircleShape)
                            .background(if (product.required) DangerSoft else PrimarySoft),
                        contentAlignment = Alignment.Center
                    ) {
                        ProductImage(product, 72.dp)
                    }
                    Spacer(Modifier.height(10.dp))
                    Text(
                        if (enough) product.title else "Пока не получится",
                        fontSize = 21.sp,
                        color = Ink,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Tag(
                        text = if (product.required) "Обязательный расход" else "Необязательный расход",
                        bg = if (product.required) DangerSoft else PrimarySoft,
                        textColor = if (product.required) Danger else Primary
                    )

                    Spacer(Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Surface2)
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(product.effectIcon, fontSize = 24.sp)
                        Spacer(Modifier.width(10.dp))
                        Text(
                            product.effect,
                            fontSize = 15.sp,
                            color = Ink,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(Modifier.height(14.dp))

                    if (enough) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            MoneyBox("Сейчас", s.coins, Ink, Modifier.weight(1f))
                            Text(" − ", fontSize = 20.sp, color = Muted, fontWeight = FontWeight.Bold)
                            MoneyBox("Цена", product.price, Danger, Modifier.weight(1f))
                            Text(" = ", fontSize = 20.sp, color = Muted, fontWeight = FontWeight.Bold)
                            MoneyBox("Станет", s.coins - product.price, Mint, Modifier.weight(1f))
                        }
                    } else {
                        Text(
                            "Не хватает ${product.price - s.coins} монет",
                            fontSize = 17.sp,
                            color = Danger,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(10.dp))
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                "Что можно сделать:",
                                fontSize = 15.sp,
                                color = Ink,
                                fontWeight = FontWeight.Bold
                            )
                            Text("🧩  выполнить задание и заработать", fontSize = 15.sp, color = Muted)
                            Text("🏷  выбрать товар подешевле", fontSize = 15.sp, color = Muted)
                            Text("🌙  купить это завтра", fontSize = 15.sp, color = Muted)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (enough) vm.buy(product)
                    selected = null
                }) {
                    CoinText(
                        if (enough) "Купить за ${product.price} 🪙" else "Понятно",
                        fontSize = 17.sp,
                        color = Primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                if (enough) {
                    TextButton(onClick = { selected = null }) {
                        Text("Отмена", fontSize = 17.sp, color = Muted)
                    }
                }
            }
        )
    }
}

// ---------------------------------------------------------------------------
// Плитка товара
// ---------------------------------------------------------------------------

@Composable
private fun ProductTile(
    product: Product,
    enough: Boolean,
    locked: Boolean,
    bought: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier.clickable(enabled = !locked) { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(26.dp))
                .background(CardCream.copy(alpha = CardAlpha))
                .border(
                    2.dp,
                    if (product.required) Danger.copy(alpha = 0.35f) else Primary.copy(alpha = 0.35f),
                    RoundedCornerShape(26.dp)
                ),
            contentAlignment = Alignment.Center
        ) {

            ProductImage(product, 72.dp)

            if (bought || locked) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White.copy(alpha = 0.60f)),
                    contentAlignment = Alignment.Center
                ) {
                    if (bought) {
                        Text("✔", fontSize = 36.sp, color = Mint, fontWeight = FontWeight.Bold)
                    } else {
                        DrawableOrText("lock_icon", "🔒", 46.dp)
                    }
                }
            }
        }

        Spacer(Modifier.height(6.dp))

        // название и цена в одной белой плашке — читается на любом фоне
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(CardCream.copy(alpha = CardAlpha))
                .padding(horizontal = 6.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                product.title,
                fontSize = 13.sp,
                color = Ink,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 2
            )

            Spacer(Modifier.height(2.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "${product.price}",
                    fontSize = 16.sp,
                    color = when {
                        bought -> Mint
                        enough -> Ink
                        else -> Danger
                    },
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.width(3.dp))
                Coin(16.dp)
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Корзина покупок дня
// ---------------------------------------------------------------------------

@Composable
private fun BasketView(items: List<Product>) {
    val context = LocalContext.current
    val basketId = context.resources.getIdentifier("basket", "drawable", context.packageName)

    val shown = items.takeLast(5)
    val hidden = items.size - shown.size

    Box(
        modifier = Modifier.fillMaxWidth().height(240.dp),
        contentAlignment = Alignment.BottomCenter
    ) {

        // 1) сначала сама корзина
        if (basketId != 0) {
            Image(
                painter = painterResource(basketId),
                contentDescription = "Корзина покупок",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(190.dp)
            )
        } else {
            Text("🧺", fontSize = 130.sp, modifier = Modifier.align(Alignment.BottomCenter))
        }

        // 2) купленные товары ПОВЕРХ корзины, нижним краем на ободке.
        // Если стоят слишком высоко/низко — меняй bottom (сейчас 105.dp)
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 105.dp),
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            shown.forEachIndexed { i, product ->
                Box(modifier = Modifier.rotate(if (i % 2 == 0) -10f else 10f)) {
                    ProductImage(product, 50.dp)
                }
            }
        }

        // 3) счётчик над корзиной
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .clip(RoundedCornerShape(50))
                .background(Color.White.copy(alpha = 0.92f))
                .padding(horizontal = 16.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CoinText(
                if (items.isEmpty()) "Корзина пуста"
                else "Куплено ${items.size} · ${items.sumOf { it.price }} 🪙" +
                        (if (hidden > 0) "  (+$hidden)" else ""),
                fontSize = 15.sp,
                color = Ink,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Работа с картинками
// ---------------------------------------------------------------------------

/**
 * Картинка товара из res/drawable по имени item_<id>.
 * Пока файла нет — показывается эмодзи, приложение работает.
 */
@Composable
private fun ProductImage(product: Product, size: Dp) {
    DrawableOrText("item_${product.id}", product.emoji, size)
}

/** Рисунок по имени файла, если он есть в drawable; иначе запасной символ. */
@Composable
private fun DrawableOrText(name: String, fallback: String, size: Dp) {
    val context = LocalContext.current
    val imageId = context.resources.getIdentifier(name, "drawable", context.packageName)

    if (imageId != 0) {
        Image(
            painter = painterResource(imageId),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(size)
        )
    } else {
        Text(fallback, fontSize = (size.value * 0.62f).sp)
    }
}

// ---------------------------------------------------------------------------
// Мелкие элементы
// ---------------------------------------------------------------------------

@Composable
private fun RoundBack(onClick: () -> Unit) {
    BackArrowButton(onClick = onClick)   // общая кнопка из NavButtons.kt
}

@Composable
private fun BalancePill(coins: Int) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color.White.copy(alpha = 0.90f))
            .padding(horizontal = 14.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Coin(22.dp)
        Spacer(Modifier.width(6.dp))
        Text("$coins", fontSize = 19.sp, color = Ink, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun SegmentTab(
    iconName: String,
    text: String,
    active: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(if (active) Primary else Color.Transparent)
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        DrawableOrText(iconName, if (active) "•" else "•", 26.dp)
        Spacer(Modifier.width(8.dp))
        Text(
            text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = if (active) Color.White else Muted
        )
    }
}

@Composable
private fun Tag(text: String, bg: Color, textColor: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(bg)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text, fontSize = 13.sp, color = textColor, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun MoneyBox(
    label: String,
    value: Int,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Surface2)
            .padding(vertical = 10.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(label, fontSize = 12.sp, color = Muted)
        Text("$value", fontSize = 17.sp, color = valueColor, fontWeight = FontWeight.Bold)
    }
}