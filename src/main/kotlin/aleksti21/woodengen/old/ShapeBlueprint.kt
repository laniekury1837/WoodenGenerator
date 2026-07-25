package aleksti21.woodengen.old

class ModelDef(
    val suffix: String,
    val parent: String,
    val textureKeys: List<String>,
    val renderType: String?
)

class VariantDef(val condition: String, val modelSuffix: String, val x: Int, val y: Int, val uvlock: Boolean)

// Новый класс для Multipart (заборы)
class MultipartDef(val condition: String?, val modelSuffix: String, val x: Int, val y: Int, val uvlock: Boolean)

class ShapeBlueprint {
    val models = mutableListOf<ModelDef>()
    val variants = mutableListOf<VariantDef>()
    val multiparts = mutableListOf<MultipartDef>()
    val entityTextures = mutableListOf<Pair<String, String>>()

    // 🏆 НОВЫЕ ФИЧИ ДЛЯ ИНВЕНТАРЯ И ТЕГОВ
    var customItemModel: String? = null
    var isGeneratedItem: Boolean = false
    var generatedItemIndex: Int = 0 // Добавили индекс текстуры!

    fun generatedItem(texIndex: Int = 0) {
        isGeneratedItem = true
        generatedItemIndex = texIndex
    }
    val tags = mutableListOf<String>()

    fun itemModel(suffix: String) { customItemModel = suffix }
    fun generatedItem() { isGeneratedItem = true } // Делает предмет плоским (для двери)
    fun tag(name: String) { tags.add(name) }

    fun model(suffix: String, parent: String, vararg texKeys: String, renderType: String? = null) {
        val resolvedParent = if (":" in parent) parent else "minecraft:block/$parent"
        models.add(ModelDef(suffix, resolvedParent, texKeys.toList(), renderType))
    }

    fun variant(condition: String, modelSuffix: String, x: Int = 0, y: Int = 0, uvlock: Boolean = false) {
        variants.add(VariantDef(condition, modelSuffix, x, y, uvlock))
    }

    // НОВАЯ функция для добавления кусочка забора
    fun part(condition: String? = null, modelSuffix: String, x: Int = 0, y: Int = 0, uvlock: Boolean = false) {
        multiparts.add(MultipartDef(condition, modelSuffix, x, y, uvlock))
    }

//    init {
//        ShapeBlueprint().apply(this)
//    }
//
//    companion object {
//        init {
//            ShapeBlueprint().apply(this)
//        }
//    }
}

//// Помощник, чтобы красиво писать чертежи в Энаме
fun blueprint(init: ShapeBlueprint.() -> Unit): ShapeBlueprint {
    return ShapeBlueprint().apply(init)
}