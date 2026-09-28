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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Раздел для взрослого — ТЗ 2.5.12.
 * Отделён простым арифметическим барьером, без пароля и аккаунта.
 */
@Composable
fun AdultScreen(vm: GameViewModel, onBack: () -> Unit) {
    var unlocked by remember { mutableStateOf(false) }

    if (!unlocked) {
        AdultGate(onBack = onBack) { unlocked = true }
        return
    }

    val s = vm.state
    var confirmReset by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Bg)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(16.dp))
        TopBar("Для взрослого") { onBack() }

        FinniCard {
            Text("Чему учит приложение", fontSize = 18.sp, color = Ink, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text(
                "Ребёнок осваивает базовые компетенции финансовой грамотности: " +
                        "понимание бюджета, различение обязательных и необязательных расходов, " +
                        "планирование покупок при ограниченных средствах, краткосрочную цель " +
                        "и регулярные накопления, оценку собственных решений.",
                fontSize = 16.sp,
                color = Muted
            )
        }

        FinniCard(bg = Surface2) {
            Text("Общий прогресс", fontSize = 18.sp, color = Ink, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            InfoLine("Пройдено дней", "${s.day - 1}")
            InfoLine("Стадия развития питомца", "${s.petStage} из 3")
            InfoLine("Выполнено заданий", "${s.completedTasks.size}")
            InfoLine("Накоплено на мечту", "${s.savings} из ${s.goalPrice}")
            Spacer(Modifier.height(8.dp))
            Text(
                "Здесь нет оценок ребёнка и сравнения с другими — только пройденные темы.",
                fontSize = 15.sp,
                color = Muted
            )
        }

        FinniCard {
            Text("Настройки", fontSize = 18.sp, color = Ink, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Демонстрационный режим", fontSize = 16.sp, color = Ink)
                    Text("Все задания открыты сразу", fontSize = 14.sp, color = Muted)
                }
                Switch(
                    checked = s.demoMode,
                    onCheckedChange = { vm.setDemoMode(it) }
                )
            }
        }

        FinniCard(bg = DangerSoft) {
            Text("Данные", fontSize = 18.sp, color = Ink, fontWeight = FontWeight.Bold)
            Text(
                "Приложение не собирает персональные данные, не запрашивает разрешения " +
                        "и работает без интернета. Всё хранится только на этом устройстве.",
                fontSize = 15.sp,
                color = Muted
            )
            Spacer(Modifier.height(12.dp))
            FinniButton("Сбросить профиль") { confirmReset = true }
        }

        Spacer(Modifier.height(24.dp))
    }

    if (confirmReset) {
        AlertDialog(
            onDismissRequest = { confirmReset = false },
            containerColor = Surface,
            title = {
                Text("Сбросить профиль?", fontSize = 20.sp, color = Ink, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    "Питомец, монеты, копилка, покупки и выполненные задания будут удалены. " +
                            "Действие необратимо.",
                    fontSize = 16.sp,
                    color = Ink
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    vm.reset()
                    confirmReset = false
                    onBack()
                }) {
                    Text("Сбросить", fontSize = 17.sp, color = Danger, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmReset = false }) {
                    Text("Отмена", fontSize = 17.sp, color = Muted)
                }
            }
        )
    }
}

@Composable
private fun AdultGate(onBack: () -> Unit, onUnlock: () -> Unit) {
    val first = remember { (6..9).random() }
    val second = remember { (6..9).random() }
    var answer by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Bg)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(16.dp))
        TopBar("Для взрослого") { onBack() }

        FinniCard {
            Text("Проверка", fontSize = 18.sp, color = Ink, fontWeight = FontWeight.Bold)
            Text(
                "Этот раздел для взрослого. Решите пример, чтобы продолжить.",
                fontSize = 16.sp,
                color = Muted
            )
            Spacer(Modifier.height(16.dp))
            Text("$first × $second = ?", fontSize = 28.sp, color = Ink, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = answer,
                onValueChange = {
                    answer = it.filter { ch -> ch.isDigit() }
                    error = false
                },
                label = { Text("Ответ") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            if (error) {
                Spacer(Modifier.height(6.dp))
                Text("Неверно, попробуйте ещё раз", fontSize = 15.sp, color = Danger)
            }
            Spacer(Modifier.height(14.dp))
            FinniButton("Войти") {
                if (answer.toIntOrNull() == first * second) onUnlock() else error = true
            }
        }
    }
}

@Composable
private fun InfoLine(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 16.sp, color = Muted, modifier = Modifier.weight(1f))
        Text(value, fontSize = 16.sp, color = Ink, fontWeight = FontWeight.Bold)
    }
}