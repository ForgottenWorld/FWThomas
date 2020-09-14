package it.forgottenworld.thomas.model

import it.forgottenworld.thomas.FWThomasPlugin
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.metadata.FixedMetadataValue

class Road(
        private val origin: Location,
        private val width: Int,
        private val depth: Int,
        private val speedBoost: Double) {

    companion object {
        fun fromConfig(conf: ConfigurationSection) =
                Road(
                        conf.getLocation("origin")!!,
                        conf.getInt("width"),
                        conf.getInt("depth"),
                        conf.getDouble("speedBoost")
                )
    }

    fun toConfig(conf: ConfigurationSection) {
        conf.run {
            set("origin", origin)
            set("width", width)
            set("depth", depth)
            set("speedBoost", speedBoost)
        }
    }

    private fun getAllBlocks() = mutableSetOf<Block>().also {
        for (i in 0 until width)
            for (j in 0 until depth)
                it.add(origin.world!!.getBlockAt(origin.blockX + i, origin.blockY, origin.blockZ + j))
        }

    fun createRoad() {
        getAllBlocks().forEach {
            it.setMetadata(
                "grantsChooChoo",
                FixedMetadataValue(FWThomasPlugin.instance, speedBoost.toFloat())
            )
        }
    }

    fun doesInstersectOtherRoad(road: Road) = road.getAllBlocks().intersect(getAllBlocks()).isNotEmpty()
}