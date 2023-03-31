package vectorwing.farmersdelight.common.world;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RandomizedIntStateProvider;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.block.MushroomColonyBlock;
import vectorwing.farmersdelight.common.registry.ModBiomeFeatures;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.world.configuration.WildCropConfiguration;
import vectorwing.farmersdelight.common.world.filter.BiomeTagFilter;

import java.util.List;

public class WildCropGeneration {
    public static ResourceKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_WILD_CABBAGES = registerFeatureKey("patch_wild_cabbages");
    public static ResourceKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_WILD_ONIONS = registerFeatureKey("patch_wild_onions");
    public static ResourceKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_WILD_TOMATOES = registerFeatureKey("patch_wild_tomatoes");
    public static ResourceKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_WILD_CARROTS = registerFeatureKey("patch_wild_carrots");
    public static ResourceKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_WILD_POTATOES = registerFeatureKey("patch_wild_potatoes");
    public static ResourceKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_WILD_BEETROOTS = registerFeatureKey("patch_wild_beetroots");
    public static ResourceKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_WILD_RICE = registerFeatureKey("patch_wild_rice");
    public static ResourceKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_BROWN_MUSHROOM_COLONIES = registerFeatureKey("patch_brown_mushroom_colonies");
    public static ResourceKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_RED_MUSHROOM_COLONIES = registerFeatureKey("patch_red_mushroom_colonies");

    public static ResourceKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_SANDY_SHRUB_BONEMEAL = registerFeatureKey("patch_sandy_shrub");

    public static ResourceKey<PlacedFeature> PATCH_WILD_CABBAGES = registerPlacedFeatureKey("patch_wild_cabbages");
    public static ResourceKey<PlacedFeature> PATCH_WILD_ONIONS = registerPlacedFeatureKey("patch_wild_onions");
    public static ResourceKey<PlacedFeature> PATCH_WILD_TOMATOES = registerPlacedFeatureKey("patch_wild_tomatoes");
    public static ResourceKey<PlacedFeature> PATCH_WILD_CARROTS = registerPlacedFeatureKey("patch_wild_carrots");
    public static ResourceKey<PlacedFeature> PATCH_WILD_POTATOES = registerPlacedFeatureKey("patch_wild_potatoes");
    public static ResourceKey<PlacedFeature> PATCH_WILD_BEETROOTS = registerPlacedFeatureKey("patch_wild_beetroots");
    public static ResourceKey<PlacedFeature> PATCH_WILD_RICE = registerPlacedFeatureKey("patch_wild_rice");
    public static ResourceKey<PlacedFeature> PATCH_BROWN_MUSHROOM_COLONIES = registerPlacedFeatureKey("patch_brown_mushroom_colonies");
    public static ResourceKey<PlacedFeature> PATCH_RED_MUSHROOM_COLONIES = registerPlacedFeatureKey("patch_red_mushroom_colonies");

    public static final BlockPos BLOCK_BELOW = new BlockPos(0, -1, 0);
    public static final BlockPos BLOCK_ABOVE = new BlockPos(0, 1, 0);

    public static final BiomeTagFilter TAGGED_IS_OVERWORLD = BiomeTagFilter.biomeIsInTag(BiomeTags.IS_OVERWORLD);

    public static ResourceKey<ConfiguredFeature<?, ?>> registerFeatureKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(FarmersDelight.MODID, name));
    }

    public static ResourceKey<PlacedFeature> registerPlacedFeatureKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(FarmersDelight.MODID, name));
    }

    public static void featureBootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        FeatureUtils.register(context, FEATURE_PATCH_WILD_CABBAGES,
                ModBiomeFeatures.WILD_CROP.get(), wildCropConfig(ModBlocks.WILD_CABBAGES.get(), ModBlocks.SANDY_SHRUB.get(), BlockPredicate.matchesBlocks(BLOCK_BELOW, Blocks.SAND)));
        FeatureUtils.register(context, FEATURE_PATCH_WILD_ONIONS,
                ModBiomeFeatures.WILD_CROP.get(), wildCropConfig(ModBlocks.WILD_ONIONS.get(), Blocks.ALLIUM, BlockPredicate.matchesTag(BLOCK_BELOW, BlockTags.DIRT)));

        FeatureUtils.register(context, FEATURE_PATCH_WILD_TOMATOES,
                ModBiomeFeatures.WILD_CROP.get(), wildCropConfig(ModBlocks.WILD_TOMATOES.get(), Blocks.DEAD_BUSH, BlockPredicate.matchesBlocks(BLOCK_BELOW, List.of(Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.COARSE_DIRT, Blocks.RED_SAND, Blocks.SAND))));

        FeatureUtils.register(context, FEATURE_PATCH_WILD_CARROTS,
                ModBiomeFeatures.WILD_CROP.get(), wildCropWithFloorConfig(ModBlocks.WILD_CARROTS.get(), Blocks.GRASS, BlockPredicate.matchesTag(BLOCK_BELOW, BlockTags.DIRT), Blocks.COARSE_DIRT, BlockPredicate.matchesTag(BlockTags.DIRT)));
        FeatureUtils.register(context, FEATURE_PATCH_WILD_POTATOES,
                ModBiomeFeatures.WILD_CROP.get(), wildCropConfig(ModBlocks.WILD_POTATOES.get(), Blocks.FERN, BlockPredicate.matchesTag(BLOCK_BELOW, BlockTags.DIRT)));
        FeatureUtils.register(context, FEATURE_PATCH_WILD_BEETROOTS,
                ModBiomeFeatures.WILD_CROP.get(), wildCropConfig(ModBlocks.WILD_BEETROOTS.get(), ModBlocks.SANDY_SHRUB.get(), BlockPredicate.matchesBlocks(BLOCK_BELOW, Blocks.SAND)));
        FeatureUtils.register(context, FEATURE_PATCH_WILD_RICE,
                ModBiomeFeatures.WILD_RICE.get(), FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.WILD_RICE.get())), List.of(Blocks.DIRT)));
        FeatureUtils.register(context, FEATURE_PATCH_BROWN_MUSHROOM_COLONIES,
                ModBiomeFeatures.WILD_CROP.get(), mushroomColonyConfig(ModBlocks.BROWN_MUSHROOM_COLONY.get(), Blocks.BROWN_MUSHROOM, BlockPredicate.matchesBlocks(BLOCK_BELOW, Blocks.MYCELIUM)));
        FeatureUtils.register(context, FEATURE_PATCH_RED_MUSHROOM_COLONIES,
                ModBiomeFeatures.WILD_CROP.get(), mushroomColonyConfig(ModBlocks.RED_MUSHROOM_COLONY.get(), Blocks.RED_MUSHROOM, BlockPredicate.matchesBlocks(BLOCK_BELOW, Blocks.MYCELIUM)));
        FeatureUtils.register(context, FEATURE_PATCH_SANDY_SHRUB_BONEMEAL,
                Feature.RANDOM_PATCH, randomPatchConfig(ModBlocks.SANDY_SHRUB.get(), 32, 2, BlockPredicate.matchesTag(BLOCK_BELOW, BlockTags.SAND)));

    }

    public static void placedFeatureBootstrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeature = context.lookup(Registries.CONFIGURED_FEATURE);
        PlacementUtils.register(context, PATCH_WILD_CABBAGES, configuredFeature.getOrThrow(FEATURE_PATCH_WILD_CABBAGES), RarityFilter.onAverageOnceEvery(20), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome(), TAGGED_IS_OVERWORLD);

        PlacementUtils.register(context, PATCH_WILD_ONIONS,
                configuredFeature.getOrThrow(FEATURE_PATCH_WILD_ONIONS), RarityFilter.onAverageOnceEvery(120), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome(), TAGGED_IS_OVERWORLD);

        PlacementUtils.register(context, PATCH_WILD_TOMATOES,
                configuredFeature.getOrThrow(FEATURE_PATCH_WILD_TOMATOES), RarityFilter.onAverageOnceEvery(100), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome(), TAGGED_IS_OVERWORLD);


        PlacementUtils.register(context, PATCH_WILD_CARROTS,
                configuredFeature.getOrThrow(FEATURE_PATCH_WILD_CARROTS), RarityFilter.onAverageOnceEvery(120), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome(), TAGGED_IS_OVERWORLD);

        PlacementUtils.register(context, PATCH_WILD_POTATOES,
                configuredFeature.getOrThrow(FEATURE_PATCH_WILD_POTATOES), RarityFilter.onAverageOnceEvery(100), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome(), TAGGED_IS_OVERWORLD);

        PlacementUtils.register(context, PATCH_WILD_BEETROOTS, configuredFeature.getOrThrow(FEATURE_PATCH_WILD_BEETROOTS), RarityFilter.onAverageOnceEvery(30), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome(), TAGGED_IS_OVERWORLD);

        PlacementUtils.register(context, PATCH_WILD_RICE,
                configuredFeature.getOrThrow(FEATURE_PATCH_WILD_RICE), RarityFilter.onAverageOnceEvery(20), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome(), TAGGED_IS_OVERWORLD);
        PlacementUtils.register(context, PATCH_BROWN_MUSHROOM_COLONIES,
                configuredFeature.getOrThrow(FEATURE_PATCH_BROWN_MUSHROOM_COLONIES), RarityFilter.onAverageOnceEvery(15), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome(), TAGGED_IS_OVERWORLD);
        PlacementUtils.register(context, PATCH_RED_MUSHROOM_COLONIES,
                configuredFeature.getOrThrow(FEATURE_PATCH_RED_MUSHROOM_COLONIES), RarityFilter.onAverageOnceEvery(15), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome(), TAGGED_IS_OVERWORLD);

    }

	public static RandomPatchConfiguration randomPatchConfig(Block block, int tries, int xzSpread, BlockPredicate plantedOn) {
		return new RandomPatchConfiguration(tries, xzSpread, 3, PlacementUtils.filtered(
				Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(block)),
				BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, plantedOn)));
	}

	public static WildCropConfiguration wildCropConfig(Block primaryBlock, Block secondaryBlock, BlockPredicate plantedOn) {
		return new WildCropConfiguration(64, 6, 3, plantBlockConfig(primaryBlock, plantedOn), plantBlockConfig(secondaryBlock, plantedOn), null);
	}

	public static WildCropConfiguration wildCropWithFloorConfig(Block primaryBlock, Block secondaryBlock, BlockPredicate plantedOn, Block floorBlock, BlockPredicate replaces) {
		return new WildCropConfiguration(64, 6, 3, plantBlockConfig(primaryBlock, plantedOn), plantBlockConfig(secondaryBlock, plantedOn), floorBlockConfig(floorBlock, replaces));
	}

	public static WildCropConfiguration mushroomColonyConfig(Block colonyBlock, Block secondaryBlock, BlockPredicate plantedOn) {
		return new WildCropConfiguration(64, 6, 3, colonyBlockConfig(colonyBlock, plantedOn), plantBlockConfig(secondaryBlock, plantedOn), null);
	}

	public static Holder<PlacedFeature> plantBlockConfig(Block block, BlockPredicate plantedOn) {
		return PlacementUtils.filtered(
				Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(block)),
				BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, plantedOn));
	}

	public static Holder<PlacedFeature> colonyBlockConfig(Block block, BlockPredicate plantedOn) {
		return PlacementUtils.filtered(
				Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(new RandomizedIntStateProvider(BlockStateProvider.simple(block), MushroomColonyBlock.COLONY_AGE, UniformInt.of(0, 3))),
				BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, plantedOn));
	}

	public static Holder<PlacedFeature> floorBlockConfig(Block block, BlockPredicate replaces) {
		return PlacementUtils.filtered(
				Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(block)),
				BlockPredicate.allOf(BlockPredicate.replaceable(BLOCK_ABOVE), replaces));
	}

	// Registry stuff
}
