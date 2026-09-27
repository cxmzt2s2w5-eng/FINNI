package com.myau.finni.tasktypes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
fun PlanningTask(
    task: Task,
    onResult:(TaskResult)->Unit
){

    val selected =
        remember {
            mutableStateListOf<String>()
        }



    val budget =
        task.options["budget"]
            ?.toIntOrNull()
            ?:100



    val options =
        task.options
            .filterKeys {

                it.startsWith("plan")

            }



    val total =
        selected.sumOf {


            options[it]
                ?.toIntOrNull()
                ?:0

        }




    Column(

        modifier =
            Modifier.fillMaxWidth(),

        verticalArrangement =
            Arrangement.spacedBy(10.dp)

    ){



        options.forEach { (key,value)->


            val active =
                selected.contains(key)



            Column(

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clip(
                            RoundedCornerShape(18.dp)
                        )
                        .background(

                            if(active)
                                PrimarySoft
                            else
                                Surface

                        )
                        .clickable {


                            if(active)
                                selected.remove(key)
                            else
                                selected.add(key)

                        }

                        .padding(18.dp)

            ){

                Text(
                    text=key,
                    fontSize=17.sp,
                    color=Ink
                )


                Text(
                    text="$value 🪙",
                    fontSize=15.sp,
                    color=Ink
                )

            }

        }



        Text(
            text="План: $total / $budget 🪙",
            fontSize=18.sp,
            color=Ink
        )



        Button(

            onClick={


                val messages =
                    mutableListOf<String>()



                if(total > budget){

                    task.feedback["over_budget"]
                        ?.let {
                            messages.add(it)
                        }

                }


                if(total < budget){

                    task.feedback["too_low"]
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

            modifier =
                Modifier.fillMaxWidth()

        ){

            Text(
                text="Проверить план"
            )
        }
    }
}