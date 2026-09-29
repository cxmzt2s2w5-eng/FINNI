package com.myau.finni

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
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

import com.myau.finni.tasktypes.*

// ---------------------------------------------------------------------------
// Цвета экрана
// ---------------------------------------------------------------------------

private val TPaper = CardCream
private val WoodLight = Color(0xFFE9BE85)
private val Wood = Color(0xFFC48B52)
private val WoodDark = Color(0xFF7A4E2A)
private val WoodText = Color(0xFF4A2C14)
private val Gold = Color(0xFFF2B51F)
private val GoldSoft = Color(0xFFFFF1C4)
private val DoneGreen = Color(0xFF3FBF8A)

/** Пастельные цвета карточек по кругу: фон и обводка */
private val cardColors = listOf(
    Color(0xFFE7F5E1) to Color(0xFF9CCB8A),  // зелёный
    Color(0xFFE3EEFF) to Color(0xFF8DB3EC),  // голубой
    Color(0xFFEFE7FF) to Color(0xFFB39DEB),  // фиолетовый
    Color(0xFFFFE7E2) to Color(0xFFEBA196),  // розовый
    Color(0xFFFFF3D6) to Color(0xFFE8C774)   // жёлтый
)

// ---------------------------------------------------------------------------
// Экран списка заданий
// ---------------------------------------------------------------------------

@Composable
fun TasksScreen(
    vm: GameViewModel,
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current

    var tasks by remember { mutableStateOf<List<Task>>(emptyList()) }
    var selectedTask by remember { mutableStateOf<Task?>(null) }
    var selectedSection by remember { mutableStateOf("all") }

    val completedTasks = vm.state.completedTasks.toSet()

    LaunchedEffect(Unit) {
        tasks = TaskParser.loadTasks(context)
    }

    // --- открыто конкретное задание ---
    val opened = selectedTask
    if (opened != null) {
        TaskDetailScreen(
            task = opened,
            completed = completedTasks.contains(opened.id),
            onComplete = { vm.completeTask(opened.id, opened.reward) },
            onBack = { selectedTask = null }
        )
        return
    }

    val filtered =
        if (selectedSection == "all") tasks
        else tasks.filter { it.section == selectedSection }

    val done = tasks.count { completedTasks.contains(it.id) }
    val earned = tasks.filter { completedTasks.contains(it.id) }.sumOf { it.reward }

    Box(modifier = Modifier.fillMaxSize()) {

        // --- фон: тропинка ---
        Image(
            painter = painterResource(R.drawable.tasks_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(modifier = Modifier.fillMaxSize()) {

            Spacer(Modifier.height(14.dp))

            // --- шапка: общая для всех экранов ---
            ScreenHeader("Задания", onBack = onBack, modifier = Modifier.padding(horizontal = 16.dp))

            Spacer(Modifier.height(14.dp))

            // --- прогресс ---
            ProgressCard(
                done = done,
                total = tasks.size,
                earned = earned,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(12.dp))

            // --- фильтры: в одну строку, листаются вбок ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip("Все", selectedSection == "all", icon = { GridIcon(selectedSection == "all") }) {
                    selectedSection = "all"
                }
                FilterChip("Бюджет", selectedSection == "budget", icon = { TIcon("icon_budget", "💰", 24.dp) }) {
                    selectedSection = "budget"
                }
                FilterChip("Сбережения", selectedSection == "savings", icon = { TIcon("icon_savings", "🐷", 24.dp) }) {
                    selectedSection = "savings"
                }
                FilterChip("Покупки", selectedSection == "shopping", icon = { TIcon("icon_cart", "🛒", 24.dp) }) {
                    selectedSection = "shopping"
                }
            }

            Spacer(Modifier.height(12.dp))

            // --- список заданий ---
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 130.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(filtered) { index, task ->
                    TaskCard(
                        task = task,
                        index = index,
                        completed = completedTasks.contains(task.id)
                    ) { selectedTask = task }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Карточка прогресса
// ---------------------------------------------------------------------------

@Composable
private fun ProgressCard(done: Int, total: Int, earned: Int, modifier: Modifier = Modifier) {
    val progress = if (total == 0) 0f else done.toFloat() / total

    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .background(TPaper)
            .border(2.dp, Wood.copy(alpha = 0.45f), RoundedCornerShape(24.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                "$done / $total выполнено",
                fontSize = 20.sp,
                color = Ink,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))

            // полоска прогресса
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(14.dp)
                    .clip(RoundedCornerShape(50))
                    .background(GoldSoft)
                    .border(1.5.dp, Gold.copy(alpha = 0.5f), RoundedCornerShape(50))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .height(14.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Brush.horizontalGradient(listOf(Color(0xFFFFD75E), Gold)))
                )
            }

            Spacer(Modifier.height(8.dp))

            // сколько монет заработано на заданиях
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(GoldSoft)
                    .padding(horizontal = 12.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Coin(20.dp)
                Spacer(Modifier.width(6.dp))
                Text("$earned заработано", fontSize = 15.sp, color = Ink, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(Modifier.width(10.dp))

        // «сундук» — горка монет
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "Выполняй\nи получай\nнаграды!",
                fontSize = 11.sp,
                color = WoodText,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 13.sp,
                modifier = Modifier
                    .rotate(-4f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFFFF4B0))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
            Spacer(Modifier.height(4.dp))
            TIcon("icon_budget", "💰", 70.dp)
        }
    }
}

// ---------------------------------------------------------------------------
// Карточка задания
// ---------------------------------------------------------------------------

@Composable
private fun TaskCard(
    task: Task,
    index: Int,
    completed: Boolean,
    onClick: () -> Unit
) {
    val (bg, line) = cardColors[index % cardColors.size]

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(bg)
            .border(2.dp, line, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // картинка задания: task_<id>, если нет — иконка раздела
        Box(
            modifier = Modifier.size(58.dp),
            contentAlignment = Alignment.Center
        ) {
            TaskImage(task, 54.dp)
        }

        Spacer(Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                task.title,
                fontSize = 16.sp,
                color = Ink,
                fontWeight = FontWeight.Bold,
                maxLines = 2
            )
            Text(
                sectionName(task.section),
                fontSize = 12.sp,
                color = Muted
            )
        }

        Spacer(Modifier.width(6.dp))

        // награда
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(Color.White.copy(alpha = 0.85f))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Coin(20.dp)
            Spacer(Modifier.width(4.dp))
            Text("+${task.reward}", fontSize = 15.sp, color = Ink, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.width(8.dp))

        // стрелка или галочка
        if (completed) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(DoneGreen)
                    .border(2.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("✓", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }
        } else {
            NextArrowCircle()   // общая стрелка из NavButtons.kt
        }
    }
}

// ---------------------------------------------------------------------------
// Детальное задание
// ---------------------------------------------------------------------------

@Composable
private fun TaskDetailScreen(
    task: Task,
    completed: Boolean,
    onComplete: () -> Unit,
    onBack: () -> Unit
) {
    var result by remember { mutableStateOf<TaskResult?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(R.drawable.tasks_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(modifier = Modifier.fillMaxSize()) {

            Spacer(Modifier.height(14.dp))

            // --- шапка: общая для всех экранов ---
            ScreenHeader(task.title, onBack = onBack, fontSize = 20.sp, modifier = Modifier.padding(horizontal = 16.dp))

            Spacer(Modifier.height(14.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {

                // --- условие ---
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(22.dp))
                        .clip(RoundedCornerShape(22.dp))
                        .background(TPaper)
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TaskImage(task, 48.dp)
                    Spacer(Modifier.width(12.dp))
                    Text(task.text, fontSize = 17.sp, color = Ink, modifier = Modifier.weight(1f))
                }

                Spacer(Modifier.height(16.dp))

                // --- само задание (компоненты из tasktypes) ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(22.dp))
                        .background(TPaper.copy(alpha = 0.93f))
                        .padding(12.dp)
                ) {
                    Column {
                        when (task.type) {
                            "distribution" -> DistributionTask(task) { result = it }
                            "amount" -> AmountTask(task) { result = it }
                            "choice" -> ChoiceTask(task) { result = it }
                            "shopping" -> ShoppingTask(task) { result = it }
                            "comparison" -> ComparisonTask(task) { result = it }
                            "planning" -> PlanningTask(task) { result = it }
                        }
                    }
                }

                // --- результат ---
                result?.let { res ->
                    Spacer(Modifier.height(16.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(4.dp, RoundedCornerShape(22.dp))
                            .clip(RoundedCornerShape(22.dp))
                            .background(if (res.completed) Color(0xFFE3F7EC) else TPaper)
                            .border(
                                2.dp,
                                if (res.completed) DoneGreen.copy(alpha = 0.6f) else Wood.copy(alpha = 0.4f),
                                RoundedCornerShape(22.dp)
                            )
                            .padding(16.dp)
                    ) {
                        Text(
                            if (res.completed) "🎉 Отлично!" else "💡 Посмотри, что получилось",
                            fontSize = 20.sp,
                            color = Ink,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(8.dp))

                        res.messages.forEach { message ->
                            Text(
                                "• $message",
                                fontSize = 16.sp,
                                color = Ink,
                                modifier = Modifier.padding(vertical = 3.dp)
                            )
                        }

                        if (res.completed && !completed) {
                            Spacer(Modifier.height(14.dp))
                            RewardButton("Получить награду +${task.reward} 🪙") { onComplete() }
                        }

                        if (res.completed && completed) {
                            Spacer(Modifier.height(10.dp))
                            Text(
                                "✓ Награда уже получена",
                                fontSize = 15.sp,
                                color = DoneGreen,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (!res.completed) {
                            Spacer(Modifier.height(10.dp))
                            Text(
                                "Попробуй изменить решение и пройти ещё раз",
                                fontSize = 15.sp,
                                color = Muted
                            )
                        }
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Мелкие элементы
// ---------------------------------------------------------------------------

/** Деревянная табличка с текстом */
@Composable
private fun WoodSign(text: String, fontSize: androidx.compose.ui.unit.TextUnit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .shadow(5.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Brush.verticalGradient(listOf(WoodLight, Wood)))
            .border(3.dp, WoodDark, RoundedCornerShape(16.dp))
            .padding(horizontal = 22.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text,
            fontSize = fontSize,
            color = WoodText,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}

@Composable
private fun RoundBackButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    BackArrowButton(modifier, onClick)   // общая кнопка из NavButtons.kt
}

@Composable
private fun FilterChip(
    text: String,
    selected: Boolean,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .shadow(if (selected) 4.dp else 2.dp, RoundedCornerShape(50))
            .clip(RoundedCornerShape(50))
            .background(if (selected) Primary else Color.White)
            .border(2.dp, if (selected) Color.White else Line, RoundedCornerShape(50))
            .clickable { onClick() }
            .padding(start = 10.dp, end = 14.dp, top = 7.dp, bottom = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()
        Spacer(Modifier.width(6.dp))
        Text(
            text,
            fontSize = 15.sp,
            color = if (selected) Color.White else Ink,
            fontWeight = FontWeight.Bold
        )
    }
}

/** Иконка «Все»: четыре квадратика */
@Composable
private fun GridIcon(selected: Boolean) {
    val c = if (selected) Color.White else Primary
    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
        repeat(2) {
            Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                repeat(2) {
                    Box(
                        Modifier
                            .size(9.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(c)
                    )
                }
            }
        }
    }
}

@Composable
private fun RewardButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(6.dp, RoundedCornerShape(28.dp))
            .clip(RoundedCornerShape(28.dp))
            .background(Brush.verticalGradient(listOf(Color(0xFF9A7BFF), Primary)))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        CoinText(text, fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
    }
}

/** Картинка задания: task_<id> → иконка раздела → эмодзи */
@Composable
private fun TaskImage(task: Task, size: Dp) {
    val context = LocalContext.current
    val own = remember(task.id) {
        context.resources.getIdentifier("task_${task.id}", "drawable", context.packageName)
    }
    if (own != 0) {
        Image(
            painter = painterResource(own),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(size)
        )
    } else {
        TIcon(sectionIcon(task.section), sectionEmoji(task.section), size)
    }
}

/** Картинка из drawable по имени; если файла нет — эмодзи. */
@Composable
private fun TIcon(name: String, fallback: String, size: Dp) {
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

private fun sectionName(section: String): String = when (section) {
    "budget" -> "Планирование бюджета"
    "savings" -> "Формирование сбережений"
    "shopping" -> "Платежи и покупки"
    else -> ""
}

private fun sectionIcon(section: String): String = when (section) {
    "budget" -> "icon_budget"
    "savings" -> "icon_savings"
    "shopping" -> "icon_cart"
    else -> "icon_budget"
}

private fun sectionEmoji(section: String): String = when (section) {
    "budget" -> "💰"
    "savings" -> "🐷"
    "shopping" -> "🛒"
    else -> "🎯"
}