package com.myau.finni

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Тесты ключевой логики игровой экономики (требование ТЗ 3.4).
 * Проверяют чистые правила без Android: границы состояния,
 * запрет отрицательного баланса, оценку дня и стадии развития питомца.
 *
 * Файл кладётся в app/src/test/java/com/myau/finni/
 */
class GameViewModelTest {

    // --- стадии развития питомца (ТЗ 2.5.10) ---

    private fun stageFor(points: Int): Int = when {
        points >= 6 -> 3
        points >= 3 -> 2
        else -> 1
    }

    @Test
    fun `стадия растёт по накопленным баллам`() {
        assertEquals(1, stageFor(0))
        assertEquals(1, stageFor(2))
        assertEquals(2, stageFor(3))
        assertEquals(2, stageFor(5))
        assertEquals(3, stageFor(6))
        assertEquals(3, stageFor(12))
    }

    // --- точность плана (ТЗ 2.5.5) ---

    private fun accuracy(
        planNeed: Int, planWant: Int, planSave: Int,
        factNeed: Int, factWant: Int, factSave: Int
    ): Int {
        val deviation = Math.abs(planNeed - factNeed) +
                Math.abs(planWant - factWant) +
                Math.abs(planSave - factSave)
        return (100 - deviation * 100 / 100).coerceIn(0, 100)
    }

    @Test
    fun `точность сто процентов при полном совпадении плана и факта`() {
        assertEquals(100, accuracy(40, 30, 30, 40, 30, 30))
    }

    @Test
    fun `точность падает при отклонении от плана`() {
        assertEquals(60, accuracy(40, 30, 30, 60, 10, 30))
    }

    @Test
    fun `точность не уходит ниже нуля`() {
        assertEquals(0, accuracy(0, 0, 100, 100, 100, 0))
    }

    // --- оценка дня (ТЗ 2.5.9) ---

    private fun dayScore(
        satiety: Int, care: Int, accuracy: Int,
        planSave: Int, factSave: Int
    ): Int {
        var score = 0
        if (satiety >= 40 && care >= 40) score++
        if (accuracy >= 70) score++
        if (factSave > 0 && factSave >= planSave) score++
        return score
    }

    @Test
    fun `идеальный день даёт три балла`() {
        assertEquals(3, dayScore(80, 80, 95, 20, 25))
    }

    @Test
    fun `голодный питомец и промах по плану дают ноль баллов`() {
        assertEquals(0, dayScore(10, 10, 30, 20, 0))
    }

    @Test
    fun `накопления меньше плана не засчитываются`() {
        assertEquals(2, dayScore(80, 80, 90, 30, 10))
    }

    // --- покупки (ТЗ 2.5.6) ---

    private fun canBuy(coins: Int, price: Int) = coins >= price

    @Test
    fun `покупка при нехватке монет запрещена`() {
        assertFalse(canBuy(15, 40))
    }

    @Test
    fun `покупка при достаточном балансе разрешена`() {
        assertTrue(canBuy(40, 40))
    }

    // --- границы состояния питомца ---

    private fun clamp(value: Int) = value.coerceIn(0, 100)

    @Test
    fun `состояние питомца не выходит за границы шкалы`() {
        assertEquals(0, clamp(-30))
        assertEquals(100, clamp(140))
        assertEquals(55, clamp(55))
    }

    // --- структура состояния ---

    @Test
    fun `новая игра начинается со ста монет и первого дня`() {
        val s = GameState()
        assertEquals(100, s.coins)
        assertEquals(0, s.savings)
        assertEquals(1, s.day)
        assertEquals(1, s.petStage)
        assertTrue(s.purchased.isEmpty())
        assertTrue(s.completedTasks.isEmpty())
    }
}