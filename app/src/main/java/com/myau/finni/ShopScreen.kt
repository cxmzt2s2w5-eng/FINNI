package com.myau.finni

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
    Product("borsch", "Борщ", "🍲", 30, true, "Сытость: Финни станет сытым", "🍽"),
    Product("rice", "Рис с брокколи", "🍚", 25, true, "Сытость: полезный обед", "🍽"),
    Product("water", "Вода", "💧", 10, true, "Финни не будет хотеть пить", "🍽"),
    Product("care", "Уход на день", "🧼", 20, true, "Уход: Финни станет ухоженным", "🧼"),

    // необязательные расходы
    Product("chips", "Чипсы", "🍟", 15, false, "Вкусно, но почти не насыщает", "😋"),
    Product("bun", "Булочка", "🥐", 15, false, "Маленькая радость к чаю", "😋"),
    Product("juice", "Сок", "🧃", 20, false, "Любимый напиток Финни", "😋"),
    Product("ball", "Мяч", "⚽", 20, false, "Настроение: можно играть", "😊"),
    Product("flower", "Цветок", "🌼", 40, false, "Украшение комнаты", "🌈"),
    Product("curtains", "Шторы", "🪟", 40, false, "В комнате станет уютнее", "🌈"),
    Product("chair", "Кресло", "🪑", 60, false, "Есть где отдохнуть", "😊"),
    Product("tv", "Телевизор", "📺", 80, false, "Можно смотреть мультики", "😊"),
    Product("console", "Приставка", "🎮", 100, false, "Игры на телевизоре", "😊", requires = "tv")
)

// ---------------------------------------------------------------------------
// Экран
// ---------------------------------------------------------------------------

@Composable
fun ShopScreen(vm: GameViewModel, onBack: () -> Unit) {
    val s = vm.state

    var showRequired by remember { mutableStateOf(true) }
    var selected by remember { mutableStateOf<Product?>(null) }

    val visible = products.filter { it.required == showRequired }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Bg)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(16.dp))
        TopBar("Магазин") { onBack() }

        Text("Баланс ${s.coins} 🪙", fontSize = 18.sp, color = Ink, fontWeight = FontWeight.Bold)

        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TabButton("Нужно", showRequired, Modifier.weight(1f)) { showRequired = true }
            TabButton("Хочется", !showRequired, Modifier.weight(1f)) { showRequired = false }
        }

        FinniCard(bg = YellowSoft) {
            Text("Перед покупкой", fontSize = 17.sp, color = Ink, fontWeight = FontWeight.Bold)
            Text(
                "Ты увидишь цену, тип расхода, влияние на Финни и остаток денег.",
                fontSize = 16.sp,
                color = Muted
            )
        }

        visible.chunked(2).forEach { pair ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                pair.forEach { product ->
                    ProductCard(
                        product = product,
                        locked = product.requires != null && !s.purchased.contains(product.requires),
                        bought = s.purchased.contains(product.id),
                        modifier = Modifier.weight(1f)
                    ) { selected = product }
                }
                if (pair.size == 1) Spacer(Modifier.weight(1f))
            }
        }

        Spacer(Modifier.height(8.dp))
        FinniCard {
            Text("Покупки сегодня", fontSize = 17.sp, color = Ink, fontWeight = FontWeight.Bold)
            if (s.purchased.isEmpty()) {
                Text("Пока ничего не куплено", fontSize = 16.sp, color = Muted)
            } else {
                s.purchased.forEach { id ->
                    val p = products.firstOrNull { it.id == id }
                    if (p != null) {
                        Text("• ${p.title} — ${p.price} 🪙", fontSize = 16.sp, color = Muted)
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))
    }

    val product = selected
    if (product != null) {
        val enough = vm.canBuy(product.price)

        AlertDialog(
            onDismissRequest = { selected = null },
            containerColor = Surface,
            title = {
                Text(
                    if (enough) "Купить «${product.title}»?" else "Пока не получится",
                    fontSize = 21.sp,
                    color = Ink,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Tag(
                        text = if (product.required) "Обязательный расход" else "Необязательный расход",
                        bg = if (product.required) DangerSoft else PrimarySoft,
                        textColor = if (product.required) Danger else Primary
                    )

                    Spacer(Modifier.height(14.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Surface2, shape = RoundedCornerShape(16.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(product.effectIcon, fontSize = 26.sp)
                        Spacer(Modifier.width(10.dp))
                        Text(product.effect, fontSize = 16.sp, color = Ink, modifier = Modifier.weight(1f))
                    }

                    Spacer(Modifier.height(16.dp))

                    if (enough) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            MoneyBox("Сейчас", s.coins, Ink, Modifier.weight(1f))
                            Text(" − ", fontSize = 22.sp, color = Muted, fontWeight = FontWeight.Bold)
                            MoneyBox("Цена", product.price, Danger, Modifier.weight(1f))
                            Text(" = ", fontSize = 22.sp, color = Muted, fontWeight = FontWeight.Bold)
                            MoneyBox("Станет", s.coins - product.price, Mint, Modifier.weight(1f))
                        }
                    } else {
                        Text(
                            "Не хватает ${product.price - s.coins} монет.",
                            fontSize = 17.sp,
                            color = Ink,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))
                        Text("Что можно сделать:", fontSize = 16.sp, color = Muted)
                        Text("• выполнить задание и заработать монеты", fontSize = 16.sp, color = Muted)
                        Text("• выбрать товар подешевле", fontSize = 16.sp, color = Muted)
                        Text("• купить это завтра", fontSize = 16.sp, color = Muted)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (enough) vm.buy(product)
                    selected = null
                }) {
                    Text(
                        if (enough) "Купить" else "Понятно",
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

@Composable
private fun TabButton(text: String, active: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .clickable { onClick() }
            .background(
                color = if (active) Primary else Surface,
                shape = RoundedCornerShape(18.dp)
            )
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = if (active) Surface else Muted
        )
    }
}

@Composable
private fun ProductCard(
    product: Product,
    locked: Boolean,
    bought: Boolean,
    modifier: Modifier = Modifier,
    onBuy: () -> Unit
) {
    FinniCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // СЮДА ВСТАНЕТ КАРТИНКА ТОВАРА ОТ ДИЗАЙНЕРА
            Text(product.emoji, fontSize = 34.sp, modifier = Modifier.weight(1f))
            Tag(
                text = if (product.required) "Нужно" else "Хочется",
                bg = if (product.required) DangerSoft else PrimarySoft,
                textColor = if (product.required) Danger else Primary
            )
        }

        Spacer(Modifier.height(8.dp))
        Text(product.title, fontSize = 18.sp, color = Ink, fontWeight = FontWeight.Bold)
        Text(product.effect, fontSize = 15.sp, color = Muted)
        Spacer(Modifier.height(8.dp))
        Text("${product.price} 🪙", fontSize = 20.sp, color = Ink, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(10.dp))

        when {
            locked -> Text("Сначала нужен телевизор", fontSize = 15.sp, color = Muted, fontWeight = FontWeight.Bold)
            bought -> Text("✔ Уже куплено", fontSize = 15.sp, color = Mint, fontWeight = FontWeight.Bold)
            else -> FinniButton("Купить") { onBuy() }
        }
    }
}

@Composable
private fun Tag(text: String, bg: Color, textColor: Color) {
    Box(
        modifier = Modifier
            .background(color = bg, shape = RoundedCornerShape(14.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text, fontSize = 14.sp, color = textColor, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun MoneyBox(label: String, value: Int, valueColor: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(color = Surface2, shape = RoundedCornerShape(14.dp))
            .padding(vertical = 10.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(label, fontSize = 13.sp, color = Muted)
        Text("$value 🪙", fontSize = 17.sp, color = valueColor, fontWeight = FontWeight.Bold)
    }
}