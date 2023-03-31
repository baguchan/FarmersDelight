package vectorwing.farmersdelight.client.model;

import com.google.common.base.Preconditions;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;

/**
 * Credits to the Botania Team for the class reference!
 */

// TODO: Check if Material::sprite is the proper substitute for ForgeModelBakery.defaultTextureGetter().

@SuppressWarnings("deprecation")
public class SkilletModel implements BakedModel
{
	private final ModelBakery bakery;
	private final BakedModel originalModel;
	private final BakedModel cookingModel;

	public SkilletModel(ModelBakery bakery, BakedModel originalModel, BakedModel cookingModel) {
		this.bakery = bakery;
		this.originalModel = Preconditions.checkNotNull(originalModel);
		this.cookingModel = Preconditions.checkNotNull(cookingModel);
	}

	private final ItemOverrides itemOverrides = new ItemOverrides()
	{
		@Nonnull
		@Override
		public BakedModel resolve(BakedModel model, ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entityIn, int seed) {
			CompoundTag tag = stack.getOrCreateTag();

			if (tag.contains("Cooking")) {
				ItemStack ingredientStack = ItemStack.of(tag.getCompound("Cooking"));
				return SkilletModel.this.getCookingModel(ingredientStack);
			}

			return originalModel;
		}
	};

	@Nonnull
	@Override
	public ItemOverrides getOverrides() {
		return itemOverrides;
	}

	@Nonnull
	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull RandomSource rand) {
		return originalModel.getQuads(state, side, rand);
	}

	@Nonnull
	@Override
	public ItemTransforms getTransforms() {
		return originalModel.getTransforms();
	}

	@Override
	public boolean useAmbientOcclusion() {
		return originalModel.useAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return originalModel.isGui3d();
	}

	@Override
	public boolean usesBlockLight() {
		return originalModel.usesBlockLight();
	}

	@Override
	public boolean isCustomRenderer() {
		return originalModel.isCustomRenderer();
	}

	@Nonnull
	@Override
	public TextureAtlasSprite getParticleIcon() {
		return originalModel.getParticleIcon();
	}

	private final HashMap<Item, CompositeBakedModel> cache = new HashMap<>();

	private CompositeBakedModel getCookingModel(ItemStack ingredientStack) {
		return cache.computeIfAbsent(ingredientStack.getItem(), p -> new CompositeBakedModel(cookingModel));
	}

	private static class CompositeBakedModel extends WrappedItemModel<BakedModel>
	{
		public CompositeBakedModel(BakedModel skillet) {
			super(skillet);
		}

		@Override
		public boolean isCustomRenderer() {
			return originalModel.isCustomRenderer();
		}

		@Nonnull
		@Override
		public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, @Nonnull RandomSource rand) {
			return originalModel.getQuads(state, face, rand);
		}

		@Override
		public BakedModel applyTransform(@Nonnull ItemDisplayContext cameraTransformType, PoseStack stack, boolean leftHand) {
			super.applyTransform(cameraTransformType, stack, leftHand);
			return this;
		}
	}
}
