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
fun ComparisonTask(
    task: Task,
    onResult: (TaskResult) -> Unit
){


    val items =
        task.options
            .filterKeys {
                it.startsWith("item")
            }



    val cheapest =
        items.values.minOfOrNull {


            it.split("|")
                .getOrNull(1)
                ?.toIntOrNull()
                ?: Int.MAX_VALUE

        }



    Column(

        modifier =
            Modifier.fillMaxWidth(),

        verticalArrangement =
            Arrangement.spacedBy(10.dp)

    ){



        items.forEach { (_, value) ->


            val parts =
                value.split("|")


            val name =
                parts[0]


            val price =
                parts[1]
                    .toInt()



            Column(

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clip(
                            RoundedCornerShape(18.dp)
                        )
                        .background(Surface)
                        .clickable {


                            val correct =
                                price == cheapest



                            val message =

                                if(correct){

                                    task.feedback["success"]

                                } else {

                                    task.feedback["expensive"]

                                }



                            onResult(

                                TaskResult(

                                    completed =
                                        correct,

                                    messages =
                                        listOfNotNull(
                                            message
                                        )
                                )
                            )

                        }

                        .padding(18.dp)

            ){


                Text(
                    text=name,
                    fontSize=18.sp,
                    color=Ink
                )


                Text(
                    text="$price 🪙",
                    fontSize=15.sp,
                    color=Ink
                )

            }

        }

    }

}