package presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import core.NavHostController
import core.log.Logcat
import datamodel.model.CategoryModel
import datamodel.model.PostModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import network.ResponseState
import org.koin.compose.koinInject
import presentation.CommonElements.cornerShapeValue
import presentation.widgets.AppColumn
import presentation.widgets.PostLists

/**
 * Composable function responsible for displaying the home screen, including tabs for different categories
 * and corresponding posts.
 *
 * @param navHostController The navigation controller used for navigating between destinations.
 *
 * @author Prashant Singh
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Home(navHostController: NavHostController) {
    // Injecting HomeViewModel using dependency injection
    val viewModel: HomeViewModel = koinInject()

    // Collecting response state from HomeViewModel
    val responseState: ResponseState by viewModel.responseState.collectAsState()

    // Collecting response message from HomeViewModel
    val responseMessage: String by viewModel.responseMessage.collectAsState()

    // Collecting list of posts from HomeViewModel
    val posts: List<PostModel> by viewModel.posts.collectAsState()

    // Collecting list of categories from HomeViewModel
    val categories: List<CategoryModel> by viewModel.categories.collectAsState()

    // Creating a coroutine scope to manage coroutine lifecycles
    val scope: CoroutineScope = rememberCoroutineScope()

    // Remembering the state of the pager
    val pagerState: PagerState = rememberPagerState {
        categories.size
    }

    // Fetching posts for the selected category when the current page or categories list changes
    LaunchedEffect(pagerState.currentPage, categories) {
        if (categories.isNotEmpty()) {
            Logcat.d("Home", "CurrentCategory-->${categories[pagerState.currentPage].category}")
            viewModel.fetchPosts(categories[pagerState.currentPage].category)
        }
    }

    // Column layout for the home screen
    AppColumn(
        modifier = Modifier.fillMaxSize().padding(0.dp),
        responseState = responseState,
        showResponseLottie = categories.isEmpty(),
        paddingRequire = false,
        onRetry = { viewModel.retrieveCategories() },
    ) {
        // Animated visibility for tabs
        AnimatedVisibility(
            visible = categories.isNotEmpty(),
            enter = slideInVertically(),
            exit = slideOutVertically(),
        ) {
            // Scrollable tab row for displaying categories
            ScrollableTabRow(
                modifier = Modifier.clip(
                    RoundedCornerShape(
                        bottomEnd = cornerShapeValue,
                        bottomStart = cornerShapeValue,
                    ),
                ),
                selectedTabIndex = pagerState.currentPage,
                edgePadding = 0.dp,
                containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                contentColor = contentColorFor(MaterialTheme.colorScheme.onPrimaryContainer),
                tabs = {
                    categories.forEachIndexed { index, categoryModel ->
                        CustomTab(
                            selected = pagerState.currentPage == index,
                            selectedColor = MaterialTheme.colorScheme.primaryContainer,
                            unselectedColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                            modifier = Modifier.height(40.dp).width(100.dp),
                        ) {
                            Text(
                                text = categoryModel.category,
                                style = MaterialTheme.typography.labelMedium.copy(color = it),
                            )
                        }
                    }
                },
            )
        }

        // Horizontal pager for switching between categories
        HorizontalPager(
            modifier = Modifier.padding(10.dp),
            state = pagerState,
        ) {
            // Column layout for displaying posts under the selected category
            AppColumn(
                modifier = Modifier.fillMaxSize(),
                responseState = responseState,
                paddingRequire = false,
                verticalArrangement = Arrangement.spacedBy(
                    space = 10.dp,
                    alignment = Alignment.Top,
                ),
                onRetry = {
                    viewModel.fetchPosts("For you")
                },
            ) {
                // Displaying category description and posts
                Text(categories[it].description, style = MaterialTheme.typography.labelLarge)
                PostLists(
                    posts = posts,
                    onClick = {
                        viewModel.handleClickEvent(it, navHostController)
                    },
                )
            }
        }
    }
}

/**
 * Composable function representing a custom tab for category selection.
 *
 * @param selected Indicates whether the tab is selected.
 * @param onClick Callback function for tab click event.
 * @param modifier The modifier for the tab.
 * @param enabled Indicates whether the tab is enabled.
 * @param selectedColor The color of the tab when selected.
 * @param unselectedColor The color of the tab when not selected.
 * @param interactionSource The interaction source for the tab.
 * @param content The content of the tab.
 */
@Composable
fun CustomTab(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selectedColor: Color,
    unselectedColor: Color,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable ColumnScope.(contentColor: Color) -> Unit,
) {
    // Ripple effect for tab click
    val ripple = rememberRipple(bounded = true, color = selectedColor)

    // Animating tab color based on selection
    val tabColor by animateColorAsState(
        targetValue = if (selected) selectedColor else unselectedColor,
        animationSpec = TweenSpec(
            durationMillis = 300,
            easing = FastOutSlowInEasing,
        ),
    )

    // Animating tab content color based on selection
    val tabContentColor by animateColorAsState(
        targetValue = if (!selected) selectedColor else unselectedColor,
        animationSpec = TweenSpec(
            durationMillis = 300,
            easing = FastOutSlowInEasing,
        ),
    )

    // Column layout for the tab
    Column(
        modifier = modifier.background(color = tabColor).selectable(
            selected = selected,
            onClick = onClick,
            enabled = enabled,
            role = Role.Tab,
            interactionSource = interactionSource,
            indication = ripple,
        ).fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        content = {
            // Content of the tab
            content(tabContentColor)
        },
    )
}
