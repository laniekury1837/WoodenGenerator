package aleksti21.woodengen.client

import aleksti21.woodengen.BlockPart
import aleksti21.woodengen.MOD_ID
import aleksti21.woodengen.PARTS
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import de.rubixdev.yarrp.api.RuntimeResourcePack
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
import net.minecraft.registry.Registries
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.ResourceType
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import kotlin.jvm.optionals.getOrNull

class ModClient : ClientModInitializer {
    override fun onInitializeClient() {
        val BLOCKSTATE_JSON_MAP = mutableMapOf<BlockPart, JsonDataClientTemplate>()
        val DYNAMIC_PACK = RuntimeResourcePack(
            RuntimeResourcePack.createInfo(Identifier.of(MOD_ID, "dynamic_pack"), Text.literal("Woodengen Dynamic Resources"), "1.0.0"),
            RuntimeResourcePack.createMetadata(Text.literal("Dynamic wood blocks"))
        )
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(
            object : SimpleSynchronousResourceReloadListener {
                override fun getFabricId() = Identifier.of("woodengen", "listener")

                override fun reload(manager: ResourceManager) {
                    PARTS.forEach { part ->
                        part.forEach { item ->
                            val id = Registries.BLOCK.getId(item.baseBlock)
                            //1: blockstate
                            val blockstateString = manager.getResource(Identifier.of(id.namespace, "blockstates/${id.path}.json")).getOrNull()?.inputStream?.bufferedReader()?.use { it.readText() } ?: return@forEach
                            val blockstateJson = JsonParser.parseString(blockstateString).asJsonObject
                            //2: model
                            val modelPaths = mutableListOf<String>()
                            val blockModels = mutableMapOf<String, String>()

                            if (blockstateJson.has("variants")) blockstateJson["variants"].asJsonObject.entrySet().forEach { entry ->
                                val variant = entry.value
                                if (variant.isJsonObject) modelPaths.add(variant.asJsonObject.get("model").asString) else variant.asJsonArray.forEach {model -> modelPaths.add(model.asJsonObject.get("model").asString)}
                            } else if (blockstateJson.has("multipart")) blockstateJson["multipart"].asJsonArray.forEach { model -> modelPaths.add(model.asJsonObject.getAsJsonObject("apply").get("model").asString) }
                            modelPaths.forEach { path ->
                                val path = Identifier.of(path)
                                blockModels[path.toString()] = manager.getResource(Identifier.of(path.namespace, "models/${path.path}.json")).getOrNull()?.inputStream?.bufferedReader()?.use { it.readText() } ?: return@forEach
                            }
                            //3: item model
                            val itemModelString = manager.getResource(Identifier.of(id.namespace, "models/item/${id.path}.json")).getOrNull()?.inputStream?.bufferedReader()?.use { it.readText() } ?: return@forEach
                            val itemModelJson = JsonParser.parseString(itemModelString).asJsonObject
                            //4: textures
                            val texturePaths = mutableSetOf<String>()
                            val textures = mutableMapOf<String, ByteArray>()

                            fun textureGet(json: JsonObject) {
                                if (json.has("texture")) {
                                    json["texture"].asJsonObject.entrySet().forEach { entry ->
                                        if (entry.value.asString[0] != '#') texturePaths.add(entry.value.asString)
                                    }
                                }
                            }
                            textureGet(blockstateJson)
                            textureGet(itemModelJson)

                            texturePaths.forEach { path ->
                                val path = Identifier.of(path)
                                textures[path.toString()] = manager.getResource(Identifier.of(path.namespace, "textures/${path.path}")).getOrNull()?.inputStream?.use { it.readBytes() } ?: return@forEach
                            }
                            //5: final
                            BLOCKSTATE_JSON_MAP[item] = JsonDataClientTemplate(blockstateString, blockModels, itemModelString, textures)
                        }
                        println("Успешно прочитано:\n${BLOCKSTATE_JSON_MAP.values.first()}")
                    }
                }
            }
        )

    }
}
