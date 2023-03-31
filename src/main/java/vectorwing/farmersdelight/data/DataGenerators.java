package vectorwing.farmersdelight.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.FarmersDelight;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

		BlockTags blockTags = new BlockTags(packOutput, lookupProvider, FarmersDelight.MODID, existingFileHelper);
		generator.addProvider(event.includeServer(), blockTags);
		generator.addProvider(event.includeServer(), new ItemTags(packOutput, lookupProvider, blockTags.contentsGetter(), FarmersDelight.MODID, existingFileHelper));
		generator.addProvider(event.includeServer(), new EntityTags(packOutput, lookupProvider, FarmersDelight.MODID, existingFileHelper));
		generator.addProvider(event.includeServer(), new Recipes(packOutput));
		generator.addProvider(event.includeServer(), new Advancements(packOutput, lookupProvider));

		BlockStates blockStates = new BlockStates(packOutput, existingFileHelper);
		generator.addProvider(event.includeClient(), blockStates);
		generator.addProvider(event.includeClient(), new ItemModels(packOutput, blockStates.models().existingFileHelper));
		generator.addProvider(event.includeServer(), new WorldGenerator(packOutput, lookupProvider));
	}
}
