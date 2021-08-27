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

    fun calculateUtility(rewardDiscount: Double, given: Map<State, Double>): Double =
        transitions.sumOf { it.calculateUtility(rewardDiscount, given) }

    fun randomTransition(): Transition {
        val p = Random.nextDouble()

        return distribution.value
            .first { (probability, _) -> p < probability }
            .let { (_, transition) -> transition }
    }
}