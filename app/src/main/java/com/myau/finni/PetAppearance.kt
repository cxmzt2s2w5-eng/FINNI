package com.myau.finni


data class PetAppearance(

    // cat / dog / rabbit
    val type: String = "cat",

    // blue / pink / brown / white
    val color: String = "blue",

    // 1 / 2 / 3
    val stage: Int = 1,

    // happy / neutral / sad
    val mood: String = "neutral"
)