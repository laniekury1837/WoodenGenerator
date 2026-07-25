package aleksti21.woodengen.Wood

import aleksti21.woodengen.Family
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry
import net.fabricmc.fabric.api.registry.FuelRegistry
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry
import net.minecraft.block.Block

data class WoodFamily(
    override val blocks: Map<WoodPart, Block> = mapOf(),
//    val items: Map<WoodPart, Item> = mapOf(),
//    val entities: Map<WoodPart, Entity> = mapOf(),

    override val config: WoodConfigData,
): Family {
    override fun onRegistered() {
        blocks.forEach { (part, block) ->
            if (config.isFlammable == true) {
                val registry = FlammableBlockRegistry.getDefaultInstance()
                val default1 = registry.get(part.baseBlock)
                registry.add(block,default1.burnChance, default1.spreadChance)
            }
            val registry2 = FuelRegistry.INSTANCE
            val default2 = registry2.get(part.baseBlock)
            if (default2 != 0 && default2 != null) registry2.add(block, default2)

            val registry3 = CompostingChanceRegistry.INSTANCE
            val default3 = registry3.get(part.baseBlock)
            if (default3 != 0.0f && default3 != null) registry3.add(block, default3)

        }
        StrippableBlockRegistry.register(blocks[WoodPart.LOG], blocks[WoodPart.STRIPPED_LOG])
    }
}
