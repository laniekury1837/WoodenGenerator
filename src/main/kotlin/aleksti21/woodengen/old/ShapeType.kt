package aleksti21.woodengen.old

enum class ShapeType(val bp: ShapeBlueprint) {

    CUBE(blueprint {
        model("", "cube_all", "all")
        variant("", "")
    }),

    LEAVES(blueprint {
        model("", "leaves", "all", renderType = "minecraft:cutout_mipped")
        variant("", "")
    }),

    PILLAR(blueprint {
        model("", "cube_column", "side", "end")
        model("_horizontal", "cube_column_horizontal", "side", "end")

        // Явно прописываем повороты. Никакой скрытой магии!
        variant("axis=y", "")
        variant("axis=x", "_horizontal", x = 90, y = 90)
        variant("axis=z", "_horizontal", x = 90)
    }),

    SLAB(blueprint {
        model("", "slab", "bottom", "top", "side")
        model("_top", "slab_top", "bottom", "top", "side")
        model("_double", "cube_all", "all")

        variant("type=bottom", "")
        variant("type=top", "_top")
        variant("type=double", "_double")
    }),
    PRESSURE_PLATE(blueprint {
        model("", "pressure_plate_up", "texture")
        model("_down", "pressure_plate_down", "texture")

        variant("powered=false", "")
        variant("powered=true", "_down")
    }),
    FENCE(blueprint {
        model("_post", "fence_post", "texture")
        model("_side", "fence_side", "texture")
        model("_inventory", "fence_inventory", "texture")

        itemModel("_inventory") // НОРМАЛЬНАЯ В ИНВЕНТАРЕ!
        tag("minecraft:fences") // ТЕПЕРЬ ОНИ БУДУТ СОЕДИНЯТЬСЯ!
        tag("minecraft:wooden_fences")

        generateFenceParts("_side")
    }),

    DOOR(blueprint {
        // Закрытые модели
        model("_bottom_left", "door_bottom_left", "bottom", "top", renderType = "minecraft:cutout")
        model("_bottom_right", "door_bottom_right", "bottom", "top", renderType = "minecraft:cutout")
        model("_top_left", "door_top_left", "bottom", "top", renderType = "minecraft:cutout")
        model("_top_right", "door_top_right", "bottom", "top", renderType = "minecraft:cutout")

        // Открытые модели!
        model("_bottom_left_open", "door_bottom_left_open", "bottom", "top", renderType = "minecraft:cutout")
        model("_bottom_right_open", "door_bottom_right_open", "bottom", "top", renderType = "minecraft:cutout")
        model("_top_left_open", "door_top_left_open", "bottom", "top", renderType = "minecraft:cutout")
        model("_top_right_open", "door_top_right_open", "bottom", "top", renderType = "minecraft:cutout")

        generatedItem(2) // 2 — это индекс ТРЕТЬЕЙ картинки (base_door_item.png)
        generateDoorVariants()
    }),

    BUTTON(blueprint {
        model("", "button", "texture")
        model("_pressed", "button_pressed", "texture")
        model("_inventory", "button_inventory", "texture")

        itemModel("_inventory")

        for (powered in listOf(false, true)) {
            val suffix = if (powered) "_pressed" else ""
            for ((facing, y) in FACINGS) {
                variant("face=floor,facing=$facing,powered=$powered", suffix, x = 0, y = y)
                variant("face=wall,facing=$facing,powered=$powered", suffix, x = 90, y = y)
                variant("face=ceiling,facing=$facing,powered=$powered", suffix, x = 180, y = y)
            }
        }
    }),
    STAIRS(blueprint {
        model("", "stairs", "bottom", "top", "side")
        model("_inner", "inner_stairs", "bottom", "top", "side")
        model("_outer", "outer_stairs", "bottom", "top", "side")
        generateStairsVariants()
    }),

    TRAPDOOR(blueprint {
        model("_bottom", "trapdoor_bottom", "texture", renderType = "minecraft:cutout")
        model("_top", "trapdoor_top", "texture", renderType = "minecraft:cutout")
        model("_open", "trapdoor_open", "texture", renderType = "minecraft:cutout")
        generateTrapdoorVariants()
    }),
}