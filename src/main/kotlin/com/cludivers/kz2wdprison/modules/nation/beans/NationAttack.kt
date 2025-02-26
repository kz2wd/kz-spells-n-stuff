package com.cludivers.kz2wdprison.modules.nation.beans

import com.cludivers.kz2wdprison.framework.configuration.PluginConfiguration
import com.cludivers.kz2wdprison.modules.player.PlayerBean
import jakarta.persistence.*
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.hibernate.annotations.CreationTimestamp
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

@Entity
class NationAttack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @ManyToOne
    var targetNation: NationBean? = null

    @ManyToOne
    var attackingPlayer: PlayerBean? = null

    var shardAmount: Int = 0

    var attackTier: ProtectionTiers = ProtectionTiers.TIER2

    @CreationTimestamp
    var creationDate: Date? = null

    private fun getRemainingTime(): Duration {
        return Duration.between(creationDate!!.toInstant(), LocalDateTime.now())
    }

    fun compactDescription(): Component {
        return Component.text(
            "${attackTier.name}: $shardAmount [${getRemainingTime()}] par ${
                Bukkit.getPlayer(
                    attackingPlayer!!.uuid!!
                )?.name
            }."
        )
    }

    companion object {
        fun createAttack(target: NationBean, attacker: PlayerBean, amount: Int): NationAttack {
            val attack = NationAttack()
            attack.attackingPlayer = attacker
            attack.targetNation = target
            attack.attackTier = target.protectionTier
            attack.shardAmount = amount

            PluginConfiguration.session.beginTransaction()
            PluginConfiguration.session.persist(attack)
            PluginConfiguration.session.transaction.commit()

            target.messagePlayers(
                Component.text("Une attaque à été lancée contre votre nation.").appendNewline().append(
                    attack.compactDescription()
                )
            )

            return attack
        }
    }

}