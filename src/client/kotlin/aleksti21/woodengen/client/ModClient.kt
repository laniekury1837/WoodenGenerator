package aleksti21.woodengen.client

import aleksti21.woodengen.MOD_ID
import de.rubixdev.yarrp.api.RuntimeResourcePack
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.resource.ResourceType
import net.minecraft.text.Text
import net.minecraft.util.Identifier

val DYNAMIC_PACK = RuntimeResourcePack(
    RuntimeResourcePack.createInfo(Identifier.of(MOD_ID, "dynamic_pack"), Text.literal("Woodengen Dynamic Resources"), "1.0.0"),
    RuntimeResourcePack.createMetadata(Text.literal("Dynamic wood blocks"))
)

class ModClient : ClientModInitializer {
    override fun onInitializeClient() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(JsonLoader)
    }
}
