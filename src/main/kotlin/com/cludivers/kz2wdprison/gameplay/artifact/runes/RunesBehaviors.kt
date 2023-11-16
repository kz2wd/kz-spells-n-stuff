package com.cludivers.kz2wdprison.gameplay.artifact.runes

import com.cludivers.kz2wdprison.gameplay.CustomShardItems
import com.cludivers.kz2wdprison.gameplay.artifact.ArtifactActivator
import com.cludivers.kz2wdprison.gameplay.artifact.ArtifactInput
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
    //<editor-fold desc="ENTITY_CASTER" defaultstate="collapsed">
    ENTITY_CASTER {
        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            repeat(inputRune.amount) { input.entities.add(artifactActivator.getSelf()) }
        }
    },

    //</editor-fold>
    //<editor-fold desc="ENTITIES_IN_SIGHT" defaultstate="collapsed">
    ENTITIES_IN_SIGHT {
        override val durationBase = Duration.ofSeconds(2)!!
        override val requirement = RuneRequirements.ENTITY
        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            input.enableRequirements = false
            val entity = input.entities.removeLastOrNull() ?: return
            input.entities.add((entity as LivingEntity).getTargetEntity(inputRune.amount) ?: return)


        }
    },

    //</editor-fold>
    //<editor-fold desc="LOCATION_SIGHT" defaultstate="collapsed">
    LOCATION_SIGHT {
        override val durationBase = Duration.ofSeconds(1)!!
        override val requirement: RuneRequirements = RuneRequirements.ENTITY
        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            input.enableRequirements = false
            val entity = input.entities.removeLastOrNull() ?: return
            val block = (entity as LivingEntity).getTargetBlockExact(inputRune.amount) ?: return
            input.locations.add(block.location)
        }
    },

    //</editor-fold>
    //<editor-fold desc="EMPTY_LOCATION_SIGHT" defaultstate="collapsed">
    EMPTY_LOCATION_SIGHT {
        override val durationBase = Duration.ofSeconds(1)!!
        override val requirement: RuneRequirements = RuneRequirements.ENTITY
        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            input.enableRequirements = false
            val entity = input.entities.removeLastOrNull() ?: return
            val block = (entity as LivingEntity).getLastTwoTargetBlocks(null, inputRune.amount)[0]
            input.locations.add(block.location)
        }
    },

    //</editor-fold>
    //<editor-fold desc="PROJECTILE_CASTING" defaultstate="collapsed">
    PROJECTILE_CASTING {
        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            repeat(inputRune.amount) {
                val entity = input.entities.removeLastOrNull() ?: return
                input.locations.add(entity.location)
                input.directions.add(entity.location.direction)
            }
        }
    },

    //</editor-fold>
    //<editor-fold desc="CASTER_PROJECTILE" defaultstate="collapsed">
    CASTER_PROJECTILE {
        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            var forwardEyeLocation = (artifactActivator.getSelf() as Player).eyeLocation
            forwardEyeLocation = forwardEyeLocation.add(forwardEyeLocation.direction)
            repeat(inputRune.amount) {
                input.locations.add(forwardEyeLocation)
                input.directions.add(artifactActivator.getLocation().direction)
            }
        }
    },

    //</editor-fold>
    //<editor-fold desc="CASTER_DIRECTION" defaultstate="collapsed">
    CASTER_DIRECTION {

        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            repeat(inputRune.amount) {
                input.directions.add(artifactActivator.getLocation().direction)
            }
        }
    },

    //</editor-fold>
    //<editor-fold desc="ENTITIES_POSITION">
    ENTITIES_POSITION {
        override val requirement = RuneRequirements.ENTITY
        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            val locationsToAdd: MutableList<Location> = mutableListOf()
            repeat(inputRune.amount) {
                val entity = input.entities.removeLastOrNull() ?: return
                locationsToAdd.add(entity.location)
            }
            input.locations.addAll(locationsToAdd)
        }
    },

    //</editor-fold>
    //<editor-fold desc="ENTITIES_DIRECTION" defaultstate="collapsed">
    ENTITIES_DIRECTION {
        override val requirement = RuneRequirements.ENTITY
        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            val directionsToAdd: MutableList<Vector> = mutableListOf()
            repeat(inputRune.amount) {
                val entity = input.entities.removeLastOrNull() ?: return
                directionsToAdd.add(entity.location.direction)
            }
            input.directions.addAll(directionsToAdd)
        }
    },

    //</editor-fold>
    //<editor-fold desc="LOCATIONS_BELOW" defaultstate="collapsed">
    LOCATIONS_BELOW {
        override val durationBase = Duration.ofMillis(200)!!
        override val requirement = RuneRequirements.LOCATION
        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            val location = input.locations.lastOrNull() ?: return
            location.add(0.0, -inputRune.amount.toDouble(), 0.0)
        }
    },

    //</editor-fold>
    //<editor-fold desc="LOCATIONS_ABOVE" defaultstate="collapsed">
    LOCATIONS_ABOVE {
        override val durationBase = Duration.ofMillis(200)!!
        override val requirement = RuneRequirements.LOCATION
        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            val location = input.locations.lastOrNull() ?: return
            location.add(0.0, inputRune.amount.toDouble(), 0.0)
        }
    },
    //</editor-fold>
    //<editor-fold desc="LOCATIONS_IN_FRONT" defaultstate="collapsed">
    LOCATIONS_IN_FRONT {
        override val durationBase = Duration.ofMillis(200)!!
        override val requirement = RuneRequirements.LOCATION_DIRECTION
        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            if (input.locations.size == 0 || input.directions.size == 0) return
            val location = input.locations.lastOrNull() ?: return
            val direction = input.directions.lastOrNull() ?: return
            location.add(direction.multiply(Vector(1, 0, 1)).normalize().multiply(inputRune.amount))
        }
    },
    //</editor-fold>
    //<editor-fold desc="LOCATI0NS_BEHIND" defaultstate="collapsed">
    LOCATIONS_BEHIND {
        override val durationBase = Duration.ofMillis(200)!!
        override val requirement = RuneRequirements.LOCATION_DIRECTION
        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            if (input.locations.size == 0 || input.directions.size == 0) return
            val location = input.locations.lastOrNull() ?: return
            val direction = input.directions.lastOrNull() ?: return
            location.subtract(direction.multiply(Vector(1, 0, 1)).normalize().multiply(inputRune.amount))
        }
    },
    //</editor-fold>
    //<editor-fold desc="LOCATION_AROUND" defaultstate="collapsed">
    LOCATION_AROUND {
        override val durationBase = Duration.ofSeconds(3)!!
        override val requirement = RuneRequirements.LOCATION
        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            val location = input.locations.removeLastOrNull() ?: return
            input.locations.addAll(locationAround(location, min(inputRune.amount, 3))) // Hard cap to prevent lag
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
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            val location = input.locations.removeLastOrNull() ?: return
            input.locations.addAll(locationAroundFlat(location, min(inputRune.amount, 10)))
        }
    },
    //</editor-fold>
    //<editor-fold desc="ENTITIES_AROUND", defaultstate="collapsed">
    ENTITIES_AROUND {
        override val durationBase = Duration.ofSeconds(3)!!
        override val requirement = RuneRequirements.ENTITY
        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            val a = inputRune.amount.toDouble()
            val entity = input.entities.removeLastOrNull() ?: return
            input.entities.addAll(entity.getNearbyEntities(a, a, a))
        }
    },
    //</editor-fold>
    //<editor-fold desc="DOWN_DIRECTION" defaultstate="collapsed">
    DOWN_DIRECTION {
        override val durationBase = Duration.ofMillis(200)!!
        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            repeat(inputRune.amount) {
                input.directions.add(Vector(0, -1, 0))
            }
        }
    },
    //</editor-fold>
    //<editor-fold desc="UP_DIRECTION" defaultstate="collapsed">
    UP_DIRECTION {
        override val durationBase = Duration.ofMillis(200)!!
        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            repeat(inputRune.amount) {
                input.directions.add(Vector(0, 1, 0))
            }
        }
    },
    //</editor-fold>
    //<editor-fold desc="INVERT_DIRECTION" defaultstate="collapsed">
    INVERT_DIRECTION {
        override val durationBase = Duration.ofMillis(200)!!
        override val requirement = RuneRequirements.DIRECTION
        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            val directionsToAdd: MutableList<Vector> = mutableListOf()
            repeat(inputRune.amount) {
                val direction = input.directions.removeLastOrNull() ?: return
                directionsToAdd.add(direction.multiply(-1))
            }
            input.directions.addAll(directionsToAdd)
        }
    },
    //</editor-fold>
    //<editor-fold desc="DOUBLE_DIRECTION" defaultstate="collapsed">
    DOUBLE_DIRECTION {
        override val durationBase = Duration.ofSeconds(1)!!
        override val requirement = RuneRequirements.DIRECTION
        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            val directionsToAdd: MutableList<Vector> = mutableListOf()
            repeat(inputRune.amount) {
                val direction = input.directions.removeLastOrNull() ?: return
                directionsToAdd.add(direction.multiply(2))
            }
            input.directions.addAll(directionsToAdd)
        }
    },
    //</editor-fold>
    //<editor-fold desc="HALF_DIRECTION" defaultstate="collapsed">
    HALF_DIRECTION {
        override val durationBase = Duration.ofMillis(200)!!
        override val requirement = RuneRequirements.DIRECTION
        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {

            val directionsToAdd: MutableList<Vector> = mutableListOf()
            repeat(inputRune.amount) {
                val direction = input.directions.removeLastOrNull() ?: return
                directionsToAdd.add(direction.multiply(0.5))
            }
            input.directions.addAll(directionsToAdd)
        }
    },
    //</editor-fold>
    //<editor-fold desc="ENTITY_ATTACKER" defaultstate="collapsed">
    ENTITY_ATTACKER {
        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            repeat(inputRune.amount) {
                input.entities.add(artifactActivator.getAttacker() ?: return)
            }
        }
    },
    //</editor-fold>
    //<editor-fold desc="ENTITY_ATTACKED" defaultstate="collapsed">
    ENTITY_ATTACKED {
        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            repeat(inputRune.amount) {
                input.entities.add(artifactActivator.getAttacked() ?: return)
            }
        }
    },
    //</editor-fold>
    //<editor-fold desc="PLACE_BLOCK" defaultstate="collapsed">
    PLACE_BLOCK {
        override val durationBase = Duration.ofMillis(200)!!
        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            val player = artifactActivator.getPermission() ?: return
            repeat(inputRune.amount) {
                val location = input.locations.removeLastOrNull() ?: return
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
                    location.block.type = inputRune.type
                }
            }
        }
    },
    //</editor-fold>
    //<editor-fold desc="LAUNCH_PROJECTILE" defaultstate="collapsed">
    LAUNCH_PROJECTILE {
        private var projectiles: MutableList<Projectile> = emptyList<Projectile>().toMutableList()

        private fun launchProjectile(
            input: ArtifactInput,
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
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            repeat(inputRune.amount) {
                when (inputRune.type) {
                    Material.ARROW -> launchProjectile(input, EntityType.ARROW, Duration.ofSeconds(1)) ?: return
                    Material.SPECTRAL_ARROW -> launchProjectile(input, EntityType.SPECTRAL_ARROW, Duration.ofSeconds(1))
                        ?: return

                    Material.TIPPED_ARROW -> {
                        val projectile = launchProjectile(input, EntityType.ARROW, Duration.ofSeconds(1)) ?: return
                        val arrow = projectile as Arrow
                        val blueprint = inputRune as Arrow
                        arrow.basePotionType = blueprint.basePotionType
                    }

                    Material.SPLASH_POTION -> {
                        val projectile =
                            launchProjectile(input, EntityType.SPLASH_POTION, Duration.ofSeconds(1)) ?: return
//                        val potion = projectile as ThrownPotion
                        // Add potion effects . . .
//                        potion.effects =
                    }

                    Material.FIRE_CHARGE -> launchProjectile(input, EntityType.ARROW, Duration.ofSeconds(1)) ?: return
                    Material.SNOWBALL -> launchProjectile(input, EntityType.SNOWBALL, Duration.ZERO) ?: return

                    else -> {}
                }
            }
        }

        override fun triggerNext(nextActivation: ((ArtifactInput) -> ArtifactInput) -> Unit) {
            ArtifactListener.trackedProjectile.putAll(projectiles.associateWith { nextActivation })
            projectiles.clear()
        }
    },

    //</editor-fold>
    //<editor-fold desc="EAT_FOOD" defaultstate="collapsed">
    EAT_FOOD {
        override val durationBase = Duration.ofSeconds(10)!!
        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            repeat(inputRune.amount) {
                val entity = input.entities.removeLastOrNull() ?: return
                foodEffect(entity, inputRune.type)
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
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
        ) {
            repeat(inputRune.amount) {
                val entity = input.entities.removeLastOrNull() ?: return
                potionEffect(entity, inputRune)
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
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
        ) {
            val player = artifactActivator.getPermission() ?: return

            when (inputRune.type) {
                Material.DIAMOND_PICKAXE -> {
                    input.locations.forEach {
                        if (it.block.type == Material.BEDROCK) { // Blacklist blocks here
                            return@forEach
                        }
                        val event = BlockBreakEvent(it.block, player)
                        Bukkit.getPluginManager().callEvent(event)
                        if (!event.isCancelled) {
                            it.block.breakNaturally(inputRune)
                        }
                    }
                }

                Material.FLINT_AND_STEEL -> {
                    input.locations.forEach {
                        if (it.block.type == Material.AIR) {
                            it.block.type = Material.FIRE
                        }
                    }
                    input.entities.forEach {
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
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            val entity = entityTypeFromItemStack(inputRune)
            if (entity === null) {
                return
            }
            repeat(inputRune.amount) {
                val location = input.locations.removeLastOrNull() ?: return
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
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            repeat(inputRune.amount) {
                val location = input.locations.removeLastOrNull() ?: return
                location.world.strikeLightning(location)
            }
        }
    },
    //</editor-fold>
    //<editor-fold desc="MOVE_RUNE" defaultstate="collapsed">
    MOVE_RUNE {
        override val durationBase = Duration.ofSeconds(5)!!
        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            repeat(inputRune.amount) {
                if (input.entities.size == 0 || input.directions.size == 0) return
                val entity = input.entities.removeLastOrNull() ?: return
                val direction = input.directions.removeLastOrNull() ?: return
                entity.velocity = entity.velocity.add(direction)
            }
        }
    },
    //</editor-fold>
    //<editor-fold desc="NONE" defaultstate="collapsed">
    NONE {
        override fun artifactActivationWithRequirements(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>
        ) {
            return
        }
    },
    //</editor-fold>
    ;

    protected open val requirement: RuneRequirements = RuneRequirements.NONE

    override fun triggerNext(nextActivation: ((ArtifactInput) -> ArtifactInput) -> Unit) {
        nextActivation(ArtifactInput::sameInput)
    }

    open val durationBase: Duration = Duration.ZERO
    override fun addDuration(input: ArtifactInput) {
        input.duration = input.duration.plus(durationBase)
    }

    override fun processArtifactActivation(
            inputRune: ItemStack,
            artifactActivator: ArtifactActivator,
            input: ArtifactInput,
            inputsTrace: MutableList<ItemStack>,
            player: Player?,
            nextActivation: ((ArtifactInput) -> ArtifactInput) -> Unit
    ) {
        requirement.ensureRequirement(input, artifactActivator)
        artifactActivationWithRequirements(inputRune, artifactActivator, input, inputsTrace)
        addDuration(input)
        triggerNext(nextActivation)
    }

    protected abstract fun artifactActivationWithRequirements(
        inputRune: ItemStack,
        artifactActivator: ArtifactActivator,
        input: ArtifactInput,
        inputsTrace: MutableList<ItemStack>,
    )

    companion object {

        fun processArtifactActivation(
                inputRune: ItemStack,
                artifactActivator: ArtifactActivator,
                input: ArtifactInput,
                inputsTrace: MutableList<ItemStack>,
                player: Player?,
                nextActivation: ((ArtifactInput) -> ArtifactInput) -> Unit
        ) {
            if (inputRune.itemMeta == null) {
                return
            }

            // Resolve rune and apply effects
            val rune = getArtifactRune(inputRune) ?: return
            rune.processArtifactActivation(
                inputRune,
                artifactActivator,
                input,
                inputsTrace,
                player,
                nextActivation
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


        private fun addDuration(duration: Duration, input: ArtifactInput) {
            input.duration = input.duration.plus(duration)
        }
    }
}