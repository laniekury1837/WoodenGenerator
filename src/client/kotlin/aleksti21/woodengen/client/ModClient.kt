package aleksti21.woodengen.client

import aleksti21.woodengen.BlockPart
import aleksti21.woodengen.MOD_ID
import aleksti21.woodengen.PARTS
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
                            val blockstateId = Identifier.of(id.namespace, "blockstates/${id.path}.json")

                            val blockstateString = manager.getResource(blockstateId).getOrNull()?.inputStream?.bufferedReader()?.use { it.readText() } ?: return@forEach
                            val modelPaths = mutableListOf<String>()
                            val blockModels = mutableMapOf<String, String>()

                            val blockstateJson = JsonParser.parseString(blockstateString).asJsonObject
                            if (blockstateJson.has("variants")) blockstateJson["variants"].asJsonObject.entrySet().forEach { entry ->
                                val variant = entry.value
                                if (variant.isJsonObject) modelPaths.add(variant.asJsonObject.get("model").asString) else variant.asJsonArray.forEach {model -> model.asJsonObject.get("model").asString}
                            } else if (blockstateJson.has("multipart")) blockstateJson["multipart"].asJsonArray.forEach { model -> modelPaths.add(model.asJsonObject.getAsJsonObject("apply").get("model").asString) }
                            modelPaths.forEach { path ->
                                val modelId = Identifier.of(id.namespace, "models/${path.drop(10)}.json")
                                blockModels[modelId.toString()] = manager.getResource(modelId).getOrNull()?.inputStream?.bufferedReader()?.use { it.readText() } ?: return@forEach
                            }

                        }
                        println("Успешно прочитано:\n${BLOCKSTATE_JSON_MAP.values.first()}")
                    }
                }
            }
        )

    }
}
