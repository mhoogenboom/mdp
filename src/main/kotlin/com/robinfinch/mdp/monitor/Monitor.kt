package com.robinfinch.mdp.monitor

object Monitor {
    var iterations: Int = 0
    var calculations: Int = 0

    fun reset() {
        iterations = 0
        calculations = 0
    }

    fun print() {
        println("$iterations iterations, $calculations calculations")
    }
}