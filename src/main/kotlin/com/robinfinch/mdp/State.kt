package com.robinfinch.mdp

data class State(
    val name: String
) {
    private val actions = mutableSetOf<Action>()

    fun add(action: Action) {
        actions.add(action)
    }

    fun action(name: String) =
        actions.find { it.name == name }

    fun calculateOptimalAction(rewardDiscount: Double, given: Map<State, Double>): Action? =
        actions.maxByOrNull { it.calculateUtility(rewardDiscount, given) }

    fun calculateUtility(rewardDiscount: Double, given: Map<State, Double>): Double? =
        actions.maxOfOrNull { it.calculateUtility(rewardDiscount, given) }
}