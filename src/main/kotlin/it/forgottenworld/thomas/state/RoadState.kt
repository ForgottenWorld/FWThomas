package it.forgottenworld.thomas.state

import it.forgottenworld.thomas.FWThomasPlugin
import it.forgottenworld.thomas.model.Road
import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.scheduler.BukkitRunnable
import java.io.File
import java.util.*
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

object RoadState {

    private val wipPos1s = mutableMapOf<UUID, Location>()
    private val wipPos2s = mutableMapOf<UUID, Location>()
    private val wipSpeeds = mutableMapOf<UUID, Double>()
    private val roads = mutableSetOf<Road>()

    fun purgeWorkingData(uuid: UUID) {
        wipPos1s.remove(uuid)
        wipPos2s.remove(uuid)
        wipSpeeds.remove(uuid)
    }

    fun setPos(uuid: UUID, whichOne: Int, loc: Location): Int {
        return when (whichOne) {
            1 -> {
                val other = wipPos2s[uuid]
                if (other != null && (other.y != loc.y || other.world != loc.world))
                    -1 //positions must be at the same y level and in the same world
                else {
                    wipPos1s[uuid] = loc
                    0
                }
            }
            2 -> {
                val other = wipPos1s[uuid]
                if (other != null && (other.y != loc.y || other.world != loc.world))
                    -1 //positions must be at the same y level and in the same world
                else {
                    wipPos2s[uuid] = loc
                    0
                }
            }
            else -> return -2 //invalid pos
        }
    }

    fun setSpeed(uuid: UUID, speed: Double) {
        wipSpeeds[uuid] = speed
    }

    fun makeRoad(uuid: UUID): Int {
        val pos1 = wipPos1s[uuid] ?: return -2 //missing pos1
        val pos2 = wipPos2s[uuid] ?: return -3 //missing pos2
        val speed = wipSpeeds[uuid] ?: return -4 //missing speed
        return doMakeRoad(pos1, pos2, speed).also { if (it == 0) purgeWorkingData(uuid) }
    }

    private fun doMakeRoad(pos1: Location, pos2: Location, speedBoost: Double): Int {
        val origin = Location(pos1.world, min(pos1.x, pos2.x), min(pos1.y, pos2.y), min(pos1.z, pos2.z))
        val originOpp = Location(pos1.world, max(pos1.x, pos2.x), max(pos1.y, pos2.y), max(pos1.z, pos2.z))
        val road = Road(origin, floor(originOpp.x - origin.x).toInt() + 1, floor(originOpp.z - origin.z).toInt() + 1, speedBoost)
        for (r in roads) if (r.doesInstersectOtherRoad(road)) return -1 //road intersects another road
        roads.add(road)
        toConfig()
        road.createRoad()
        return 0 //all ok
    }

    private fun toConfig() {
        val file = File(FWThomasPlugin.pluginDataFolder, "roads.yml")
        val conf = YamlConfiguration()
        roads.forEachIndexed { i, it -> it.toConfig(conf.createSection("$i")) }
        object: BukkitRunnable() {
            override fun run() {
                conf.save(file)
            }
        }.runTaskAsynchronously(FWThomasPlugin.instance)
    }

    fun fromConfig() {
        val file = File(FWThomasPlugin.pluginDataFolder, "roads.yml")
        if (!file.exists()) return
        val conf = YamlConfiguration().apply { load(file) }
        conf.getKeys(false).forEach {
            roads.add(
                    Road.fromConfig(
                            conf.getConfigurationSection(it)!!
                    ).apply { createRoad() })
        }
        roads.forEachIndexed { i, it -> it.toConfig(conf.createSection("$i")) }
    }
}