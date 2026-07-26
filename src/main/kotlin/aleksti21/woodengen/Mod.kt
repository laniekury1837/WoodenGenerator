package aleksti21.woodengen

import aleksti21.woodengen.Wood.WoodConfigData
import aleksti21.woodengen.Wood.WoodPart
import net.fabricmc.api.ModInitializer

const val MOD_ID = "woodengen"
val CONFIGS = listOf<ConfigData>(WoodConfigData("custom", woodColor = 0xFFC7FCEC.toInt(), leavesColor = 0xFFFC6DA0.toInt()))
val PARTS = listOf(WoodPart.entries)
class Mod : ModInitializer {
    override fun onInitialize() {
        Registrator.registerAll(CONFIGS)
//        YarrpCallbacks.register(PackPosition.AFTER_VANILLA, ResourceType.SERVER_DATA) { add(DYNAMIC_PACK) }
    }
}
