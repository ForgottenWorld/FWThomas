package it.forgottenworld.thomas.config

import it.forgottenworld.thomas.FWThomasPlugin
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent

@KoinApiExtension
class Config(private val plugin: FWThomasPlugin) : KoinComponent {

    val maxSpeed by lazy { plugin.config.getDouble("maxSpeed", 0.4) }
    val serializationDelay by lazy { plugin.config.getInt("serializationDelay", 300000).toLong() }
    val serializationInterval by lazy { plugin.config.getInt("serializationInterval", 300000).toLong() }
    val cleanupDelay by lazy { plugin.config.getInt("cleanupDelay", 450000).toLong() }
    val cleanupInterval by lazy { plugin.config.getInt("cleanupInterval", 300000).toLong() }
}