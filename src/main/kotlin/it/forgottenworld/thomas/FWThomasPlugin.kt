package it.forgottenworld.thomas

import it.forgottenworld.thomas.command.ThomasCommand
import it.forgottenworld.thomas.listener.EventListener
import org.bukkit.plugin.java.JavaPlugin

class FWThomasPlugin: JavaPlugin() {

    companion object {
        lateinit var instance: FWThomasPlugin
        //lateinit var storage: FileConfiguration
        //lateinit var pluginDataFolder: File
    }

    override fun onEnable() {
        logger.info("Enabling FWThomas...")

        saveDefaultConfig()

        instance = this

        getCommand("thomas")?.setExecutor(ThomasCommand())

        server.pluginManager.registerEvents(EventListener(), this)
    }

    override fun onDisable() {
        logger.info("Disabling FWThomas...")
    }
}