package aleksti21.woodengen

import aleksti21.woodengen.Wood.WoodConfigData
import aleksti21.woodengen.Wood.WoodPart
import aleksti21.woodengen.old.DYNAMIC_PACK
import aleksti21.woodengen.old.DummyWoodRegistry
import de.rubixdev.yarrp.api.PackPosition
import de.rubixdev.yarrp.api.YarrpCallbacks
import net.fabricmc.api.ModInitializer
import net.minecraft.resource.ResourceType
import kotlin.enums.EnumEntries
import kotlin.reflect.KClass

const val MOD_ID = "woodengen"
val CONFGIS = listOf<ConfigData>(WoodConfigData("custom"))
val PARTS = listOf(WoodPart.entries)
class Mod : ModInitializer {
    override fun onInitialize() {
        Registrator.registerAll(listOf(WoodConfigData("custom")))
//        YarrpCallbacks.register(PackPosition.AFTER_VANILLA, ResourceType.CLIENT_RESOURCES) { add(DYNAMIC_PACK) }
//        YarrpCallbacks.register(PackPosition.AFTER_VANILLA, ResourceType.SERVER_DATA) { add(DYNAMIC_PACK) }
    }
}
