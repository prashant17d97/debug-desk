package datamodel.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import network.apiendpoints.ApiEndpointConstants

@Serializable
data class PostCommentRequest(
    @SerialName(ApiEndpointConstants.ID)
    val _id: String = "",
    val userName: String,
    val userEmail: String,
    val commentDate: String,
    val comment: String,
    val postId: String = "",
    val childComments: List<ChildComment> = emptyList(),
)
