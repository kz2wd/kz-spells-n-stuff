package com.cludivers.kz2wdprison

import com.cludivers.kz2wdprison.framework.configuration.HibernateConfigurationHandler
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.Artifact2
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.inputs.InputTypes
import com.cludivers.kz2wdprison.gameplay.artifact.ArtifactListener
import com.cludivers.kz2wdprison.gameplay.attributes.PlayerAttributesDeclaration
import com.cludivers.kz2wdprison.gameplay.event.BonusXpEvent
import com.cludivers.kz2wdprison.gameplay.listeners.ListenersDeclaration
import com.cludivers.kz2wdprison.gameplay.nation.NationDeclaration
import com.cludivers.kz2wdprison.gameplay.utils.Utils
import com.cludivers.kz2wdprison.gameplay.world.mines.MinesDeclaration
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.plugin.java.JavaPlugin
import org.hibernate.Session
import org.hibernate.SessionFactory


@Suppress("unused")
class Kz2wdPrison : JavaPlugin() {

    lateinit var sessionFactory: SessionFactory
    lateinit var session: Session

    override fun onEnable() {
        sessionFactory = HibernateConfigurationHandler.loadHibernateConfiguration(config)
        session = sessionFactory.openSession()

        PlayerAttributesDeclaration.declare(this, session)
        ListenersDeclaration.declare(this, server.pluginManager, session)
        MinesDeclaration.declare(this)
        NationDeclaration.declare(this, session)

        BonusXpEvent.start(this)

        // val currentEventCmd = CurrentEventCommand()

        Bukkit.addRecipe(getRecipe())

        val artifact = defaultArtifact2()
        ArtifactListener.registerArtifact(artifact, ItemStack(Material.STICK))

    }

    override fun onDisable() {
        session.close()
        sessionFactory.close()
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

    private fun defaultArtifact2(): Artifact2 {
        val artifact = Artifact2()

        artifact.itemStack = ItemStack(Material.DIAMOND_PICKAXE)
        artifact.input = Utils.buildItemStack(
            Component.text(""),
            Material.GOLD_BLOCK,
            customData = InputTypes.LOCATION_SIGHT.customData
        )
        artifact.converters = listOf()

        return artifact
    }
}