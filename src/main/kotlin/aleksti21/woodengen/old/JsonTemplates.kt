package aleksti21.woodengen.old

object JsonTemplates {
    fun buildModel(parent: String, vararg textures: Pair<String, String>, renderType: String? = null): String {
        val texJson = textures.joinToString(", ") { """ "${it.first}": "${it.second}" """ }
        val renderJson = if (renderType != null) """"render_type": "$renderType", """ else ""
        return """{ "parent": "$parent", $renderJson"textures": { $texJson } }"""
    }

    // 🏆 Магия DSL для сборки Blockstate
    class StateBuilder {
        private val variants = mutableListOf<String>()

        fun variant(condition: String, modelRef: String, x: Int = 0, y: Int = 0, uvlock: Boolean = false) {
            val rotX = if (x != 0) """, "x": $x""" else ""
            val rotY = if (y != 0) """, "y": $y""" else ""
            val uv = if (uvlock) """, "uvlock": true""" else ""
            variants.add(""""$condition": { "model": "$modelRef"$rotX$rotY$uv }""")
        }

        fun buildVariants(): String = """{ "variants": { ${variants.joinToString(", ")} } }"""
        fun buildMultipart(): String = """{ "multipart": [ ${variants.joinToString(", ")} ] }"""

        fun part(condition: String?, modelRef: String, x: Int = 0, y: Int = 0, uvlock: Boolean = false) {
            val rotX = if (x != 0) """, "x": $x""" else ""
            val rotY = if (y != 0) """, "y": $y""" else ""
            val uv = if (uvlock) """, "uvlock": true""" else ""
            val applyStr = """{ "model": "$modelRef"$rotX$rotY$uv }"""

            if (condition == null) {
                variants.add("""{ "apply": $applyStr }""")
            } else {
                val (k, v) = condition.split("=")
                variants.add("""{ "when": { "$k": "$v" }, "apply": $applyStr }""")
            }
        }
    }

    fun itemModel(modelRef: String) = """{ "parent": "$modelRef" }"""
}