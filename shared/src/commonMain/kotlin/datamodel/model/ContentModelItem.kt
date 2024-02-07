package datamodel.model


import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContentModelItem(
    @SerialName("children")
    val children: List<@Contextual Any>,
    @SerialName("tag")
    val tag: String
)

