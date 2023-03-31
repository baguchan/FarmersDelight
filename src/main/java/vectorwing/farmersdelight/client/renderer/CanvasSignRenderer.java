package vectorwing.farmersdelight.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.util.FastColor;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vectorwing.farmersdelight.common.block.state.CanvasSign;
import vectorwing.farmersdelight.common.registry.ModAtlases;

import javax.annotation.Nullable;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class CanvasSignRenderer extends SignRenderer
{
	public static final float TEXT_LINE_HEIGHT = 10;
	public static final float TEXT_VERTICAL_OFFSET = 19;
	private static final int OUTLINE_RENDER_DISTANCE = Mth.square(16);

	private final SignModel signModel;
	private final Font font;

	public CanvasSignRenderer(BlockEntityRendererProvider.Context context) {
		super(context);

		this.signModel = new SignRenderer.SignModel(context.bakeLayer(ModelLayers.createSignModelName(WoodType.SPRUCE)));
		this.font = context.getFont();
	}

	@Override
	public void render(SignBlockEntity blockEntity, float pPartialTick, PoseStack poseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
		BlockState state = blockEntity.getBlockState();
		poseStack.pushPose();
		SignRenderer.SignModel signrenderer$signmodel = signModel;
		if (state.getBlock() instanceof StandingSignBlock) {
			poseStack.translate(0.5D, 0.5D, 0.5D);
			float f1 = -((float) (state.getValue(StandingSignBlock.ROTATION) * 360) / 16.0F);
			poseStack.mulPose(Axis.YP.rotationDegrees(f1));
			signrenderer$signmodel.stick.visible = true;
		} else {
			poseStack.translate(0.5D, 0.5D, 0.5D);
			float f4 = -state.getValue(WallSignBlock.FACING).toYRot();
			poseStack.mulPose(Axis.YP.rotationDegrees(f4));
			poseStack.translate(0.0D, -0.3125D, -0.4375D);
			signrenderer$signmodel.stick.visible = false;
		}

		poseStack.pushPose();
		float rootScale = 0.6666667F;
		poseStack.scale(rootScale, -rootScale, -rootScale);
		DyeColor dye = null;
		if (state.getBlock() instanceof CanvasSign canvasSign) {
			dye = canvasSign.getBackgroundColor();
		}
		Material material = getMaterial(dye);
		VertexConsumer vertexconsumer = material.buffer(pBufferSource, signrenderer$signmodel::renderType);
		signrenderer$signmodel.root.render(poseStack, vertexconsumer, pPackedLight, pPackedOverlay);
		poseStack.popPose();
		float textScale = 0.010416667F;
		poseStack.translate(0.0D, 0.33333334F, 0.046666667F);
		poseStack.scale(textScale, -textScale, textScale);
		FormattedCharSequence[] aformattedcharsequence = blockEntity.getRenderMessages(Minecraft.getInstance().isTextFilteringEnabled(), (component) -> {
			List<FormattedCharSequence> list = this.font.split(component, 90);
			return list.isEmpty() ? FormattedCharSequence.EMPTY : list.get(0);
		});

		int darkColor;
		int baseColor;
		boolean hasOutline;
		int light;
		if (blockEntity.hasGlowingText()) {
			darkColor = getDarkColor(blockEntity, true);
			baseColor = blockEntity.getColor().getTextColor();
			hasOutline = isOutlineVisible(blockEntity, baseColor);
			light = 15728880;
		} else {
			darkColor = getDarkColor(blockEntity, false);
			baseColor = darkColor;
			hasOutline = false;
			light = pPackedLight;
		}

		for (int i1 = 0; i1 < 4; ++i1) {
			FormattedCharSequence formattedcharsequence = aformattedcharsequence[i1];
			float x = (float) (-this.font.width(formattedcharsequence) / 2);
			float y = i1 * TEXT_LINE_HEIGHT - TEXT_VERTICAL_OFFSET;
			if (hasOutline) {
				this.font.drawInBatch8xOutline(formattedcharsequence, x, y, baseColor, darkColor, poseStack.last().pose(), pBufferSource, light);
			} else {
				this.font.drawInBatch(formattedcharsequence, x, y, baseColor, false, poseStack.last().pose(), pBufferSource, Font.DisplayMode.NORMAL, 0, light);
			}
		}

		poseStack.popPose();
	}

	private static boolean isOutlineVisible(SignBlockEntity blockEntity, int textColor) {
		if (textColor == DyeColor.BLACK.getTextColor()) {
			return true;
		} else {
			Minecraft minecraft = Minecraft.getInstance();
			LocalPlayer localPlayer = minecraft.player;
			if (localPlayer != null && minecraft.options.getCameraType().isFirstPerson() && localPlayer.isScoping()) {
				return true;
			} else {
				Entity entity = minecraft.getCameraEntity();
				return entity != null && entity.distanceToSqr(Vec3.atCenterOf(blockEntity.getBlockPos())) < (double) OUTLINE_RENDER_DISTANCE;
			}
		}
	}

	private static int getDarkColor(SignBlockEntity blockEntity, boolean isOutlineVisible) {
		int textColor = blockEntity.getColor().getTextColor();
		double brightness = isOutlineVisible ? 0.4D : 0.6D;
		int i = blockEntity.getColor().getTextColor();
		if (i == DyeColor.BLACK.getTextColor() && blockEntity.hasGlowingText()) {
			return -988212;
		} else {
			double d0 = 0.4D;
			int red = (int) ((double) FastColor.ARGB32.red(i) * 0.4D);
			int green = (int) ((double) FastColor.ARGB32.green(i) * 0.4D);
			int blue = (int) ((double) FastColor.ARGB32.blue(i) * 0.4D);
			return FastColor.ARGB32.color(0, red, green, blue);
		}
	}
	public static Material getMaterial(@Nullable DyeColor dyeColor) {
		return dyeColor != null ? ModAtlases.DYED_CANVAS_SIGN_MATERIALS.get(dyeColor) : ModAtlases.BLANK_CANVAS_SIGN_MATERIAL;
	}
}
