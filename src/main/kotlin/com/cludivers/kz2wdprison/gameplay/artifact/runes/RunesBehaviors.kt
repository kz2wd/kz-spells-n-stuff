package com.cludivers.kz2wdprison.gameplay.artifact.runes

import com.cludivers.kz2wdprison.gameplay.CustomShardItems
import com.cludivers.kz2wdprison.gameplay.artifact.ArtifactExecution
import com.cludivers.kz2wdprison.gameplay.artifact.listeners.ArtifactListener
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.*
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector
import java.time.Duration
import kotlin.math.min

enum class RunesBehaviors : ArtifactRuneInterface {
    // Entities
    //<editor-fold desc="ENTITY_CASTER" defaultstate="collapsed">
    ENTITY_CASTER {
        override fun artifactActivationWithRequirements(
            execution: ArtifactExecution
        ) {
            repeat(execution.inputRune.amount) { execution.entities.add(execution.activator.getSelf()) }
        }
    },
    //</editor-fold>
    //<editor-fold desc="ENTITY_ATTACKER" defaultstate="collapsed">
    ENTITY_ATTACKER {
        override fun artifactActivationWithRequirements(
            execution: ArtifactExecution
        ) {
            repeat(execution.inputRune.amount) {
                execution.entities.add(execution.activator.getAttacker() ?: return)
            }
        }
    },
    //</editor-fold>
    //<editor-fold desc="ENTITY_ATTACKED" defaultstate="collapsed">
    ENTITY_ATTACKED {
        override fun artifactActivationWithRequirements(
            execution: ArtifactExecution
        ) {
            repeat(execution.inputRune.amount) {
                execution.entities.add(execution.activator.getAttacked() ?: return)
            }
        }
    },
    //</editor-fold>
    //<editor-fold desc="ENTITIES_IN_SIGHT" defaultstate="collapsed">
    ENTITIES_IN_SIGHT {
        override val durationBase = Duration.ofSeconds(2)!!
        override val requirement = RuneRequirements.ENTITY
        override fun artifactActivationWithRequirements(
            execution: ArtifactExecution
        ) {
            execution.enableRequirements = false
            val entity = execution.entities.removeLastOrNull() ?: return
            execution.entities.add((entity as LivingEntity).getTargetEntity(execution.inputRune.amount) ?: return)


        }
    },

    //</editor-fold>
    //<editor-fold desc="ENTITIES_AROUND", defaultstate="collapsed">
    ENTITIES_AROUND {
        override val durationBase = Duration.ofSeconds(3)!!
        override val requirement = RuneRequirements.ENTITY
        override fun artifactActivationWithRequirements(
            execution: ArtifactExecution
        ) {
            val a = execution.inputRune.amount.toDouble()
            val entity = execution.entities.removeLastOrNull() ?: return
            execution.entities.addAll(entity.getNearbyEntities(a, a, a))
        }
    },
    //</editor-fold>
    //<editor-fold desc="REMOVE_ENTITY", defaultstate="collapsed">
    REMOVE_ENTITY {
        override fun artifactActivationWithRequirements(
            execution: ArtifactExecution
        ) {
            repeat(execution.inputRune.amount) {
                execution.entities.removeLastOrNull() ?: return
            }
        }
    },
    //</editor-fold>
    //<editor-fold desc="COPY_ENTITY", defaultstate="collapsed">
    COPY_ENTITY {
        override fun artifactActivationWithRequirements(
            execution: ArtifactExecution
        ) {
            repeat(execution.inputRune.amount) {
                execution.entities.add(execution.entities.last())
            }
        }
    },
    //</editor-fold>

    // Locations
    //<editor-fold desc="ENTITIES_LOCATION">
    ENTITIES_LOCATION {
        override val requirement = RuneRequirements.ENTITY
        override fun artifactActivationWithRequirements(
            execution: ArtifactExecution
        ) {
            val locationsToAdd: MutableList<Location> = mutableListOf()
            repeat(execution.inputRune.amount) {
                val entity = execution.entities.removeLastOrNull() ?: return
                locationsToAdd.add(entity.location)
            }
            execution.locations.addAll(locationsToAdd)
        }
    },
    //</editor-fold>
    //<editor-fold desc="EMPTY_LOCATION_SIGHT" defaultstate="collapsed">
    EMPTY_LOCATION_SIGHT {
        override val durationBase = Duration.ofSeconds(1)!!
        override val requirement: RuneRequirements = RuneRequirements.ENTITY
        override fun artifactActivationWithRequirements(
            execution: ArtifactExecution
        ) {
            execution.enableRequirements = false
            val entity = execution.entities.removeLastOrNull() ?: return
            val block = (entity as LivingEntity).getLastTwoTargetBlocks(null, execution.inputRune.amount)[0]
            execution.locations.add(block.location)
        }
    },

    //</editor-fold>
    //<editor-fold desc="LOCATION_SIGHT" defaultstate="collapsed">
    LOCATION_SIGHT {
        override val durationBase = Duration.ofSeconds(1)!!
        override val requirement: RuneRequirements = RuneRequirements.ENTITY
        override fun artifactActivationWithRequirements(
            execution: ArtifactExecution
        ) {
            execution.enableRequirements = false
            val entity = execution.entities.removeLastOrNull() ?: return
            val block = (entity as LivingEntity).getTargetBlockExact(execution.inputRune.amount) ?: return
            execution.locations.add(block.location)
        }
    },

    //</editor-fold>
    //<editor-fold desc="LOCATIONS_BELOW" defaultstate="collapsed">
    LOCATIONS_BELOW {
        override val durationBase = Duration.ofMillis(200)!!
        override val requirement = RuneRequirements.LOCATION
        override fun artifactActivationWithRequirements(
            execution: ArtifactExecution
        ) {
            val location = execution.locations.lastOrNull() ?: return
            location.add(0.0, -execution.inputRune.amount.toDouble(), 0.0)
        }
    },

    //</editor-fold>
    //<editor-fold desc="LOCATIONS_ABOVE" defaultstate="collapsed">
    LOCATIONS_ABOVE {
        override val durationBase = Duration.ofMillis(200)!!
        override val requirement = RuneRequirements.LOCATION
        override fun artifactActivationWithRequirements(
            execution: ArtifactExecution
        ) {
            val location = execution.locations.lastOrNull() ?: return
            location.add(0.0, execution.inputRune.amount.toDouble(), 0.0)
        }
    },
    //</editor-fold>
    //<editor-fold desc="LOCATIONS_IN_FRONT" defaultstate="collapsed">
    LOCATIONS_IN_FRONT {
        override val durationBase = Duration.ofMillis(200)!!
        override val requirement = RuneRequirements.LOCATION_DIRECTION
        override fun artifactActivationWithRequirements(
            execution: ArtifactExecution
        ) {
            if (execution.locations.size == 0 || execution.directions.size == 0) return
            val location = execution.locations.lastOrNull() ?: return
            val direction = execution.directions.lastOrNull() ?: return
            location.add(direction.multiply(Vector(1, 0, 1)).normalize().multiply(execution.inputRune.amount))
        }
    },
    //</editor-fold>
    //<editor-fold desc="LOCATI0NS_BEHIND" defaultstate="collapsed">
    LOCATIONS_BEHIND {
        override val durationBase = Duration.ofMillis(200)!!
        override val requirement = RuneRequirements.LOCATION_DIRECTION
        override fun artifactActivationWithRequirements(
            execution: ArtifactExecution
        ) {
            if (execution.locations.size == 0 || execution.directions.size == 0) return
            val location = execution.locations.lastOrNull() ?: return
            val direction = execution.directions.lastOrNull() ?: return
            location.subtract(direction.multiply(Vector(1, 0, 1)).normalize().multiply(execution.inputRune.amount))
        }
    },
    //</editor-fold>
    //<editor-fold desc="LOCATION_AROUND" defaultstate="collapsed">
    LOCATION_AROUND {
        override val durationBase = Duration.ofSeconds(3)!!
        override val requirement = RuneRequirements.LOCATION
        override fun artifactActivationWithRequirements(
            execution: ArtifactExecution
        ) {
            val location = execution.locations.removeLastOrNull() ?: return
            execution.locations.addAll(
                locationAround(
                    location,
                    min(execution.inputRune.amount, 3)
                )
            ) // Hard cap to prevent lag
        }

        private fun locationAround(location: Location, radius: Int): List<Location> {
            return (-radius..radius).map { locationAroundFlat(location.clone().add(Vector(0, it, 0)), radius) }
                .flatten()
        }
    },
    //</editor-fold>
    //<editor-fold desc="LOCATION_AROUND_FLAT" defaultstate="collapsed">
    LOCATION_AROUND_FLAT {
        override val durationBase = Duration.ofSeconds(1)!!
        override val requirement = RuneRequirements.LOCATION
        override fun artifactActivationWithRequirements(
            execution: ArtifactExecution
        ) {
            val location = execution.locations.removeLastOrNull() ?: return
            execution.locations.addAll(locationAroundFlat(location, min(execution.inputRune.amount, 10)))
        }
    },
    //</editor-fold>
    //<editor-fold desc="REMOVE_LOCATION", defaultstate="collapsed">
    REMOVE_LOCATION {
        override fun artifactActivationWithRequirements(
            execution: ArtifactExecution
        ) {
            repeat(execution.inputRune.amount) {
                execution.locations.removeLastOrNull() ?: return
            }
        }
    },

    //</editor-fold>
    //<editor-fold desc="COPY_LOCATION", defaultstate="collapsed">
    COPY_LOCATION {
        override fun artifactActivationWithRequirements(
            execution: ArtifactExecution
        ) {
            repeat(execution.inputRune.amount) {
                execution.locations.add(execution.locations.last())
            }
        }
    },
    //</editor-fold>

    // Directions
    //<editor-fold desc="CASTER_DIRECTION" defaultstate="collapsed">
    CASTER_DIRECTION {

        override fun artifactActivationWithRequirements(
            execution: ArtifactExecution
        ) {
            repeat(execution.inputRune.amount) {
                execution.directions.add(execution.activator.getLocation().direction)
            }
        }
    },

    //</editor-fold>
    //<editor-fold desc="ENTITIES_DIRECTION" defaultstate="collapsed">
    ENTITIES_DIRECTION {
        override val requirement = RuneRequirements.ENTITY
        override fun artifactActivationWithRequirements(
            execution: ArtifactExecution
        ) {
            val directionsToAdd: MutableList<Vector> = mutableListOf()
            repeat(execution.inputRune.amount) {
                val entity = execution.entities.removeLastOrNull() ?: return
                directionsToAdd.add(entity.location.direction)
            }
            execution.directions.addAll(directionsToAdd)
        }
    },

    //</editor-fold>
    //<editor-fold desc="DOWN_DIRECTION" defaultstate="collapsed">
    DOWN_DIRECTION {
        override val durationBase = Duration.ofMillis(200)!!
        override fun artifactActivationWithRequirements(
            execution: ArtifactExecution
        ) {
            repeat(execution.inputRune.amount) {
                execution.directions.add(Vector(0, -1, 0))
            }
        }
    },
    //</editor-fold>
    //<editor-fold desc="UP_DIRECTION" defaultstate="collapsed">
    UP_DIRECTION {
        override val durationBase = Duration.ofMillis(200)!!
        override fun artifactActivationWithRequirements(
            execution: ArtifactExecution
        ) {
            repeat(execution.inputRune.amount) {
                execution.directions.add(Vector(0, 1, 0))
            }
        }
    },
    //</editor-fold>
    //<editor-fold desc="INVERT_DIRECTION" defaultstate="collapsed">
    INVERT_DIRECTION {
        override val durationBase = Duration.ofMillis(200)!!
        override val requirement = RuneRequirements.DIRECTION
        override fun artifactActivationWithRequirements(
            execution: ArtifactExecution
        ) {
            val directionsToAdd: MutableList<Vector> = mutableListOf()
            repeat(execution.inputRune.amount) {
                val direction = execution.directions.removeLastOrNull() ?: return
                directionsToAdd.add(direction.multiply(-1))
            }
            execution.directions.addAll(directionsToAdd)
        }
    },
    //</editor-fold>
    //<editor-fold desc="DOUBLE_DIRECTION" defaultstate="collapsed">
    DOUBLE_DIRECTION {
        override val durationBase = Duration.ofSeconds(1)!!
        override val requirement = RuneRequirements.DIRECTION
        override fun artifactActivationWithRequirements(
            execution: ArtifactExecution
        ) {
            val directionsToAdd: MutableList<Vector> = mutableListOf()
            repeat(execution.inputRune.amount) {
                val direction = execution.directions.removeLastOrNull() ?: return
                directionsToAdd.add(direction.add(direction.normalize()))
            }
            execution.directions.addAll(directionsToAdd)
        }
    },
    //</editor-fold>
    //<editor-fold desc="HALF_DIRECTION" defaultstate="collapsed">
    HALF_DIRECTION {
        override val durationBase = Duration.ofMillis(200)!!
        override val requirement = RuneRequirements.DIRECTION
        override fun artifactActivationWithRequirements(
            execution: ArtifactExecution
        ) {

            val directionsToAdd: MutableList<Vector> = mutableListOf()
            repeat(execution.inputRune.amount) {
                val direction = execution.directions.removeLastOrNull() ?: return
                directionsToAdd.add(direction.multiply(0.5))
            }
            execution.directions.addAll(directionsToAdd)
        }
    },
    //</editor-fold>
    //<editor-fold desc="REMOVE_DIRECTIONS", defaultstate="collapsed">
    REMOVE_DIRECTION {
        override fun artifactActivationWithRequirements(
            execution: ArtifactExecution
        ) {
            repeat(execution.inputRune.amount) {
                execution.directions.removeLastOrNull() ?: return
            }
        }
    },

    //</editor-fold>
    //<editor-fold desc="COPY_DIRECTIONS", defaultstate="collapsed">
    COPY_DIRECTION {
        override fun artifactActivationWithRequirements(
            execution: ArtifactExecution
        ) {
            repeat(execution.inputRune.amount) {
                execution.directions.add(execution.directions.last())
            }
        }
    },
    //</editor-fold>

    // Casting projectile
    //<editor-fold desc="CASTER_PROJECTILE" defaultstate="collapsed">
    CASTER_PROJECTILE {
        override fun artifactActivationWithRequirements(
            execution: ArtifactExecution
        ) {
            var forwardEyeLocation = (execution.activator.getSelf() as Player).eyeLocation
            forwardEyeLocation = forwardEyeLocation.add(forwardEyeLocation.direction)
            repeat(execution.inputRune.amount) {
                execution.locations.add(forwardEyeLocation)
                execution.directions.add(execution.activator.getLocation().direction)
            }
        }
    },

    //</editor-fold>
    //<editor-fold desc="PROJECTILE_CASTING" defaultstate="collapsed">
    PROJECTILE_CASTING {
        override fun artifactActivationWithRequirements(
            execution: ArtifactExecution
        ) {
            repeat(execution.inputRune.amount) {
                val entity = execution.entities.removeLastOrNull() ?: return
                execution.locations.add(entity.location)
                execution.directions.add(entity.location.direction)
            }
        }
    },

    //</editor-fold>

    // Effect
    //<editor-fold desc="PLACE_BLOCK" defaultstate="collapsed">
    PLACE_BLOCK {
        override val durationBase = Duration.ofMillis(200)!!
        override fun artifactActivationWithRequirements(
            execution: ArtifactExecution
        ) {
            val player = execution.activator.getPermission() ?: return
            repeat(execution.inputRune.amount) {
                val location = execution.locations.removeLastOrNull() ?: return
                val event = BlockPlaceEvent(
                    location.block,
                    location.block.state,
                    location.block,
                    player.inventory.itemInMainHand,
                    player,
                    true,
                    EquipmentSlot.HAND
                )
                Bukkit.getPluginManager().callEvent(event)
                if (!event.isCancelled) {
                    location.block.type = execution.inputRune.type
                }
            }
        }
    },
    //</editor-fold>
    //<editor-fold desc="LAUNCH_PROJECTILE" defaultstate="collapsed">
    LAUNCH_PROJECTILE {
        private var projectiles: MutableList<Projectile> = emptyList<Projectile>().toMutableList()

        private fun launchProjectile(
            input: ArtifactExecution,
            entityType: EntityType,
            bonusDuration: Duration
        ): Projectile? {
            if (input.locations.size == 0 || input.directions.size == 0) return null
            val location = input.locations.removeLastOrNull() ?: return null
            val direction = input.directions.removeLastOrNull() ?: return null
            val projectile = location.world.spawnEntity(location, entityType) as Projectile
            projectile.velocity = direction
            projectiles.add(projectile)
            Companion.addDuration(bonusDuration, input)
            return projectile
        }

        override fun artifactActivationWithRequirements(
            execution: ArtifactExecution
        ) {
            repeat(execution.inputRune.amount) {
                when (execution.inputRune.type) {
                    Material.ARROW -> launchProjectile(execution, EntityType.ARROW, Duration.ofSeconds(1)) ?: return
                    Material.SPECTRAL_ARROW -> launchProjectile(
                        execution,
                        EntityType.SPECTRAL_ARROW,
                        Duration.ofSeconds(1)
                    )
                        ?: return

                    Material.TIPPED_ARROW -> {
                        val projectile = launchProjectile(execution, EntityType.ARROW, Duration.ofSeconds(1)) ?: return
                        val arrow = projectile as Arrow
                        val blueprint = execution.inputRune as Arrow
                        arrow.basePotionType = blueprint.basePotionType
                    }

                    Material.SPLASH_POTION -> {
                        val projectile =
                            launchProjectile(execution, EntityType.SPLASH_POTION, Duration.ofSeconds(1)) ?: return
//                        val potion = projectile as ThrownPotion
                        // Add potion effects . . .
//                        potion.effects =
                    }

                    Material.FIRE_CHARGE -> launchProjectile(execution, EntityType.ARROW, Duration.ofSeconds(1))
                        ?: return

                    Material.SNOWBALL -> launchProjectile(execution, EntityType.SNOWBALL, Duration.ZERO) ?: return

                    else -> {}
                }
            }
        }

        override fun triggerNext(execution: ArtifactExecution) {
            ArtifactListener.trackedProjectile.putAll(projectiles.associateWith { { execution.nextActivation() } })
            projectiles.clear()
        }
    },
    //</editor-fold>
    //<editor-fold desc="EAT_FOOD" defaultstate="collapsed">
    EAT_FOOD {
        override val durationBase = Duration.ofSeconds(10)!!
        override fun artifactActivationWithRequirements(
            execution: ArtifactExecution
        ) {
            repeat(execution.inputRune.amount) {
                val entity = execution.entities.removeLastOrNull() ?: return
                foodEffect(entity, execution.inputRune.type)
            }
        }

        private fun feedPlayer(player: Player, saturationDelta: Float, foodDelta: Int) {
            player.saturation += saturationDelta
            player.foodLevel += foodDelta
        }

        private fun foodEffect(entity: Entity, material: Material) {
            if (entity !is Player) {
                return
            }
            when (material) {
                Material.APPLE -> feedPlayer(entity, 2.4f, 4)
                Material.BAKED_POTATO -> feedPlayer(entity, 6f, 5)
                Material.BEETROOT -> feedPlayer(entity, 1.2f, 1)
                Material.BEETROOT_SOUP -> feedPlayer(entity, 7.2f, 6)
                Material.BREAD -> feedPlayer(entity, 5f, 6)
                Material.CAKE -> feedPlayer(entity, 0.4f, 2)
                Material.CARROT -> feedPlayer(entity, 3.6f, 3)
                Material.CHORUS_FRUIT -> feedPlayer(entity, 2.4f, 4)
                Material.COOKED_CHICKEN -> feedPlayer(entity, 7.2f, 6)
                Material.COOKED_COD -> feedPlayer(entity, 6f, 5)
                Material.COOKED_MUTTON -> feedPlayer(entity, 9.6f, 6)
                Material.COOKED_PORKCHOP -> feedPlayer(entity, 12.8f, 8)
                Material.COOKED_RABBIT -> feedPlayer(entity, 6f, 5)
                Material.COOKED_SALMON -> feedPlayer(entity, 9.6f, 6)
                Material.COOKIE -> feedPlayer(entity, 0.4f, 2)
                Material.DRIED_KELP -> feedPlayer(entity, 0.6f, 1)
                Material.ENCHANTED_GOLDEN_APPLE -> {
                    feedPlayer(entity, 9.6f, 4)
                    entity.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 30 * 20, 3))
                    entity.addPotionEffect(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 300, 0))
                    entity.addPotionEffect(PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 300, 0))
                }

                Material.GOLDEN_APPLE -> {
                    feedPlayer(entity, 9.6f, 4)
                    entity.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 100, 1))
                    entity.addPotionEffect(PotionEffect(PotionEffectType.ABSORPTION, 20 * 120, 0))
                }

                Material.GLOW_BERRIES -> feedPlayer(entity, 0.4f, 2)
                Material.GOLDEN_CARROT -> feedPlayer(entity, 14.4f, 6)
                Material.HONEY_BOTTLE -> feedPlayer(entity, 1.2f, 6)
                Material.MELON_SLICE -> feedPlayer(entity, 1.2f, 2)
                Material.MUSHROOM_STEW -> feedPlayer(entity, 7.2f, 6)
                Material.POISONOUS_POTATO -> feedPlayer(entity, 1.2f, 2)
                Material.POTATO -> feedPlayer(entity, 0.6f, 1)
                Material.PUFFERFISH -> feedPlayer(entity, 0.2f, 1)
                Material.PUMPKIN_PIE -> feedPlayer(entity, 4.8f, 8)
                Material.RABBIT_STEW -> feedPlayer(entity, 12f, 10)
                Material.BEEF -> feedPlayer(entity, 1.8f, 3)
                Material.CHICKEN -> feedPlayer(entity, 1.2f, 2)
                Material.COD -> feedPlayer(entity, 0.4f, 2)
                Material.MUTTON -> feedPlayer(entity, 1.2f, 2)
                Material.PORKCHOP -> feedPlayer(entity, 1.8f, 3)
                Material.RABBIT -> feedPlayer(entity, 1.8f, 3)
                Material.SALMON -> feedPlayer(entity, 0.4f, 2)
                Material.ROTTEN_FLESH -> feedPlayer(entity, 0.8f, 4)
                Material.SPIDER_EYE -> feedPlayer(entity, 3.2f, 2)
                Material.COOKED_BEEF -> feedPlayer(entity, 12.8f, 8)
                Material.SUSPICIOUS_STEW -> feedPlayer(entity, 7.2f, 6)
                Material.SWEET_BERRIES -> feedPlayer(entity, 0.4f, 2)
                Material.TROPICAL_FISH -> feedPlayer(entity, 0.2f, 1)
                else -> {}
            }
        }
    },

    //</editor-fold>
    //<editor-fold desc="DRINK_POTION" defaultstate="collapsed">
    DRINK_POTION {
        override val durationBase = Duration.ofSeconds(10)!!
        override fun artifactActivationWithRequirements(
            execution: ArtifactExecution,
        ) {
            repeat(execution.inputRune.amount) {
                val entity = execution.entities.removeLastOrNull() ?: return
                potionEffect(entity, execution.inputRune)
            }
        }

        private fun potionEffect(entity: Entity, itemStack: ItemStack) {
            if (itemStack.type != Material.POTION) {
                return
            }
            val meta = itemStack.itemMeta as PotionMeta
            (entity as LivingEntity).addPotionEffects(meta.customEffects)
        }
    },

    //</editor-fold>
    //<editor-fold desc="USE_TOOL" defaultstate="collapsed">
    USE_TOOL {
        override val durationBase = Duration.ofMillis(200)!!
        override fun artifactActivationWithRequirements(
            execution: ArtifactExecution,
        ) {
            val player = execution.activator.getPermission() ?: return

            when (execution.inputRune.type) {
                Material.DIAMOND_PICKAXE -> {
                    execution.locations.forEach {
                        if (it.block.type == Material.BEDROCK) { // Blacklist blocks here
                            return@forEach
                        }
                        val event = BlockBreakEvent(it.block, player)
                        Bukkit.getPluginManager().callEvent(event)
                        if (!event.isCancelled) {
                            it.block.breakNaturally(execution.inputRune)
                        }
                    }
                }

                Material.FLINT_AND_STEEL -> {
                    execution.locations.forEach {
                        if (it.block.type == Material.AIR) {
                            it.block.type = Material.FIRE
                        }
                    }
                    execution.entities.forEach {
                        it.fireTicks = 40
                    }
                }

                else -> {}
            }

        }
    },

    //</editor-fold>
    //<editor-fold desc="SUMMON_ENTITY" defaultstate="collapsed">
    SUMMON_ENTITY {
        override val durationBase = Duration.ofSeconds(15)!!
        override fun artifactActivationWithRequirements(
            execution: ArtifactExecution
        ) {
            val entity = entityTypeFromItemStack(execution.inputRune)
            if (entity === null) {
                return
            }
            repeat(execution.inputRune.amount) {
                val location = execution.locations.removeLastOrNull() ?: return
                location.world.spawnEntity(location, entity)
            }
        }

        private fun entityTypeFromItemStack(itemStack: ItemStack): EntityType? {
            return when (itemStack.type) {
                Material.COW_SPAWN_EGG -> EntityType.COW
                Material.CHICKEN_SPAWN_EGG -> EntityType.CHICKEN
                else -> null
            }
        }
    },
    //</editor-fold>
    //<editor-fold desc="LIGHTNING_SPARK" defaultstate="collapsed">
    LIGHTNING_SPARK {
        override val durationBase = Duration.ofSeconds(10)!!
        override fun artifactActivationWithRequirements(
            execution: ArtifactExecution
        ) {
            repeat(execution.inputRune.amount) {
                val location = execution.locations.removeLastOrNull() ?: return
                location.world.strikeLightning(location)
            }
        }
    },
    //</editor-fold>
    //<editor-fold desc="MOVE_RUNE" defaultstate="collapsed">
    MOVE_RUNE {
        override val durationBase = Duration.ofSeconds(5)!!
        override fun artifactActivationWithRequirements(
            execution: ArtifactExecution
        ) {
            repeat(execution.inputRune.amount) {
                if (execution.entities.size == 0 || execution.directions.size == 0) return
                val entity = execution.entities.removeLastOrNull() ?: return
                val direction = execution.directions.removeLastOrNull() ?: return
                entity.velocity = entity.velocity.add(direction)
            }
        }
    },
    //</editor-fold>
    //<editor-fold desc="TELEPORT_RUNE" defaultstate="collapsed">
    TELEPORT_RUNE {
        override val durationBase = Duration.ofSeconds(10)!!
        override fun artifactActivationWithRequirements(
            execution: ArtifactExecution
        ) {
            repeat(execution.inputRune.amount) {
                if (execution.entities.size == 0 || execution.locations.size == 0) return
                val entity = execution.entities.removeLastOrNull() ?: return
                val location = execution.locations.removeLastOrNull() ?: return
                location.direction = entity.location.direction
                entity.teleport(location)
            }
        }
    },
    //</editor-fold>


    // None
    //<editor-fold desc="NONE" defaultstate="collapsed">
    NONE {
        override fun artifactActivationWithRequirements(
            execution: ArtifactExecution
        ) {
            return
        }
    },
    //</editor-fold>
    ;

    protected open val requirement: RuneRequirements = RuneRequirements.NONE

    open val durationBase: Duration = Duration.ZERO
    override fun addDuration(input: ArtifactExecution) {
        input.duration = input.duration.plus(durationBase)
    }

    override fun processArtifactActivation(
        execution: ArtifactExecution
    ) {
        requirement.ensureRequirement(execution)
        artifactActivationWithRequirements(execution)
        addDuration(execution)
        triggerNext(execution)
    }

    protected abstract fun artifactActivationWithRequirements(
        execution: ArtifactExecution,
    )

    override fun triggerNext(execution: ArtifactExecution) {
        execution.nextActivation()
    }

    companion object {

        fun processArtifactActivation(
            execution: ArtifactExecution
        ) {
            if (execution.inputRune.itemMeta == null) {
                return
            }

            // Resolve rune and apply effects
            val rune = getArtifactRune(execution.inputRune) ?: return
            rune.processArtifactActivation(
                execution
            )
        }

        private fun getArtifactRune(itemStack: ItemStack?): ArtifactRuneInterface? {

            if (itemStack == null) return null

            val customShardItems = CustomShardItems.getCustomItemStack(itemStack)
            if (customShardItems != null) {
                return customShardItems.runeBehavior
            }
            // Handle blocks separately, too much cases to be in a when, causes a stackoverflow at compilation
            if (itemStack.type.isBlock) {
                return PLACE_BLOCK
            }

            if (itemStack.type.isEdible) {
                return EAT_FOOD
            }

            return when (itemStack.type) {
                Material.ARROW, Material.SPECTRAL_ARROW, Material.TIPPED_ARROW, Material.SPLASH_POTION, Material.FIRE_CHARGE -> LAUNCH_PROJECTILE

                Material.DIAMOND_PICKAXE, Material.FLINT_AND_STEEL -> USE_TOOL

                // Add all spawn eggs, boring . . .
                Material.COW_SPAWN_EGG, Material.CHICKEN_SPAWN_EGG, Material.GOAT_SPAWN_EGG -> SUMMON_ENTITY
                else -> NONE
            }
        }


        private fun locationAroundFlat(location: Location, radius: Int): List<Location> {
            return (-radius..radius).map { (-radius..radius).map { itt -> Pair(it, itt) } }.flatten()
                .map { location.clone().add(Vector(it.first, 0, it.second)) }
        }


        private fun addDuration(duration: Duration, input: ArtifactExecution) {
            input.duration = input.duration.plus(duration)
        }
    }
}