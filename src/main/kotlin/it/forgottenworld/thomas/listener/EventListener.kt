package it.forgottenworld.thomas.listener

import it.forgottenworld.thomas.manager.ThomasManager.isThomasfied
import it.forgottenworld.thomas.manager.ThomasManager.spawnThomasCart
import it.forgottenworld.thomas.utils.isMinecart
import it.forgottenworld.thomas.utils.launch
import it.forgottenworld.thomas.utils.minecartEntityClass
import kotlinx.coroutines.delay
import org.bukkit.block.Dispenser
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockDispenseEvent


class EventListener: Listener {

    @EventHandler
    fun onDispense(event: BlockDispenseEvent) {
        val dispenser = event.block.state as? Dispenser ?: return
        if (!dispenser.isThomasfied || !event.item.type.isMinecart) return
        val minecartClass = event.item.type.minecartEntityClass ?: return
        if (!dispenser.spawnThomasCart(minecartClass)) return
        event.isCancelled = true
        launch {
            delay(50)
            dispenser.inventory.removeItem(event.item)
        }
    }
}