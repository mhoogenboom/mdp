package com.robinfinch.mdp

import com.robinfinch.mdp.monitor.Monitor
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * The 4x3 world example is from Russell, S., Norvig, P., 2021. Artificial Intelligence: A Modern Approach.
 * 4rd ed. Pearson. p562-.
 */
class FourTimesThreeWorld {

    @Test
    fun moveToNearestGoalWithValueIteration() {

        // cost of being is so high that the agent moves to the
        // nearest goal
        val squares = setUpThreeTimesFourSquares(-1.7)

        val mdp = Process(
            states = squares.flatMap(Array<State>::toSet).toSet(),
            rewardDiscount = 0.99
        )

        Monitor.reset()

        val policy = mdp.calculateOptimalPolicy()

        Monitor.print()

        assertEquals("RIGHT", policy[squares[1][0]]?.action?.name)
        assertEquals("RIGHT", policy[squares[2][0]]?.action?.name)
        assertEquals("UP", policy[squares[3][0]]?.action?.name)

        val utility = mdp.simulatePolicy(squares[1][0], policy)

        assertEquals(-5.6, utility, 0.2)
    }

    @Test
    fun moveToNearestGoalWithPolicyIteration() {

        // cost of being is so high that the agent moves to the
        // nearest goal
        val squares = setUpThreeTimesFourSquares(-1.7)

        val mdp = Process(
            states = squares.flatMap(Array<State>::toSet).toSet(),
            rewardDiscount = 0.99
        )

        Monitor.reset()

        val policy = mdp.calculateOptimalPolicy2()

        Monitor.print()

        assertEquals("RIGHT", policy[squares[1][0]]?.action?.name)
        assertEquals("RIGHT", policy[squares[2][0]]?.action?.name)
        assertEquals("UP", policy[squares[3][0]]?.action?.name)

        val utility = mdp.simulatePolicy(squares[1][0], policy)

        assertEquals(-5.6, utility, 0.2)
    }

    @Test
    fun riskyRouteIsBetterThanSafeRoute() {

        // cost of moving is high, so the agent takes the risk
        // of reaching the negative goal
        val squares = setUpThreeTimesFourSquares(-0.6)

        val mdp = Process(
            states = squares.flatMap(Array<State>::toSet).toSet(),
            rewardDiscount = 0.99
        )

        val policy = mapOf(
            squares[1][0] act "LEFT",
            squares[0][0] act "UP",
            squares[0][1] act "UP",
            squares[0][2] act "RIGHT",
            squares[1][2] act "RIGHT",
            squares[2][2] act "RIGHT",
            squares[2][1] act "UP"
        )

        val utility = mdp.simulatePolicy(squares[1][0], policy)

        assertEquals(-3.1, utility, 0.2)
    }

    @Test
    fun takeTheRiskyRouteWithValueIteration() {

        // cost of moving is high, so the agent takes the risk
        // of reaching the negative goal
        val squares = setUpThreeTimesFourSquares(-0.6)

        val mdp = Process(
            states = squares.flatMap(Array<State>::toSet).toSet(),
            rewardDiscount = 0.99
        )

        Monitor.reset()

        val policy = mdp.calculateOptimalPolicy()

        Monitor.print()

        assertEquals("RIGHT", policy[squares[1][0]]?.action?.name)
        assertEquals("UP", policy[squares[2][0]]?.action?.name)
        assertEquals("UP", policy[squares[2][1]]?.action?.name)
        assertEquals("RIGHT", policy[squares[2][2]]?.action?.name)

        val utility = mdp.simulatePolicy(squares[1][0], policy)

        assertEquals(-1.6, utility, 0.2)
    }

    @Test
    fun takeTheRiskyRouteWithPolicyIteration() {

        // cost of moving is high, so the agent takes the risk
        // of reaching the negative goal
        val squares = setUpThreeTimesFourSquares(-0.6)

        val mdp = Process(
            states = squares.flatMap(Array<State>::toSet).toSet(),
            rewardDiscount = 0.99
        )

        Monitor.reset()

        val policy = mdp.calculateOptimalPolicy2()

        Monitor.print()

        assertEquals("RIGHT", policy[squares[1][0]]?.action?.name)
        assertEquals("UP", policy[squares[2][0]]?.action?.name)
        assertEquals("UP", policy[squares[2][1]]?.action?.name)
        assertEquals("RIGHT", policy[squares[2][2]]?.action?.name)

        val utility = mdp.simulatePolicy(squares[1][0], policy)

        assertEquals(-1.6, utility, 0.2)
    }

    @Test
    fun safeRouteIsBetterThanRiskyRoute() {

        // cost of moving is low, so the agent takes the safe route
        // around the water
        val squares = setUpThreeTimesFourSquares(-0.01)

        val mdp = Process(
            states = squares.flatMap(Array<State>::toSet).toSet(),
            rewardDiscount = 0.99
        )

        val policy = mapOf(
            squares[1][0] act "RIGHT",
            squares[2][0] act "UP",
            squares[3][0] act "LEFT",
            squares[2][1] act "UP",
            squares[2][2] act "RIGHT"
        )

        val utility = mdp.simulatePolicy(squares[1][0], policy)

        assertEquals(0.6, utility, 0.2)
    }

    @Test
    fun takeTheSafeRouteWithValueIteration() {

        // cost of moving is low, so the agent takes the safe route
        // around the water
        val squares = setUpThreeTimesFourSquares(-0.01)

        val mdp = Process(
            states = squares.flatMap(Array<State>::toSet).toSet(),
            rewardDiscount = 0.99
        )

        Monitor.reset()

        val policy = mdp.calculateOptimalPolicy()

        Monitor.print()

        assertEquals("LEFT", policy[squares[1][0]]?.action?.name)
        assertEquals("UP", policy[squares[0][0]]?.action?.name)
        assertEquals("UP", policy[squares[0][1]]?.action?.name)
        assertEquals("RIGHT", policy[squares[0][2]]?.action?.name)
        assertEquals("RIGHT", policy[squares[1][2]]?.action?.name)
        assertEquals("RIGHT", policy[squares[2][2]]?.action?.name)

        val utility = mdp.simulatePolicy(squares[1][0], policy)

        assertEquals(0.9, utility, 0.2)
    }

    @Test
    fun takeTheSafeRouteWithPolicyIteration() {

        // cost of moving is low, so the agent takes the safe route
        // around the water
        val squares = setUpThreeTimesFourSquares(-0.01)

        val mdp = Process(
            states = squares.flatMap(Array<State>::toSet).toSet(),
            rewardDiscount = 0.99
        )

        Monitor.reset()

        val policy = mdp.calculateOptimalPolicy2()

        Monitor.print()

        assertEquals("LEFT", policy[squares[1][0]]?.action?.name)
        assertEquals("UP", policy[squares[0][0]]?.action?.name)
        assertEquals("UP", policy[squares[0][1]]?.action?.name)
        assertEquals("RIGHT", policy[squares[0][2]]?.action?.name)
        assertEquals("RIGHT", policy[squares[1][2]]?.action?.name)
        assertEquals("RIGHT", policy[squares[2][2]]?.action?.name)

        val utility = mdp.simulatePolicy(squares[1][0], policy)

        assertEquals(0.9, utility, 0.2)
    }

    private fun setUpThreeTimesFourSquares(rewardToMove: Double): Array<Array<State>> {

        val squares = Array(4) { x -> Array(3) { y -> State("Square $x,$y") } }

        fun isWater(x: Int, y: Int): Boolean =
            (x == 1) && (y == 1)

        fun isPositiveGoal(x: Int, y: Int): Boolean =
            (x == 3) && (y == 2)

        fun isNegativeGoal(x: Int, y: Int): Boolean =
            (x == 3) && (y == 1)

        fun reward(x: Int, y: Int): Double =
            when {
                isPositiveGoal(x, y) -> 1.0
                isNegativeGoal(x, y) -> -1.0
                else -> rewardToMove
            }

        fun up(x: Int, y: Int, probability: Double): Transition =
            if ((y + 1 == 3) || isWater(x, y + 1)) {
                Transition(probability, reward(x, y), squares[x][y])
            } else {
                Transition(probability, reward(x, y + 1), squares[x][y + 1])
            }

        fun right(x: Int, y: Int, probability: Double): Transition =
            if ((x + 1 == 4) || isWater(x + 1, y)) {
                Transition(probability, reward(x, y), squares[x][y])
            } else {
                Transition(probability, reward(x + 1, y), squares[x + 1][y])
            }

        fun down(x: Int, y: Int, probability: Double): Transition =
            if ((y - 1 == -1) || isWater(x, y - 1)) {
                Transition(probability, reward(x, y), squares[x][y])
            } else {
                Transition(probability, reward(x, y - 1), squares[x][y - 1])
            }

        fun left(x: Int, y: Int, probability: Double): Transition =
            if ((x - 1 == -1) || isWater(x - 1, y)) {
                Transition(probability, reward(x, y), squares[x][y])
            } else {
                Transition(probability, reward(x - 1, y), squares[x - 1][y])
            }

        fun up(x: Int, y: Int): Action =
            Action(
                "UP",
                listOf(
                    up(x, y, 0.8),
                    left(x, y, 0.1),
                    right(x, y, 0.1)
                )
            )

        fun right(x: Int, y: Int): Action =
            Action(
                "RIGHT",
                listOf(
                    right(x, y, 0.8),
                    up(x, y, 0.1),
                    down(x, y, 0.1)
                )
            )

        fun down(x: Int, y: Int): Action =
            Action(
                "DOWN",
                listOf(
                    down(x, y, 0.8),
                    right(x, y, 0.1),
                    left(x, y, 0.1)
                )
            )

        fun left(x: Int, y: Int): Action =
            Action(
                "LEFT",
                listOf(
                    left(x, y, 0.8),
                    down(x, y, 0.1),
                    up(x, y, 0.1)
                )
            )

        squares.forEachIndexed { x, col ->
            col.forEachIndexed { y, square ->
                if (isWater(x, y) || isPositiveGoal(x, y) || isNegativeGoal(x, y)) {
                    // no actions
                } else {
                    square.add(up(x, y))
                    square.add(right(x, y))
                    square.add(down(x, y))
                    square.add(left(x, y))
                }
            }
        }

        return squares
    }

    private infix fun State.act(name: String) =
        this to this.action(name)?.evaluate(0.0)
}