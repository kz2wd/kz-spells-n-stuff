package com.cludivers.kz2wdprison.modules.player

import com.cludivers.kz2wdprison.Kz2wdPrison
import com.cludivers.kz2wdprison.framework.configuration.PluginConfiguration
import com.cludivers.kz2wdprison.framework.utils.Utils.buildItemStack
import com.cludivers.kz2wdprison.modules.player.PlayerBean.Companion.getData
import com.cludivers.kz2wdprison.modules.shards.CustomShardItems
import com.cludivers.kz2wdprison.modules.shards.gamble.LootBox
import dev.triumphteam.gui.components.GuiType
import dev.triumphteam.gui.guis.Gui
import dev.triumphteam.gui.guis.GuiItem
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import kotlin.math.roundToInt


fun Player.bossBarDisplay(text: Component, progress: Float, color: BossBar.Color = BossBar.Color.PURPLE,
                          overlay: BossBar.Overlay = BossBar.Overlay.PROGRESS){
    val playerTransientData = PlayerTransientData.getPlayerTransientData(this)

    if (playerTransientData.currentBossBarDisplay !is BossBar){
        val bossBar: BossBar = BossBar.bossBar(text, progress, color, overlay)
        playerTransientData.currentBossBarDisplay = bossBar
        this.showBossBar(playerTransientData.currentBossBarDisplay!!)
    }
    playerTransientData.currentBossBarDisplay!!.name(text).progress(progress).color(color).overlay(overlay)
}

fun Player.sendErrorMessage(text: String) {
    this.sendMessage(Component.text(text).color(NamedTextColor.RED))
}

fun Player.sendSuccessMessage(text: String) {
    this.sendMessage(Component.text(text).color(NamedTextColor.GREEN))
}

fun Player.sendNotificationMessage(text: String) {
    this.sendMessage(Component.text(text).color(NamedTextColor.YELLOW))
}

fun Player.sendConfirmationMessage(text: String, onAccept: String, onRefuse: String){
    val msg = Component.text(text)

    val acceptButton = Component.text("Accepter").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD)
    val acceptClick = ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, onAccept)
    val acceptHover = HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Accepter"))

    val refuseButton = Component.text("Refuser").color(NamedTextColor.RED).decorate(TextDecoration.BOLD)
    val refuseCLick = ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, onRefuse)
    val refuseHover = HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Refuser"))

     this.sendMessage(msg
         .appendNewline().append(acceptButton.clickEvent(acceptClick).hoverEvent(acceptHover))
         .appendSpace().append(refuseButton.clickEvent(refuseCLick).hoverEvent(refuseHover)))
}

fun Player.requestShards(args: Array<String>, index: Int): Int? {
    if (args.size < index + 1) {
        this.sendErrorMessage("Vous devez préciser une quantité de fragments à ajouter.")
        return null
    }

    val shardAmount = try {
        args[index].toInt()
    } catch (e: NumberFormatException) {
        this.sendErrorMessage("Quantité invalide : ${args[1]}.")
        return null
    }

    if (this.getData().shards < shardAmount) {
        this.sendErrorMessage("Vous n'avez pas assez de fragments.")
        return null
    }

    return shardAmount
}

fun Player.notifyPlayer(notification: Component, sound: Sound? = null, volume: Float, inChat: Boolean = false) {
    if (inChat) {
        this.sendMessage(notification)
    } else {
        this.sendActionBar(notification)
    }
    if (sound == null) return
    this.playSound(this, sound, volume, 1.0f)
}

fun Player.giveShards(shards: Int) {
    this.giveShards(shards.toDouble())
}

fun Player.giveShards(shards: Double) {
    PluginConfiguration.session.beginTransaction()
    this.getData().shards += shards
    PluginConfiguration.session.transaction.commit()
    this.notifyPlayer(Component.text("+${shards.toInt()} (${this.getData().shards.roundToInt()})").color(NamedTextColor.GREEN), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, .3f)
}

fun Player.tryTakeShards(shards: Int): Boolean {
    return this.tryTakeShards(shards.toDouble())
}

fun Player.tryTakeShards(shards: Double): Boolean {
    if (this.getData().shards < shards) {
        this.playSound(this, Sound.BLOCK_STONE_BREAK, 1.0f, 1.0f)
        return false
    }
    PluginConfiguration.session.beginTransaction()
    this.getData().shards -= shards
    PluginConfiguration.session.transaction.commit()
    this.notifyPlayer(Component.text("-${shards.toInt()} (${this.getData().shards.roundToInt()})").color(NamedTextColor.RED), Sound.BLOCK_DISPENSER_FAIL, .3f)
    return true
}

fun Player.openLootboxesMenu() {
    val lootboxesChoice = Gui.gui()
        .title(Component.text("Lootbox selection"))
        .disableAllInteractions()
        .type(GuiType.CHEST)
        .rows(6)
        .create()

    LootBox.ALL_LOOTBOXES.forEach { lootbox ->
        val guiItem = lootbox.getGuiPreview()
        guiItem.setAction { _ -> this.tryPull(lootbox) }
        lootboxesChoice.addItem(guiItem)
    }

    lootboxesChoice.open(this)
}

fun Player.tryPull(source: LootBox): Boolean {
    if (!this.tryTakeShards(source.pullCost)) {
        this.sendMessage(source.getPullErrorMessage())
        return false
    }
    val pulledItem = source.pull()

    val gui = Gui.storage()
        .title(source.name)
        .rows(1)
        .disableItemPlace()
        .disableItemSwap()
        .create()
    gui.addItem(pulledItem)

    fun ensurePlayerReceiveItem() {
        val itemStack = gui.inventory.getItem(0) ?: return
        this.giveOrDropItem(itemStack)
    }

    fun getShardsIndicator(): GuiItem {
        val shardsAmount = GuiItem(CustomShardItems.SHARDS.itemStack.withName("You have ${this.getData().shards.toInt()} shards"))
        shardsAmount.setAction {
            it.isCancelled = true
        }
        return shardsAmount
    }

    val rerollButton = GuiItem(buildItemStack(Component.text("Reroll (${source.pullCost} shards)"), Material.GOLD_BLOCK))
    rerollButton.setAction {
        ensurePlayerReceiveItem()
        gui.inventory.setItem(0, ItemStack(Material.AIR))
        if (!this.tryTakeShards(source.pullCost)) {
            return@setAction
        }
        gui.addItem(source.pull())
        gui.updateItem(7, getShardsIndicator())
        it.isCancelled = true
    }
    gui.setItem(6, rerollButton)

    val shardsAmountItem = getShardsIndicator()
    gui.setItem(7, shardsAmountItem)


    val goBackButton = GuiItem(buildItemStack(Component.text("Back"), Material.OAK_DOOR))
    goBackButton.setAction {
        Bukkit.getScheduler().runTaskLater(Kz2wdPrison.plugin, Runnable {
            this.openLootboxesMenu()
        }, 1)
        it.isCancelled = true
    }

    gui.setItem(8, goBackButton)

    gui.open(this)
    gui.setCloseGuiAction { Bukkit.getScheduler().runTaskLater(Kz2wdPrison.plugin, Runnable {
        ensurePlayerReceiveItem()
    }, 1) }

    gui.filler.fill(GuiItem(Material.BLACK_STAINED_GLASS_PANE))

    this.playSound(this, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 0.5f, 1f)
    return true
}

private fun ItemStack.withName(s: String): ItemStack {
    val clonedItem = this.clone()
    val meta: ItemMeta = clonedItem.itemMeta ?: return clonedItem
    val clonedMeta = meta.clone()
    clonedMeta.displayName(Component.text(s))
    clonedItem.itemMeta = clonedMeta
    return clonedItem
}

fun Player.giveOrDropItem(itemStack: ItemStack) {
    // Try to add the item to the player's inventory
    val remainingItems = inventory.addItem(itemStack).values

    // If there are remaining items, drop them on the ground
    if (remainingItems.isNotEmpty()) {
        for (item in remainingItems) {
            dropItem(item, location)
        }
    }
}

fun dropItem(itemStack: ItemStack, location: Location) {
    val world: World = location.world
    world.dropItem(location, itemStack)
}

