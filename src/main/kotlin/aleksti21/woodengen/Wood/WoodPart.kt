package aleksti21.woodengen.Wood

import aleksti21.woodengen.BlockPart
import net.minecraft.block.*

enum class WoodPart(
    override val baseBlock: Block,
    override val factory: (AbstractBlock.Settings) -> Block,
    override val multiplier: Float = 1.0f,
    override val isCutout: Boolean = false
): BlockPart {
    PLANKS(Blocks.OAK_PLANKS, ::Block),
    LOG(Blocks.OAK_LOG, ::PillarBlock),
    STRIPPED_LOG(Blocks.STRIPPED_OAK_LOG, ::PillarBlock),
    LEAVES(Blocks.OAK_LEAVES, ::LeavesBlock, isCutout = true),
    SLAB(Blocks.OAK_SLAB, ::SlabBlock),
    PRESSURE_PLATE(Blocks.OAK_PRESSURE_PLATE, { settings -> PressurePlateBlock(BlockSetType.OAK, settings) }, 0.25f),
    BUTTON(Blocks.OAK_BUTTON, { settings -> ButtonBlock(BlockSetType.OAK, 30, settings) }, 0.25f),
    FENCE(Blocks.OAK_FENCE, ::FenceBlock),
    DOOR(Blocks.OAK_DOOR, { settings -> DoorBlock(BlockSetType.OAK, settings) }, 1.5f, true),
    STAIRS(Blocks.OAK_STAIRS, { settings -> StairsBlock(Blocks.OAK_PLANKS.defaultState, settings) }),
    TRAPDOOR(Blocks.OAK_TRAPDOOR, { settings -> TrapdoorBlock(BlockSetType.OAK, settings) }, isCutout = true),
}