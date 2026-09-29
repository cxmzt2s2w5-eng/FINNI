package com.myau.finni

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun BottomNavigation(
    currentScreen:String,
    onNavigate:(String)->Unit
){

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 12.dp,
                vertical = 8.dp
            )
            .clip(
                RoundedCornerShape(24.dp)
            )
            .background(
                Color.White.copy(
                    alpha = 0.92f
                )
            )
            .padding(
                vertical = 8.dp
            ),

        horizontalArrangement =
            Arrangement.SpaceEvenly

    ){

        NavButton(
            "🏠",
            "Дом",
            currentScreen=="home"
        ){
            onNavigate("home")
        }


        NavButton(
            "📋",
            "План",
            currentScreen=="plan"
        ){
            onNavigate("plan")
        }


        NavButton(
            "🛒",
            "Магазин",
            currentScreen=="shop"
        ){
            onNavigate("shop")
        }


        NavButton(
            "🧩",
            "Задания",
            currentScreen=="tasks"
        ){
            onNavigate("tasks")
        }


        NavButton(
            "🐷",
            "Копилка",
            currentScreen=="savings"
        ){
            onNavigate("savings")
        }

    }
}



@Composable
private fun NavButton(
    icon:String,
    text:String,
    selected:Boolean,
    onClick:()->Unit
){

    Column(

        modifier = Modifier
            .clip(
                RoundedCornerShape(16.dp)
            )
            .background(
                if(selected)
                    PrimarySoft
                else
                    Color.Transparent
            )
            .clickable {
                onClick()
            }
            .padding(
                horizontal = 8.dp,
                vertical = 5.dp
            ),

        horizontalAlignment =
            Alignment.CenterHorizontally

    ){

        Text(
            text = icon
        )

        Text(
            text=text,
            fontSize=11f.sp,
            color =
                if(selected)
                    Primary
                else
                    Muted
        )

    }

}