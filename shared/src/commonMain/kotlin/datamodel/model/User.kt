package datamodel.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import network.apiendpoints.ApiEndpointConstants.ID

@Serializable
data class User(
    @SerialName(ID)
    val _id: Int,
    val name: String,
)
