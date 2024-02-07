package datamodel.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import network.apiendpoints.ApiEndpointConstants.ID

@Serializable
data class PostComment(
    @SerialName(ID)
    val _id: String = "",
    val userName: String,
    val userImage: String = "/iconresources/SuggestionOne.png",
    val userEmail: String,
    val commentDate: String,
    val comment: String,
    val postId: String,
    val isReplyingForThisThread: Boolean = false,
    val childComments: List<ChildComment> = emptyList(),
)

@Serializable
data class ChildComment(
    val userName: String,
    val userImage: String = "",
    val userEmail: String,
    val commentDate: String,
    val comment: String,
)
