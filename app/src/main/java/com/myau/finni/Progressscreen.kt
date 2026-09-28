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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * История, учебный прогресс и справочный раздел — ТЗ 2.5.11.
 */
@Composable
fun ProgressScreen(vm: GameViewModel, onBack: () -> Unit) {
    val s = vm.state

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Bg)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(16.dp))
        TopBar("Мой прогресс") { onBack() }

        FinniCard(bg = PrimarySoft) {
            Text("Развитие питомца", fontSize = 18.sp, color = Ink, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text(
                "Стадия ${s.petStage} из 3 — ${stageTitle(s.petStage)}",
                fontSize = 17.sp,
                color = Primary,
                fontWeight = FontWeight.Bold
            )
            Text("Баллов за дни: ${s.stagePoints}", fontSize = 15.sp, color = Muted)
            Spacer(Modifier.height(8.dp))
            Text(
                when (s.petStage) {
                    3 -> "Максимальная стадия. Продолжай заботиться о Финни."
                    2 -> "До взрослой стадии осталось ${6 - s.stagePoints} балла(ов)."
                    else -> "До следующей стадии осталось ${3 - s.stagePoints} балла(ов). За день можно получить до 3."
                },
                fontSize = 15.sp,
                color = Muted
            )
        }

        FinniCard {
            Text("Моя мечта", fontSize = 18.sp, color = Ink, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            if (s.goalTitle.isBlank()) {
                Text("Цель ещё не выбрана", fontSize = 16.sp, color = Muted)
            } else {
                Text("${s.goalTitle}: ${s.savings} из ${s.goalPrice}", fontSize = 16.sp, color = Ink)
                Spacer(Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = {
                        if (s.goalPrice == 0) 0f
                        else (s.savings.toFloat() / s.goalPrice).coerceIn(0f, 1f)
                    },
                    modifier = Modifier.fillMaxWidth().height(10.dp),
                    color = Primary,
                    trackColor = PrimarySoft
                )
            }
        }

        FinniCard(bg = Surface2) {
            Text("Задания", fontSize = 18.sp, color = Ink, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text("Выполнено: ${s.completedTasks.size}", fontSize = 16.sp, color = Ink)
            Text("Заработано за задания: ${s.completedTasks.size * 10} 🪙", fontSize = 15.sp, color = Muted)
        }

        FinniCard {
            Text("Итоги текущего дня", fontSize = 18.sp, color = Ink, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text("День ${s.day} из 5", fontSize = 16.sp, color = Ink)
            Text("В кошельке ${s.coins} 🪙, в копилке ${s.savings} 🪙", fontSize = 16.sp, color = Muted)
            Text("Куплено сегодня: ${s.purchased.size}", fontSize = 16.sp, color = Muted)
        }

        Text(
            "Словарик",
            fontSize = 22.sp,
            color = Ink,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
        )

        glossary.forEach { (term, text) ->
            FinniCard {
                Text(term, fontSize = 17.sp, color = Primary, fontWeight = FontWeight.Bold)
                Text(text, fontSize = 15.sp, color = Muted)
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}

private fun stageTitle(stage: Int): String = when (stage) {
    3 -> "взрослый"
    2 -> "подросток"
    else -> "малыш"
}

private val glossary = listOf(
    "Бюджет" to "Сумма, которой ты можешь распоряжаться за день.",
    "Обязательные расходы" to "То, без чего Финни плохо: еда и уход. Их планируют первыми.",
    "Необязательные расходы" to "Приятные покупки: игрушки, украшения. Их можно отложить на потом.",
    "Копилка" to "Монеты, отложенные отдельно от кошелька. Их не тратят на покупки.",
    "Мечта" to "То, на что ты копишь. У мечты есть цена и прогресс.",
    "План" to "Как ты собираешься разделить деньги до начала дня.",
    "Факт" to "Как деньги потратились на самом деле.",
    "Остаток" to "Сколько денег ещё не распределено в плане."
)