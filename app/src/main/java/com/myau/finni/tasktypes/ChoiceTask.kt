package com.myau.finni.tasktypes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myau.finni.Ink
import com.myau.finni.Surface
import com.myau.finni.Task
import com.myau.finni.TaskResult


@Composable
fun ChoiceTask(
    task: Task,
    onResult: (TaskResult) -> Unit
){

    val choices =
        task.options
            .filterKeys {
                it.startsWith("choice")
            }


    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement =
            Arrangement.spacedBy(10.dp)
    ){


        choices.forEach { (_, value) ->


            val parts =
                value.split("|")


            val text =
                parts[0]


            val result =
                parts.getOrNull(1)
                    ?: "wrong"



            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(
                        RoundedCornerShape(18.dp)
                    )
                    .background(Surface)
                    .clickable {


                        val success =
                            result == "correct"



                        val messageKey =
                            if(success)
                                "success"
                            else
                                result



                        onResult(

                            TaskResult(

                                completed =
                                    success,

                                messages =
                                    listOf(
                                        task.feedback[messageKey]
                                            ?: ""
                                    )
                            )
                        )
                    }
                    .padding(18.dp)

            ){

                Text(
                    text=text,
                    fontSize=17.sp,
                    color=Ink
                )
            }
        }
    }
}