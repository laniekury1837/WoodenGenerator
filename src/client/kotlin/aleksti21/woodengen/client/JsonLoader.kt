package aleksti21.woodengen.client

import aleksti21.woodengen.BlockPart
import aleksti21.woodengen.PARTS
import aleksti21.woodengen.Registrator
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
import net.minecraft.client.MinecraftClient
import net.minecraft.registry.Registries
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import kotlin.jvm.optionals.getOrNull

object JsonLoader : SimpleSynchronousResourceReloadListener {
    val JSON_MAP = mutableMapOf<BlockPart, JsonDataClientTemplate>()
    private var isGenerated = false
    override fun getFabricId() = Identifier.of("woodengen", "listener")

    override fun reload(manager: ResourceManager) {
        PARTS.forEach { part ->
            part.forEach { item ->
                val id = Registries.BLOCK.getId(item.baseBlock)
                //1: blockstate
                val blockstateString = manager.getResource(Identifier.of(id.namespace, "blockstates/${id.path}.json")).getOrNull()?.inputStream?.bufferedReader()?.use { it.readText() } ?: return@forEach
                val blockstateJson = JsonParser.parseString(blockstateString).asJsonObject
                //2: models
                val modelPaths = mutableSetOf<String>()
                val blockModels = mutableMapOf<String, String>()

                val itemModelString = manager.getResource(Identifier.of(id.namespace, "models/item/${id.path}.json")).getOrNull()?.inputStream?.bufferedReader()?.use { it.readText() } ?: blockModels.values.firstOrNull() ?: ""
                val itemModelJson = JsonParser.parseString(itemModelString).asJsonObject

                if (blockstateJson.has("variants")) blockstateJson["variants"].asJsonObject.entrySet().forEach { entry ->
                    val variant = entry.value
                    if (variant.isJsonObject) variant.asJsonObject.get("model")?.asString?.let { modelPaths.add(it) } else variant.asJsonArray.forEach {model -> modelPaths.add(model.asJsonObject.get("model")?.asString ?: return@forEach) }
                } else if (blockstateJson.has("multipart")) blockstateJson["multipart"].asJsonArray.forEach { model -> modelPaths.add(model.asJsonObject.get("apply")?.asJsonObject?.get("model")?.asString ?: return@forEach) }

                if (itemModelJson.has("parent")) if (itemModelJson["parent"].asString.contains("block/")) modelPaths.add(itemModelJson["parent"].asString)

                modelPaths.forEach { path ->
                    val path = Identifier.of(path)
                    blockModels[path.toString()] = manager.getResource(Identifier.of(path.namespace, "models/${path.path}.json")).getOrNull()?.inputStream?.bufferedReader()?.use { it.readText() } ?: return@forEach
                }
                //3: textures
                val texturePaths = mutableSetOf<String>()
                val textures = mutableMapOf<String, ByteArray>()

                fun textureGet(json: JsonObject) {
                    if (json.has("textures")) {
                        json["textures"].asJsonObject.entrySet().forEach { entry ->
                            if (entry.value.asString[0] != '#') texturePaths.add(entry.value.asString)
                        }
                    }
                }

                blockModels.values.forEach { model ->
                    textureGet(JsonParser.parseString(model).asJsonObject)
                }

                textureGet(itemModelJson)

                texturePaths.forEach { path ->
                    val path = Identifier.of(path)
                    textures[path.toString()] = manager.getResource(Identifier.of(path.namespace, "textures/${path.path}.png")).getOrNull()?.inputStream?.use { it.readBytes() } ?: return@forEach
                }
                //5: final
                JSON_MAP[item] = JsonDataClientTemplate(blockstateString, blockModels, itemModelString, textures)
                println("[JsonLoader] ✅ Загружен шаблон для: ${item.name}. Модели блоков: ${blockModels.keys}")
            }
        }
        Registrator.families.forEach { family -> JsonReplacer.transformAndRegister(family) }
        if (!isGenerated) {
            isGenerated = true
            MinecraftClient.getInstance().reloadResources()
        }
    }
}