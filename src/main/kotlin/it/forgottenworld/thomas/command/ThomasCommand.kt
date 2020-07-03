package it.forgottenworld.thomas.command

import it.forgottenworld.thomas.state.RoadState
import net.md_5.bungee.api.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ThomasCommand: CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("This command may only be executed by players")
            return true
        } else {
            return when (args[0]) {
                "pos1" -> {
                    val target = sender.getTargetBlockExact(5)
                    if (target == null) {
                        sender.sendMessage("${ChatColor.RED}Please execute this command while aiming towards a block within 5 blocks from your position")
                    } else {
                        sender.sendMessage(when (RoadState.setPos(sender.uniqueId, 1, target.location)) {
                            0 -> "First road position set"
                            -1 -> "${ChatColor.RED}Both positions must be in the same world and at the same Y level"
                            -2 -> "${ChatColor.RED}Looks like someone messed up big time editing the source code"
                            else -> ""
                        })
                    }
                    true
                }
                "pos2" -> {
                    val target = sender.getTargetBlockExact(5)
                    if (target == null) {
                        sender.sendMessage("${ChatColor.RED}Please execute this command while aiming towards a block within 5 blocks from your position")
                    } else {
                        sender.sendMessage(when (RoadState.setPos(sender.uniqueId, 2, target.location)) {
                            0 -> "Second road position set"
                            -1 -> "${ChatColor.RED}Both positions must be in the same world and at the same Y level"
                            -2 -> "${ChatColor.RED}Thomas had never seen such BS before"
                            else -> ""
                        })
                    }
                    true
                }
                "speed" -> {
                    val speed = args[1].toFloatOrNull()
                    if (speed == null) {
                        sender.sendMessage("${ChatColor.RED}Please provide speed as a positive floating point number")
                    } else {
                        RoadState.setSpeed(sender.uniqueId, speed)
                        sender.sendMessage("Road speed set")
                    }
                    true
                }
                "make" -> {
                    sender.sendMessage(when (RoadState.makeRoad(sender.uniqueId)) {
                        0 -> "Road created"
                        -1 -> "${ChatColor.RED}Cannot create road as it would intersect another road"
                        -2 -> "${ChatColor.RED}First position is missing"
                        -3 -> "${ChatColor.RED}Second position is missing"
                        -4 -> "${ChatColor.RED}Speed is missing"
                        else -> ""
                    })
                    true
                }
                else -> false
            }
        }
    }
}