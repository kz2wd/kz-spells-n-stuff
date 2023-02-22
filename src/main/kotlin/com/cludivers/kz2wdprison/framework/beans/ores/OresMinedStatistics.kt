package com.cludivers.kz2wdprison.framework.beans.ores

import jakarta.persistence.AttributeOverride
import jakarta.persistence.AttributeOverrides
import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import org.bukkit.Material

@Embeddable
class OresMinedStatistics {
    @AttributeOverrides(
        AttributeOverride(name = "maxXp", column = Column(name = "stone_max_xp")),
        AttributeOverride(name = "amountMined", column = Column(name = "stone_amount_mined"))
    )
    var stone =
        OreStats(Ores.STONE.minXp * 1.5f)

    @AttributeOverrides(
        AttributeOverride(name = "maxXp", column = Column(name = "coal_max_xp")),
        AttributeOverride(name = "amountMined", column = Column(name = "coal_amount_mined"))
    )
    var coal =
        OreStats(Ores.COAL.minXp * 1.5f)

    @AttributeOverrides(
        AttributeOverride(name = "maxXp", column = Column(name = "iron_max_xp")),
        AttributeOverride(name = "amountMined", column = Column(name = "iron_amount_mined"))
    )
    var iron =
        OreStats(Ores.IRON.minXp * 1.5f)

    @AttributeOverrides(
        AttributeOverride(name = "maxXp", column = Column(name = "gold_max_xp")),
        AttributeOverride(name = "amountMined", column = Column(name = "gold_amount_mined"))
    )
    var gold =
        OreStats(Ores.GOLD.minXp * 1.5f)

    @AttributeOverrides(
        AttributeOverride(name = "maxXp", column = Column(name = "diamond_max_xp")),
        AttributeOverride(name = "amountMined", column = Column(name = "diamond_amount_mined"))
    )
    var diamond =
        OreStats(Ores.DIAMOND.minXp * 1.5f)

    @AttributeOverrides(
        AttributeOverride(name = "maxXp", column = Column(name = "redstone_max_xp")),
        AttributeOverride(name = "amountMined", column = Column(name = "redstone_amount_mined"))
    )
    var redstone =
        OreStats(Ores.REDSTONE.minXp * 1.5f)

    fun getOreInfos(material: Material): OreStats? {
        return when(material){
            Material.STONE, Material.DEEPSLATE -> stone
            Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE -> coal
            Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE -> iron
            Material.GOLD_ORE, Material.DEEPSLATE_GOLD_ORE -> gold
            Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE -> diamond
            Material.REDSTONE_ORE, Material.DEEPSLATE_REDSTONE_ORE -> redstone
            else -> null
        }
    }
}