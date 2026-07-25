package aleksti21.woodengen.old

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

const val MOD_ID = "woodengen"

enum class WoodPart(
    val baseBlock: Block,
    val factory: (AbstractBlock.Settings) -> Block,
    val shape: ShapeType,
    vararg val textureNames: String // Передаем просто названия файлов!
) {
    PLANKS(Blocks.OAK_PLANKS, ::Block, ShapeType.CUBE, "base_planks.png"),
    LOG(Blocks.OAK_LOG, ::PillarBlock, ShapeType.PILLAR, "base_log_side.png", "base_log_top.png"),
    LEAVES(Blocks.OAK_LEAVES, ::LeavesBlock, ShapeType.LEAVES, "base_leaves.png"),
    SLAB(Blocks.OAK_SLAB, ::SlabBlock, ShapeType.SLAB, "base_planks.png"),
    PRESSURE_PLATE(
        Blocks.OAK_PRESSURE_PLATE,
        { settings -> PressurePlateBlock(BlockSetType.OAK, settings) },
        ShapeType.PRESSURE_PLATE,
        "base_planks.png"),
    BUTTON(Blocks.OAK_BUTTON, { settings -> ButtonBlock(BlockSetType.OAK, 30, settings) }, ShapeType.BUTTON, "base_planks.png"),
    FENCE(Blocks.OAK_FENCE, ::FenceBlock, ShapeType.FENCE, "base_planks.png"),
    DOOR(Blocks.OAK_DOOR, { settings -> DoorBlock(BlockSetType.OAK, settings) }, ShapeType.DOOR, "base_door_bottom.png", "base_door_top.png"),
    STAIRS(Blocks.OAK_STAIRS, { settings -> StairsBlock(Blocks.OAK_PLANKS.defaultState, settings) }, ShapeType.STAIRS, "base_planks.png"),
    TRAPDOOR(Blocks.OAK_TRAPDOOR, { settings -> TrapdoorBlock(BlockSetType.OAK, settings) }, ShapeType.TRAPDOOR, "base_trapdoor.png"),
}

data class WoodFamily(
    val index: Int,
    val blocks: Map<WoodPart, Block>
) {
    val planks get() = blocks[WoodPart.PLANKS]!!
    val log get() = blocks[WoodPart.LOG]!!
    val leaves get() = blocks[WoodPart.LEAVES]!!
}

enum class JsonType(vararg val pathDir: String) {
    BLOCK_MODEL("models", "block"),
    ITEM_MODEL("models", "item"),
    BLOCKSTATE("blockstates")
}