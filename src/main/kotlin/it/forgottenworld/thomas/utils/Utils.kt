package it.forgottenworld.thomas.utils

import it.forgottenworld.thomas.FWThomasPlugin
import org.bukkit.Material
import org.bukkit.block.Dispenser
import org.bukkit.entity.EntityType
import org.bukkit.entity.Minecart
import org.bukkit.entity.Player
import org.bukkit.entity.minecart.*
import org.bukkit.plugin.java.JavaPlugin

private val minecartTypes = setOf(
    EntityType.MINECART,
    EntityType.MINECART_CHEST,
    EntityType.MINECART_COMMAND,
    EntityType.MINECART_FURNACE,
    EntityType.MINECART_HOPPER,
    EntityType.MINECART_MOB_SPAWNER,
    EntityType.MINECART_TNT
)

private val minecartMaterialTypes = setOf(
    Material.MINECART,
    Material.CHEST_MINECART,
    Material.COMMAND_BLOCK_MINECART,
    Material.FURNACE_MINECART,
    Material.HOPPER_MINECART,
    Material.TNT_MINECART
)

private val ignoredMaterials by lazy { setOf(Material.AIR) }

val Player.targetDispenser get() = getTargetBlock(ignoredMaterials, 5).state as? Dispenser

val EntityType.isMinecart get() = minecartTypes.contains(this)

val Material.isMinecart get() = minecartMaterialTypes.contains(this)

val Material.minecartEntityClass get() = when (this) {
    Material.MINECART -> Minecart::class.java
    Material.CHEST_MINECART -> StorageMinecart::class.java
    Material.COMMAND_BLOCK_MINECART -> CommandMinecart::class.java
    Material.FURNACE_MINECART -> PoweredMinecart::class.java
    Material.TNT_MINECART -> ExplosiveMinecart::class.java
    Material.HOPPER_MINECART -> HopperMinecart::class.java
    else -> null
}

val thomasPlugin get() = JavaPlugin.getPlugin(FWThomasPlugin::class.java)