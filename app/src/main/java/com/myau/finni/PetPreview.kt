package com.myau.finni

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext


@Composable
fun PetPreview(
    appearance: PetAppearance,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current


    val body = remember(appearance) {

        PetImageResolver.loadBitmap(
            context,
            PetImageResolver.getBodyFile(
                appearance
            )
        )
    }


    val face = remember(appearance) {

        PetImageResolver.loadBitmap(
            context,
            PetImageResolver.getFaceFile(
                appearance
            )
        )
    }



    Box(
        modifier = modifier
            .size(260.dp),
        contentAlignment = Alignment.Center
    ) {


        body?.let {

            Image(
                bitmap = it,
                contentDescription = null,
                modifier = Modifier
                    .size(230.dp)
                    .offset(
                        y = if (appearance.type == "cat")
                            37.dp
                        else
                            0.dp
                    )
            )
        }



        face?.let {

            Image(
                bitmap = it,
                contentDescription = null,
                modifier = Modifier
                    .size(230.dp)
                    .offset(
                        y = if (appearance.type == "cat")
                            0.dp
                        else
                            0.dp
                    )
            )
        }
    }
}