package com.cludivers.kz2wdprison.gameplay.shardsworld

import com.sk89q.worldedit.EditSession
import com.sk89q.worldedit.function.block.Naturalizer
import com.sk89q.worldedit.math.BlockVector3

class ShardsNaturalizer(editSession: EditSession?) : Naturalizer(editSession) {
    override fun apply(position: BlockVector3?, depth: Int): Boolean {
        return super.apply(position, depth)
    }
}