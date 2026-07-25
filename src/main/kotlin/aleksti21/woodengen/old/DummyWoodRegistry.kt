package aleksti21.woodengen.old

import net.minecraft.block.*
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object DummyWoodRegistry {
    val DUMMY_FAMILIES = mutableListOf<WoodFamily>()
    val MAX_SETS = 1
    val name: String = "custom"

    fun registerAll() {
        for (i in 1..MAX_SETS) {

            val generatedBlocks = WoodPart.entries.associateWith { part ->
                val settings = AbstractBlock.Settings.copy(part.baseBlock)
                val blockInstance = part.factory(settings)
                registerBlock("${name}_${part.name.lowercase()}_${i}", blockInstance)
            }
            DUMMY_FAMILIES.add(WoodFamily(i, generatedBlocks))
            DynamicResources.generateForFamily(DUMMY_FAMILIES.last(), 0xFF0347)
        }
    }

    private fun registerBlock(name: String, block: Block): Block {
        val id = Identifier.of(MOD_ID, name)
        Registry.register(Registries.BLOCK, id, block)
        Registry.register(Registries.ITEM, id, BlockItem(block, Item.Settings()))
        return block
    }
}