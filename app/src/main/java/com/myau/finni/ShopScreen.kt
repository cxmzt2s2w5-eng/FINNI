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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ---------- данные ----------

data class Product(
    val id: String,
    val title: String,
    val emoji: String,
    val price: Int,
    val required: Boolean,        // true = Нужно, false = Хочется
    val effect: String,           // влияние на питомца
    val requires: String? = null  // id товара, который надо купить раньше
)

val products = listOf(
    // --- обязательные расходы ---
    Product("borsch", "Борщ", "🍲", 30, true, "Сытость: Финни станет сытым"),
    Product("rice", "Рис с брокколи", "🍚", 25, true, "Сытость: полезный обед"),
    Product("water", "Вода", "💧", 10, true, "Сытость: Финни не будет хотеть пить"),
    Product("care", "Уход на день", "🧼", 20, true, "Уход: Финни станет ухоженным"),

    // --- необязательные расходы ---
    Product("chips", "Чипсы", "🍟", 15, false, "Настроение: вкусно, но не насыщает"),
    Product("bun", "Булочка", "🥐", 15, false, "Настроение: маленькая радость"),
    Product("juice", "Сок", "🧃", 20, false, "Настроение: любимый напиток"),
    Product("ball", "Мяч", "⚽", 20, false, "Настроение: Финни сможет играть"),
    Product("flower", "Цветок", "🌼", 40, false, "Украшение комнаты"),
    Product("curtains", "Шторы", "🪟", 40, false, "Украшение: в комнате уютнее"),
    Product("chair", "Кресло", "🪑", 60, false, "Настроение: есть где отдохнуть"),
    Product("tv", "Телевизор", "📺", 80, false, "Настроение: можно смотреть мультики"),
    Product("console", "Приставка", "🎮", 100, false, "Настроение: игры на телевизоре", requires = "tv")
)

// ---------- экран ----------

@Composable
fun ShopScreen() {
    var coins by remember { mutableStateOf(100) }
    var showRequired by remember { mutableStateOf(true) }
    var selected by remember { mutableStateOf<Product?>(null) }
    val purchased = remember { mutableStateListOf<String>() }

    val visible = products.filter { it.required == showRequired }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Bg)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(16.dp))
        Text("Баланс $coins 🪙", fontSize = 16.sp, color = Muted, fontWeight = FontWeight.Bold)
        Text("Магазин", fontSize = 28.sp, color = Ink, fontWeight = FontWeight.Bold)

        // --- переключатель категорий ---
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TabButton("Нужно", showRequired, Modifier.weight(1f)) { showRequired = true }
            TabButton("Хочется", !showRequired, Modifier.weight(1f)) { showRequired = false }
        }

        // --- подсказка ---
        FinniCard(bg = YellowSoft) {
            Text("Перед покупкой", fontSize = 17.sp, color = Ink, fontWeight = FontWeight.Bold)
            Text(
                "Ты увидишь цену, тип расхода, влияние на Финни и остаток денег.",
                fontSize = 16.sp,
                color = Muted
            )
        }

        // --- сетка товаров по два в ряд ---
        visible.chunked(2).forEach { pair ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                pair.forEach { product ->
                    ProductCard(
                        product = product,
                        coins = coins,
                        locked = product.requires != null && !purchased.contains(product.requires),
                        bought = purchased.contains(product.id),
                        modifier = Modifier.weight(1f)
                    ) { selected = product }
                }
                if (pair.size == 1) Spacer(Modifier.weight(1f))
            }
        }

        // --- история покупок периода ---
        Spacer(Modifier.height(8.dp))
        FinniCard {
            Text("Покупки сегодня", fontSize = 17.sp, color = Ink, fontWeight = FontWeight.Bold)
            if (purchased.isEmpty()) {
                Text("Пока ничего не куплено", fontSize = 16.sp, color = Muted)
            } else {
                purchased.forEach { id ->
                    val p = products.first { it.id == id }
                    Text("• ${p.title} — ${p.price} 🪙", fontSize = 16.sp, color = Muted)
                }
            }
        }
        Spacer(Modifier.height(24.dp))
    }

    // --- окно подтверждения покупки ---
    val product = selected
    if (product != null) {
        val enough = coins >= product.price
        AlertDialog(
            onDismissRequest = { selected = null },
            title = {
                Text(
                    if (enough) "Купить «${product.title}»?" else "Пока не получится",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        if (product.required) "Тип: обязательный расход"
                        else "Тип: необязательный расход",
                        fontSize = 16.sp, color = Muted
                    )
                    Text(product.effect, fontSize = 16.sp, color = Muted)
                    Spacer(Modifier.height(8.dp))
                    if (enough) {
                        Text("Цена: ${product.price} 🪙", fontSize = 17.sp, color = Ink)
                        Text("Останется: ${coins - product.price} 🪙", fontSize = 17.sp, color = Ink)
                    } else {
                        Text(
                            "Не хватает ${product.price - coins} монет. " +
                                    "Можно выполнить задание, выбрать товар подешевле " +
                                    "или купить это завтра.",
                            fontSize = 16.sp, color = Ink
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (enough) {
                        coins -= product.price
                        purchased.add(product.id)
                    }
                    selected = null
                }) {
                    Text(if (enough) "Купить" else "Понятно", fontSize = 17.sp, color = Primary)
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

// ---------- помощники ----------

@Composable
private fun TabButton(
    text: String,
    active: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
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
    coins: Int,
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
            // ЗДЕСЬ БУДЕТ КАРТИНКА ОТ ДИЗАЙНЕРА
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
            locked -> Text(
                "Сначала нужен телевизор",
                fontSize = 15.sp, color = Muted, fontWeight = FontWeight.Bold
            )
            bought -> Text(
                "✔ Уже куплено",
                fontSize = 15.sp, color = Mint, fontWeight = FontWeight.Bold
            )
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