package com.myau.finni

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.Image
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun FinniBottomBar(
    currentScreen: String,
    onNavigate: (String) -> Unit
) {

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
                Color.White.copy(alpha = 0.6f)
            )
            .padding(vertical = 8.dp),


        horizontalArrangement =
            Arrangement.SpaceEvenly

    ) {


        BottomItem(
            icon = R.drawable.icon_home,
            title = "Дом",
            selected = currentScreen == "home"
        ) {
            onNavigate("home")
        }



        BottomItem(
            icon = R.drawable.icon_plan,
            title = "План",
            selected = currentScreen == "plan"
        ) {
            onNavigate("plan")
        }



        BottomItem(
            icon = R.drawable.icon_shop,
            title = "Магазин",
            selected = currentScreen == "shop"
        ) {
            onNavigate("shop")
        }



        BottomItem(
            icon = R.drawable.icon_tasks,
            title = "Задания",
            selected = currentScreen == "tasks"
        ) {
            onNavigate("tasks")
        }



        BottomItem(
            icon = R.drawable.icon_savings,
            title = "Копилка",
            selected = currentScreen == "savings"
        ) {
            onNavigate("savings")
        }

    }
}



@Composable
private fun BottomItem(
    icon: Int,
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {


    val iconSize =
        if(selected)
            42.dp
        else
            36.dp


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
                horizontal = 10.dp,
                vertical = 6.dp
            ),

        horizontalAlignment =
            Alignment.CenterHorizontally

    ) {


        Image(

            painter =
                painterResource(icon),

            contentDescription =
                title,

            modifier =
                Modifier.size(iconSize)

        )


        if(selected){

            Box(

                modifier =
                    Modifier
                        .padding(top = 2.dp)
                        .size(
                            width = 22.dp,
                            height = 4.dp
                        )
                        .clip(
                            RoundedCornerShape(50)
                        )
                        .background(Primary)

            )

        }


        Text(

            text = title,

            fontSize = 12.sp,

            color =
                if(selected)
                    Primary
                else
                    Muted

        )

    }

}