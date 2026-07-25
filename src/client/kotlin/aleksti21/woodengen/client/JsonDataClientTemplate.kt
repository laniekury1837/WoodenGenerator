package aleksti21.woodengen.client

data class JsonDataClientTemplate(
    val blockstate: String,
    val blockModels: Map<String, String>,
    val itemModel: String,
    val textures: Map<String, ByteArray>,
)
