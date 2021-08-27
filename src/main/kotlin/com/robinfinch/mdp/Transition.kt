package com.robinfinch.mdp

import com.robinfinch.mdp.monitor.Monitor

data class Transition(
    val state: State,
    val probability: Double,
    val reward: Double
) {
    fun calculateUtility(rewardDiscount: Double, given: Map<State, Double>): Double {
        Monitor.calculations++
        val utilityOfState = given[state] ?: 0.0
        return probability * (reward + rewardDiscount * utilityOfState)
    }
}