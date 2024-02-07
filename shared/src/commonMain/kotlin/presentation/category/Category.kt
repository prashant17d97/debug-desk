package presentation.category

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import core.NavHostController
import org.koin.compose.koinInject
import presentation.widgets.AppColumn
import presentation.widgets.PostLists

/**
 * Composable function responsible for displaying posts of a specific category.
 * @param navHostController The navigation controller used for navigating between destinations.
 * @author Prashant Singh
 */
@Composable
fun Category(navHostController: NavHostController) {
    // Collecting category ID from navigation arguments
    val categoryId by navHostController.getArguments.collectAsState()

    // Initializing CategoryViewModel using dependency injection
    val categoryViewModel: CategoryViewModel = koinInject()

    // Collecting response state from CategoryViewModel
    val responseState by categoryViewModel.responseState.collectAsState()

    // Collecting category post data from CategoryViewModel
    val categoryPost by categoryViewModel.post.collectAsState()

    // Collecting category data from CategoryViewModel
    val category by categoryViewModel.category.collectAsState()

    // Fetching category post and category data when category ID is not empty
    LaunchedEffect(Unit) {
        if (categoryId.isNotEmpty()) {
            categoryViewModel.getPostOnCategory(categoryId)
            categoryViewModel.getCategory(categoryId)
        }
    }

    // Column layout for displaying category information and posts
    AppColumn(responseState = responseState) {
        // Displaying category name
        Text(
            text = category?.category ?: "",
            style = MaterialTheme.typography.headlineMedium.copy(
                color = MaterialTheme.colorScheme.primary,
            ),
            modifier = Modifier.padding(vertical = 10.dp),
        )

        // Displaying category description
        Text(
            text = category?.description ?: "",
            style = MaterialTheme.typography.labelMedium,
        )

        Spacer(Modifier.padding(top = 20.dp))

        // Displaying list of posts in the category
        PostLists(
            posts = categoryPost,
            onClick = { categoryViewModel.handleClickEvent(it, navHostController) },
        )
    }
}

