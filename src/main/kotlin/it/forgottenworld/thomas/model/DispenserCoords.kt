package it.forgottenworld.thomas.model

import org.bukkit.Location
import org.bukkit.configuration.ConfigurationSection
import java.util.*

data class DispenserCoords(
    val x: Int,
    val y: Int,
    val z: Int,
    val worldId: UUID
) {

    fun toYaml(config: ConfigurationSection) = config.run {
        set("x", x)
        set("y", y)
        set("z", z)
        set("uid", worldId.toString())
    }

    companion object {

        fun Location.getDispenserCoords() = world?.uid?.let { DispenserCoords(blockX, blockY, blockZ, it) }

        fun ConfigurationSection.getDispenserCoords(key: String) = getConfigurationSection(key).run {
            DispenserCoords(
                getInt("x"),
                getInt("y"),
                getInt("z"),
                UUID.fromString(getString("uid"))
            )
        }
    }
}