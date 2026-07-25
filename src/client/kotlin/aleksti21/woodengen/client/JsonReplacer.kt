package aleksti21.woodengen.client

import aleksti21.woodengen.Family
import net.minecraft.registry.Registries

object JsonReplacer {
    fun transformAndRegister(family: Family) {
        for ((part, block) in family.blocks) {
            val template = JsonLoader.JSON_MAP[part]
            val blockId = Registries.BLOCK.getId(part.baseBlock)
            val customBlockId = Registries.BLOCK.getId(block)
            val list = listOf<String?>(template?.blockstate, *template?.blockModels?.keys, )

            val blockstate = template?.blockstate
                ?.replace(blockId.namespace, customBlockId.namespace)
                ?.replace(blockId.path, customBlockId.path)


            template?.blockModels?.forEach { (path, json) ->

            }

        }
    }
}