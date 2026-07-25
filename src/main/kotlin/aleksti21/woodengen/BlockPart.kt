package aleksti21.woodengen

import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block

interface BlockPart {
    val baseBlock: Block
    val factory: (AbstractBlock.Settings) -> Block
    val multiplier: Float
    val name: String
}