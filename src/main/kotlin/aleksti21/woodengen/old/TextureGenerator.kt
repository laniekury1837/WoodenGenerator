package aleksti21.woodengen.old

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

object TextureGenerator {

    /**
     * Читает серую маску из файлов мода, красит её в нужный цвет и отдает готовый массив байтов для YARRP.
     *
     * @param path Путь к файлу (например: "/assets/woodengen/textures/base/base_planks.png")
     * @param hexColor Цвет в формате HEX (например: 0xFF5555)
     */
    fun generateRecoloredTexture(path: String, hexColor: Int): ByteArray {
        // 1. Находим наш серый шаблон прямо внутри jar-файла мода
        val inputStream = TextureGenerator::class.java.getResourceAsStream(path)
            ?: throw IllegalArgumentException("Не найдена текстура: $path")

        val originalImage = ImageIO.read(inputStream)

        // 🏆 ФИКС ПРОЗРАЧНОСТИ: Принудительно создаем холст с альфа-каналом!
        val image = BufferedImage(originalImage.width, originalImage.height, BufferedImage.TYPE_INT_ARGB)
        val g2d = image.createGraphics()
        g2d.drawImage(originalImage, 0, 0, null)
        g2d.dispose()

        val targetColor = Color(hexColor)

        // 2. Пробегаемся по каждому пикселю картинки (ширина х высота)
        for (x in 0 until image.width) {
            for (y in 0 until image.height) {
                val pixelColor = Color(image.getRGB(x, y), true)

                // Если пиксель полностью прозрачный (например, дырка в листве) — просто пропускаем
                if (pixelColor.alpha == 0) continue

                // 3. Математика наложения цвета (Режим Multiply/Умножение)
                val r = (pixelColor.red * targetColor.red) / 255
                val g = (pixelColor.green * targetColor.green) / 255
                val b = (pixelColor.blue * targetColor.blue) / 255

                // Собираем новый пиксель, обязательно сохраняя его оригинальную прозрачность (alpha)
                val newColor = Color(r, g, b, pixelColor.alpha)
                image.setRGB(x, y, newColor.rgb)
            }
        }

        // 4. Упаковываем готовую картинку в массив байтов
        val outputStream = ByteArrayOutputStream()
        ImageIO.write(image, "PNG", outputStream)
        return outputStream.toByteArray()
    }
}