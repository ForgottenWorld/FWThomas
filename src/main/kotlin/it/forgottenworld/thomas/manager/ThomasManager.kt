package it.forgottenworld.thomas.manager

import it.forgottenworld.thomas.FWThomasPlugin
import it.forgottenworld.thomas.config.Config
import it.forgottenworld.thomas.model.DispenserCoords
import it.forgottenworld.thomas.model.DispenserCoords.Companion.getDispenserCoords
import it.forgottenworld.thomas.utils.isMinecart
import it.forgottenworld.thomas.utils.launch
import it.forgottenworld.thomas.utils.launchAsync
import it.forgottenworld.thomas.utils.minecartEntityClass
import kotlinx.coroutines.delay
import org.bukkit.Bukkit
import org.bukkit.block.Dispenser
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Minecart
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockDispenseEvent
import org.bukkit.util.Vector
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File
import org.bukkit.block.data.type.Dispenser as DispenserBlockData

@KoinApiExtension
class ThomasManager : KoinComponent {

    private val config by inject<Config>()
    private val plugin by inject<FWThomasPlugin>()

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
        ?.setMaxSpeed(config.maxSpeed) != null

    fun scheduleSerialization() {
        launch {
            delay(config.serializationDelay)
            while(true) {
                saveToYaml()
                delay(config.serializationInterval)
            }
        }
    }

    fun scheduleCleanup() {
        launch {
            delay(config.cleanupDelay)
            while(true) {
                thomasDispensers.removeIf { coords ->
                    Bukkit
                        .getWorld(coords.worldId)
                        ?.getBlockAt(coords.x, coords.y, coords.z)
                        ?.let { it.state !is Dispenser }
                        ?: true
                }
                delay(config.cleanupInterval)
            }
        }
    }

    private fun saveToYaml() {
        val file = File(plugin.dataFolder, DISPENSERS_FILE_NAME)
        if (!file.exists() && !file.createNewFile())
            Bukkit.getLogger().warning("Couldn't create dispensers file")
        with (YamlConfiguration()) {
            thomasDispensers.forEachIndexed { i,c -> c.toYaml(createSection("$i")) }
            @Suppress("BlockingMethodInNonBlockingContext")
            launchAsync { save(file) }
        }
    }

    fun loadFromYaml() {
        val file = File(plugin.dataFolder, DISPENSERS_FILE_NAME)
        if (!file.exists()) return
        with (YamlConfiguration()) {
            load(file)
            thomasDispensers.clear()
            thomasDispensers.addAll(getKeys(false).map { getDispenserCoords(it) })
        }
    }

    inner class EventListener: Listener {

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

    companion object {
        private const val DISPENSERS_FILE_NAME = "dispensers.yml"
    }
}