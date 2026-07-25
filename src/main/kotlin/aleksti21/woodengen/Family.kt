package aleksti21.woodengen

import net.minecraft.block.Block
import net.minecraft.entity.Entity
import net.minecraft.item.Item

interface Family {
    val blocks: Map<out BlockPart, Block>
//    val items: Map<ItemPart, Item>
//    val entities: Map<EntityPart, Entity>
    val config: ConfigData
    fun onRegistered() {}
}