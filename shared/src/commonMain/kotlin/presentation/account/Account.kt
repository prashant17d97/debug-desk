package presentation.account

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import clickevents.AppTheme
import clickevents.ClickEvent
import core.NavHostController
import network.ResponseState
import org.koin.compose.koinInject
import presentation.CommonElements.border
import presentation.widgets.AppColumn
import utils.isAndroid

/**
 * Composable function representing the account screen.
 *
 * @param navHostController The navigation controller for handling navigation events.
 * @author Prashant Singh
 */
@Composable
fun Account(navHostController: NavHostController) {
    // Inject ViewModel using Koin
    val accountViewModel: AccountViewModel = koinInject()

    // State variables
    var isDropDownVisible by remember { mutableStateOf(false) }
    val isUsingSystemPalette by accountViewModel.isUsingSystemPalette.collectAsState()
    val appTheme by accountViewModel.appTheme.collectAsState()

    // UI Composition
    AppColumn(
        responseState = ResponseState.Loaded,
        showResponseLottie = false,
        paddingRequire = false,
        showDefaultLottie = false,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Call to a private Composable function for the account menu
        AccountMenu(
            accountViewModel = accountViewModel,
            navHostController = navHostController,
            isUsingSystemPalette = isUsingSystemPalette
        ) { isDropDownVisible = true }

        // Additional items in the LazyColumn if needed
    }
    // DropdownMenu for selecting app theme
    DropDownMenu(
        dropdownItem = accountViewModel.appThemeItems,
        isExpanded = isDropDownVisible,
        selectedTheme = appTheme,
        onDismiss = { isDropDownVisible = false },
        onItemClickEvent = {
            accountViewModel.handleClickEvent(it, navHostController)
        }
    )
}

/**
 * Private Composable function for rendering the account menu.
 *
 * @param accountViewModel The ViewModel for handling account-related logic.
 * @param navHostController The navigation controller for handling navigation events.
 * @param isUsingSystemPalette Flag indicating whether App is using system palette.
 * @param onAppearanceClick .
 */
@Composable
private fun AccountMenu(
    accountViewModel: AccountViewModel,
    navHostController: NavHostController,
    isUsingSystemPalette: Boolean,
    onAppearanceClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp, alignment = Alignment.Top)
    ) {
        // Menu item for appearance
        item {
            MenuItem(text = "Appearance", onClick = onAppearanceClick)
        }

        // UseSystemDynamic item
        item {
            UseSystemDynamic(isUsingSystemPalette) {
                accountViewModel.handleClickEvent(
                    ClickEvent.UseSystemPalette(it), navHostController
                )
            }
        }

        // Additional menu items from accountViewModel.menus
        items(items = accountViewModel.menus) { (text, screen) ->
            MenuItem(text = text, onClick = {
                accountViewModel.handleClickEvent(
                    ClickEvent.NavigateScreen(screen = screen), navHostController
                )
            })
        }
    }


}


/**
 * Private Composable function representing a menu item.
 *
 * @param text The text to display for the menu item.
 * @param onClick The callback to be invoked when the menu item is clicked.
 */
@Composable
private fun MenuItem(
    text: String, onClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.border
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(
                10.dp
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(text = text)

            Icon(
                Icons.Rounded.KeyboardArrowRight,
                contentDescription = "Arrow",
                modifier = Modifier.size(20.dp)
            )
        }
    }


}

/**
 * Private Composable function representing a dropdown menu.
 *
 * @param dropdownItem The list of items for the dropdown menu.
 * @param isExpanded Flag indicating whether the dropdown menu is expanded.
 * @param selectedTheme The currently selected app theme.
 * @param onDismiss The callback to be invoked when the dropdown menu is dismissed.
 * @param onItemClickEvent The callback to be invoked when an item in the dropdown menu is clicked.
 */
@Composable
private fun DropDownMenu(
    dropdownItem: List<AppTheme>,
    isExpanded: Boolean,
    selectedTheme: AppTheme,
    onDismiss: () -> Unit,
    onItemClickEvent: (ClickEvent) -> Unit
) {
    Box(
        modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp).fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd),
    ) {
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = onDismiss,
        ) {
            dropdownItem.forEach { appTheme ->
                DropdownMenuItem(text = { Text(appTheme.text) },
                    onClick = { onItemClickEvent(ClickEvent.SaveTheme(appTheme = appTheme)) },
                    trailingIcon = {
                        AnimatedVisibility(visible = selectedTheme == appTheme) {
                            Icon(
                                modifier = Modifier.size(16.dp),
                                imageVector = Icons.Rounded.Check,
                                contentDescription = "CurrentTheme",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )
            }

        }

    }
}


/**
 * Private Composable function representing a dynamic switch for system theme palette usage.
 *
 * @param isUseSystemPalette Flag indicating whether the system theme palette is in use.
 * @param onCheckedChange The callback to be invoked when the switch state changes.
 */
@Composable
private fun UseSystemDynamic(
    isUseSystemPalette: Boolean = false,
    onCheckedChange: (Boolean) -> Unit
) {
    AnimatedVisibility(isAndroid()) {
        Box(
            modifier = Modifier.fillMaxWidth().border
                .clickable { onCheckedChange(!isUseSystemPalette) }

        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(
                    horizontal = 10.dp
                ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(text = "Use System Theme Palette")

                Switch(
                    modifier = Modifier.scale(0.7f),
                    checked = isUseSystemPalette,
                    onCheckedChange = onCheckedChange,
                    colors = SwitchDefaults.colors()
                )
            }
        }

    }

}
