package com.robinfinch.mdp

import kotlin.random.Random

class Action(
    val name: String,
    private val transitions: List<Transition>
) {
    private val distribution = lazy {
        var probabilitySum = 0.0
        transitions.map { transition ->
            probabilitySum += transition.probability
            Pair(probabilitySum, transition)
        }
    }

    fun calculateExpectedUtility(given: Utilities, rewardDiscount: Double): Double =
        transitions.sumOf { transition ->
            transition.probability * transition.calculateUtility(given, rewardDiscount)
        }

    fun simulatePolicy(given: Policy, maxDepth: Int, rewardDiscount: Double): Double {
        val transition = randomTransition()
        return transition.simulatePolicy(given, maxDepth, rewardDiscount)
    }

    private fun randomTransition(): Transition {
        val p = Random.nextDouble()

        return distribution.value
            .first { (probability, _) -> p < probability }
            .let { (_, transition) -> transition }
    }
}