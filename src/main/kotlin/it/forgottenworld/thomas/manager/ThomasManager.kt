package it.forgottenworld.thomas.manager

import it.forgottenworld.thomas.config.Config
import it.forgottenworld.thomas.utils.thomasPlugin
import org.bukkit.NamespacedKey
import org.bukkit.block.Dispenser
import org.bukkit.entity.Minecart
import org.bukkit.persistence.PersistentDataType
import org.bukkit.util.Vector
import org.bukkit.block.data.type.Dispenser as DispenserBlockData

object ThomasManager {

    private const val NSKEY_NAME = "FW_THOMAS"

    val Dispenser.isThomasfied get() = persistentDataContainer.has(NamespacedKey(thomasPlugin, NSKEY_NAME), PersistentDataType.INTEGER)

    fun Dispenser.thomasfy() = persistentDataContainer.set(NamespacedKey(thomasPlugin, NSKEY_NAME), PersistentDataType.INTEGER, 1)

    fun Dispenser.unthomasfy() = persistentDataContainer.remove(NamespacedKey(thomasPlugin, NSKEY_NAME))

    fun Dispenser.spawnThomasCart(vel: Vector) {
        val blockData = blockData as? DispenserBlockData ?: return
        val outputLocation = block.getRelative(blockData.facing)
        outputLocation.location
            .let { it.world?.spawn(it, Minecart::class.java) }
            ?.run {
                velocity = vel
                maxSpeed = Config.maxSpeed
            }
    }
}