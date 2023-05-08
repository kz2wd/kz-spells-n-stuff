package com.cludivers.kz2wdprison.gameplay.attributes

import com.cludivers.kz2wdprison.framework.persistance.beans.player.PlayerBean
import com.cludivers.kz2wdprison.gameplay.commands.MainCommandExecutor
import com.cludivers.kz2wdprison.gameplay.commands.xp.IncreaseAttributeCommand
import com.cludivers.kz2wdprison.gameplay.listeners.PrisonListener
import com.cludivers.kz2wdprison.gameplay.player.intrinsic.IntrinsicManagerMenu
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.plugin.java.JavaPlugin
import org.hibernate.Session

object PlayerAttributesDeclaration {

    fun declare(plugin: JavaPlugin, session: Session) {
        // Set up xp command
        val xpCommandName = "xp"

        // Player Attributes
        val successText = Component.text("${ChatColor.GREEN}Amélioration de")

        val healthAttribute = PlayerAttribute(
            "PV",
            51,
            "health",
            xpCommandName,
            {p: PlayerBean -> p.healthLevel},
            {p: PlayerBean -> p.healthLevel += 1},
            {p: PlayerBean -> p.healthLevel / 10 + 1},
            {n: Int -> "${PrisonListener.getHealth(n).toInt()} PV"},
            successText.append(Component.text(" vos PV !")))

        val pickaxesName = listOf("bois", "pierre", "fer", "diamant", "netherite")
        val pickaxeAttribute = PlayerAttribute(
            "Pioche",
            4,
            "pickaxe",
            xpCommandName,
            {p: PlayerBean -> p.pickaxeLevel},
            {p: PlayerBean -> p.pickaxeLevel += 1},
            {p: PlayerBean -> (p.pickaxeLevel + 1) * 2},
            {n: Int -> "Pioche en ${pickaxesName[n]}"},
            successText.append(Component.text(" votre ${ChatColor.BOLD}pioche !")))

        val miningSpeedAttribute = PlayerAttribute(
            "Minage",
            101,
            "mining",
            xpCommandName,
            {p: PlayerBean -> p.miningLevel},
            {p: PlayerBean -> p.miningLevel += 1},
            {p: PlayerBean -> if (p.miningLevel < 5) p.miningLevel + 1 else (p.miningLevel) * 3 },
            {n: Int -> "Efficacité $n" },
            successText.append(Component.text(" votre vitesse de minage !")))

        val criticOddAttribute = PlayerAttribute(
            "Chance de minage critique",
            101,
            "criticOdd",
            xpCommandName,
            {p: PlayerBean -> p.criticOddLevel},
            {p: PlayerBean -> p.criticOddLevel += 1},
            {p: PlayerBean -> p.criticOddLevel + 1},
            {n: Int -> "${(PrisonListener.getCriticOdd(n) * 100).toInt()}% de Minage Critique"},
            successText.append(Component.text( " vos chances de minage critique"))
        )

        val criticFactorAttribute = PlayerAttribute(
            "Multiplication de minage critique",
            101,
            "criticFactor",
            xpCommandName,
            {p: PlayerBean -> p.criticFactorLevel},
            {p: PlayerBean -> p.criticFactorLevel += 1},
            {p: PlayerBean -> p.criticFactorLevel + 1},
            {n: Int -> "${PrisonListener.getCriticFactor(n)} fois plus d'expérience"},
            successText.append(Component.text( " votre gain d'expérience en cas de minage critique"))
        )

        val allAttributes = listOf(pickaxeAttribute, miningSpeedAttribute, healthAttribute,
            criticOddAttribute, criticFactorAttribute )
        val attributesMaterials = listOf(
            Material.ANVIL, Material.ENCHANTING_TABLE, Material.GOLDEN_APPLE,
            Material.ENDER_EYE, Material.EXPERIENCE_BOTTLE)
//        val xpCmd = SkillMenu(session, attributesMaterials.zip(allAttributes))
        val xpCmd = IntrinsicManagerMenu(session)
        val xpCommands = allAttributes.associate {
            it.increaseCommandCallName to IncreaseAttributeCommand(session, it, xpCommandName) }
        val xpCommandsExecutor = MainCommandExecutor(xpCommands, xpCmd)
        plugin.getCommand(xpCommandName)?.setExecutor(xpCommandsExecutor)
        plugin.getCommand(xpCommandName)?.tabCompleter = xpCommandsExecutor
    }
}