package com.myau.finni.tasktypes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myau.finni.Ink
import com.myau.finni.Primary
import com.myau.finni.Task
import com.myau.finni.TaskResult


@Composable
fun DistributionTask(
    task: Task,
    onResult: (TaskResult) -> Unit
) {

    var need by remember {
        mutableIntStateOf(0)
    }

    var want by remember {
        mutableIntStateOf(0)
    }

    var save by remember {
        mutableIntStateOf(0)
    }


    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {


        DistributionRow(
            title = "🍎 Необходимое",
            value = need,
            onMinus = {
                if (need >= 10)
                    need -= 10
            },
            onPlus = {
                need += 10
            }
        )


        DistributionRow(
            title = "🎮 Желания",
            value = want,
            onMinus = {
                if (want >= 10)
                    want -= 10
            },
            onPlus = {
                want += 10
            }
        )


        DistributionRow(
            title = "🐷 Накопления",
            value = save,
            onMinus = {
                if (save >= 10)
                    save -= 10
            },
            onPlus = {
                save += 10
            }
        )


        Spacer(
            modifier = Modifier.height(10.dp)
        )


        Text(
            text = "Всего: ${need + want + save} 🪙",
            fontSize = 18.sp,
            color = Ink
        )


        Button(
            onClick = {


                val messages =
                    mutableListOf<String>()



                val targetNeed =
                    task.options["need"]
                        ?.toIntOrNull()
                        ?: 0


                val targetWant =
                    task.options["want"]
                        ?.toIntOrNull()
                        ?: 0


                val targetSave =
                    task.options["save"]
                        ?.toIntOrNull()
                        ?: 0



                // Проверяем каждую категорию отдельно

                if (need < targetNeed) {

                    task.feedback["need_low"]
                        ?.let {
                            messages.add(it)
                        }
                }


                if (need > targetNeed) {

                    task.feedback["need_high"]
                        ?.let {
                            messages.add(it)
                        }
                }


                if (want > targetWant) {

                    task.feedback["want_high"]
                        ?.let {
                            messages.add(it)
                        }
                }


                if (want < targetWant) {

                    task.feedback["want_low"]
                        ?.let {
                            messages.add(it)
                        }
                }


                if (save < targetSave) {

                    task.feedback["save_low"]
                        ?.let {
                            messages.add(it)
                        }
                }


                if (save > targetSave) {

                    task.feedback["save_high"]
                        ?.let {
                            messages.add(it)
                        }
                }



                onResult(

                    TaskResult(

                        completed =
                            messages.isEmpty(),

                        messages =
                            messages
                    )
                )
            },

            modifier = Modifier.fillMaxWidth()

        ) {

            Text(
                text = "Проверить"
            )
        }
    }
}



@Composable
private fun DistributionRow(
    title: String,
    value: Int,
    onMinus: () -> Unit,
    onPlus: () -> Unit
) {


    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.SpaceBetween,
        verticalAlignment =
            Alignment.CenterVertically
    ) {


        Text(
            text = title,
            fontSize = 17.sp,
            color = Ink
        )


        Row {

            Button(
                onClick = onMinus
            ) {
                Text("−")
            }


            Text(
                text = "$value 🪙",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 10.dp),
                fontSize = 17.sp
            )


            Button(
                onClick = onPlus
            ) {
                Text("+")
            }
        }
    }
}