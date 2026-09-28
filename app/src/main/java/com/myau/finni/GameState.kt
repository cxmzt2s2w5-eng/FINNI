package com.myau.finni

data class GameState(
    // --- питомец ---
    // --- питомец ---
    val petName: String = "Финни",
    val petType: String = "cat",
    val petColor: String = "blue",
    val petStage: Int = 1,
    val petMood: String = "neutral",
    val satiety: Int = 50,
    val care: Int = 50,
    val mood: Int = 50,


    // --- деньги ---
    val coins: Int = 100,
    val savings: Int = 0,

    // --- цель ---
    val goalTitle: String = "",
    val goalPrice: Int = 0,

    // --- день ---
    val day: Int = 1,
    val planNeed: Int = 0,
    val planWant: Int = 0,
    val planSave: Int = 0,
    val planConfirmed: Boolean = false,
    val factNeed: Int = 0,
    val factWant: Int = 0,
    val factSave: Int = 0,

    // --- прогресс ---
    val purchased: List<String> = emptyList(),
    val completedTasks: List<Int> = emptyList(),
    val stagePoints: Int = 0,
    val demoMode: Boolean = false
)