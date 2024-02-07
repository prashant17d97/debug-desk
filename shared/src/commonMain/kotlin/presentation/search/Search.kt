package presentation.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import core.NavHostController
import core.ResourcePath
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import presentation.CommonElements.cornerShape
import presentation.widgets.AppColumn
import presentation.widgets.PostLists
import utils.CommonFunctions.isDataEmpty

/**
 * Composable function representing the search screen.
 *
 * This screen allows users to search for posts based on the entered query. It displays a search bar
 * where users can input their search query and see the results below.
 *
 * @param navHostController The navigation controller for navigating between composables.
 */
@OptIn(ExperimentalResourceApi::class, ExperimentalComposeUiApi::class)
@Composable
fun Search(navHostController: NavHostController) {
    // Injecting SearchViewModel using Koin
    val searchViewModel: SearchViewModel = koinInject()

    // State for holding the search string entered by the user
    var searchString: String by remember { mutableStateOf("") }

    // Collecting state from SearchViewModel
    val postOnSearchList by searchViewModel.postsOnSearch.collectAsState()
    val responseMessage by searchViewModel.responseMessage.collectAsState()
    val responseState by searchViewModel.responseState.collectAsState()

    // Effect for updating response state
    LaunchedEffect(Unit) {
        searchViewModel.updateResponseState()
    }

    // Accessing keyboard controller and focus manager
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    // Composable structure
    AppColumn {
        // Search TextField
        OutlinedTextField(
            value = searchString,
            onValueChange = { searchString = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("Search.....", style = MaterialTheme.typography.bodySmall)
            },
            leadingIcon = {
                // Search Icon
                Icon(
                    painter = painterResource(ResourcePath.Drawable.SearchIcon),
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        searchString.trim().isDataEmpty()?.let {
                            searchViewModel.postOnSearch(it)
                        }
                    },
                )
            },
            trailingIcon = {
                // Close Icon
                AnimatedVisibility(
                    searchString.isNotBlank(),
                    enter = (slideInVertically { height -> -height } + fadeIn()),
                    exit = slideOutHorizontally { height -> height } + fadeOut(),
                ) {
                    Icon(
                        painter = painterResource(ResourcePath.Drawable.Close),
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable {
                            searchString = ""
                        },
                    )
                }
            },
            keyboardActions = KeyboardActions(
                onSearch = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    searchString.trim().isDataEmpty()?.let {
                        searchViewModel.postOnSearch(it)
                        searchString = ""
                    }
                },
            ),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                autoCorrect = true,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search,
            ),
            shape = cornerShape,
        )

        // Displaying search results
        AppColumn(
            responseState = responseState,
            paddingRequire = false,
            showDefaultLottie = postOnSearchList.isEmpty(),
            retryText = "",
            defaultToDisplayLottie = ResourcePath.Lottie.SEARCH,
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            // List of search results
            PostLists(
                posts = postOnSearchList,
                onClick = {
                    searchViewModel.handleClickEvent(it, navHostController)
                },
            )
        }
    }
}
