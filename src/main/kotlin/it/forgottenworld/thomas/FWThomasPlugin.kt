package it.forgottenworld.thomas

import it.forgottenworld.thomas.command.ThomasCommand
import it.forgottenworld.thomas.command.UnthomasCommand
import it.forgottenworld.thomas.listener.EventListener
import org.bukkit.plugin.java.JavaPlugin

class FWThomasPlugin: JavaPlugin() {

    override fun onEnable() {
        saveDefaultConfig()

        getCommand("thomas")?.setExecutor(ThomasCommand())
        getCommand("unthomas")?.setExecutor(UnthomasCommand())

        server.pluginManager.registerEvents(EventListener(), this)
    }

    override fun onDisable() {
        logger.info("Disabling FWThomas...")
    }
}