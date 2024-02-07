package repo

import datamodel.model.ChildComment
import datamodel.model.PostComment
import datamodel.model.PostCommentRequest
import kotlinx.coroutines.flow.StateFlow

interface CommentProcessor {
    val comments: StateFlow<List<PostComment>>

    suspend fun addCommentFrontEnd(postComment: PostComment)

    suspend fun addChildComment(
        parentIndex: Int,
        childComment: ChildComment,
        id: String,
    )

    fun updateReplyChatWindow(selectedIndex: Int)

    suspend fun addCommentFrontEnd(postCommentRequest: PostCommentRequest)

    suspend fun getCommentFrontEnd(postId: String)

    suspend fun updateChildCommentFrontEnd(postCommentRequest: PostCommentRequest)
}
