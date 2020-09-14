package it.forgottenworld.thomas

import it.forgottenworld.thomas.command.ThomasCommand
import it.forgottenworld.thomas.listener.EventListener
import it.forgottenworld.thomas.state.RoadState
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class FWThomasPlugin: JavaPlugin() {

    companion object {
        lateinit var instance: FWThomasPlugin
        lateinit var pluginDataFolder: File
        lateinit var pluginConfig: FileConfiguration
    }

    override fun onEnable() {
        logger.info("Enabling FWThomas...")

        saveDefaultConfig()
        pluginDataFolder = dataFolder
        pluginConfig = config

        instance = this

        getCommand("thomas")?.setExecutor(ThomasCommand())

        server.pluginManager.registerEvents(EventListener(), this)

        RoadState.fromConfig()
    }

    override fun onDisable() {
        logger.info("Disabling FWThomas...")
    }
}