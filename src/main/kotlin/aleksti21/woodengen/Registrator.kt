package aleksti21.woodengen

import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object Registrator {
    val families = mutableListOf<Family>()
    const val MAX_SETS = 1

    private fun registerBlock(name: String, block: Block): Block {
        val id = Identifier.of(MOD_ID, name)
        Registry.register(Registries.BLOCK, id, block)
        Registry.register(Registries.ITEM, id, BlockItem(block, Item.Settings()))
        return block
    }

    fun registerAll(configs: List<ConfigData>) {
        for (i in 1..MAX_SETS) {
            for (config in configs) {
                val family = config.register()
                for ((part, block) in family.blocks) registerBlock("${family.config.id}_${part.name.lowercase()}", block)
                family.onRegistered()
                families.add(family)
            }
        }
    }
}