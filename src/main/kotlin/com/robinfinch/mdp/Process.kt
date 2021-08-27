package com.robinfinch.mdp

import com.robinfinch.mdp.monitor.Monitor
import kotlin.math.abs

class Process(
    private val states: Set<State>,
    private val rewardDiscount: Double
) {
    fun calculateOptimalPolicy(): Map<State, Action?> {

        val utilities = calculateUtilities(1e-5)

        return states.associateWith { it.calculateOptimalAction(rewardDiscount, given = utilities) }
    }

    private fun calculateUtilities(maxError: Double): Map<State, Double> {

        val maxDifference = maxError * (1.0 - rewardDiscount) / rewardDiscount

        var utilities1: Map<State, Double>
        var utilities2 = states.associateWith { 0.0 }

        do {
            Monitor.iterations++

            utilities1 = utilities2
            utilities2 = updateUtilities(utilities1)

        } while (difference(utilities1, utilities2) > maxDifference)

        return utilities2
    }

    private fun updateUtilities(utilities: Map<State, Double>): Map<State, Double> =
        states.associateWith { it.calculateUtility(rewardDiscount, given = utilities) ?: 0.0 }

    private fun difference(utilities1: Map<State, Double>, utilities2: Map<State, Double>): Double =
        states.maxOf { state ->
            val utility1OfState = utilities1[state] ?: 0.0
            val utility2OfState = utilities2[state] ?: 0.0
            abs(utility1OfState - utility2OfState)
        }

    fun estimateExpectedUtility(start: State, policy: Map<State, Action?>): Double =
        (0..1000).map { applyPolicy(start, policy) }.average()

    private fun applyPolicy(start: State, policy: Map<State, Action?>, maxDepth: Int = 40): Double {

        val action = policy[start]

        return if ((action == null) || (maxDepth == 0)) {
            0.0
        } else {
            val transition = action.randomTransition()
            transition.reward + rewardDiscount * applyPolicy(transition.state, policy, maxDepth - 1)
        }
    }
}