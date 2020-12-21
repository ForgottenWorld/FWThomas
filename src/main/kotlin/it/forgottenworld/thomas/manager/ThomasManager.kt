package it.forgottenworld.thomas.manager

import it.forgottenworld.thomas.config.Config
import it.forgottenworld.thomas.model.DispenserCoords
import it.forgottenworld.thomas.model.DispenserCoords.Companion.getDispenserCoords
import it.forgottenworld.thomas.utils.launch
import it.forgottenworld.thomas.utils.launchAsync
import it.forgottenworld.thomas.utils.thomasPlugin
import kotlinx.coroutines.delay
import org.bukkit.Bukkit
import org.bukkit.block.Dispenser
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Minecart
import org.bukkit.util.Vector
import java.io.File
import org.bukkit.block.data.type.Dispenser as DispenserBlockData

object ThomasManager {

    private const val DISPENSERS_FILE_NAME = "dispensers.yml"

    private val thomasDispensers = mutableSetOf<DispenserCoords>()

    val Dispenser.isThomasfied get() = thomasDispensers.contains(location.getDispenserCoords())

    fun Dispenser.thomasfy() = location.getDispenserCoords()?.let { thomasDispensers.add(it) }

    fun Dispenser.unthomasfy() = thomasDispensers.remove(location.getDispenserCoords())

    fun Dispenser.spawnThomasCart(minecartClass: Class<out Minecart>) = (blockData as? DispenserBlockData)
        ?.let { block.getRelative(it.facing) }
        ?.location
        ?.clone()
        ?.add(Vector(0.5, 0.0, 0.5))
        ?.let { it.world?.spawn(it, minecartClass) }
        ?.setMaxSpeed(Config.maxSpeed) != null

    fun scheduleSerialization() {
        launch {
            delay(Config.serializationInterval)
            while(true) {
                delay(Config.serializationInterval)
                launchAsync { saveToYaml() }
            }
        }
    }

    private fun saveToYaml() {
        val file = File(thomasPlugin.dataFolder, DISPENSERS_FILE_NAME)
        if (!file.exists() && !file.createNewFile())
            Bukkit.getLogger().warning("Couldn't create dispensers file")
        with (YamlConfiguration()) {
            thomasDispensers.forEachIndexed { i,c -> c.toYaml(createSection("$i")) }
            save(file)
        }
    }

    fun loadFromYaml() {
        val file = File(thomasPlugin.dataFolder, DISPENSERS_FILE_NAME)
        if (!file.exists()) return
        with (YamlConfiguration()) {
            load(file)
            thomasDispensers.clear()
            thomasDispensers.addAll(getKeys(false).map { getDispenserCoords(it) })
        }
    }
}