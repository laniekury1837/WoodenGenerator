package aleksti21.woodengen.old

class FamilyContext(val familyIndex: Int, val color: Int) {
    // 🏆 Тот самый КЭШ! Сохраняет связку "исходник -> готовый путь"
    private val textureCache = mutableMapOf<String, String>()

    // Умная функция: если текстура уже красилась - отдает ссылку. Если нет - красит и сохраняет.
    private fun getOrMakeTexture(partName: String, sourceName: String, texSuffix: String = ""): String {
        // Проверяем кэш!
        if (textureCache.containsKey(sourceName)) {
            return textureCache[sourceName]!!
        }

        val texName = if (texSuffix.isEmpty()) partName else "${partName}_$texSuffix"
        val bytes = TextureGenerator.generateRecoloredTexture("/assets/$MOD_ID/textures/base/$sourceName", color)
        DynamicResources.addImage(listOf(MOD_ID, "textures", "block", "$texName.png"), bytes)

        val ref = "$MOD_ID:block/$texName"
        textureCache[sourceName] = ref // Записываем в кэш на будущее
        return ref
    }

    fun make(part: WoodPart) {
        val blockName = "dummy_${familyIndex}_${part.name.lowercase()}"
        val baseRef = "$MOD_ID:block/$blockName"

        // 1. Добываем/кэшируем текстуры
        val texRefs = part.textureNames.mapIndexed { index, tex ->
            getOrMakeTexture(blockName, tex, if (index == 1) "top" else if (index == 2) "bottom" else "")
        }

        val blueprint = part.shape.bp // Берем чертеж из Энама

        // 2. Генератор Моделей (читает чертеж)
        blueprint.models.forEach { modelDef ->
            val modelName = "$blockName${modelDef.suffix}"
            // Соединяем ключи из чертежа с реальными текстурами
            val mappedTextures = modelDef.textureKeys.mapIndexed { i, key ->
                key to texRefs.getOrElse(i) { texRefs[0] }
            }.toTypedArray()

            val json = JsonTemplates.buildModel(modelDef.parent, *mappedTextures, renderType = modelDef.renderType)
            DynamicResources.addJson(JsonType.BLOCK_MODEL, modelName, json)
        }

        // 3. Генератор Blockstate (читает чертеж)
        val stateJson = if (blueprint.multiparts.isNotEmpty()) {
            JsonTemplates.StateBuilder().apply {
                blueprint.multiparts.forEach { p -> part(p.condition, "$baseRef${p.modelSuffix}", p.x, p.y, p.uvlock) }
            }.buildMultipart()
        } else {
            JsonTemplates.StateBuilder().apply {
                blueprint.variants.forEach { v -> variant(v.condition, "$baseRef${v.modelSuffix}", v.x, v.y, v.uvlock) }
            }.buildVariants()
        }

        DynamicResources.addJson(JsonType.BLOCKSTATE, blockName, stateJson)
        // --- 4. Генератор Предметов (Читает из чертежа) ---
        if (blueprint.isGeneratedItem) {
            // Теперь он берет ту картинку, которую мы указали в generatedItem(индекс)
            val tex = texRefs.getOrElse(blueprint.generatedItemIndex) { texRefs[0] }
            val json = JsonTemplates.buildModel("minecraft:item/generated", "layer0" to tex)
            DynamicResources.addJson(JsonType.ITEM_MODEL, blockName, json)
        } else {
            // ... (остальной код для customItemModel оставляем как был)
            val itemRef = blueprint.customItemModel?.let { "$baseRef$it" } ?: baseRef
            DynamicResources.addJson(JsonType.ITEM_MODEL, blockName, JsonTemplates.itemModel(itemRef))
        }

        // --- 5. Генератор Тегов (Чтобы заборы соединялись) ---
        blueprint.tags.forEach { tag ->
            val (ns, path) = tag.split(":")
            val json = """{ "replace": false, "values": [ "$MOD_ID:$blockName" ] }"""
            // В 1.21 папка для тегов блоков называется "block" (ед. число)
            DynamicResources.addDataJson(listOf(ns, "tags", "block", "$path.json"), json)
        }
    }
}