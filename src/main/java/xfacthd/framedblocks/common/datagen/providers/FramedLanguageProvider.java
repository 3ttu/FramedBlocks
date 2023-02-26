package xfacthd.framedblocks.common.datagen.providers;

import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraftforge.common.data.LanguageProvider;
import xfacthd.framedblocks.api.block.IFramedBlock;
import xfacthd.framedblocks.api.util.FramedConstants;
import xfacthd.framedblocks.client.screen.*;
import xfacthd.framedblocks.client.screen.overlay.*;
import xfacthd.framedblocks.client.util.KeyMappings;
import xfacthd.framedblocks.common.FBContent;
import xfacthd.framedblocks.common.block.FramingSawBlock;
import xfacthd.framedblocks.common.blockentity.FramedStorageBlockEntity;
import xfacthd.framedblocks.common.crafting.FramingSawRecipe;
import xfacthd.framedblocks.common.item.FramedBlueprintItem;
import xfacthd.framedblocks.common.blockentity.FramedChestBlockEntity;
import xfacthd.framedblocks.api.block.FramedBlockEntity;
import xfacthd.framedblocks.common.util.FramedCreativeTab;

public final class FramedLanguageProvider extends LanguageProvider
{
    public FramedLanguageProvider(PackOutput output) { super(output, FramedConstants.MOD_ID, "en_us"); }

    @Override
    protected void addTranslations()
    {
        add(FBContent.blockFramedCube.get(), "Framed Cube");
        add(FBContent.blockFramedSlope.get(), "Framed Slope");
        add(FBContent.blockFramedCornerSlope.get(), "Framed Corner Slope");
        add(FBContent.blockFramedInnerCornerSlope.get(), "Framed Inner Corner Slope");
        add(FBContent.blockFramedPrismCorner.get(), "Framed Prism Corner");
        add(FBContent.blockFramedInnerPrismCorner.get(), "Framed Inner Prism Corner");
        add(FBContent.blockFramedThreewayCorner.get(), "Framed Threeway Corner");
        add(FBContent.blockFramedInnerThreewayCorner.get(), "Framed Inner Threeway Corner");
        add(FBContent.blockFramedSlab.get(), "Framed Slab");
        add(FBContent.blockFramedSlabEdge.get(), "Framed Slab Edge");
        add(FBContent.blockFramedSlabCorner.get(), "Framed Slab Corner");
        add(FBContent.blockFramedDividedSlab.get(), "Framed Divided Slab");
        add(FBContent.blockFramedPanel.get(), "Framed Panel");
        add(FBContent.blockFramedCornerPillar.get(), "Framed Corner Pillar");
        add(FBContent.blockFramedDividedPanelHor.get(), "Framed Divided Panel (Horizontal)");
        add(FBContent.blockFramedDividedPanelVert.get(), "Framed Divided Panel (Vertical)");
        add(FBContent.blockFramedStairs.get(), "Framed Stairs");
        add(FBContent.blockFramedWall.get(), "Framed Wall");
        add(FBContent.blockFramedFence.get(), "Framed Fence");
        add(FBContent.blockFramedFenceGate.get(), "Framed Fence Gate");
        add(FBContent.blockFramedDoor.get(), "Framed Door");
        add(FBContent.blockFramedIronDoor.get(), "Framed Iron Door");
        add(FBContent.blockFramedTrapDoor.get(), "Framed Trapdoor");
        add(FBContent.blockFramedIronTrapDoor.get(), "Framed Iron Trapdoor");
        add(FBContent.blockFramedPressurePlate.get(), "Framed Pressure Plate");
        add(FBContent.blockFramedWaterloggablePressurePlate.get(), "Framed Pressure Plate");
        add(FBContent.blockFramedStonePressurePlate.get(), "Framed Stone Pressure Plate");
        add(FBContent.blockFramedWaterloggableStonePressurePlate.get(), "Framed Stone Pressure Plate");
        add(FBContent.blockFramedObsidianPressurePlate.get(), "Framed Obsidian Pressure Plate");
        add(FBContent.blockFramedWaterloggableObsidianPressurePlate.get(), "Framed Obsidian Pressure Plate");
        add(FBContent.blockFramedGoldPressurePlate.get(), "Framed Light Weighted Pressure Plate");
        add(FBContent.blockFramedWaterloggableGoldPressurePlate.get(), "Framed Light Weighted Pressure Plate");
        add(FBContent.blockFramedIronPressurePlate.get(), "Framed Heavy Weighted Pressure Plate");
        add(FBContent.blockFramedWaterloggableIronPressurePlate.get(), "Framed Heavy Weighted Pressure Plate");
        add(FBContent.blockFramedLadder.get(), "Framed Ladder");
        add(FBContent.blockFramedButton.get(), "Framed Button");
        add(FBContent.blockFramedStoneButton.get(), "Framed Stone Button");
        add(FBContent.blockFramedLever.get(), "Framed Lever");
        add(FBContent.blockFramedSign.get(), "Framed Sign");
        add(FBContent.blockFramedWallSign.get(), "Framed Sign");
        add(FBContent.blockFramedDoubleSlab.get(), "Framed Double Slab");
        add(FBContent.blockFramedDoublePanel.get(), "Framed Double Panel");
        add(FBContent.blockFramedDoubleSlope.get(), "Framed Double Slope");
        add(FBContent.blockFramedDoubleCorner.get(), "Framed Double Corner");
        add(FBContent.blockFramedDoublePrismCorner.get(), "Framed Double Prism Corner");
        add(FBContent.blockFramedDoubleThreewayCorner.get(), "Framed Double Threeway Corner");
        add(FBContent.blockFramedTorch.get(), "Framed Torch"); //Wall torch name is handled through WallTorchBlock
        add(FBContent.blockFramedSoulTorch.get(), "Framed Soul Torch"); //See above
        add(FBContent.blockFramedRedstoneTorch.get(), "Framed Redstone Torch"); //See above
        add(FBContent.blockFramedFloor.get(), "Framed Floor Board");
        add(FBContent.blockFramedLattice.get(), "Framed Lattice");
        add(FBContent.blockFramedVerticalStairs.get(), "Framed Vertical Stairs");
        add(FBContent.blockFramedChest.get(), "Framed Chest");
        add(FBContent.blockFramedBars.get(), "Framed Bars");
        add(FBContent.blockFramedPane.get(), "Framed Pane");
        add(FBContent.blockFramedRailSlope.get(), "Framed Rail Slope");
        add(FBContent.blockFramedPoweredRailSlope.get(), "Framed Powered Rail Slope");
        add(FBContent.blockFramedDetectorRailSlope.get(), "Framed Detector Rail Slope");
        add(FBContent.blockFramedActivatorRailSlope.get(), "Framed Activator Rail Slope");
        add(FBContent.blockFramedFlowerPot.get(), "Framed Flower Pot");
        add(FBContent.blockFramedPillar.get(), "Framed Pillar");
        add(FBContent.blockFramedHalfPillar.get(), "Framed Half Pillar");
        add(FBContent.blockFramedPost.get(), "Framed Post");
        add(FBContent.blockFramedCollapsibleBlock.get(), "Framed Collapsible Block");
        add(FBContent.blockFramedHalfStairs.get(), "Framed Half Stairs");
        add(FBContent.blockFramedBouncyCube.get(), "Framed Bouncy Cube");
        add(FBContent.blockFramedSecretStorage.get(), "Framed Secret Storage");
        add(FBContent.blockFramedRedstoneBlock.get(), "Framed Redstone Block");
        add(FBContent.blockFramedPrism.get(), "Framed Prism");
        add(FBContent.blockFramedInnerPrism.get(), "Framed Inner Prism");
        add(FBContent.blockFramedDoublePrism.get(), "Framed Double Prism");
        add(FBContent.blockFramedSlopedPrism.get(), "Framed Sloped Prism");
        add(FBContent.blockFramedInnerSlopedPrism.get(), "Framed Inner Sloped Prism");
        add(FBContent.blockFramedDoubleSlopedPrism.get(), "Framed Double Sloped Prism");
        add(FBContent.blockFramedSlopeSlab.get(), "Framed Slope Slab");
        add(FBContent.blockFramedElevatedSlopeSlab.get(), "Framed Elevated Slope Slab");
        add(FBContent.blockFramedDoubleSlopeSlab.get(), "Framed Double Slope Slab");
        add(FBContent.blockFramedInverseDoubleSlopeSlab.get(), "Framed Inverted Double Slope Slab");
        add(FBContent.blockFramedElevatedDoubleSlopeSlab.get(), "Framed Elevated Double Slope Slab");
        add(FBContent.blockFramedStackedSlopeSlab.get(), "Framed Stacked Slope Slab");
        add(FBContent.blockFramedFlatSlopeSlabCorner.get(), "Framed Flat Slope Slab Corner");
        add(FBContent.blockFramedFlatInnerSlopeSlabCorner.get(), "Framed Flat Inner Slope Slab Corner");
        add(FBContent.blockFramedFlatElevatedSlopeSlabCorner.get(), "Framed Flat Elevated Slope Slab Corner");
        add(FBContent.blockFramedFlatElevatedInnerSlopeSlabCorner.get(), "Framed Flat Elevated Inner Slope Slab Corner");
        add(FBContent.blockFramedFlatDoubleSlopeSlabCorner.get(), "Framed Flat Double Slope Slab Corner");
        add(FBContent.blockFramedFlatInverseDoubleSlopeSlabCorner.get(), "Framed Flat Inverse Double Slope Slab Corner");
        add(FBContent.blockFramedFlatElevatedDoubleSlopeSlabCorner.get(), "Framed Flat Elevated Double Slope Slab Corner");
        add(FBContent.blockFramedFlatElevatedInnerDoubleSlopeSlabCorner.get(), "Framed Flat Elevated Inner Double Slope Slab Corner");
        add(FBContent.blockFramedFlatStackedSlopeSlabCorner.get(), "Framed Flat Stacked Slope Slab Corner");
        add(FBContent.blockFramedFlatStackedInnerSlopeSlabCorner.get(), "Framed Flat Stacked Inner Slope Slab Corner");
        add(FBContent.blockFramedVerticalHalfStairs.get(), "Framed Vertical Half Stairs");
        add(FBContent.blockFramedSlopePanel.get(), "Framed Slope Panel");
        add(FBContent.blockFramedExtendedSlopePanel.get(), "Framed Extended Slope Panel");
        add(FBContent.blockFramedDoubleSlopePanel.get(), "Framed Double Slope Panel");
        add(FBContent.blockFramedInverseDoubleSlopePanel.get(), "Framed Inverted Double Slope Panel");
        add(FBContent.blockFramedExtendedDoubleSlopePanel.get(), "Framed Extended Double Slope Panel");
        add(FBContent.blockFramedStackedSlopePanel.get(), "Framed Stacked Slope Panel");
        add(FBContent.blockFramedFlatSlopePanelCorner.get(), "Framed Flat Slope Panel Corner");
        add(FBContent.blockFramedFlatInnerSlopePanelCorner.get(), "Framed Flat Inner Slope Panel Corner");
        add(FBContent.blockFramedFlatExtendedSlopePanelCorner.get(), "Framed Flat Extended Slope Panel Corner");
        add(FBContent.blockFramedFlatExtendedInnerSlopePanelCorner.get(), "Framed Flat Extended Inner Slope Panel Corner");
        add(FBContent.blockFramedFlatDoubleSlopePanelCorner.get(), "Framed Flat Double Slope Panel Corner");
        add(FBContent.blockFramedFlatInverseDoubleSlopePanelCorner.get(), "Framed Flat Inverse Double Slope Panel Corner");
        add(FBContent.blockFramedFlatExtendedDoubleSlopePanelCorner.get(), "Framed Flat Extended Double Slope Panel Corner");
        add(FBContent.blockFramedFlatExtendedInnerDoubleSlopePanelCorner.get(), "Framed Flat Extended Inner Double Slope Panel Corner");
        add(FBContent.blockFramedFlatStackedSlopePanelCorner.get(), "Framed Flat Stacked Slope Panel Corner");
        add(FBContent.blockFramedFlatStackedInnerSlopePanelCorner.get(), "Framed Flat Stacked Inner Slope Panel Corner");
        add(FBContent.blockFramedDoubleStairs.get(), "Framed Double Stairs");
        add(FBContent.blockFramedVerticalDoubleStairs.get(), "Framed Vertical Double Stairs");
        add(FBContent.blockFramedWallBoard.get(), "Framed Wall Board");
        add(FBContent.blockFramedGlowingCube.get(), "Framed Glowing Cube");
        add(FBContent.blockFramedPyramid.get(), "Framed Pyramid");
        add(FBContent.blockFramedPyramidSlab.get(), "Framed Pyramid Slab");
        add(FBContent.blockFramedHorizontalPane.get(), "Framed Horizontal Pane");
        add(FBContent.blockFramedLargeButton.get(), "Large Framed Button");
        add(FBContent.blockFramedLargeStoneButton.get(), "Large Framed Stone Button");
        add(FBContent.blockFramedTarget.get(), "Framed Target");
        add(FBContent.blockFramedGate.get(), "Framed Gate");
        add(FBContent.blockFramedIronGate.get(), "Framed Iron Gate");
        add(FBContent.blockFramedItemFrame.get(), "Framed Item Frame");
        add(FBContent.blockFramedGlowingItemFrame.get(), "Framed Glow Item Frame");
        add(FBContent.blockFramedFancyRail.get(), "Framed Fancy Rail");
        add(FBContent.blockFramedFancyPoweredRail.get(), "Framed Fancy Powered Rail");
        add(FBContent.blockFramedFancyDetectorRail.get(), "Framed Fancy Detector Rail");
        add(FBContent.blockFramedFancyActivatorRail.get(), "Framed Fancy Activator Rail");
        add(FBContent.blockFramedFancyRailSlope.get(), "Framed Fancy Rail Slope");
        add(FBContent.blockFramedFancyPoweredRailSlope.get(), "Framed Fancy Powered Rail Slope");
        add(FBContent.blockFramedFancyDetectorRailSlope.get(), "Framed Fancy Detector Rail Slope");
        add(FBContent.blockFramedFancyActivatorRailSlope.get(), "Framed Fancy Activator Rail Slope");
        add(FBContent.blockFramedHalfSlope.get(), "Framed Half Slope");
        add(FBContent.blockFramedVerticalHalfSlope.get(), "Framed Half Slope");
        add(FBContent.blockFramedDividedSlope.get(), "Framed Divided Slope");
        add(FBContent.blockFramedDoubleHalfSlope.get(), "Framed Double Half Slope");
        add(FBContent.blockFramedVerticalDoubleHalfSlope.get(), "Framed Double Half Slope");
        add(FBContent.blockFramedSlopedStairs.get(), "Framed Sloped Stairs");
        add(FBContent.blockFramedVerticalSlopedStairs.get(), "Framed Vertical Sloped Stairs");
        add(FBContent.blockFramedMiniCube.get(), "Framed Mini Cube");

        add(FBContent.blockFramingSaw.get(), "Framing Saw");

        add(FBContent.itemFramedHammer.get(), "Framed Hammer");
        add(FBContent.itemFramedWrench.get(), "Framed Wrench");
        add(FBContent.itemFramedBlueprint.get(), "Framed Blueprint");
        add(FBContent.itemFramedKey.get(), "Framed Key");
        add(FBContent.itemFramedScrewdriver.get(), "Framed Screwdriver");
        add(FBContent.itemFramedReinforcement.get(), "Framed Reinforcement");

        add(KeyMappings.KEY_CATEGORY, "FramedBlocks");
        add(KeyMappings.KEYMAPPING_UPDATE_CULLING.get().getName(), "Update culling cache");

        add(FramedCreativeTab.get().getDisplayName(), "FramedBlocks");
        add(FramedBlockEntity.MSG_BLACKLISTED, "This block is blacklisted!");
        add(FramedBlockEntity.MSG_BLOCK_ENTITY, "Blocks with BlockEntities cannot be inserted into framed blocks!");
        add(FramedChestBlockEntity.TITLE, "Framed Chest");
        add(FramedStorageBlockEntity.TITLE, "Framed Secret Storage");
        add(FramedBlueprintItem.CONTAINED_BLOCK, "Contained Block: %s");
        add(FramedBlueprintItem.CAMO_BLOCK, "Camo Block: %s");
        add(FramedBlueprintItem.IS_ILLUMINATED, "Illuminated: %s");
        add(FramedBlueprintItem.IS_INTANGIBLE, "Intangible: %s");
        add(FramedBlueprintItem.IS_REINFORCED, "Reinforced: %s");
        add(FramedBlueprintItem.BLOCK_NONE, "None");
        add(FramedBlueprintItem.BLOCK_INVALID, "Invalid");
        add(FramedBlueprintItem.FALSE, "false");
        add(FramedBlueprintItem.TRUE, "true");
        add(FramedBlueprintItem.CANT_COPY, "This block can currently not be copied!");
        add(FramedBlueprintItem.CANT_PLACE_FLUID_CAMO, "Copying blocks with fluid camos is currently not possible!");
        add(FramedSignScreen.TITLE, "Edit sign");
        add(FramedSignScreen.DONE, "Done");
        add(IFramedBlock.LOCK_MESSAGE, "The state of this block is now %s");
        add(StateLockOverlay.LOCK_MESSAGE, "State %s");
        add(IFramedBlock.STATE_LOCKED, "locked");
        add(IFramedBlock.STATE_UNLOCKED, "unlocked");
        add(ToggleWaterloggableOverlay.MSG_IS_WATERLOGGABLE, "Block is waterloggable");
        add(ToggleWaterloggableOverlay.MSG_IS_NOT_WATERLOGGABLE, "Block is not waterloggable");
        add(ToggleWaterloggableOverlay.MSG_MAKE_WATERLOGGABLE, "Hit with a Framed Hammer to make waterloggable");
        add(ToggleWaterloggableOverlay.MSG_MAKE_NOT_WATERLOGGABLE, "Hit with a Framed Hammer to make not waterloggable");
        add(ToggleYSlopeOverlay.SLOPE_MESSAGE, "Block uses %s faces for vertical sloped faces");
        add(ToggleYSlopeOverlay.TOGGLE_MESSAGE, "Hit with a Framed Wrench to switch to %s faces");
        add(ToggleYSlopeOverlay.SLOPE_HOR, "horizontal");
        add(ToggleYSlopeOverlay.SLOPE_VERT, "vertical");
        add(ReinforcementOverlay.REINFORCE_MESSAGE, "Block is %s");
        add(ReinforcementOverlay.STATE_NOT_REINFORCED, "not reinforced");
        add(ReinforcementOverlay.STATE_REINFORCED, "reinforced");
        add(PrismOffsetOverlay.PRISM_OFFSET_FALSE, "Triangle texture is not offset.");
        add(PrismOffsetOverlay.PRISM_OFFSET_TRUE, "Triangle texture is offset by half a block.");
        add(PrismOffsetOverlay.MSG_SWITCH_OFFSET, "Hit with a Framed Hammer to toggle the offset");
        add(SplitLineOverlay.SPLIT_LINE_FALSE, "Split-line of the deformed face runs along the steep diagonal.");
        add(SplitLineOverlay.SPLIT_LINE_TRUE, "Split-line of the deformed face runs along the shallow diagonal.");
        add(SplitLineOverlay.MSG_SWITCH_SPLIT_LINE, "Hit with a Framed Hammer to switch the orientation of the split-line");
        add(FramingSawBlock.MENU_TITLE, "Framing Saw");
        add(FramingSawScreen.TOOLTIP_MATERIAL, "Material value: %s");
        add(FramingSawScreen.TOOLTIP_LOOSE_ADDITIVE, "Item was crafted with additive ingredients, these will be lost");
        add(FramingSawScreen.TOOLTIP_HAVE_X_BUT_NEED_Y_ITEM, "Have %s, but need %s");
        add(FramingSawScreen.TOOLTIP_HAVE_X_BUT_NEED_Y_TAG, "Have %s, but need any %s");
        add(FramingSawScreen.TOOLTIP_HAVE_X_BUT_NEED_Y_ITEM_COUNT, "Have %s item(s), but need at least %s item(s)");
        add(FramingSawScreen.TOOLTIP_HAVE_X_BUT_NEED_Y_MATERIAL_COUNT, "Have %s material, but need at least %s material");
        add(FramingSawScreen.TOOLTIP_HAVE_ITEM_NONE, "none");
        add(FramingSawScreen.TOOLTIP_PRESS_TO_SHOW, "Press [%s] to show all possible items");
        add(FramingSawRecipe.FailReason.NONE.translation(), "Craftable");
        add(FramingSawRecipe.FailReason.MATERIAL_VALUE.translation(), "Insufficient input material available");
        add(FramingSawRecipe.FailReason.MATERIAL_LCM.translation(), "Too few input items to evenly convert to this output");
        add(FramingSawRecipe.FailReason.MISSING_ADDITIVE.translation(), "Missing additive ingredient");
        add(FramingSawRecipe.FailReason.UNEXPECTED_ADDITIVE.translation(), "Unexpected additive ingredient present");
        add(FramingSawRecipe.FailReason.INCORRECT_ADDITIVE.translation(), "Incorrect additive ingredient present");
        add(FramingSawRecipe.FailReason.INSUFFICIENT_ADDITIVE.translation(), "Insufficient amount of additive ingredient present");
    }

    private void add(Component key, String value)
    {
        ComponentContents contents = key.getContents();
        if (contents instanceof TranslatableContents translatable)
        {
            add(translatable.getKey(), value);
        }
        else
        {
            add(key.getString(), value);
        }
    }
}