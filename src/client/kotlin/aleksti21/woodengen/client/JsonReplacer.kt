package aleksti21.woodengen.client

import aleksti21.woodengen.Family
import aleksti21.woodengen.JsonType
import aleksti21.woodengen.addImage
import aleksti21.woodengen.addJson
import net.minecraft.registry.Registries

object JsonReplacer {
    fun transformAndRegister(family: Family) {
        for ((part, block) in family.blocks) {
            val template = JsonLoader.JSON_MAP[part] ?: return
            val blockId = Registries.BLOCK.getId(part.baseBlock)
            val customBlockId = Registries.BLOCK.getId(block)

            fun String.transform(): String {
                return this
                    .replace(blockId.namespace, customBlockId.namespace)
                    .replace(blockId.path, customBlockId.path)
            }

            addJson(JsonType.BLOCKSTATE, customBlockId.path, template.blockstate.transform())
            template.blockModels.mapKeys { it.key.transform() } .mapValues { it.value.transform() } .forEach { (_, json) -> 
                addJson(JsonType.BLOCK_MODEL, customBlockId.path, json)
            }
            addJson(JsonType.ITEM_MODEL, customBlockId.path, template.itemModel.transform())
            template.textures.mapKeys { it.key.transform() }.forEach { ( _, bytes) ->
                addImage(listOf(customBlockId.path), bytes)
            }
        }
    }
}