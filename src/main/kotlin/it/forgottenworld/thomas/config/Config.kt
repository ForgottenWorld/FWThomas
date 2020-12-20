package it.forgottenworld.thomas.config

import it.forgottenworld.thomas.utils.thomasPlugin

object Config {

    val maxSpeed by lazy { thomasPlugin.config.getDouble("maxSpeed", 0.4) }
}