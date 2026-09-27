package com.myau.finni

import android.content.Context

class ProgressManager(context: Context) {

    private val prefs = context.getSharedPreferences(
        "finni_progress",
        Context.MODE_PRIVATE
    )

    fun save(state: GameState) {
        prefs.edit()
            .putString("petName", state.petName)
            .putString("petColor", state.petColor)
            .putInt("petStage", state.petStage)
            .putInt("satiety", state.satiety)
            .putInt("care", state.care)
            .putInt("mood", state.mood)
            .putInt("coins", state.coins)
            .putInt("savings", state.savings)
            .putString("goalTitle", state.goalTitle)
            .putInt("goalPrice", state.goalPrice)
            .putInt("day", state.day)
            .putInt("planNeed", state.planNeed)
            .putInt("planWant", state.planWant)
            .putInt("planSave", state.planSave)
            .putBoolean("planConfirmed", state.planConfirmed)
            .putInt("factNeed", state.factNeed)
            .putInt("factWant", state.factWant)
            .putInt("factSave", state.factSave)
            .putString("purchased", state.purchased.joinToString(","))
            .putString("completedTasks", state.completedTasks.joinToString(","))
            .putInt("stagePoints", state.stagePoints)
            .putBoolean("demoMode", state.demoMode)
            .putBoolean("hasProfile", true)
            .apply()
    }

    fun load(): GameState {
        val empty = GameState()
        if (!prefs.getBoolean("hasProfile", false)) return empty

        return GameState(
            petName = prefs.getString("petName", empty.petName) ?: empty.petName,
            petColor = prefs.getString("petColor", empty.petColor) ?: empty.petColor,
            petStage = prefs.getInt("petStage", empty.petStage),
            satiety = prefs.getInt("satiety", empty.satiety),
            care = prefs.getInt("care", empty.care),
            mood = prefs.getInt("mood", empty.mood),
            coins = prefs.getInt("coins", empty.coins),
            savings = prefs.getInt("savings", empty.savings),
            goalTitle = prefs.getString("goalTitle", "") ?: "",
            goalPrice = prefs.getInt("goalPrice", 0),
            day = prefs.getInt("day", 1),
            planNeed = prefs.getInt("planNeed", 0),
            planWant = prefs.getInt("planWant", 0),
            planSave = prefs.getInt("planSave", 0),
            planConfirmed = prefs.getBoolean("planConfirmed", false),
            factNeed = prefs.getInt("factNeed", 0),
            factWant = prefs.getInt("factWant", 0),
            factSave = prefs.getInt("factSave", 0),
            purchased = readStringList("purchased"),
            completedTasks = readIntList("completedTasks"),
            stagePoints = prefs.getInt("stagePoints", 0),
            demoMode = prefs.getBoolean("demoMode", false)
        )
    }

    fun hasProfile(): Boolean = prefs.getBoolean("hasProfile", false)

    fun clear() {
        prefs.edit().clear().apply()
    }

    private fun readStringList(key: String): List<String> {
        val raw = prefs.getString(key, "") ?: ""
        if (raw.isBlank()) return emptyList()
        return raw.split(",")
    }

    private fun readIntList(key: String): List<Int> {
        val raw = prefs.getString(key, "") ?: ""
        if (raw.isBlank()) return emptyList()
        return raw.split(",").mapNotNull { it.toIntOrNull() }
    }
}