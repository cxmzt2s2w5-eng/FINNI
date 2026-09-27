package com.myau.finni

data class Task(
    val id: Int,
    val section: String,
    val title: String,
    val type: String,
    val text: String,
    val reward: Int,
    val options: Map<String, String>,
    val feedback: Map<String, String>
)