package aleksti21.woodengen.Wood

import aleksti21.woodengen.ConfigData
import aleksti21.woodengen.Family
import net.minecraft.block.AbstractBlock

data class WoodConfigData(
    //server:
    override val id: String,
    val isFlammable: Boolean = true,
    val hardness: Float = 2.0f,
    val resistance: Float = 3.0f,
    val allowsCocoa: Boolean = false,
    val mushrooms: Boolean = false,
    val width: Int = 1,
    val height: Int = 5,
    val mangroveDown: Boolean = false,
    val mangroveUp: Boolean = false,

    //client:
    val woodColor: Int = 0,
    val grassColor: Int = 0,
    val leavesColor: Int = grassColor,
    val doorForm: WoodForm = WoodForm.OAK,
    val trapdoorForm: WoodForm = WoodForm.OAK,
    val treeBlockForm: WoodForm = WoodForm.OAK,
): ConfigData {
    override fun register(): Family {
        val generatedBlocks = WoodPart.entries.associateWith { part ->
            part.factory(AbstractBlock.Settings.copy(part.baseBlock)
                .strength(hardness * part.multiplier, resistance * part.multiplier)
            )
        }
        return WoodFamily(generatedBlocks, this)
    }
}
