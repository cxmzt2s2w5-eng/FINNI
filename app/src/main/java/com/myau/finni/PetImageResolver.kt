package com.myau.finni

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap


object PetImageResolver {


    fun getBodyFile(
        appearance: PetAppearance
    ): String {

        val animal = when (appearance.type) {

            "cat" -> "c"
            "dog" -> "d"
            "rabbit" -> "r"

            else -> "c"
        }


        val stage = when (appearance.stage) {

            1 -> "child"
            2 -> "teen"
            else -> "adult"
        }


        return "${animal}${stage}bud${appearance.color}.png"
    }



    fun getFaceFile(
        appearance: PetAppearance
    ): String {


        val prefix = when (appearance.type) {

            "dog" -> "d"

            // кот и кролик используют одинаковые лица
            else -> "c"
        }


        return when(appearance.mood){

            "happy" ->
                "${prefix}happyface.png"

            "sad" ->
                "${prefix}sadface.png"

            else ->
                "${prefix}neutralface.png"
        }

    }



    fun loadBitmap(
        context: Context,
        file:String
    ): ImageBitmap? {


        return try {


            val stream =
                context.assets.open(
                    "pets/$file"
                )


            BitmapFactory
                .decodeStream(stream)
                .asImageBitmap()


        } catch(e:Exception){

            null
        }

    }

}