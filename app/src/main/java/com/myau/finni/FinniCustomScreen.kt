package com.myau.finni


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



@Composable
fun FinniCustomScreen(
    vm: GameViewModel,
    onDone: () -> Unit
) {


    var type by remember {
        mutableStateOf("cat")
    }


    var color by remember {
        mutableStateOf("blue")
    }


    var name by remember {
        mutableStateOf("Финни")
    }



    val appearance = PetAppearance(
        type = type,
        color = color,
        stage = 1,
        mood = "neutral"
    )


    Column(

        modifier =
            Modifier
                .fillMaxSize()
                .background(Bg)
                .padding(20.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally

    ){


        Text(
            text="Создай Финни",
            fontSize=28.sp,
            fontWeight=FontWeight.Bold,
            color=Ink
        )


        Spacer(
            Modifier.height(20.dp)
        )



        // -----------------------------
        // ПРЕВЬЮ
        // -----------------------------


        FinniPreview(
            appearance
        )



        Spacer(
            Modifier.height(25.dp)
        )



        // -----------------------------
        // ЖИВОТНОЕ
        // -----------------------------


        Row(

            modifier =
                Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.SpaceEvenly

        ){


            PetChoice(
                "🐶",
                type=="dog"
            ){
                type="dog"
            }


            PetChoice(
                "🐰",
                type=="rabbit"
            ){
                type="rabbit"
            }


            PetChoice(
                "🐱",
                type=="cat"
            ){
                type="cat"
            }

        }



        Spacer(
            Modifier.height(20.dp)
        )



        // -----------------------------
        // ЦВЕТ
        // -----------------------------


        Text(
            "Цвет",
            fontSize=18.sp,
            fontWeight=FontWeight.Bold
        )


        Row(

            horizontalArrangement =
                Arrangement.spacedBy(20.dp)

        ){


            ColorChoice(
                Color(0xFF80B7FF),
                color=="blue"
            ){
                color="blue"
            }


            ColorChoice(
                Color(0xFFFF9FCB),
                color=="pink"
            ){
                color="pink"
            }


            ColorChoice(
                Color(0xFFB9906A),
                color=="brown"
            ){
                color="brown"
            }

            ColorChoice(
                Color(0xFFFFFFFF),
                color=="white"
            ){
                color="white"
            }
        }



        Spacer(
            Modifier.height(20.dp)
        )



        // -----------------------------
        // ИМЯ
        // -----------------------------


        OutlinedTextField(

            value=name,

            onValueChange={
                name=it
            },

            label={
                Text("Имя Финни")
            },

            singleLine=true

        )



        Spacer(
            Modifier.height(20.dp)
        )



        NextButton("Готово") {     // общая кнопка «вперёд»
            vm.setPet(
                name,
                type,
                color
            )
            onDone()
        }

    }

}






@Composable
private fun FinniPreview(
    appearance:PetAppearance
){

    val context =
        androidx.compose.ui.platform
            .LocalContext.current



    val body =
        remember(
            appearance
        ){

            PetImageResolver
                .loadBitmap(
                    context,
                    PetImageResolver
                        .getBodyFile(
                            appearance
                        )
                )

        }
    val face =
        remember(
            appearance
        ){

            PetImageResolver
                .loadBitmap(
                    context,
                    PetImageResolver
                        .getFaceFile(
                            appearance
                        )
                )

        }


    Box(

        modifier =
            Modifier
                .size(260.dp)
                .clip(
                    RoundedCornerShape(30.dp)
                )
                .background(
                    PrimarySoft
                ),

        contentAlignment =
            Alignment.Center

    ){


        body?.let {

            Image(
                bitmap = it,
                contentDescription = null,
                Modifier
                    .size(220.dp)
                    .offset(
                        y = petOffset(appearance.type)
                    )
                    .align(Alignment.Center)
            )

        }


        face?.let {

            Image(
                bitmap = it,
                contentDescription = null,
                modifier = Modifier
                    .size(220.dp)
                    .offset(
                        y = petfaceOffset(appearance.type)
                    )
                    .align(Alignment.Center)
            )

        }

    }

}

private fun petOffset(
    type: String
): Dp {

    return when(type){

        "cat" ->
            37.dp

        "dog" ->
            0.dp

        "rabbit" ->
            0.dp

        else ->
            0.dp
    }
}

private fun petfaceOffset(
    type: String
): Dp {

    return when(type){

        "cat" ->
            0.dp

        "dog" ->
            25.dp

        "rabbit" ->
            0.dp

        else ->
            0.dp
    }
}


@Composable
private fun PetChoice(
    icon:String,
    selected:Boolean,
    onClick:()->Unit
){

    Box(

        modifier =
            Modifier
                .size(70.dp)
                .clip(CircleShape)
                .background(
                    if(selected)
                        PrimarySoft
                    else
                        Surface
                )
                .clickable {
                    onClick()
                },

        contentAlignment =
            Alignment.Center

    ){

        Text(
            icon,
            fontSize=35.sp
        )

    }

}






@Composable
private fun ColorChoice(
    color:Color,
    selected:Boolean,
    onClick:()->Unit
){

    Box(

        modifier =
            Modifier
                .size(55.dp)
                .clip(CircleShape)
                .background(color)
                .clickable {
                    onClick()
                }
                .padding(5.dp),

        contentAlignment =
            Alignment.Center

    ){

        if(selected){

            Text(
                "✓",
                fontSize=22.sp
            )

        }

    }

}