package aleksti21.woodengen.client

import aleksti21.woodengen.DYNAMIC_PACK
import aleksti21.woodengen.Registrator
import de.rubixdev.yarrp.api.PackPosition
import de.rubixdev.yarrp.api.YarrpCallbacks
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.resource.ResourceType

class ModClient : ClientModInitializer {
    override fun onInitializeClient() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(JsonLoader)
        Registrator.families.forEach { family -> JsonReplacer.transformAndRegister(family) }
        YarrpCallbacks.register(PackPosition.AFTER_VANILLA, ResourceType.CLIENT_RESOURCES) {add(DYNAMIC_PACK)}
    }
}
