package it.forgottenworld.thomas.command

import it.forgottenworld.thomas.manager.ThomasManager
import it.forgottenworld.thomas.utils.Strings
import it.forgottenworld.thomas.utils.targetDispenser
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent

@KoinApiExtension
class UnthomasCommand(private val thomasManager: ThomasManager): CommandExecutor, KoinComponent {

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

        with(thomasManager) {
            if (!dispenser.isThomasfied) {
                sender.sendMessage(Strings.DISPENSER_NOT_THOMASFIED)
                return true
            }

            dispenser.unthomasfy()
            sender.sendMessage(Strings.DISPENSER_UNTHOMASFIED)
        }
        return true
    }
}