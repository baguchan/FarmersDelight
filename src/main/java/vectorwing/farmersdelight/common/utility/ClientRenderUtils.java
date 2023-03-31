package vectorwing.farmersdelight.common.utility;

/**
 * Util for helping with rendering elements across the mod, when vanilla methods don't expose enough to use.
 */
public class ClientRenderUtils {
	/**
	 * Renders an Item into the GUI, allowing the size to be defined instead of hardcoded.
	 * This function is ripped right from the game's rendering code. I am probably doing something stupid.
	 */
	//TODO remake renderItemIntoGUIScalable
	/*public static void renderItemIntoGUIScalable(ItemStack stack, float width, float height, BakedModel bakedmodel, ItemRenderer renderer, TextureManager textureManager) {
		textureManager.getTexture(TextureAtlas.LOCATION_BLOCKS).setFilter(false, false);
		RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

		PoseStack modelViewStack = RenderSystem.getModelViewStack();
		modelViewStack.pushPose();
		modelViewStack.translate((float) 0, (float) 0, 100.0F + renderer.blitOffset);
		modelViewStack.translate(8.0F, 8.0F, 0.0F);
		modelViewStack.scale(1.0F, -1.0F, 1.0F);
		modelViewStack.scale(width, height, 48.0F);
		RenderSystem.applyModelViewMatrix();

		PoseStack poseStack = new PoseStack();
		MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
		boolean usesBlockLight = !bakedmodel.usesBlockLight();
		if (usesBlockLight) {
			Lighting.setupForFlatItems();
		}

		renderer.render(stack, ItemDisplayContext.GUI, false, poseStack, multibuffersource$buffersource, 15728880, OverlayTexture.NO_OVERLAY, bakedmodel);
		multibuffersource$buffersource.endBatch();
		RenderSystem.enableDepthTest();
		if (usesBlockLight) {
			Lighting.setupFor3DItems();
		}

		poseStack.popPose();
		RenderSystem.applyModelViewMatrix();
	}*/
}
