package com.cludivers.kz2wdprison.gameplay.listeners

import com.cludivers.kz2wdprison.framework.persistance.beans.nation.AreaPermission
import com.cludivers.kz2wdprison.framework.persistance.beans.nation.ChunkBean
import com.cludivers.kz2wdprison.framework.persistance.beans.nation.NationBean
import com.cludivers.kz2wdprison.framework.persistance.beans.nation.PermissionGroup
import com.cludivers.kz2wdprison.gameplay.player.getData
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Chunk
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.hibernate.Session

class NationListener(private val session: Session): Listener {

    @EventHandler
    fun onPlayerEnterChunk(event: PlayerMoveEvent){
        if (event.from.chunk == event.to.chunk){
            return
        }
        val newChunkData = ChunkBean.getChunkBean(session, event.to.chunk)
        val oldChunkData = ChunkBean.getChunkBean(session, event.from.chunk)

        if (newChunkData.nation == oldChunkData.nation){
            return
        }

        if (newChunkData.nation !is NationBean){
            event.player.showTitle(Title.title(Component.text("${ChatColor.DARK_GREEN}Zone neutre"), Component.text("")))
        } else {
            event.player.showTitle(Title.title(Component.text(newChunkData.nation!!.name!!), Component.text("")))
        }
    }

    private fun isChunkEventUnauthorized(player: Player, getChunk: () -> Chunk, getPerm: (AreaPermission) -> Boolean): Boolean{
        val chunkData = ChunkBean.getChunkBean(session, getChunk())

        if (chunkData.nation !is NationBean){
            return true
        }

        val playerData = player.getData(session)
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