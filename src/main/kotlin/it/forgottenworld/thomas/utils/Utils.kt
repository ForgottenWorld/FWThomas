package it.forgottenworld.thomas.utils

import it.forgottenworld.thomas.FWThomasPlugin
import org.bukkit.block.Dispenser
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

val Player.targetDispenser get() = getTargetBlock(null, 5).state as? Dispenser

val thomasPlugin get() = JavaPlugin.getPlugin(FWThomasPlugin::class.java)