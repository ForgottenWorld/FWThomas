package it.forgottenworld.thomas.command

import it.forgottenworld.thomas.manager.ThomasManager.isThomasfied
import it.forgottenworld.thomas.manager.ThomasManager.unthomasfy
import it.forgottenworld.thomas.utils.Strings
import it.forgottenworld.thomas.utils.targetDispenser
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class UnthomasCommand: CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("This command may only be executed by players")
            return true
        }

        if (!sender.hasPermission("unthomas")) {
            sender.sendMessage("You don't have permission to use this command")
            return true
        }

        val dispenser = sender.targetDispenser ?: return false

        if (!dispenser.isThomasfied) {
            sender.sendMessage(Strings.DISPENSER_NOT_THOMASFIED)
            return true
        }

        dispenser.unthomasfy()
        sender.sendMessage(Strings.DISPENSER_UNTHOMASFIED)
        return true
    }
}