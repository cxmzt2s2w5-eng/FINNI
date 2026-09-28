package com.myau.finni

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Итоги игрового дня — ТЗ 2.5.9 и 2.5.10.
 * Показывает план и факт, объясняет причину изменения состояния питомца
 * и переводит на следующий период.
 */
@Composable
fun SummaryScreen(vm: GameViewModel, onBack: () -> Unit) {
    val s = vm.state
    val score = vm.dayScore()
    val accuracy = vm.planAccuracy()

    val fed = s.satiety >= 40
    val cared = s.care >= 40
    val saved = s.factSave > 0 && s.factSave >= s.planSave

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Bg)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(16.dp))
        TopBar("Итоги дня ${s.day}") { onBack() }

        FinniCard(bg = PrimarySoft) {
            Text("Оценка дня", fontSize = 16.sp, color = Muted, fontWeight = FontWeight.Bold)
            Text("$score из 3 ⭐", fontSize = 30.sp, color = Primary, fontWeight = FontWeight.Bold)
            Text(
                when (score) {
                    3 -> "Отличный день: всё куплено, план выполнен, копилка пополнилась."
                    2 -> "Хороший день, одно правило не сработало."
                    1 -> "Часть решений сработала. Завтра будет легче."
                    else -> "День вышел сложным. Это поправимо — скорректируй завтрашний план."
                },
                fontSize = 16.sp,
                color = Ink
            )
        }

        FinniCard {
            Text("Что получилось", fontSize = 18.sp, color = Ink, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(10.dp))
            RuleRow("Финни сыт и ухожен", fed && cared)
            RuleRow("План выполнен точно (не меньше 70%)", accuracy >= 70)
            RuleRow("Отложено в копилку не меньше плана", saved)
        }

        FinniCard(bg = Surface2) {
            Text("План и факт", fontSize = 18.sp, color = Ink, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(10.dp))
            CompareLine("🍲 Нужно", s.planNeed, s.factNeed)
            CompareLine("🎈 Хочется", s.planWant, s.factWant)
            CompareLine("🐷 В копилку", s.planSave, s.factSave)
            Spacer(Modifier.height(10.dp))
            Text(
                "Точность плана: $accuracy%",
                fontSize = 17.sp,
                color = Primary,
                fontWeight = FontWeight.Bold
            )
        }

        FinniCard {
            Text("Как себя чувствует ${s.petName}", fontSize = 18.sp, color = Ink, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text(
                buildString {
                    append(if (fed) "Еда была куплена. " else "Еды на день не хватило. ")
                    append(if (cared) "Про уход не забыли. " else "Про уход забыли. ")
                    append(
                        if (score >= 2) "Финни доволен и подрос в опыте."
                        else "Финни немного расстроен, но всё поправимо."
                    )
                },
                fontSize = 16.sp,
                color = Muted
            )
            Spacer(Modifier.height(10.dp))
            Text(
                "Баллов развития: ${s.stagePoints} · стадия ${s.petStage} из 3",
                fontSize = 16.sp,
                color = Ink,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(8.dp))

        if (s.day >= 5) {
            FinniCard(bg = MintSoft) {
                Text("Пять дней пройдено!", fontSize = 18.sp, color = Ink, fontWeight = FontWeight.Bold)
                Text(
                    "Можно продолжать играть дальше или сбросить профиль в разделе для взрослого.",
                    fontSize = 16.sp,
                    color = Muted
                )
            }
        }

        FinniButton("Начать день ${s.day + 1}") {
            vm.endDay()
            onBack()
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun RuleRow(text: String, done: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            if (done) "✔" else "•",
            fontSize = 20.sp,
            color = if (done) Mint else Muted,
            fontWeight = FontWeight.Bold
        )
        Text(
            "  $text",
            fontSize = 16.sp,
            color = Ink,
            modifier = Modifier.weight(1f)
        )
        Text(
            if (done) "+1 ⭐" else "0",
            fontSize = 15.sp,
            color = if (done) Mint else Muted,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun CompareLine(label: String, plan: Int, fact: Int) {
    val diff = fact - plan
    val comment = when {
        diff == 0 -> "как в плане"
        diff > 0 -> "больше на $diff"
        else -> "меньше на ${-diff}"
    }
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 16.sp, color = Ink, modifier = Modifier.weight(1f))
        Text("$plan → $fact", fontSize = 16.sp, color = Ink, fontWeight = FontWeight.Bold)
        Text("  ($comment)", fontSize = 14.sp, color = Muted)
    }
}