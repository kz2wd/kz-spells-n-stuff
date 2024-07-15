package com.cludivers.kz2wdprison.nation.listeners

import com.cludivers.kz2wdprison.persistence.beans.player.PlayerBean.Companion.getData
import com.cludivers.kz2wdprison.nation.beans.AreaPermission
import com.cludivers.kz2wdprison.nation.beans.ChunkBean
import com.cludivers.kz2wdprison.nation.beans.NationBean
import com.cludivers.kz2wdprison.nation.beans.PermissionGroup
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title
import org.bukkit.Chunk
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerMoveEvent
import java.time.Duration

class NationListener : Listener {

    companion object {
        val DEFAULT_TIME = Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(500), Duration.ofMillis(500))
    }

    @EventHandler
    fun onPlayerEnterChunk(event: PlayerMoveEvent) {
        if (event.from.chunk == event.to.chunk) {
            return
        }
        val newChunkData = ChunkBean.getChunkBean(event.to.chunk)
        val oldChunkData = ChunkBean.getChunkBean(event.from.chunk)

        if (newChunkData.nation == oldChunkData.nation) {
            return
        }

        if (newChunkData.nation !is NationBean) {
            event.player.showTitle(
                Title.title(
                    Component.text(""),
                    Component.text("Zone neutre").color(NamedTextColor.GREEN),
                    DEFAULT_TIME
                )
                )
        } else {
            event.player.showTitle(
                Title.title(
                    Component.text(""),
                    Component.text(newChunkData.nation!!.name), DEFAULT_TIME
                )
            )
        }
    }

    private fun isChunkEventUnauthorized(player: Player, getChunk: () -> Chunk, getPerm: (AreaPermission) -> Boolean): Boolean{
        val chunkData = ChunkBean.getChunkBean(getChunk())

        if (chunkData.nation !is NationBean){
            return true
        }

        val playerData = player.getData()
        if (playerData.nation !is NationBean || chunkData.nation != playerData.nation){
            return true
        }

        if (playerData.permissionGroup is PermissionGroup && !getPerm(playerData.permissionGroup!!.areaPermission!!)){
            return true
        }
        return false
    }

    private fun isBlockEventUnauthorized(player: Player, event: BlockEvent, getPerm: (AreaPermission) -> Boolean): Boolean {
        return isChunkEventUnauthorized(player, {event.block.chunk}, getPerm)
    }

    @EventHandler(priority = EventPriority.LOW)
    fun onPlayerPlaceInArea(event: BlockPlaceEvent){
        event.isCancelled = isBlockEventUnauthorized(event.player, event) { perm -> perm.canPlace }
    }

    @EventHandler(priority = EventPriority.LOW)
    fun onPlayerBreakInArea(event: BlockBreakEvent){
        event.isCancelled = isBlockEventUnauthorized(event.player, event) { perm -> perm.canBreak }
    }
}