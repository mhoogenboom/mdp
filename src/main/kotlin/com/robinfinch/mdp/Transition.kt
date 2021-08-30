package com.robinfinch.mdp

import com.robinfinch.mdp.monitor.Monitor

data class Transition(
    val probability: Double,
    private val reward: Double,
    private val state: State
) {
    fun calculateUtility(given: Utilities, rewardDiscount: Double): Double {
        Monitor.calculations++

        val utilityOfState = given[state] ?: 0.0
        return reward + rewardDiscount * utilityOfState
    }

    fun simulatePolicy(given: Policy, maxDepth: Int, rewardDiscount: Double): Double {
        Monitor.calculations++

        return reward + rewardDiscount * state.simulatePolicy(given, maxDepth - 1, rewardDiscount)
    }
}