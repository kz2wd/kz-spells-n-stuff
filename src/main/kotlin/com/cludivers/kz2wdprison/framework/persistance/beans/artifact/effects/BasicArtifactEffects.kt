package com.cludivers.kz2wdprison.framework.persistance.beans.artifact.effects

import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.ArtifactComplexRune
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.ArtifactRuneTypes
import com.cludivers.kz2wdprison.framework.persistance.beans.artifact.inputs.ArtifactInput
import com.cludivers.kz2wdprison.gameplay.artifact.CustomShardItems
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.*
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta


enum class BasicArtifactEffects : ArtifactEffectInterface {
    BLOCKS {
        override fun triggerArtifactEffect(itemStack: ItemStack, input: ArtifactInput, player: Player?) {
            if (player == null) {
                return
            }
            input.locations.forEach {
                // Don't really know how to implement it right, I don't think I have enough data in this scope
                val event = BlockPlaceEvent(
                    it.block,
                    it.block.state,
                    it.block,
                    player.inventory.itemInMainHand,
                    player,
                    true,
                    EquipmentSlot.HAND
                )
                Bukkit.getPluginManager().callEvent(event)
                if (!event.isCancelled) {
                    it.block.type = itemStack.type
                }
            }
        }
    },
    PROJECTILES {
        override fun triggerArtifactEffect(itemStack: ItemStack, input: ArtifactInput, player: Player?) {
            when (itemStack.type) {
                Material.ARROW, Material.SPECTRAL_ARROW, Material.TIPPED_ARROW -> input.locations.zip(input.vectors)
                    .forEach {
                        it.first.world.spawnArrow(
                            it.first,
                            it.second,
                            1f,
                            1f
                        )
                    }

                Material.SPLASH_POTION -> input.locations.zip(input.vectors).forEach {
                    val potion = (it.first.world.spawnEntity(it.first, EntityType.SPLASH_POTION) as ThrownPotion)
                    potion.item = itemStack
                    potion.velocity = it.second
                }

                Material.FIRE_CHARGE -> {
                    input.locations.zip(input.vectors).forEach {
                        val fireball = it.first.world.spawnEntity(it.first, EntityType.SMALL_FIREBALL)
                        fireball.velocity = it.second
                    }
                }

                else -> {}
            }
        }
    },
    CONSUMABLE {
        override fun triggerArtifactEffect(itemStack: ItemStack, input: ArtifactInput, player: Player?) {
            input.entities.forEach { foodEffect(it, itemStack.type) }
            input.entities.forEach { potionEffect(it, itemStack) }
        }

        private fun potionEffect(entity: Entity, itemStack: ItemStack) {
            if (itemStack.type != Material.POTION) {
                return
            }
            val meta = itemStack.itemMeta as PotionMeta
            (entity as LivingEntity).addPotionEffects(meta.customEffects)
        }
    },
    TOOLS {
        override fun triggerArtifactEffect(itemStack: ItemStack, input: ArtifactInput, player: Player?) {
            if (player == null) {
                return
            }
            when (itemStack.type) {
                Material.DIAMOND_PICKAXE -> {
                    input.locations.forEach {
                        if (it.block.type == Material.BEDROCK) { // Blacklist blocks here
                            return
                        }
                        val event = BlockBreakEvent(it.block, player)
                        Bukkit.getPluginManager().callEvent(event)
                        if (!event.isCancelled) {
                            it.block.breakNaturally(itemStack)
                        }
                    }
                }

                Material.FLINT_AND_STEEL -> {
                    input.locations.forEach {
                        if (it.block.type == Material.AIR) {
                            it.block.type = Material.FIRE
                        }
                    }
                }

                else -> {}
            }

        }
    },
    ENTITY {
        override fun triggerArtifactEffect(itemStack: ItemStack, input: ArtifactInput, player: Player?) {
            val entity = entityTypeFromItemStack(itemStack)
            if (entity === null) {
                return
            }
            input.locations.forEach { it.world.spawnEntity(it, entity) }
        }
    },
    CUSTOM {
        override fun triggerArtifactEffect(itemStack: ItemStack, input: ArtifactInput, player: Player?) {
            when (itemStack) {
                CustomShardItems.FIRE_SPARK.itemStack -> {
                    input.entities.forEach { it.fireTicks = 100 }
                }

                CustomShardItems.LIGHTNING_SPARK.itemStack -> {
                    input.locations.forEach { it.world.strikeLightning(it) }
                }

                CustomShardItems.MOVE_RUNE.itemStack -> {
                    input.entities.zip(input.vectors).forEach {

                        it.first.velocity = it.first.velocity.add(it.second.multiply(3))
                    }
                }

                else -> {}

            }
        }
    },
    COMPLEX {
        override fun triggerArtifactEffect(itemStack: ItemStack, input: ArtifactInput, player: Player?) {
            val artifactComplexEffect = ArtifactComplexRune.getComplexRune(itemStack) ?: return

            if (artifactComplexEffect.runeType != ArtifactRuneTypes.GENERIC_EFFECT_RUNE) {
                return
            }
            artifactComplexEffect.triggerArtifactEffect(itemStack, input, player)
        }
    },

    None {
        override fun triggerArtifactEffect(itemStack: ItemStack, input: ArtifactInput, player: Player?) {
            // Do nothing
        }
    };

    companion object {

        private val blocks = setOf(
            Material.STONE, Material.GRANITE, Material.POLISHED_GRANITE, Material.DIORITE,
            Material.POLISHED_DIORITE, Material.ANDESITE, Material.POLISHED_ANDESITE, Material.DEEPSLATE,
            Material.COBBLED_DEEPSLATE, Material.POLISHED_DEEPSLATE, Material.CALCITE, Material.TUFF,
            Material.DRIPSTONE_BLOCK, Material.GRASS_BLOCK, Material.DIRT, Material.COARSE_DIRT, Material.PODZOL,
            Material.ROOTED_DIRT, Material.MUD, Material.CRIMSON_NYLIUM, Material.WARPED_NYLIUM,
            Material.COBBLESTONE, Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.BIRCH_PLANKS,
            Material.JUNGLE_PLANKS, Material.ACACIA_PLANKS, Material.DARK_OAK_PLANKS, Material.MANGROVE_PLANKS,
            Material.BAMBOO_PLANKS, Material.CRIMSON_PLANKS, Material.WARPED_PLANKS, Material.BAMBOO_MOSAIC,
            Material.OAK_SAPLING, Material.SPRUCE_SAPLING, Material.BIRCH_SAPLING, Material.JUNGLE_SAPLING,
            Material.ACACIA_SAPLING, Material.DARK_OAK_SAPLING, Material.MANGROVE_PROPAGULE, Material.SAND,
            Material.RED_SAND, Material.GRAVEL, Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE, Material.IRON_ORE,
            Material.DEEPSLATE_IRON_ORE, Material.COPPER_ORE, Material.DEEPSLATE_COPPER_ORE, Material.GOLD_ORE,
            Material.DEEPSLATE_GOLD_ORE, Material.REDSTONE_ORE, Material.DEEPSLATE_REDSTONE_ORE,
            Material.EMERALD_ORE, Material.DEEPSLATE_EMERALD_ORE, Material.LAPIS_ORE, Material.DEEPSLATE_LAPIS_ORE,
            Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE, Material.NETHER_GOLD_ORE,
            Material.NETHER_QUARTZ_ORE, Material.ANCIENT_DEBRIS, Material.COAL_BLOCK, Material.RAW_IRON_BLOCK,
            Material.RAW_COPPER_BLOCK, Material.RAW_GOLD_BLOCK, Material.AMETHYST_BLOCK, Material.BUDDING_AMETHYST,
            Material.IRON_BLOCK, Material.COPPER_BLOCK, Material.GOLD_BLOCK, Material.DIAMOND_BLOCK,
            Material.NETHERITE_BLOCK, Material.EXPOSED_COPPER, Material.WEATHERED_COPPER, Material.OXIDIZED_COPPER,
            Material.CUT_COPPER, Material.EXPOSED_CUT_COPPER, Material.WEATHERED_CUT_COPPER,
            Material.OXIDIZED_CUT_COPPER, Material.CUT_COPPER_STAIRS, Material.EXPOSED_CUT_COPPER_STAIRS,
            Material.WEATHERED_CUT_COPPER_STAIRS, Material.OXIDIZED_CUT_COPPER_STAIRS, Material.CUT_COPPER_SLAB,
            Material.EXPOSED_CUT_COPPER_SLAB, Material.WEATHERED_CUT_COPPER_SLAB, Material.OXIDIZED_CUT_COPPER_SLAB,
            Material.WAXED_COPPER_BLOCK, Material.WAXED_EXPOSED_COPPER, Material.WAXED_WEATHERED_COPPER,
            Material.WAXED_OXIDIZED_COPPER, Material.WAXED_CUT_COPPER, Material.WAXED_EXPOSED_CUT_COPPER,
            Material.WAXED_WEATHERED_CUT_COPPER, Material.WAXED_OXIDIZED_CUT_COPPER,
            Material.WAXED_CUT_COPPER_STAIRS, Material.WAXED_EXPOSED_CUT_COPPER_STAIRS,
            Material.WAXED_WEATHERED_CUT_COPPER_STAIRS, Material.WAXED_OXIDIZED_CUT_COPPER_STAIRS,
            Material.WAXED_CUT_COPPER_SLAB, Material.WAXED_EXPOSED_CUT_COPPER_SLAB,
            Material.WAXED_WEATHERED_CUT_COPPER_SLAB, Material.WAXED_OXIDIZED_CUT_COPPER_SLAB,
            Material.OAK_LOG, Material.SPRUCE_LOG, Material.BIRCH_LOG, Material.JUNGLE_LOG, Material.ACACIA_LOG,
            Material.DARK_OAK_LOG, Material.MANGROVE_LOG, Material.MANGROVE_ROOTS, Material.MUDDY_MANGROVE_ROOTS,
            Material.CRIMSON_STEM, Material.WARPED_STEM, Material.BAMBOO_BLOCK, Material.STRIPPED_OAK_LOG,
            Material.STRIPPED_SPRUCE_LOG, Material.STRIPPED_BIRCH_LOG, Material.STRIPPED_JUNGLE_LOG,
            Material.STRIPPED_ACACIA_LOG, Material.STRIPPED_DARK_OAK_LOG, Material.STRIPPED_MANGROVE_LOG,
            Material.STRIPPED_CRIMSON_STEM, Material.STRIPPED_WARPED_STEM, Material.STRIPPED_OAK_WOOD,
            Material.STRIPPED_SPRUCE_WOOD, Material.STRIPPED_BIRCH_WOOD, Material.STRIPPED_JUNGLE_WOOD,
            Material.STRIPPED_ACACIA_WOOD, Material.STRIPPED_DARK_OAK_WOOD, Material.STRIPPED_MANGROVE_WOOD,
            Material.STRIPPED_CRIMSON_HYPHAE, Material.STRIPPED_WARPED_HYPHAE, Material.STRIPPED_BAMBOO_BLOCK,
            Material.OAK_WOOD, Material.SPRUCE_WOOD, Material.BIRCH_WOOD, Material.JUNGLE_WOOD,
            Material.ACACIA_WOOD, Material.DARK_OAK_WOOD, Material.MANGROVE_WOOD, Material.CRIMSON_HYPHAE,
            Material.WARPED_HYPHAE, Material.OAK_LEAVES, Material.SPRUCE_LEAVES, Material.BIRCH_LEAVES,
            Material.JUNGLE_LEAVES, Material.ACACIA_LEAVES, Material.DARK_OAK_LEAVES, Material.MANGROVE_LEAVES,
            Material.AZALEA_LEAVES, Material.FLOWERING_AZALEA_LEAVES, Material.SPONGE, Material.WET_SPONGE,
            Material.GLASS, Material.TINTED_GLASS, Material.LAPIS_BLOCK, Material.SANDSTONE,
            Material.CHISELED_SANDSTONE, Material.CUT_SANDSTONE, Material.COBWEB, Material.GRASS, Material.FERN,
            Material.AZALEA, Material.FLOWERING_AZALEA, Material.DEAD_BUSH, Material.SEAGRASS, Material.SEA_PICKLE,
            Material.WHITE_WOOL, Material.ORANGE_WOOL, Material.MAGENTA_WOOL, Material.LIGHT_BLUE_WOOL,
            Material.YELLOW_WOOL, Material.LIME_WOOL, Material.PINK_WOOL, Material.GRAY_WOOL,
            Material.LIGHT_GRAY_WOOL, Material.CYAN_WOOL, Material.PURPLE_WOOL, Material.BLUE_WOOL,
            Material.BROWN_WOOL, Material.GREEN_WOOL, Material.RED_WOOL, Material.BLACK_WOOL, Material.DANDELION,
            Material.POPPY, Material.BLUE_ORCHID, Material.ALLIUM, Material.AZURE_BLUET, Material.RED_TULIP,
            Material.ORANGE_TULIP, Material.WHITE_TULIP, Material.PINK_TULIP, Material.OXEYE_DAISY,
            Material.CORNFLOWER, Material.LILY_OF_THE_VALLEY, Material.WITHER_ROSE, Material.SPORE_BLOSSOM,
            Material.BROWN_MUSHROOM, Material.RED_MUSHROOM, Material.CRIMSON_FUNGUS, Material.WARPED_FUNGUS,
            Material.CRIMSON_ROOTS, Material.WARPED_ROOTS, Material.NETHER_SPROUTS, Material.WEEPING_VINES,
            Material.TWISTING_VINES, Material.SUGAR_CANE, Material.KELP, Material.MOSS_CARPET, Material.MOSS_BLOCK,
            Material.HANGING_ROOTS, Material.BIG_DRIPLEAF, Material.SMALL_DRIPLEAF, Material.BAMBOO,
            Material.OAK_SLAB, Material.SPRUCE_SLAB, Material.BIRCH_SLAB, Material.JUNGLE_SLAB,
            Material.ACACIA_SLAB, Material.DARK_OAK_SLAB, Material.MANGROVE_SLAB, Material.BAMBOO_SLAB,
            Material.BAMBOO_MOSAIC_SLAB, Material.CRIMSON_SLAB, Material.WARPED_SLAB, Material.STONE_SLAB,
            Material.SMOOTH_STONE_SLAB, Material.SANDSTONE_SLAB, Material.CUT_SANDSTONE_SLAB,
            Material.PETRIFIED_OAK_SLAB, Material.COBBLESTONE_SLAB, Material.BRICK_SLAB, Material.STONE_BRICK_SLAB,
            Material.MUD_BRICK_SLAB, Material.NETHER_BRICK_SLAB, Material.QUARTZ_SLAB, Material.RED_SANDSTONE_SLAB,
            Material.CUT_RED_SANDSTONE_SLAB, Material.PURPUR_SLAB, Material.PRISMARINE_SLAB,
            Material.PRISMARINE_BRICK_SLAB, Material.DARK_PRISMARINE_SLAB, Material.SMOOTH_QUARTZ,
            Material.SMOOTH_RED_SANDSTONE, Material.SMOOTH_SANDSTONE, Material.SMOOTH_STONE, Material.BRICKS,
            Material.BOOKSHELF, Material.CHISELED_BOOKSHELF, Material.MOSSY_COBBLESTONE, Material.OBSIDIAN,
            Material.TORCH, Material.END_ROD, Material.PURPUR_BLOCK, Material.PURPUR_PILLAR, Material.PURPUR_STAIRS,
            Material.SPAWNER, Material.CHEST, Material.CRAFTING_TABLE, Material.FARMLAND, Material.FURNACE,
            Material.LADDER, Material.COBBLESTONE_STAIRS, Material.SNOW, Material.ICE,
            Material.CLAY, Material.JUKEBOX, Material.OAK_FENCE, Material.SPRUCE_FENCE, Material.BIRCH_FENCE,
            Material.JUNGLE_FENCE, Material.ACACIA_FENCE, Material.DARK_OAK_FENCE, Material.MANGROVE_FENCE,
            Material.BAMBOO_FENCE, Material.CRIMSON_FENCE, Material.WARPED_FENCE, Material.PUMPKIN,
            Material.CARVED_PUMPKIN, Material.JACK_O_LANTERN, Material.NETHERRACK, Material.SOUL_SAND,
            Material.SOUL_SOIL, Material.BASALT, Material.POLISHED_BASALT, Material.SMOOTH_BASALT,
            Material.SOUL_TORCH, Material.GLOWSTONE, Material.INFESTED_STONE, Material.INFESTED_COBBLESTONE,
            Material.INFESTED_STONE_BRICKS, Material.INFESTED_MOSSY_STONE_BRICKS,
            Material.INFESTED_CRACKED_STONE_BRICKS, Material.INFESTED_CHISELED_STONE_BRICKS,
            Material.INFESTED_DEEPSLATE, Material.STONE_BRICKS, Material.MOSSY_STONE_BRICKS,
            Material.CRACKED_STONE_BRICKS, Material.CHISELED_STONE_BRICKS, Material.PACKED_MUD,
            Material.MUD_BRICKS, Material.DEEPSLATE_BRICKS, Material.CRACKED_DEEPSLATE_BRICKS,
            Material.DEEPSLATE_TILES, Material.CRACKED_DEEPSLATE_TILES, Material.CHISELED_DEEPSLATE,
            Material.REINFORCED_DEEPSLATE, Material.BROWN_MUSHROOM_BLOCK, Material.RED_MUSHROOM_BLOCK,
            Material.MUSHROOM_STEM, Material.IRON_BARS, Material.CHAIN, Material.GLASS_PANE, Material.MELON,
            Material.VINE, Material.GLOW_LICHEN, Material.BRICK_STAIRS, Material.STONE_BRICK_STAIRS,
            Material.MUD_BRICK_STAIRS, Material.MYCELIUM, Material.LILY_PAD, Material.NETHER_BRICKS,
            Material.CRACKED_NETHER_BRICKS, Material.CHISELED_NETHER_BRICKS, Material.NETHER_BRICK_FENCE,
            Material.NETHER_BRICK_STAIRS, Material.SCULK, Material.SCULK_VEIN, Material.SCULK_CATALYST,
            Material.SCULK_SHRIEKER, Material.ENCHANTING_TABLE,
            Material.SNOW_BLOCK, Material.END_STONE, Material.END_STONE_BRICKS, Material.DRAGON_EGG,
            Material.SANDSTONE_STAIRS, Material.ENDER_CHEST, Material.EMERALD_BLOCK, Material.OAK_STAIRS,
            Material.SPRUCE_STAIRS, Material.BIRCH_STAIRS, Material.JUNGLE_STAIRS, Material.ACACIA_STAIRS,
            Material.DARK_OAK_STAIRS, Material.MANGROVE_STAIRS, Material.BAMBOO_STAIRS,
            Material.BAMBOO_MOSAIC_STAIRS, Material.CRIMSON_STAIRS, Material.WARPED_STAIRS, Material.COMMAND_BLOCK,
            Material.BEACON, Material.COBBLESTONE_WALL, Material.MOSSY_COBBLESTONE_WALL, Material.BRICK_WALL,
            Material.PRISMARINE_WALL, Material.RED_SANDSTONE_WALL, Material.MOSSY_STONE_BRICK_WALL,
            Material.GRANITE_WALL, Material.STONE_BRICK_WALL, Material.MUD_BRICK_WALL, Material.NETHER_BRICK_WALL,
            Material.ANDESITE_WALL, Material.RED_NETHER_BRICK_WALL, Material.SANDSTONE_WALL,
            Material.END_STONE_BRICK_WALL, Material.DIORITE_WALL, Material.BLACKSTONE_WALL,
            Material.POLISHED_BLACKSTONE_WALL, Material.POLISHED_BLACKSTONE_BRICK_WALL,
            Material.COBBLED_DEEPSLATE_WALL, Material.POLISHED_DEEPSLATE_WALL, Material.DEEPSLATE_BRICK_WALL,
            Material.DEEPSLATE_TILE_WALL, Material.ANVIL, Material.CHIPPED_ANVIL, Material.DAMAGED_ANVIL,
            Material.CHISELED_QUARTZ_BLOCK, Material.QUARTZ_BLOCK, Material.QUARTZ_BRICKS, Material.QUARTZ_PILLAR,
            Material.QUARTZ_STAIRS, Material.WHITE_TERRACOTTA, Material.ORANGE_TERRACOTTA,
            Material.MAGENTA_TERRACOTTA, Material.LIGHT_BLUE_TERRACOTTA, Material.YELLOW_TERRACOTTA,
            Material.LIME_TERRACOTTA, Material.PINK_TERRACOTTA, Material.GRAY_TERRACOTTA,
            Material.LIGHT_GRAY_TERRACOTTA, Material.CYAN_TERRACOTTA, Material.PURPLE_TERRACOTTA,
            Material.BLUE_TERRACOTTA, Material.BROWN_TERRACOTTA, Material.GREEN_TERRACOTTA, Material.RED_TERRACOTTA,
            Material.BLACK_TERRACOTTA, Material.BARRIER, Material.LIGHT, Material.HAY_BLOCK, Material.WHITE_CARPET,
            Material.ORANGE_CARPET, Material.MAGENTA_CARPET, Material.LIGHT_BLUE_CARPET, Material.YELLOW_CARPET,
            Material.LIME_CARPET, Material.PINK_CARPET, Material.GRAY_CARPET, Material.LIGHT_GRAY_CARPET,
            Material.CYAN_CARPET, Material.PURPLE_CARPET, Material.BLUE_CARPET, Material.BROWN_CARPET,
            Material.GREEN_CARPET, Material.RED_CARPET, Material.BLACK_CARPET, Material.TERRACOTTA,
            Material.PACKED_ICE, Material.DIRT_PATH, Material.SUNFLOWER, Material.LILAC, Material.ROSE_BUSH,
            Material.PEONY, Material.TALL_GRASS, Material.LARGE_FERN, Material.WHITE_STAINED_GLASS,
            Material.ORANGE_STAINED_GLASS, Material.MAGENTA_STAINED_GLASS, Material.LIGHT_BLUE_STAINED_GLASS,
            Material.YELLOW_STAINED_GLASS, Material.LIME_STAINED_GLASS, Material.PINK_STAINED_GLASS,
            Material.GRAY_STAINED_GLASS, Material.LIGHT_GRAY_STAINED_GLASS, Material.CYAN_STAINED_GLASS,
            Material.PURPLE_STAINED_GLASS, Material.BLUE_STAINED_GLASS, Material.BROWN_STAINED_GLASS,
            Material.GREEN_STAINED_GLASS, Material.RED_STAINED_GLASS, Material.BLACK_STAINED_GLASS,
            Material.WHITE_STAINED_GLASS_PANE, Material.ORANGE_STAINED_GLASS_PANE,
            Material.MAGENTA_STAINED_GLASS_PANE, Material.LIGHT_BLUE_STAINED_GLASS_PANE,
            Material.YELLOW_STAINED_GLASS_PANE, Material.LIME_STAINED_GLASS_PANE, Material.PINK_STAINED_GLASS_PANE,
            Material.GRAY_STAINED_GLASS_PANE, Material.LIGHT_GRAY_STAINED_GLASS_PANE,
            Material.CYAN_STAINED_GLASS_PANE, Material.PURPLE_STAINED_GLASS_PANE, Material.BLUE_STAINED_GLASS_PANE,
            Material.BROWN_STAINED_GLASS_PANE, Material.GREEN_STAINED_GLASS_PANE, Material.RED_STAINED_GLASS_PANE,
            Material.BLACK_STAINED_GLASS_PANE, Material.PRISMARINE, Material.PRISMARINE_BRICKS,
            Material.DARK_PRISMARINE, Material.PRISMARINE_STAIRS, Material.PRISMARINE_BRICK_STAIRS,
            Material.DARK_PRISMARINE_STAIRS, Material.SEA_LANTERN, Material.RED_SANDSTONE,
            Material.CHISELED_RED_SANDSTONE, Material.CUT_RED_SANDSTONE, Material.RED_SANDSTONE_STAIRS,
            Material.REPEATING_COMMAND_BLOCK, Material.CHAIN_COMMAND_BLOCK, Material.MAGMA_BLOCK,
            Material.NETHER_WART_BLOCK, Material.WARPED_WART_BLOCK, Material.RED_NETHER_BRICKS, Material.BONE_BLOCK,
            Material.STRUCTURE_VOID, Material.SHULKER_BOX, Material.WHITE_SHULKER_BOX, Material.ORANGE_SHULKER_BOX,
            Material.MAGENTA_SHULKER_BOX, Material.LIGHT_BLUE_SHULKER_BOX, Material.YELLOW_SHULKER_BOX,
            Material.LIME_SHULKER_BOX, Material.PINK_SHULKER_BOX, Material.GRAY_SHULKER_BOX,
            Material.LIGHT_GRAY_SHULKER_BOX, Material.CYAN_SHULKER_BOX, Material.PURPLE_SHULKER_BOX,
            Material.BLUE_SHULKER_BOX, Material.BROWN_SHULKER_BOX, Material.GREEN_SHULKER_BOX,
            Material.RED_SHULKER_BOX, Material.BLACK_SHULKER_BOX, Material.WHITE_GLAZED_TERRACOTTA,
            Material.ORANGE_GLAZED_TERRACOTTA, Material.MAGENTA_GLAZED_TERRACOTTA,
            Material.LIGHT_BLUE_GLAZED_TERRACOTTA, Material.YELLOW_GLAZED_TERRACOTTA,
            Material.LIME_GLAZED_TERRACOTTA, Material.PINK_GLAZED_TERRACOTTA, Material.GRAY_GLAZED_TERRACOTTA,
            Material.LIGHT_GRAY_GLAZED_TERRACOTTA, Material.CYAN_GLAZED_TERRACOTTA,
            Material.PURPLE_GLAZED_TERRACOTTA, Material.BLUE_GLAZED_TERRACOTTA, Material.BROWN_GLAZED_TERRACOTTA,
            Material.GREEN_GLAZED_TERRACOTTA, Material.RED_GLAZED_TERRACOTTA, Material.BLACK_GLAZED_TERRACOTTA,
            Material.WHITE_CONCRETE, Material.ORANGE_CONCRETE, Material.MAGENTA_CONCRETE,
            Material.LIGHT_BLUE_CONCRETE, Material.YELLOW_CONCRETE, Material.LIME_CONCRETE, Material.PINK_CONCRETE,
            Material.GRAY_CONCRETE, Material.LIGHT_GRAY_CONCRETE, Material.CYAN_CONCRETE, Material.PURPLE_CONCRETE,
            Material.BLUE_CONCRETE, Material.BROWN_CONCRETE, Material.GREEN_CONCRETE, Material.RED_CONCRETE,
            Material.BLACK_CONCRETE, Material.WHITE_CONCRETE_POWDER, Material.ORANGE_CONCRETE_POWDER,
            Material.MAGENTA_CONCRETE_POWDER, Material.LIGHT_BLUE_CONCRETE_POWDER, Material.YELLOW_CONCRETE_POWDER,
            Material.LIME_CONCRETE_POWDER, Material.PINK_CONCRETE_POWDER, Material.GRAY_CONCRETE_POWDER,
            Material.LIGHT_GRAY_CONCRETE_POWDER, Material.CYAN_CONCRETE_POWDER, Material.PURPLE_CONCRETE_POWDER,
            Material.BLUE_CONCRETE_POWDER, Material.BROWN_CONCRETE_POWDER, Material.GREEN_CONCRETE_POWDER,
            Material.RED_CONCRETE_POWDER, Material.BLACK_CONCRETE_POWDER, Material.TURTLE_EGG,
            Material.DEAD_TUBE_CORAL_BLOCK, Material.DEAD_BRAIN_CORAL_BLOCK, Material.DEAD_BUBBLE_CORAL_BLOCK,
            Material.DEAD_FIRE_CORAL_BLOCK, Material.DEAD_HORN_CORAL_BLOCK, Material.TUBE_CORAL_BLOCK,
            Material.BRAIN_CORAL_BLOCK, Material.BUBBLE_CORAL_BLOCK, Material.FIRE_CORAL_BLOCK,
            Material.HORN_CORAL_BLOCK, Material.TUBE_CORAL, Material.BRAIN_CORAL, Material.BUBBLE_CORAL,
            Material.FIRE_CORAL, Material.HORN_CORAL, Material.DEAD_BRAIN_CORAL, Material.DEAD_BUBBLE_CORAL,
            Material.DEAD_FIRE_CORAL, Material.DEAD_HORN_CORAL, Material.DEAD_TUBE_CORAL, Material.TUBE_CORAL_FAN,
            Material.BRAIN_CORAL_FAN, Material.BUBBLE_CORAL_FAN, Material.FIRE_CORAL_FAN, Material.HORN_CORAL_FAN,
            Material.DEAD_TUBE_CORAL_FAN, Material.DEAD_BRAIN_CORAL_FAN, Material.DEAD_BUBBLE_CORAL_FAN,
            Material.DEAD_FIRE_CORAL_FAN, Material.DEAD_HORN_CORAL_FAN, Material.BLUE_ICE, Material.CONDUIT,
            Material.POLISHED_GRANITE_STAIRS, Material.SMOOTH_RED_SANDSTONE_STAIRS,
            Material.MOSSY_STONE_BRICK_STAIRS, Material.POLISHED_DIORITE_STAIRS, Material.MOSSY_COBBLESTONE_STAIRS,
            Material.END_STONE_BRICK_STAIRS, Material.STONE_STAIRS, Material.SMOOTH_SANDSTONE_STAIRS,
            Material.SMOOTH_QUARTZ_STAIRS, Material.GRANITE_STAIRS, Material.ANDESITE_STAIRS,
            Material.RED_NETHER_BRICK_STAIRS, Material.POLISHED_ANDESITE_STAIRS, Material.DIORITE_STAIRS,
            Material.COBBLED_DEEPSLATE_STAIRS, Material.POLISHED_DEEPSLATE_STAIRS, Material.DEEPSLATE_BRICK_STAIRS,
            Material.DEEPSLATE_TILE_STAIRS, Material.POLISHED_GRANITE_SLAB, Material.SMOOTH_RED_SANDSTONE_SLAB,
            Material.MOSSY_STONE_BRICK_SLAB, Material.POLISHED_DIORITE_SLAB, Material.MOSSY_COBBLESTONE_SLAB,
            Material.END_STONE_BRICK_SLAB, Material.SMOOTH_SANDSTONE_SLAB, Material.SMOOTH_QUARTZ_SLAB,
            Material.GRANITE_SLAB, Material.ANDESITE_SLAB, Material.RED_NETHER_BRICK_SLAB,
            Material.POLISHED_ANDESITE_SLAB, Material.DIORITE_SLAB, Material.COBBLED_DEEPSLATE_SLAB,
            Material.POLISHED_DEEPSLATE_SLAB, Material.DEEPSLATE_BRICK_SLAB, Material.DEEPSLATE_TILE_SLAB,
            Material.SCAFFOLDING, Material.REDSTONE, Material.REDSTONE_TORCH, Material.REDSTONE_BLOCK,
            Material.REPEATER, Material.COMPARATOR, Material.PISTON, Material.STICKY_PISTON, Material.SLIME_BLOCK,
            Material.HONEY_BLOCK, Material.OBSERVER, Material.HOPPER, Material.DISPENSER, Material.DROPPER,
            Material.LECTERN, Material.TARGET, Material.LEVER, Material.LIGHTNING_ROD, Material.DAYLIGHT_DETECTOR,
            Material.SCULK_SENSOR, Material.TRIPWIRE_HOOK, Material.TRAPPED_CHEST, Material.TNT,
            Material.REDSTONE_LAMP, Material.NOTE_BLOCK, Material.STONE_BUTTON, Material.POLISHED_BLACKSTONE_BUTTON,
            Material.OAK_BUTTON, Material.SPRUCE_BUTTON, Material.BIRCH_BUTTON, Material.JUNGLE_BUTTON,
            Material.ACACIA_BUTTON, Material.DARK_OAK_BUTTON, Material.MANGROVE_BUTTON, Material.BAMBOO_BUTTON,
            Material.CRIMSON_BUTTON, Material.WARPED_BUTTON, Material.STONE_PRESSURE_PLATE,
            Material.POLISHED_BLACKSTONE_PRESSURE_PLATE, Material.LIGHT_WEIGHTED_PRESSURE_PLATE,
            Material.HEAVY_WEIGHTED_PRESSURE_PLATE, Material.OAK_PRESSURE_PLATE, Material.SPRUCE_PRESSURE_PLATE,
            Material.BIRCH_PRESSURE_PLATE, Material.JUNGLE_PRESSURE_PLATE, Material.ACACIA_PRESSURE_PLATE,
            Material.DARK_OAK_PRESSURE_PLATE, Material.MANGROVE_PRESSURE_PLATE, Material.BAMBOO_PRESSURE_PLATE,
            Material.CRIMSON_PRESSURE_PLATE, Material.WARPED_PRESSURE_PLATE, Material.IRON_DOOR, Material.OAK_DOOR,
            Material.SPRUCE_DOOR, Material.BIRCH_DOOR, Material.JUNGLE_DOOR, Material.ACACIA_DOOR,
            Material.DARK_OAK_DOOR, Material.MANGROVE_DOOR, Material.BAMBOO_DOOR, Material.CRIMSON_DOOR,
            Material.WARPED_DOOR, Material.IRON_TRAPDOOR, Material.OAK_TRAPDOOR, Material.SPRUCE_TRAPDOOR,
            Material.BIRCH_TRAPDOOR, Material.JUNGLE_TRAPDOOR, Material.ACACIA_TRAPDOOR, Material.DARK_OAK_TRAPDOOR,
            Material.MANGROVE_TRAPDOOR, Material.BAMBOO_TRAPDOOR, Material.CRIMSON_TRAPDOOR,
            Material.WARPED_TRAPDOOR, Material.OAK_FENCE_GATE, Material.SPRUCE_FENCE_GATE,
            Material.BIRCH_FENCE_GATE, Material.JUNGLE_FENCE_GATE, Material.ACACIA_FENCE_GATE,
            Material.DARK_OAK_FENCE_GATE, Material.MANGROVE_FENCE_GATE, Material.BAMBOO_FENCE_GATE,
            Material.CRIMSON_FENCE_GATE, Material.WARPED_FENCE_GATE, Material.POWERED_RAIL,
            Material.DETECTOR_RAIL, Material.RAIL, Material.ACTIVATOR_RAIL
        )

        fun getEffectType(itemStack: ItemStack): BasicArtifactEffects {

            if (ArtifactComplexRune.getComplexRune(itemStack) != null) {
                return COMPLEX
            }
            if (CustomShardItems.getCustomItemStack(itemStack) != null) {
                return CUSTOM
            }

            // Handle blocks separately, too much cases to be in a when, causes a stackoverflow at compilation
            if (itemStack.type in blocks) {
                return BLOCKS
            }

            return when (itemStack.type) {
                Material.ARROW, Material.SPECTRAL_ARROW, Material.TIPPED_ARROW, Material.SPLASH_POTION, Material.FIRE_CHARGE -> PROJECTILES

                Material.APPLE, Material.BAKED_POTATO, Material.BEETROOT, Material.BEETROOT_SOUP, Material.BREAD,
                Material.CAKE, Material.CARROT, Material.CHORUS_FRUIT, Material.COOKED_CHICKEN, Material.COOKED_COD,
                Material.COOKED_MUTTON, Material.COOKED_PORKCHOP, Material.COOKED_RABBIT, Material.COOKED_SALMON,
                Material.COOKIE, Material.DRIED_KELP, Material.ENCHANTED_GOLDEN_APPLE, Material.GOLDEN_APPLE,
                Material.GLOW_BERRIES, Material.GOLDEN_CARROT, Material.HONEY_BOTTLE, Material.MELON_SLICE,
                Material.MUSHROOM_STEW, Material.POISONOUS_POTATO, Material.POTATO, Material.PUFFERFISH,
                Material.PUMPKIN_PIE, Material.RABBIT_STEW, Material.BEEF, Material.CHICKEN, Material.COD,
                Material.MUTTON, Material.PORKCHOP, Material.RABBIT, Material.SALMON, Material.ROTTEN_FLESH,
                Material.SPIDER_EYE, Material.COOKED_BEEF, Material.SUSPICIOUS_STEW, Material.SWEET_BERRIES,
                Material.TROPICAL_FISH,
                Material.POTION -> CONSUMABLE

                Material.DIAMOND_PICKAXE -> TOOLS

                // Add all spawn eggs, boring . . .
                Material.COW_SPAWN_EGG, Material.CHICKEN_SPAWN_EGG, Material.GOAT_SPAWN_EGG -> ENTITY
                else -> None
            }
        }
    }

    fun foodEffect(entity: Entity, material: Material) {
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
            Material.ENCHANTED_GOLDEN_APPLE -> feedPlayer(entity, 9.6f, 4)
            Material.GOLDEN_APPLE -> feedPlayer(entity, 9.6f, 4)
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

    fun entityTypeFromItemStack(itemStack: ItemStack): EntityType? {
        return when (itemStack.type) {
            Material.COW_SPAWN_EGG -> EntityType.COW
            else -> null
        }
    }

    private fun feedPlayer(player: Player, saturationDelta: Float, foodDelta: Int) {
        player.saturation += saturationDelta
        player.foodLevel += foodDelta
    }

}