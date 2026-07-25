package aleksti21.woodengen.old

val FACINGS = listOf("north" to 0, "east" to 90, "south" to 180, "west" to 270)

// Хелпер для генерации Забора в 4 строчки!
fun ShapeBlueprint.generateFenceParts(sideModel: String) {
    part(null, "_post")
    FACINGS.forEach { (dir, y) ->
        part("$dir=true", sideModel, y = y, uvlock = true)
    }
}

// Хелпер для генерации Двери (скрывает всю математику петель и открывания)
fun ShapeBlueprint.generateDoorVariants() {
    for (half in listOf("lower" to "_bottom", "upper" to "_top")) {
        for (hinge in listOf("left" to "_left", "right" to "_right")) {
            for (open in listOf(false, true)) {
                FACINGS.forEach { (facing, y) ->
                    var y = y
                    if (open) {
                        y = if (hinge.first == "left") (y + 90) else (y - 90)
                        if (y < 0) y += 360 else if (y >= 360) y -= 360
                    }
                    val openSuffix = if (open) "_open" else ""
                    variant("facing=$facing,half=${half.first},hinge=${hinge.first},open=$open", "${half.second}${hinge.second}$openSuffix", y = y)
                }
            }
        }
    }
}

fun ShapeBlueprint.generateStairsVariants() {
//    val directions = mapOf("east" to 0, "south" to 90, "west" to 180, "north" to 270)
    for (half in listOf("bottom", "top")) {
        val x = if (half == "top") 180 else 0
        for ((facing, y) in FACINGS) {
            // Математика ванильных ступенек
            variant("facing=$facing,half=$half,shape=straight", "", x = x, y = y)
            variant("facing=$facing,half=$half,shape=inner_left", "_inner", x = x, y = (y + if (half == "top") 0 else 270) % 360)
            variant("facing=$facing,half=$half,shape=inner_right", "_inner", x = x, y = (y + if (half == "top") 90 else 0) % 360)
            variant("facing=$facing,half=$half,shape=outer_left", "_outer", x = x, y = (y + if (half == "top") 0 else 270) % 360)
            variant("facing=$facing,half=$half,shape=outer_right", "_outer", x = x, y = (y + if (half == "top") 90 else 0) % 360)
        }
    }
}

fun ShapeBlueprint.generateTrapdoorVariants() {
    for (half in listOf("bottom", "top")) {
        for (open in listOf(false, true)) {
            for ((facing, baseY) in FACINGS) {
                var x = 0
                var y = baseY
                var suffix = "_bottom"

                if (open) {
                    suffix = "_open"
                    if (half == "top") { x = 180; y = (y + 180) % 360 }
                } else {
                    if (half == "top") suffix = "_top"
                }
                variant("facing=$facing,half=$half,open=$open", suffix, x = x, y = y)
            }
        }
    }
}