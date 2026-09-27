package com.myau.finni


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class GameViewModel(private val progress: ProgressManager) : ViewModel() {

    var state by mutableStateOf(progress.load())
        private set

    val hasProfile: Boolean get() = progress.hasProfile()

    // ---------- питомец и цель ----------

    fun setPet(name: String, color: String) {
        update(state.copy(petName = name, petColor = color))
    }

    fun setGoal(title: String, price: Int) {
        update(state.copy(goalTitle = title, goalPrice = price))
    }

    // ---------- план бюджета (ТЗ 2.5.5) ----------

    fun confirmPlan(need: Int, want: Int, save: Int) {
        if (need + want + save > state.coins) return
        update(
            state.copy(
                planNeed = need,
                planWant = want,
                planSave = save,
                planConfirmed = true
            )
        )
    }

    // ---------- покупки (ТЗ 2.5.6) ----------

    fun canBuy(price: Int): Boolean = state.coins >= price

    fun buy(product: Product) {
        if (!canBuy(product.price)) return

        update(
            state.copy(
                coins = state.coins - product.price,
                purchased = state.purchased + product.id,
                satiety = clamp(state.satiety + if (product.required) 25 else 0),
                care = clamp(state.care + if (product.id == "care") 30 else 0),
                mood = clamp(state.mood + if (product.required) 3 else 12),
                factNeed = if (product.required) state.factNeed + product.price else state.factNeed,
                factWant = if (!product.required) state.factWant + product.price else state.factWant
            )
        )
    }

    // ---------- накопления (ТЗ 2.5.7) ----------

    fun addToSavings(amount: Int) {
        if (amount <= 0 || amount > state.coins) return
        update(
            state.copy(
                coins = state.coins - amount,
                savings = state.savings + amount,
                factSave = state.factSave + amount
            )
        )
    }

    fun withdrawFromSavings(amount: Int) {
        if (amount <= 0 || amount > state.savings) return
        update(
            state.copy(
                coins = state.coins + amount,
                savings = state.savings - amount,
                factSave = (state.factSave - amount).coerceAtLeast(0)
            )
        )
    }

    // ---------- задания ----------

    fun isTaskDone(id: Int): Boolean = state.completedTasks.contains(id)

    fun completeTask(id: Int, reward: Int) {
        if (isTaskDone(id)) return
        update(
            state.copy(
                coins = state.coins + reward,
                completedTasks = state.completedTasks + id
            )
        )
    }

    // ---------- конец дня (ТЗ 2.5.9, 2.5.10) ----------

    fun dayScore(): Int {
        var score = 0
        val fedAndCared = state.satiety >= 40 && state.care >= 40
        if (fedAndCared) score++
        if (planAccuracy() >= 70) score++
        if (state.factSave > 0 && state.factSave >= state.planSave) score++
        return score
    }

    fun planAccuracy(): Int {
        val income = 100
        val deviation = Math.abs(state.planNeed - state.factNeed) +
                Math.abs(state.planWant - state.factWant) +
                Math.abs(state.planSave - state.factSave)
        return (100 - deviation * 100 / income).coerceIn(0, 100)
    }

    fun endDay() {
        val score = dayScore()
        val points = state.stagePoints + score

        update(
            state.copy(
                day = state.day + 1,
                stagePoints = points,
                petStage = stageFor(points),
                coins = state.coins + 100,
                satiety = clamp(state.satiety - 20),
                care = clamp(state.care - 15),
                mood = clamp(state.mood + if (score >= 2) 8 else -6),
                planNeed = 0,
                planWant = 0,
                planSave = 0,
                planConfirmed = false,
                factNeed = 0,
                factWant = 0,
                factSave = 0,
                purchased = emptyList()
            )
        )
    }

    // ---------- сброс (ТЗ 2.5.12, 2.5.13) ----------

    fun reset() {
        progress.clear()
        state = GameState()
    }

    fun setDemoMode(on: Boolean) {
        update(state.copy(demoMode = on))
    }

    // ---------- внутреннее ----------

    private fun update(newState: GameState) {
        state = newState
        progress.save(newState)
    }

    private fun clamp(value: Int): Int = value.coerceIn(0, 100)

    private fun stageFor(points: Int): Int = when {
        points >= 6 -> 3
        points >= 3 -> 2
        else -> 1
    }
}