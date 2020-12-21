package it.forgottenworld.thomas.listener

import it.forgottenworld.thomas.manager.ThomasManager.isThomasfied
import it.forgottenworld.thomas.manager.ThomasManager.spawnThomasCart
import org.bukkit.Material
import org.bukkit.block.Dispenser
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockDispenseEvent


class EventListener: Listener {

    @EventHandler
    fun onDispense(event: BlockDispenseEvent) {
        val dispenser = event.block.state as? Dispenser ?: return
        if (!dispenser.isThomasfied || event.item.type != Material.MINECART) return
        event.isCancelled = true
        dispenser.spawnThomasCart()
        dispenser.inventory.removeItem(event.item)
    }
}