package com.cludivers.kz2wdprison.modules.nation

import com.cludivers.kz2wdprison.modules.nation.beans.NationBean
import com.cludivers.kz2wdprison.modules.nation.listeners.NationListener
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EnderCrystal
import org.bukkit.entity.EntityType
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class NationCoreEntity(private val nation: NationBean, location: Location, private val maxHealth: Double = 100.0) {

    private val armorStand: ArmorStand = location.world.spawnEntity(location, EntityType.ARMOR_STAND) as ArmorStand
    private val crystal: EnderCrystal = location.world.spawnEntity(location, EntityType.ENDER_CRYSTAL) as EnderCrystal

    init {
        armorStand.isVisible = false
        armorStand.setGravity(false)
        armorStand.isCustomNameVisible = true
        armorStand.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = maxHealth
        armorStand.health = maxHealth
        updateHealthBar()
        armorStand.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, Int.MAX_VALUE, 1))
        NationListener.cores[crystal] = this
    }

    private fun updateHealthBar() {
        armorStand.customName(Component.text("${armorStand.health.toInt()}/${maxHealth.toInt()}"))
    }

    fun damage(amount: Double) {
        armorStand.health -= amount
        if (armorStand.health <= 0) {
            armorStand.health = 0.0
            onDestroy()
        }
        updateHealthBar()
        alertPlayers(
            Component.text("The base core is under attack! Health: ${armorStand.health.toInt()}/${maxHealth.toInt()}")
                .color(NamedTextColor.RED)
        )
    }

    private fun onDestroy() {
        armorStand.remove()
        alertPlayers(Component.text("The base core is destroyed!").color(NamedTextColor.RED))
    }

    private fun alertPlayers(message: Component) {
        nation.notifyPlayers(message, Sound.ENTITY_WITHER_HURT)
    }

    companion object {
        // Todo when persistence
//        @FetchAfterDatabaseInit
//        private fun fetchCores() {
//            val query = PluginConfiguration.session.createQuery("from NationBean", NationBean::class.java)
//            query.list().associateBy { it.name }.toMutableMap()
//            NationListener.cores.putAll()
//        }
    }

}