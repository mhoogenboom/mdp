package com.robinfinch.mdp

import com.robinfinch.mdp.monitor.Monitor
import kotlin.math.abs

class Process(
    private val states: Set<State>,
    private val rewardDiscount: Double
) {
    fun calculateOptimalPolicy(): Policy {

        val utilities = calculateUtilities(1e-5)

        return states.associateWith { it.calculateOptimalAction(given = utilities, rewardDiscount) }
    }

    private fun calculateUtilities(maxError: Double): Map<State, Double> {

        val maxDifference = maxError * (1.0 - rewardDiscount) / rewardDiscount

        var utilities1: Map<State, Double>
        var utilities2 = states.associateWith { 0.0 }

        do {
            Monitor.iterations++

            utilities1 = utilities2
            utilities2 = states.associateWith { it.calculateUtilityForOptimalAction(given = utilities1, rewardDiscount) ?: 0.0 }

        } while (difference(utilities1, utilities2) > maxDifference)

        return utilities2
    }

    private fun difference(utilities1: Utilities, utilities2: Utilities): Double =
        states.maxOf { state ->
            val utility1OfState = utilities1[state] ?: 0.0
            val utility2OfState = utilities2[state] ?: 0.0
            abs(utility1OfState - utility2OfState)
        }

    fun calculateOptimalPolicy2(): Policy {

        var policy: Policy = states.associateWith { it.randomAction() }

        var utilities = states.associateWith { 0.0 }

        var changed: Boolean
        do {
            Monitor.iterations++

            utilities = states.associateWith { it.calculateUtilityForPolicy(policy, given = utilities, rewardDiscount) }

            changed = false

            policy = policy.mapValues { (state, action) ->
                val optimalAction = state.calculateOptimalAction(given = utilities, rewardDiscount)

                changed = changed || (action != optimalAction)

                optimalAction
            }
        } while (changed)

        return policy
    }

    fun simulatePolicy(start: State, policy: Policy): Double =
        (0..1000).map { start.simulatePolicy(policy, maxDepth = 40, rewardDiscount) }.average()
}

typealias Utilities = Map<State, Double>
typealias Policy = Map<State, Action?>