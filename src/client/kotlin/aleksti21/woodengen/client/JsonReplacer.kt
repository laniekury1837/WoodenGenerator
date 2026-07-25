package aleksti21.woodengen.client

import aleksti21.woodengen.Family
import aleksti21.woodengen.JsonType
import aleksti21.woodengen.addImage
import aleksti21.woodengen.addJson
import net.minecraft.registry.Registries

object JsonReplacer {
    fun transformAndRegister(family: Family) {
        for ((part, block) in family.blocks) {
            val template = JsonLoader.JSON_MAP[part]
            val blockId = Registries.BLOCK.getId(part.baseBlock)
            val customBlockId = Registries.BLOCK.getId(block)

            fun String.transform(): String {
                return this
                    .replace(blockId.namespace, customBlockId.namespace)
                    .replace(blockId.path, customBlockId.path)
            }

            addJson(JsonType.BLOCKSTATE, customBlockId.path, template?.blockstate?.transform())
            addJson(JsonType.BLOCK_MODEL, customBlockId.path,  template?.blockModels?.mapKeys { it.key.transform() } ?.mapValues { it.value.transform() })
            addJson(JsonType.ITEM_MODEL, customBlockId.path, template?.itemModel?.transform())
            addImage(customBlockId.path,  template?.textures?.mapKeys { it.key.transform() })
        }
    }
}