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

    fun randomAction() =
        actions.randomOrNull() // uniformly distributed

    fun calculateOptimalAction(given: Utilities, rewardDiscount: Double): Action? =
        actions.maxByOrNull { it.calculateExpectedUtility(given, rewardDiscount) }

    fun calculateUtilityForOptimalAction(given: Utilities, rewardDiscount: Double): Double? =
        actions.maxOfOrNull { it.calculateExpectedUtility(given, rewardDiscount) }

    fun calculateUtilityForPolicy(policy: Policy, given: Utilities, rewardDiscount: Double): Double {

        val action = policy[this]

        return if (action == null) {
            0.0
        } else {
            action.calculateExpectedUtility(given, rewardDiscount)
        }
    }

    fun simulatePolicy(policy: Policy, maxDepth: Int, rewardDiscount: Double): Double {

        val action = policy[this]

        return if ((action == null) || (maxDepth == 0)) {
            0.0
        } else {
            action.simulatePolicy(given = policy, maxDepth, rewardDiscount)
        }
    }
}