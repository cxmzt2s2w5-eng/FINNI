package com.myau.finni.tasktypes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myau.finni.Ink
import com.myau.finni.Task
import com.myau.finni.TaskResult


@Composable
fun AmountTask(
    task: Task,
    onResult: (TaskResult) -> Unit
) {


    val amounts =
        task.options["amounts"]
            ?.split("|")
            ?.mapNotNull {
                it.toIntOrNull()
            }
            ?: emptyList()


    val target =
        task.options["target"]
            ?.toIntOrNull()
            ?: 0



    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Text(
            text = "Сколько отложить?",
            fontSize = 20.sp,
            color = Ink
        )


        Spacer(
            modifier = Modifier.height(16.dp)
        )


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {


            amounts.forEach { amount ->


                AmountButton(
                    amount = amount
                ) {


                    if(amount >= target){

                        onResult(

                            TaskResult(
                                completed = true,

                                messages = listOf(
                                    task.feedback["success"]
                                        ?: "Отлично! Ты приблизился к цели."
                                )
                            )
                        )

                    } else {


                        onResult(

                            TaskResult(
                                completed = false,

                                messages = listOf(
                                    task.feedback["amount_low"]
                                        ?: "Попробуй отложить немного больше."
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}



@Composable
private fun AmountButton(
    amount:Int,
    onClick:()->Unit
){

    androidx.compose.material3.Button(
        onClick = onClick
    ){

        Text(
            text="$amount 🪙"
        )
    }
}