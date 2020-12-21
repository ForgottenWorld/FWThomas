package it.forgottenworld.thomas.config

import it.forgottenworld.thomas.utils.thomasPlugin

object Config {

    val maxSpeed by lazy { thomasPlugin.config.getDouble("maxSpeed", 0.4) }
    val serializationInterval by lazy { thomasPlugin.config.getInt("serializationInterval", 12000).toLong() }
}