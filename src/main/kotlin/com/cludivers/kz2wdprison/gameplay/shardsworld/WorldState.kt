package com.cludivers.kz2wdprison.gameplay.shardsworld

import com.cludivers.kz2wdprison.framework.configuration.PluginConfiguration
import com.cludivers.kz2wdprison.gameplay.nation.beans.NationBean
import jakarta.persistence.*
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.World

@Entity
class WorldState() {

    @Id
    @GeneratedValue
    var id: Long? = null

    var linkedWorld: String = DEFAULT_UNASSIGNED_NAME

    @Embedded
    var worldRules: WorldRules = WorldRules()

    var overallShardsValue: Int = 0

    var worldLevel: Int = 0

    @ManyToOne
    var owningNation: NationBean? = null

    constructor(linkedWorld: String) : this() {
        this.linkedWorld = linkedWorld
    }

    fun getSeed(): Int {
        return this.linkedWorld.hashCode()
    }

    fun showInfo(): Component {
        return worldRules.showWorldRules()
    }

    companion object {

        private var worldsState: MutableMap<World, WorldState>? = null
            get() {
                if (field == null) {
                    field = fetchAllWorldState().toMutableMap()
                }
                return field
            }

        fun getWorldState(world: World): WorldState? {
            return worldsState!![world]
        }

        private const val DEFAULT_UNASSIGNED_NAME = ""

        private fun fetchAllWorldState(): Map<World, WorldState> {
            return PluginConfiguration.session
                .createQuery("from WorldState", WorldState::class.java)
                .list()
                .filter { it.linkedWorld != DEFAULT_UNASSIGNED_NAME }
                .map { Bukkit.getWorld(it.linkedWorld) to it }
                .mapNotNull { it.first?.let { world -> Pair(world, it.second) } }
                .toMap()
        }

        fun registerNewWorld(world: World, generalDifficultyFactor: Float) {
            worldsState!![world] = newPersistentWorldState(world.name, generalDifficultyFactor)
        }

        private fun newPersistentWorldState(linkedWorld: String, generalDifficultyFactor: Float): WorldState {
            PluginConfiguration.session.beginTransaction()
            val newWorldState = WorldState(linkedWorld)

            newWorldState.worldRules =
                WorldRules.generatePseudoRandomRules(newWorldState.getSeed(), generalDifficultyFactor)

            PluginConfiguration.session.persist(newWorldState)
            PluginConfiguration.session.transaction.commit()
            return newWorldState
        }
    }


}