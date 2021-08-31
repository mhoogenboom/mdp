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
            ?.evaluate(Double.NEGATIVE_INFINITY)

    fun calculateOptimalAction(given: Utilities, rewardDiscount: Double): EvaluatedAction? =
        actions
            .map { it.evaluate(it.calculateExpectedUtility(given, rewardDiscount)) }
            .maxByOrNull { it.utility }

    fun calculateUtilityForPolicy(policy: Policy, given: Utilities, rewardDiscount: Double): Double {

        val action = policy[this]?.action

        return if (action == null) {
            0.0
        } else {
            action.calculateExpectedUtility(given, rewardDiscount)
        }
    }

    fun simulatePolicy(policy: Policy, maxDepth: Int, rewardDiscount: Double): Double {

        val action = policy[this]?.action

        return if ((action == null) || (maxDepth == 0)) {
            0.0
        } else {
            action.simulatePolicy(given = policy, maxDepth, rewardDiscount)
        }
    }
}