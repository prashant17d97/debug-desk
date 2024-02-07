package presentation.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import core.ResourcePath
import network.ResponseState
import presentation.CommonElements.cornerShape
import presentation.CommonElements.lottieResource
import presentation.widgets.shared.LottieAnimationView

/**
 * Composable function for displaying content in a column layout, with support for handling different API response states,
 * showing loading animations, and handling retry actions.
 *
 * @param modifier The modifier to apply to the column.
 * @param responseState The current state of the API response.
 * @param showResponseLottie Determines whether to show loading animations based on the response state.
 * @param paddingValues The padding values to apply to the column.
 * @param paddingRequire Determines whether to apply padding based on the response state.
 * @param showDefaultLottie Determines whether to show a default animation in certain cases.
 * @param retryText The text to display for retry action.
 * @param defaultToDisplayLottie The default animation to display in certain cases.
 * @param verticalArrangement The vertical arrangement of the content within the column.
 * @param horizontalAlignment The horizontal alignment of the content within the column.
 * @param onRetry The action to perform when retrying after an error.
 * @param content The content to display within the column.
 *
 * @author Prashant Singh
 */

@Composable
fun AppColumn(
    modifier: Modifier = Modifier,
    responseState: ResponseState = ResponseState.Loaded,
    showResponseLottie: Boolean = true,
    paddingValues: PaddingValues = PaddingValues(10.dp),
    paddingRequire: Boolean = true,
    showDefaultLottie: Boolean = false,
    retryText: String = "Tap to refresh",
    defaultToDisplayLottie: String? = null,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    onRetry: () -> Unit = {},
    content: @Composable (ColumnScope.() -> Unit),
) {
    val lottie: String? by rememberUpdatedState(
        when (responseState) {
            ResponseState.Loaded -> null
            ResponseState.Loading -> lottieResource(ResourcePath.Lottie.LOADING)
            ResponseState.NoData -> lottieResource(ResourcePath.Lottie.NO_DATA)
            ResponseState.NotFound -> lottieResource(ResourcePath.Lottie.NOT_FOUND)
            ResponseState.SomeErrorOccurred -> lottieResource(ResourcePath.Lottie.WENT_WRONG)
        },
    )
    val padding by rememberUpdatedState(
        if (paddingRequire) {
            paddingValues
        } else {
            PaddingValues(0.dp)
        },
    )

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        AnimatedVisibility(
            visible = responseState !in setOf(
                ResponseState.NotFound,
                ResponseState.NoData,
                ResponseState.SomeErrorOccurred
            ),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                modifier = modifier.fillMaxSize().padding(padding),
                verticalArrangement = verticalArrangement,
                horizontalAlignment = horizontalAlignment,
                content = content,
            )
        }

        AnimatedVisibility(
            visible = lottie != null && showResponseLottie,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Column(
                modifier =
                Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background.copy(alpha = 0.7f)),
                verticalArrangement =
                Arrangement.spacedBy(
                    space = 10.dp,
                    alignment = Alignment.CenterVertically,
                ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                lottie?.let { lottieJson ->
//                    CircularProgressIndicator()
                    LottieAnimationView(
                        modifier = Modifier.size(200.dp),
                        lottieJsonSrc = lottieJson,
                    )
                    if (responseState != ResponseState.Loading && responseState != ResponseState.NoData && retryText.isNotEmpty()) {
                        Box(
                            modifier =
                            Modifier
                                .height(40.dp)
                                .border(
                                    width = 2.dp,
                                    shape = cornerShape,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                                .padding(10.dp)
                                .clickable {
                                    if (lottieJson != ResourcePath.Lottie.NO_DATA) {
                                        onRetry()
                                    }
                                },
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = retryText,
                                style = MaterialTheme.typography.labelSmall,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            }
        }
        AnimatedVisibility(showDefaultLottie && responseState == ResponseState.Loaded) {
            defaultToDisplayLottie?.let {
                LottieAnimationView(
                    modifier = Modifier.size(200.dp),
                    lottieJsonSrc =
                    lottieResource(
                        it,
                    ),
                )
            }
        }
    }
}
