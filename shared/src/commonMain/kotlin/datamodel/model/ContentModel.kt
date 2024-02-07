package datamodel.model


import kotlinx.serialization.Serializable

@Serializable
data class ContentModel(val contentModel: List<ContentModelItem>)
