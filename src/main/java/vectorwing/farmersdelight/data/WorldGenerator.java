package vectorwing.farmersdelight.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.world.WildCropGeneration;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class WorldGenerator extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, WildCropGeneration::featureBootstrap)
            .add(Registries.PLACED_FEATURE, WildCropGeneration::placedFeatureBootstrap);


    public WorldGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries.thenApply(r -> createLookup()), Set.of(FarmersDelight.MODID));
    }

    public static HolderLookup.Provider createLookup() {
        return BUILDER.buildPatch(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY), VanillaRegistries.createLookup());
    }
}
