package it.forgottenworld.thomas.model

import it.forgottenworld.thomas.FWThomasPlugin
import org.bukkit.Bukkit.getServer
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.util.BlockVector

class Road(
        val origin: Location,
        val width: Int,
        val depth: Int,
        val speedBoost: Float) {

    private fun getAllBlocks() = mutableSetOf<Block>().also {
        for (i in 0 until width)
            for (j in 0 until depth)
                it.add(origin.world!!.getBlockAt(origin.blockX + i, origin.blockY, origin.blockZ + j))
        }

    fun createRoad() {
        getAllBlocks().forEach {
            it.setMetadata(
                "grantsChooChoo",
                FixedMetadataValue(FWThomasPlugin.instance, speedBoost)
            )
        }
    }

    fun containsBlock(block: Block) = getAllBlocks().contains(block)

    fun doesInstersectOtherRoad(road: Road) = road.getAllBlocks().intersect(getAllBlocks()).isNotEmpty()
}