package com.myau.finni

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun FinniCustomScreen(
    onBack: () -> Unit = {},
    onDone: () -> Unit = {}
) {

    // Выбранный цвет
    var selectedColor by remember {
        mutableIntStateOf(0)
    }

    // Выбранная форма ушей
    var selectedEars by remember {
        mutableIntStateOf(0)
    }

    // Имя питомца
    var petName by remember {
        mutableStateOf("Финни")
    }

    val finniColors = listOf(
        Color(0xFFFFCF55), // жёлтый
        Color(0xFF68B5E8), // голубой
        Color(0xFFB58AEF)  // фиолетовый
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Bg)
    ) {

        // ---------------------------------------------------------
        // ВЕРХНЯЯ ЧАСТЬ
        // ---------------------------------------------------------

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 18.dp,
                    bottom = 8.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(17.dp))
                    .background(Surface)
                    .clickable { onBack() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "←",
                    fontSize = 25.sp,
                    color = Ink
                )
            }

            Text(
                text = "Создай Финни",
                modifier = Modifier.weight(1f),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Ink,
                textAlign = TextAlign.Center
            )

            // Пустое место, чтобы заголовок был по центру
            Spacer(
                modifier = Modifier.size(52.dp)
            )
        }


        // ---------------------------------------------------------
        // ОСНОВНАЯ ОБЛАСТЬ С ПРОКРУТКОЙ
        // ---------------------------------------------------------

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(10.dp))


            // -----------------------------------------------------
            // ПРЕВЬЮ ФИННИ
            // -----------------------------------------------------

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(355.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(PrimarySoft),
                contentAlignment = Alignment.Center
            ) {

                FinniPreview(
                    color = finniColors[selectedColor],
                    earsType = selectedEars
                )
            }


            Spacer(modifier = Modifier.height(18.dp))


            // -----------------------------------------------------
            // ЦВЕТ
            // -----------------------------------------------------

            CustomSectionCard {

                Text(
                    text = "Цвет",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Ink
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    finniColors.forEachIndexed { index, color ->

                        ColorChoice(
                            color = color,
                            selected = selectedColor == index,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                selectedColor = index
                            }
                        )
                    }
                }
            }


            // -----------------------------------------------------
            // УШИ
            // -----------------------------------------------------

            CustomSectionCard {

                Text(
                    text = "Ушки",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Ink
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    EarChoice(
                        text = "Круглые",
                        selected = selectedEars == 0,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            selectedEars = 0
                        }
                    )

                    EarChoice(
                        text = "Длинные",
                        selected = selectedEars == 1,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            selectedEars = 1
                        }
                    )

                    EarChoice(
                        text = "Острые",
                        selected = selectedEars == 2,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            selectedEars = 2
                        }
                    )
                }
            }


            // -----------------------------------------------------
            // ИМЯ
            // -----------------------------------------------------

//            CustomSectionCard {
//
//                Text(
//                    text = "Игровое имя питомца",
//                    fontSize = 15.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Muted
//                )
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                BasicTextField(
//                    value = petName,
//                    onValueChange = {
//                        if (it.length <= 16) {
//                            petName = it
//                        }
//                    },
//                    singleLine = true,
//                    textStyle = androidx.compose.ui.text.TextStyle(
//                        fontSize = 20.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Ink
//                    ),
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clip(RoundedCornerShape(18.dp))
//                        .background(Surface)
//                        .border(
//                            width = 2.dp,
//                            color = Line,
//                            shape = RoundedCornerShape(18.dp)
//                        )
//                        .padding(
//                            horizontal = 18.dp,
//                            vertical = 16.dp
//                        )
//                )
//            }

            Spacer(modifier = Modifier.height(10.dp))
        }


        // ---------------------------------------------------------
        // КНОПКА ГОТОВО
        // ---------------------------------------------------------

        Button(
            onClick = {
                onDone()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 16.dp
                )
                .height(58.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary
            )
        ) {
            Text(
                text = "Готово →",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


// =================================================================
// ФИННИ
// =================================================================

@Composable
private fun FinniPreview(
    color: Color,
    earsType: Int
) {

    Box(
        modifier = Modifier.size(250.dp),
        contentAlignment = Alignment.Center
    ) {

        // УШИ
        when (earsType) {

            // Круглые
            0 -> {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .align(Alignment.TopStart)
                        .padding(start = 35.dp)
                        .clip(CircleShape)
                        .background(color)
                )

                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .align(Alignment.TopEnd)
                        .padding(end = 35.dp)
                        .clip(CircleShape)
                        .background(color)
                )
            }

            // Длинные
            1 -> {
                Box(
                    modifier = Modifier
                        .size(width = 58.dp, height = 100.dp)
                        .align(Alignment.TopStart)
                        .padding(start = 40.dp)
                        .clip(RoundedCornerShape(40.dp))
                        .background(color)
                )

                Box(
                    modifier = Modifier
                        .size(width = 58.dp, height = 100.dp)
                        .align(Alignment.TopEnd)
                        .padding(end = 40.dp)
                        .clip(RoundedCornerShape(40.dp))
                        .background(color)
                )
            }

            // Острые
            2 -> {
                Box(
                    modifier = Modifier
                        .size(75.dp)
                        .align(Alignment.TopStart)
                        .padding(start = 35.dp)
                        .clip(
                            RoundedCornerShape(
                                topStart = 45.dp,
                                topEnd = 45.dp,
                                bottomStart = 12.dp,
                                bottomEnd = 12.dp
                            )
                        )
                        .background(color)
                )

                Box(
                    modifier = Modifier
                        .size(75.dp)
                        .align(Alignment.TopEnd)
                        .padding(end = 35.dp)
                        .clip(
                            RoundedCornerShape(
                                topStart = 45.dp,
                                topEnd = 45.dp,
                                bottomStart = 12.dp,
                                bottomEnd = 12.dp
                            )
                        )
                        .background(color)
                )
            }
        }


        // ТЕЛО
        Box(
            modifier = Modifier
                .size(width = 190.dp, height = 200.dp)
                .align(Alignment.Center)
                .clip(
                    RoundedCornerShape(
                        topStart = 90.dp,
                        topEnd = 90.dp,
                        bottomStart = 65.dp,
                        bottomEnd = 65.dp
                    )
                )
                .background(color)
        )


        // МОРДОЧКА
        Box(
            modifier = Modifier
                .size(width = 115.dp, height = 90.dp)
                .align(Alignment.Center)
                .padding(top = 35.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(
                    Color(
                        red = 255,
                        green = 239,
                        blue = 188
                    )
                )
        )


        // ЛЕВЫЙ ГЛАЗ
        Box(
            modifier = Modifier
                .size(18.dp)
                .align(Alignment.Center)
                .padding(
                    end = 42.dp,
                    bottom = 35.dp
                )
                .clip(CircleShape)
                .background(Ink)
        )

        // ПРАВЫЙ ГЛАЗ
        Box(
            modifier = Modifier
                .size(18.dp)
                .align(Alignment.Center)
                .padding(
                    start = 42.dp,
                    bottom = 35.dp
                )
                .clip(CircleShape)
                .background(Ink)
        )


        // НОС
        Box(
            modifier = Modifier
                .size(34.dp)
                .align(Alignment.Center)
                .padding(top = 18.dp)
                .clip(CircleShape)
                .background(Ink)
        )


        // РОТ
        Text(
            text = "⌣",
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = 53.dp),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Ink
        )
    }
}


// =================================================================
// КАРТОЧКА РАЗДЕЛА
// =================================================================

@Composable
private fun CustomSectionCard(
    content: @Composable () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(Surface)
            .padding(18.dp)
    ) {
        content()
    }
}


// =================================================================
// ВЫБОР ЦВЕТА
// =================================================================

@Composable
private fun ColorChoice(
    color: Color,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Box(
        modifier = modifier
            .height(72.dp)
            .clip(RoundedCornerShape(18.dp))
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) Primary else Line,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(color)
                .border(
                    width = 3.dp,
                    color = Surface,
                    shape = CircleShape
                )
        )
    }
}


// =================================================================
// ВЫБОР УШЕЙ
// =================================================================

@Composable
private fun EarChoice(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Box(
        modifier = modifier
            .height(58.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) Primary else Line,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = text,
            fontSize = 15.sp,
            fontWeight = if (selected) {
                FontWeight.Bold
            } else {
                FontWeight.Medium
            },
            color = if (selected) Primary else Ink
        )
    }
}