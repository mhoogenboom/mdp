package com.robinfinch.mdp

import com.robinfinch.mdp.monitor.Monitor
import org.junit.Assert.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * The 4x3 world example is from Russell, S., Norvig, P., 2021. Artificial Intelligence: A Modern Approach.
 * 4rd ed. Pearson. p562-.
 */
class FourTimesThreeWorld {

    @Test
    fun moveToNearestGoal() {

        // cost of being is so high that the agent moves to the
        // nearest goal
        val squares = setUpThreeTimesFourSquares(-1.7)

        val mdp = Process(
            states = squares.flatMap(Array<State>::toSet).toSet(),
            rewardDiscount = 0.99
        )

        val policy = mdp.calculateOptimalPolicy();

        Monitor.print()

        assertEquals("RIGHT", policy[squares[0][0]]?.name)
        assertEquals("RIGHT", policy[squares[1][0]]?.name)
        assertEquals("RIGHT", policy[squares[2][1]]?.name)
    }

    @Test
    fun takeTheRiskyRoute() {

        // cost of moving is high, so the agent takes the risk
        // of reaching the negative goal
        val squares = setUpThreeTimesFourSquares(-0.6)

        val mdp = Process(
            states = squares.flatMap(Array<State>::toSet).toSet(),
            rewardDiscount = 0.99
        )

        val policy = mdp.calculateOptimalPolicy();

        Monitor.print()

        assertEquals("UP", policy[squares[0][0]]?.name)
        assertEquals("RIGHT", policy[squares[1][0]]?.name)

        val expectedUtilityRiskyRoute = mdp.estimateExpectedUtility(squares[1][0], policy)

        val lessOptimalPolicy = mapOf(
            squares[1][0] to squares[1][0].action("LEFT"),
            squares[0][0] to squares[0][0].action("UP"),
            squares[0][1] to squares[0][1].action("UP"),
            squares[0][2] to squares[0][2].action("RIGHT"),
            squares[1][2] to squares[1][2].action("RIGHT"),
            squares[2][2] to squares[2][2].action("RIGHT"),
            squares[2][1] to squares[2][1].action("UP")
        )

        val expectedUtilitySafeRoute = mdp.estimateExpectedUtility(squares[1][0], lessOptimalPolicy)

        assertTrue(expectedUtilityRiskyRoute > expectedUtilitySafeRoute)
    }

    @Test
    fun takeTheSafeRoute() {

        // cost of moving is low, so the agent takes the safe route
        // around the water
        val squares = setUpThreeTimesFourSquares(-0.01)

        val mdp = Process(
            states = squares.flatMap(Array<State>::toSet).toSet(),
            rewardDiscount = 0.99
        )

        val policy = mdp.calculateOptimalPolicy();

        Monitor.print()

        assertEquals("UP", policy[squares[0][0]]?.name)
        assertEquals("LEFT", policy[squares[1][0]]?.name)

        val expectedUtilitySafeRoute = mdp.estimateExpectedUtility(squares[1][0], policy)

        val lessOptimalPolicy = mapOf(
            squares[1][0] to squares[1][0].action("RIGHT"),
            squares[2][0] to squares[2][0].action("UP"),
            squares[3][0] to squares[3][0].action("LEFT"),
            squares[2][1] to squares[2][1].action("UP"),
            squares[2][2] to squares[2][2].action("RIGHT")
        )

        val expectedUtilityRiskyRoute = mdp.estimateExpectedUtility(squares[1][0], lessOptimalPolicy)

        assertTrue(expectedUtilitySafeRoute > expectedUtilityRiskyRoute)
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
                Transition(squares[x][y], probability, reward(x, y))
            } else {
                Transition(squares[x][y + 1], probability, reward(x, y + 1))
            }

        fun right(x: Int, y: Int, probability: Double): Transition =
            if ((x + 1 == 4) || isWater(x + 1, y)) {
                Transition(squares[x][y], probability, reward(x, y))
            } else {
                Transition(squares[x + 1][y], probability, reward(x + 1, y))
            }

        fun down(x: Int, y: Int, probability: Double): Transition =
            if ((y - 1 == -1) || isWater(x, y - 1)) {
                Transition(squares[x][y], probability, reward(x, y))
            } else {
                Transition(squares[x][y - 1], probability, reward(x, y - 1))
            }

        fun left(x: Int, y: Int, probability: Double): Transition =
            if ((x - 1 == -1) || isWater(x - 1, y)) {
                Transition(squares[x][y], probability, reward(x, y))
            } else {
                Transition(squares[x - 1][y], probability, reward(x - 1, y))
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
}