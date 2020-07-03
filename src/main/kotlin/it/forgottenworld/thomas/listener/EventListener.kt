package it.forgottenworld.thomas.listener

import it.forgottenworld.thomas.FWThomasPlugin
import it.forgottenworld.thomas.state.RoadState
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.metadata.FixedMetadataValue

fun Player.getBlockBelow() = world.getBlockAt(location.blockX, location.blockY-1, location.blockZ)

class EventListener: Listener {

    @EventHandler
    fun onPlayerMove(e: PlayerMoveEvent) {
        val to = e.to ?: return
        if (e.from.x == to.x && e.from.y == to.y && e.from.z == to.z) return
        val meta = to.block.getRelative(BlockFace.DOWN).getMetadata("grantsChooChoo")
        if (meta.isNotEmpty()) {
            val speed = meta.first().asFloat()
            val ccb = e.player.getMetadata("chooChooedBy")
            if (ccb.isEmpty()) {
                e.player.setMetadata("chooChooedBy", FixedMetadataValue(FWThomasPlugin.instance, speed))
                e.player.walkSpeed += speed
            } else {
                val curChooChoo = ccb.first().asFloat()
                if (curChooChoo != speed) {
                    e.player.walkSpeed = e.player.walkSpeed - curChooChoo + speed
                    e.player.setMetadata("chooChooedBy", FixedMetadataValue(FWThomasPlugin.instance, speed))
                }
            }
        } else {
            val ccb = e.player.getMetadata("chooChooedBy")
            if (ccb.isNotEmpty()) {
                e.player.removeMetadata("chooChooedBy", FWThomasPlugin.instance)
                e.player.walkSpeed -= ccb.first().asFloat()
            }
        }
    }
    
    @EventHandler
    fun onPlayerQuit(e: PlayerQuitEvent) {
        val choo = e.player.getMetadata("chooChooedBy")
        if (choo.isNotEmpty()) {
            e.player.walkSpeed -= choo.first().asFloat()
            e.player.removeMetadata("chooChooedBy", FWThomasPlugin.instance)
        }
        RoadState.purgeWorkingData(e.player.uniqueId)
    }
}