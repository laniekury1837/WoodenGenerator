package aleksti21.woodengen.Wood

import aleksti21.woodengen.BlockPart
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.BlockSetType
import net.minecraft.block.Blocks
import net.minecraft.block.ButtonBlock
import net.minecraft.block.DoorBlock
import net.minecraft.block.FenceBlock
import net.minecraft.block.LeavesBlock
import net.minecraft.block.PillarBlock
import net.minecraft.block.PressurePlateBlock
import net.minecraft.block.SlabBlock
import net.minecraft.block.StairsBlock
import net.minecraft.block.TrapdoorBlock

enum class WoodPart(
    override val baseBlock: Block,
    override val factory: (AbstractBlock.Settings) -> Block,
    override val multiplier: Float = 1.0f,
): BlockPart {
    PLANKS(Blocks.OAK_PLANKS, ::Block),
    LOG(Blocks.OAK_LOG, ::PillarBlock),
    STRIPPED_LOG(Blocks.STRIPPED_OAK_LOG, ::PillarBlock),
    LEAVES(Blocks.OAK_LEAVES, ::LeavesBlock),
    SLAB(Blocks.OAK_SLAB, ::SlabBlock),
    PRESSURE_PLATE(Blocks.OAK_PRESSURE_PLATE, { settings -> PressurePlateBlock(BlockSetType.OAK, settings) }, 0.25f),
    BUTTON(Blocks.OAK_BUTTON, { settings -> ButtonBlock(BlockSetType.OAK, 30, settings) }, 0.25f),
    FENCE(Blocks.OAK_FENCE, ::FenceBlock),
    DOOR(Blocks.OAK_DOOR, { settings -> DoorBlock(BlockSetType.OAK, settings) }, 1.5f),
    STAIRS(Blocks.OAK_STAIRS, { settings -> StairsBlock(Blocks.OAK_PLANKS.defaultState, settings) }),
    TRAPDOOR(Blocks.OAK_TRAPDOOR, { settings -> TrapdoorBlock(BlockSetType.OAK, settings) }),
}