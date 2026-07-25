package aleksti21.woodengen

import de.rubixdev.yarrp.api.RuntimeResourcePack
import net.minecraft.resource.ResourceType
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import java.io.ByteArrayInputStream

enum class JsonType(vararg val pathDir: String) {
    BLOCK_MODEL("models", "block"),
    ITEM_MODEL("models", "item"),
    BLOCKSTATE("blockstates")
}

val DYNAMIC_PACK = RuntimeResourcePack(
    RuntimeResourcePack.createInfo(Identifier.of(MOD_ID, "dynamic_pack"), Text.literal("Woodengen Dynamic Resources"), "1.0.0"),
    RuntimeResourcePack.createMetadata(Text.literal("Dynamic wood blocks"))
)

fun addImage(path: List<String>, bytes: ByteArray) {
    DYNAMIC_PACK.addResource(ResourceType.CLIENT_RESOURCES, path) { ByteArrayInputStream(bytes) }
}

fun addJson(type: JsonType, name: String, json: String) {
    DYNAMIC_PACK.addResource(ResourceType.CLIENT_RESOURCES, listOf(MOD_ID, *type.pathDir, "$name.json"), json.trimIndent())
}

fun addDataJson(path: List<String>, json: String) {
    DYNAMIC_PACK.addResource(ResourceType.SERVER_DATA, path, json.trimIndent())
}

