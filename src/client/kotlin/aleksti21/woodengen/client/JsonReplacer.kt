package aleksti21.woodengen.client

import aleksti21.woodengen.Family
import aleksti21.woodengen.JsonType
import aleksti21.woodengen.addImage
import aleksti21.woodengen.addJson
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.minecraft.client.render.RenderLayer
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

object JsonReplacer {
    private fun recolor(texture: ByteArray, color: Int?): ByteArray {
        if (color == null) return texture
        val texture = ImageIO.read(ByteArrayInputStream(texture))
        val image = BufferedImage(texture.width, texture.height, BufferedImage.TYPE_INT_ARGB)
        image.graphics.drawImage(texture, 0, 0, null)
        val tr = (color shr 16) and 0xFF
        val tg = (color shr 8) and 0xFF
        val tb = (color and 0xFF)
        val targetHsb = FloatArray(3)
        Color.RGBtoHSB(tr, tg, tb, targetHsb)

        for (x in 0 until image.width) {
            for (y in 0 until image.height) {
                val argb = Color(image.getRGB(x, y))
                val pixelHsb = FloatArray(3)
                val newRgb = Color.HSBtoRGB(targetHsb[0], targetHsb[1], pixelHsb[2])
                val finalRgb = (argb.alpha shl 24) or (newRgb and 0x00FFFFFF)
                image.setRGB(x,y, finalRgb)
            }
        }
        val outputStream = ByteArrayOutputStream()
        ImageIO.write(image, "png", outputStream)
        println("цвет $color применён")
        return outputStream.toByteArray()
    }

    fun transformAndRegister(family: Family) {
        for ((part, block) in family.blocks) {
            val template = JsonLoader.JSON_MAP[part] ?: continue
            val blockId = Registries.BLOCK.getId(part.baseBlock)
            val customBlockId = Registries.BLOCK.getId(block)

            fun String.transform(): String {
                return this
                    .replace("${blockId.namespace}:block/${family.getForm(part).name.lowercase()}", "${customBlockId.namespace}:block/${family.config.id}")
            }

            addJson(JsonType.BLOCKSTATE, customBlockId.path, template.blockstate.transform())
            template.blockModels.mapKeys { it.key.transform() } .mapValues { it.value.transform() } .forEach { (key, json) ->
                addJson(JsonType.BLOCK_MODEL, Identifier.of(key).path.removePrefix("block/"), json)
            }
            addJson(JsonType.ITEM_MODEL, customBlockId.path, template.itemModel.transform())
            template.textures.mapKeys { it.key.transform() }.forEach { ( key, bytes) ->
                val key = Identifier.of(key).path
                addImage(listOf(customBlockId.namespace, "textures", key.substringBefore("/"), "${key.substringAfter("/")}.png"), recolor(bytes, family.getColorForPart(part)))
            }

            if (part.isCutout) BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout())

            println("[JsonReplacer] 🛠️ Заменяем и регистрируем в YARRP: ${customBlockId.path}")
            println("   - Зарегистрированы модели: ${template.blockModels.keys.map { it.transform() }}")
        }
    }
}