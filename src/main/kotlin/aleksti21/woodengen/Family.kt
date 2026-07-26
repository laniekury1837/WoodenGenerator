package aleksti21.woodengen

import net.minecraft.block.Block

interface Family {
    val blocks: Map<out BlockPart, Block>
//    val items: Map<ItemPart, Item>
//    val entities: Map<EntityPart, Entity>
    val config: ConfigData
    fun onRegistered() {}
    fun getColorForPart(part: BlockPart): Int? = null
    fun getForm(part: BlockPart): Form
}