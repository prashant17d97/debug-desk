package repo

import datamodel.model.ChildComment
import datamodel.model.PostComment
import datamodel.model.PostCommentRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import network.netweorkcall.ApiCalls
import utils.NetworkUtil.handleResponse

class CommentProcessorImpl(private val apiCalls: ApiCalls) : CommentProcessor {
    private var _comments: MutableStateFlow<List<PostComment>> = MutableStateFlow(emptyList())
    override val comments: StateFlow<List<PostComment>> = _comments

    override suspend fun addCommentFrontEnd(postComment: PostComment) {
        _comments.tryEmit(_comments.value + postComment)
        addCommentFrontEnd(
            postCommentRequest =
                PostCommentRequest(
                    _id = "",
                    userName = postComment.userName,
                    userEmail = postComment.userEmail,
                    commentDate = postComment.commentDate,
                    comment = postComment.comment,
                    postId = postComment.postId,
                    childComments = postComment.childComments,
                ),
        )
    }

    override suspend fun addChildComment(
        parentIndex: Int,
        childComment: ChildComment,
        id: String,
    ) {
        _comments.tryEmit(
            _comments.value.mapIndexed { index, topComment ->
                val childComments = topComment.childComments.toMutableList()
                if (topComment._id == id && parentIndex == index) {
                    topComment.copy(
                        childComments = childComments.apply { add(childComment) }.toList(),
                    )
                } else {
                    topComment
                }
            },
        )
        val comment = comments.value[parentIndex]
        updateChildCommentFrontEnd(
            postCommentRequest =
                PostCommentRequest(
                    _id = comment._id,
                    userName = comment.userName,
                    userEmail = comment.userEmail,
                    commentDate = comment.commentDate,
                    comment = comment.userEmail,
                    postId = comment.postId,
                    childComments = comment.childComments,
                ),
        )
    }

    override fun updateReplyChatWindow(selectedIndex: Int) {
        _comments.tryEmit(
            _comments.value.mapIndexed { index, topComment ->
                topComment.copy(
                    isReplyingForThisThread = selectedIndex == index,
                )
            },
        )
    }

    override suspend fun addCommentFrontEnd(postCommentRequest: PostCommentRequest) {
        apiCalls.addComment(postCommentRequest)
    }

    override suspend fun getCommentFrontEnd(postId: String) {
        apiCalls.getComment(postId).handleResponse(
            apiCalls,
            onSuccess = {
                _comments.tryEmit(it.data)
            },
        )
    }

    override suspend fun updateChildCommentFrontEnd(postCommentRequest: PostCommentRequest) {
        apiCalls.updateChildComment(postCommentRequest)
            .handleResponse(apiCalls, onSuccess = {
                getCommentFrontEnd(postCommentRequest.postId)
            })
    }
}
