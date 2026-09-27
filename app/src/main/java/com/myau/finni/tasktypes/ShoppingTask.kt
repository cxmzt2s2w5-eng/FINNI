package com.myau.finni.tasktypes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myau.finni.Ink
import com.myau.finni.PrimarySoft
import com.myau.finni.Surface
import com.myau.finni.Task
import com.myau.finni.TaskResult


@Composable
fun ShoppingTask(
    task: Task,
    onResult: (TaskResult) -> Unit
) {

    val selected =
        remember {
            mutableStateListOf<String>()
        }


    val items =
        task.options
            .filterKeys {
                it.startsWith("item")
            }


    val budget =
        task.options["budget"]
            ?.toIntOrNull()
            ?: 100



    val total =
        selected.sumOf { key ->

            val value =
                items[key]
                    ?: ""

            value
                .split("|")
                .getOrNull(1)
                ?.toIntOrNull()
                ?: 0
        }



    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement =
            Arrangement.spacedBy(10.dp)
    ) {


        items.forEach { (key, value) ->


            val parts =
                value.split("|")


            val name =
                parts.getOrNull(0)
                    ?: ""


            val price =
                parts.getOrNull(1)
                    ?.toIntOrNull()
                    ?: 0



            val selectedItem =
                selected.contains(key)



            Row(

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clip(
                            RoundedCornerShape(18.dp)
                        )
                        .background(
                            if(selectedItem)
                                PrimarySoft
                            else
                                Surface
                        )
                        .clickable {


                            if(selectedItem){

                                selected.remove(key)

                            } else {

                                selected.add(key)

                            }

                        }
                        .padding(18.dp),

                horizontalArrangement =
                    Arrangement.SpaceBetween

            ){


                Text(
                    text=name,
                    fontSize=17.sp,
                    color=Ink
                )


                Text(
                    text="$price 🪙",
                    fontSize=17.sp,
                    color=Ink
                )
            }
        }



        Text(
            text=
                "Корзина: $total / $budget 🪙",
            fontSize=18.sp,
            color=Ink
        )



        Button(

            onClick = {


                val messages =
                    mutableListOf<String>()



                if(total > budget){

                    task.feedback["over_budget"]
                        ?.let {
                            messages.add(it)
                        }
                }



                val required =
                    items
                        .filter { (_, value) ->

                            value.endsWith(
                                "|required"
                            )

                        }
                        .keys



                required.forEach {


                    if(!selected.contains(it)){


                        task.feedback["missing_required"]
                            ?.let { msg ->

                                messages.add(msg)

                            }
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

            modifier =
                Modifier.fillMaxWidth()

        ){

            Text(
                text="Проверить покупку"
            )
        }
    }
}