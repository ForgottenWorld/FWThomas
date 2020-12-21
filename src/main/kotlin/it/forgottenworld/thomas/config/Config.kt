package it.forgottenworld.thomas.config

import it.forgottenworld.thomas.utils.thomasPlugin

object Config {

    val maxSpeed by lazy { thomasPlugin.config.getDouble("maxSpeed", 0.4) }
    val serializationDelay by lazy { thomasPlugin.config.getInt("serializationDelay", 300000).toLong() }
    val serializationInterval by lazy { thomasPlugin.config.getInt("serializationInterval", 300000).toLong() }
    val cleanupDelay by lazy { thomasPlugin.config.getInt("cleanupDelay", 450000).toLong() }
    val cleanupInterval by lazy { thomasPlugin.config.getInt("cleanupInterval", 300000).toLong() }
}