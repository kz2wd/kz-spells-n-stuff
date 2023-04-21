package com.cludivers.kz2wdprison

import com.cludivers.kz2wdprison.framework.beans.artifact.Artifact
import com.cludivers.kz2wdprison.framework.beans.artifact.Specifications
import com.cludivers.kz2wdprison.framework.configuration.HibernateConfigurationHandler
import com.cludivers.kz2wdprison.gameplay.artifact.ArtifactListener
import com.cludivers.kz2wdprison.gameplay.artifact.CustomShardItems
import com.cludivers.kz2wdprison.gameplay.attributes.PlayerAttributesDeclaration
import com.cludivers.kz2wdprison.gameplay.event.BonusXpEvent
import com.cludivers.kz2wdprison.gameplay.listeners.ListenersDeclaration
import com.cludivers.kz2wdprison.gameplay.nation.NationDeclaration
import com.cludivers.kz2wdprison.gameplay.world.mines.MinesDeclaration
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.plugin.java.JavaPlugin


@Suppress("unused")
class Kz2wdPrison : JavaPlugin() {
    override fun onEnable() {
        val sessionFactory = HibernateConfigurationHandler.loadHibernateConfiguration(config)
        val session = sessionFactory.openSession()

        PlayerAttributesDeclaration.declare(this, session)
        ListenersDeclaration.declare(this,  server.pluginManager, session)
        MinesDeclaration.declare(this)
        NationDeclaration.declare(this, session)

        BonusXpEvent.start(this)

        // val currentEventCmd = CurrentEventCommand()

        Bukkit.addRecipe(getRecipe())

        val artifact = defaultArtifact()
        ArtifactListener.registerArtifact(artifact)


    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    private fun getRecipe(): ShapedRecipe {
        val item = ItemStack(Material.GOLDEN_SWORD)
        val meta = item.itemMeta
        meta.setCustomModelData(1234567)
        item.setItemMeta(meta)

        val key = NamespacedKey(this, "custom_sword")
        val recipe = ShapedRecipe(key, item)

        recipe.shape(" B ", " B ", " R ")
        recipe.setIngredient('B', Material.GOLD_BLOCK)
        recipe.setIngredient('R', Material.BLAZE_ROD)

        return recipe
    }

    private fun defaultArtifact(): Artifact {
        val artifact = Artifact()

        artifact.producers = mapOf(Specifications.BLOCKS to CustomShardItems.SHARDS.itemStack)
        artifact.consumers = mapOf(Specifications.PROJECTILE to ItemStack(Material.ARROW))
        artifact.activateOnInteract = true
        artifact.itemStack = ItemStack(Material.STICK)

        return artifact
    }
}