package com.myau.finni

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.PaddingValues

import com.myau.finni.tasktypes.*


@Composable
fun TasksScreen(
    onBack: () -> Unit = {}
) {

    val context =
        LocalContext.current


    val progressManager =
        remember {
            ProgressManager(context)
        }


    var tasks by remember {
        mutableStateOf<List<Task>>(emptyList())
    }


    var selectedTask by remember {
        mutableStateOf<Task?>(null)
    }


    var selectedSection by remember {
        mutableStateOf("all")
    }


    var completedTasks by remember {
        mutableStateOf(
            progressManager
                .load()
                .completedTasks
                .toSet()
        )
    }



    LaunchedEffect(Unit) {

        tasks =
            TaskParser
                .loadTasks(context)

    }



    // открыто конкретное задание

    if(selectedTask != null){


        TaskDetailScreen(

            task = selectedTask!!,


            completed =
                completedTasks.contains(
                    selectedTask!!.id
                ),


            onComplete = {


                val current =
                    progressManager.load()


                progressManager.save(
                    current.copy(
                        completedTasks =
                            current.completedTasks +
                                    selectedTask!!.id,

                        coins =
                            current.coins +
                                    selectedTask!!.reward
                    )
                )


                completedTasks =
                    progressManager
                        .load()
                        .completedTasks
                        .toSet()

            },


            onBack = {

                selectedTask = null

            }
        )


        return

    }




    Column(

        modifier =
            Modifier
                .fillMaxSize()
                .background(Bg)

    ){



        // ----------------------------
        // Шапка
        // ----------------------------


        Row(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        20.dp
                    ),

            verticalAlignment =
                Alignment.CenterVertically

        ){


            Box(

                modifier =
                    Modifier
                        .size(50.dp)
                        .clip(
                            RoundedCornerShape(16.dp)
                        )
                        .background(Surface)
                        .clickable {
                            onBack()
                        },

                contentAlignment =
                    Alignment.Center

            ){

                Text(
                    text="←",
                    fontSize=24.sp
                )

            }



            Text(

                text="Задания",

                modifier =
                    Modifier.weight(1f),

                textAlign =
                    TextAlign.Center,

                fontSize=26.sp,

                fontWeight =
                    FontWeight.Bold,

                color=Ink

            )



            Spacer(
                modifier =
                    Modifier.size(50.dp)
            )

        }





        // ----------------------------
        // Прогресс
        // ----------------------------


        FinniCard(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),

            bg = PrimarySoft

        ){


            Column {


                Text(

                    text =
                        "Прогресс",

                    fontSize =
                        14.sp,

                    color =
                        Muted

                )



                Text(

                    text =
                        "${completedTasks.size} / ${tasks.size} выполнено",

                    fontSize =
                        22.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        Ink

                )



                Spacer(
                    modifier =
                        Modifier.height(5.dp)
                )


                Text(

                    text =
                        "⭐ ${completedTasks.size * 10}",

                    fontSize =
                        18.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        Primary

                )

            }

        }




        Spacer(
            Modifier.height(15.dp)
        )



        // ----------------------------
        // Фильтры
        // ----------------------------


        Row(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),

            horizontalArrangement =
                Arrangement.spacedBy(8.dp)

        ){


            FilterButton(
                "Все",
                selectedSection=="all"
            ){
                selectedSection="all"
            }



            FilterButton(
                "💰 Бюджет",
                selectedSection=="budget"
            ){
                selectedSection="budget"
            }



            FilterButton(
                "🐷 Сбережения",
                selectedSection=="savings"
            ){
                selectedSection="savings"
            }

        }



        Spacer(
            Modifier.height(8.dp)
        )



        Row(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)

        ){

            FilterButton(
                "🛒 Покупки",
                selectedSection=="shopping"
            ){
                selectedSection="shopping"
            }

        }




        Spacer(
            Modifier.height(15.dp)
        )





        val filtered =

            if(selectedSection=="all")

                tasks

            else

                tasks.filter {

                    it.section ==
                            selectedSection

                }




        LazyColumn(

            modifier =
                Modifier.fillMaxSize(),

            contentPadding =
                PaddingValues(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 30.dp
                ),

            verticalArrangement =
                Arrangement.spacedBy(12.dp)

        ){


            items(filtered){ task ->


                TaskCard(

                    task = task,

                    completed =
                        completedTasks
                            .contains(task.id)

                ){

                    selectedTask =
                        task

                }

            }

        }

    }

}






// =====================================================
// Карточка задания
// =====================================================


@Composable
private fun TaskCard(

    task: Task,

    completed:Boolean,

    onClick:()->Unit

){


    FinniCard(

        modifier =
            Modifier
                .fillMaxWidth()
                .clickable {
                    onClick()
                }

    ){


        Row(

            verticalAlignment =
                Alignment.CenterVertically

        ){


            Text(

                text =
                    when(task.section){

                        "budget" -> "💰"

                        "savings" -> "🐷"

                        "shopping" -> "🛒"

                        else -> "🎯"

                    },

                fontSize =
                    35.sp

            )



            Spacer(
                Modifier.width(15.dp)
            )



            Column(
                modifier =
                    Modifier.weight(1f)
            ){


                Text(

                    text =
                        task.title,

                    fontSize =
                        18.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        Ink

                )



                Text(

                    text =
                        when(task.section){

                            "budget" ->
                                "Планирование бюджета"

                            "savings" ->
                                "Формирование сбережений"

                            "shopping" ->
                                "Платежи и покупки"

                            else ->
                                ""

                        },

                    fontSize =
                        13.sp,

                    color =
                        Muted

                )

            }




            Text(

                text =
                    if(completed)
                        "✅"
                    else
                        "›",

                fontSize =
                    26.sp

            )


        }


    }

}






// =====================================================
// Детальное задание
// =====================================================


@Composable
private fun TaskDetailScreen(

    task:Task,

    completed:Boolean,

    onComplete:()->Unit,

    onBack:()->Unit

){


    var result by remember {

        mutableStateOf<TaskResult?>(null)

    }



    Column(

        modifier =
            Modifier
                .fillMaxSize()
                .background(Bg)

    ){


        Row(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp),

            verticalAlignment =
                Alignment.CenterVertically

        ){


            Text(

                text="←",

                fontSize=28.sp,

                modifier =
                    Modifier.clickable {
                        onBack()
                    }

            )


            Text(

                text =
                    task.title,

                modifier =
                    Modifier.weight(1f),

                textAlign =
                    TextAlign.Center,

                fontSize =
                    22.sp,

                fontWeight =
                    FontWeight.Bold

            )

        }



        Column(

            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)

        ){


            FinniCard(

                modifier =
                    Modifier.fillMaxWidth(),

                bg = Surface

            ){

                Text(

                    text =
                        task.text,

                    fontSize =
                        18.sp

                )

            }



            Spacer(
                Modifier.height(20.dp)
            )




            when(task.type){


                "distribution" ->

                    DistributionTask(
                        task
                    ){
                        result = it
                    }



                "amount" ->

                    AmountTask(
                        task
                    ){
                        result = it
                    }



                "choice" ->

                    ChoiceTask(
                        task
                    ){
                        result = it
                    }



                "shopping" ->

                    ShoppingTask(
                        task
                    ){
                        result = it
                    }



                "comparison" ->

                    ComparisonTask(
                        task
                    ){
                        result = it
                    }



                "planning" ->

                    PlanningTask(
                        task
                    ){
                        result = it
                    }

            }




            result?.let { res ->



                Spacer(
                    Modifier.height(20.dp)
                )



                FinniCard(

                    modifier =
                        Modifier.fillMaxWidth(),

                    bg =
                        if(res.completed)
                            PrimarySoft
                        else
                            Surface

                ){



                    Text(

                        text =
                            if(res.completed)
                                "🎉 Отлично!"
                            else
                                "💡 Посмотри, что получилось",

                        fontSize =
                            20.sp,

                        fontWeight =
                            FontWeight.Bold

                    )



                    Spacer(
                        Modifier.height(10.dp)
                    )



                    res.messages.forEach { message ->

                        Text(

                            text =
                                "• $message",

                            fontSize =
                                16.sp,

                            modifier =
                                Modifier.padding(
                                    vertical = 3.dp
                                )

                        )

                    }



                    if(res.completed && !completed){


                        Spacer(
                            Modifier.height(15.dp)
                        )


                        Button(

                            onClick = {

                                onComplete()

                            },

                            modifier =
                                Modifier.fillMaxWidth()

                        ){

                            Text(
                                "Получить награду ⭐ ${task.reward}"
                            )

                        }

                    }



                    if(!res.completed){


                        Spacer(
                            Modifier.height(10.dp)
                        )


                        Text(

                            text =
                                "Попробуй изменить решение и пройти ещё раз",

                            color =
                                Muted

                        )

                    }

                }

            }

        }

    }

}






@Composable
private fun FilterButton(

    text:String,

    selected:Boolean,

    onClick:()->Unit

){


    Box(

        modifier =
            Modifier
                .clip(
                    RoundedCornerShape(14.dp)
                )
                .background(
                    if(selected)
                        Primary
                    else
                        Surface
                )
                .border(
                    1.dp,
                    Line,
                    RoundedCornerShape(14.dp)
                )
                .clickable {
                    onClick()
                }
                .padding(
                    horizontal = 14.dp,
                    vertical = 10.dp
                ),

        contentAlignment =
            Alignment.Center

    ){

        Text(
            text=text,
            color =
                if(selected)
                    Surface
                else
                    Ink
        )

    }

}