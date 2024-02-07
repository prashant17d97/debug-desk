import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import core.BackPressHandler
import core.BottomBarTab
import core.CloseApplication
import core.NavHostController
import core.Size
import core.log.Logcat
import core.rememberNavHostController
import main.AppContentData
import main.MainViewModel
import navigation.NavGraph
import navigation.Screens
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import presentation.CommonElements.globalClip
import theme.AppTheme
import utils.CommonFunctions.isSystemInDarkMode

/**
 * Top-level composable representing the main entry point of the application.
 * It sets up the navigation controller, observes necessary state variables, and delegates the UI
 * composition to [AppContent]. It also handles the case where the application can exit.
 *
 * @see AppContent
 * @author Prashant Singh
 */
@Composable
fun App() {
    // Set up navigation controller with the starting destination
    val navHostController = rememberNavHostController(startDestination = Screens.Splash)

    // Observe whether the application can exit
    val canExit by navHostController.canExit.collectAsState()

    // Inject the MainViewModel using Koin
    val mainViewModel: MainViewModel = koinInject()

    // Handle back button presses
    BackPressHandler(enabled = true) {
        mainViewModel.updateTabNPopUp(navHostController)
        mainViewModel.updateResponseState()
        navHostController.getArguments
    }

    // Observe various state variables using collectAsState
    val currentSelectedTab: BottomBarTab by mainViewModel.currentBottomBarTab.collectAsState()
    val currentScreen: Screens by navHostController.currentStack.collectAsState()
    val backButtonVisibility: Boolean by mainViewModel.backButtonVisibility.collectAsState()
    val topBarVisibility by rememberUpdatedState(currentScreen != Screens.Splash)
    val appTheme by mainViewModel.appTheme.collectAsState()
    val isUsingSystemPalette by mainViewModel.isUsingSystemPalette.collectAsState()

    // Compose the UI using AppContent composable
    AppContent(
        appContentData =
        AppContentData(
            screens = currentScreen,
            currentSelectedTab = currentSelectedTab,
            backButtonVisibility = backButtonVisibility,
            topBarVisibility = topBarVisibility,
            bottomBarVisibility = currentScreen.bottomBarVisibility,
            appTheme = appTheme,
            isUsingSystemPalette = isUsingSystemPalette
        ),
        navHostController = navHostController,
        mainViewModel = mainViewModel,
    )

    // If the application can exit, display the close application UI
    if (canExit) {
        CloseApplication()
    }
}

/**
 * Composable function responsible for rendering the main content of the application.
 * It sets up the application theme, the Scaffold structure, and delegates UI composition to
 * [TopAppBar] and [BottomTabNavigationBar].
 *
 * @param appContentData Data class containing various UI-related state variables.
 * @param navHostController Navigation controller for handling navigation within the app.
 * @param mainViewModel ViewModel containing business logic for the main content.
 *
 * @see TopAppBar
 * @see BottomTabNavigationBar
 * @author Prashant Singh
 */
@Composable
private fun AppContent(
    appContentData: AppContentData,
    navHostController: NavHostController,
    mainViewModel: MainViewModel,
) {
    val applyStaticColor by mainViewModel.applyStaticColor.collectAsState()
    Logcat.d(
        "AppContent",
        "IsSplash:-->Palette:${appContentData.isUsingSystemPalette} ${appContentData.appTheme},  ${appContentData.appTheme.isSystemInDarkMode()}"
    )
    // Set up the application theme
    AppTheme(
        darkTheme = appContentData.appTheme.isSystemInDarkMode(),
        dynamicColor = appContentData.isUsingSystemPalette,
        applyStaticColor = applyStaticColor,
    ) {
        // Scaffold structure for the main content
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                // Compose the top app bar
                TopAppBar(
                    appContentData = appContentData,
                    navHostController = navHostController,
                    mainViewModel = mainViewModel,
                )
            },
            bottomBar = {
                // Compose the bottom navigation bar
                BottomTabNavigationBar(
                    appContentData = appContentData,
                    navHostController = navHostController,
                    mainViewModel = mainViewModel,
                )
            },
        ) {
            // Column for the main content, including the navigation graph
            Box(
                modifier = Modifier.fillMaxSize().padding(it),
                contentAlignment = Alignment.Center,
            ) {
                // Compose the navigation graph
                NavGraph(navHostController)
            }
        }
    }
}

/**
 * Composable function responsible for rendering the top app bar.
 * It includes a dynamic visibility based on [appContentData.topBarVisibility] and handles the
 * display of the back button and app title.
 *
 * @param appContentData Data class containing various UI-related state variables.
 * @param navHostController Navigation controller for handling navigation within the app.
 * @param mainViewModel ViewModel containing business logic for the top app bar.
 * @author Prashant Singh
 *
 */
@OptIn(ExperimentalResourceApi::class)
@Composable
private fun TopAppBar(
    appContentData: AppContentData,
    navHostController: NavHostController,
    mainViewModel: MainViewModel,
) {
    // AnimatedVisibility composable for dynamic visibility
    AnimatedVisibility(appContentData.topBarVisibility) {
        // Row composable representing the top app bar
        Row(
            modifier =
            Modifier.fillMaxWidth().height(54.dp)
                .background(MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            // AnimatedVisibility for the back button
            AnimatedVisibility(
                visible = appContentData.backButtonVisibility,
                enter =
                slideInHorizontally(
                    initialOffsetX = {
                        -it * 2
                    },
                ),
                exit =
                slideOutHorizontally(
                    targetOffsetX = {
                        -it * 2
                    },
                ),
            ) {
                // Box composable for the back button
                Box(
                    modifier =
                    Modifier.size(50.dp).padding(
                        5.dp,
                    ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        modifier =
                        Modifier.size(Size.Button.backButton).globalClip
                            .background(MaterialTheme.colorScheme.onPrimaryContainer).clickable {
                                // Handle back button click
                                mainViewModel.updateTabNPopUp(navHostController)
                            },
                        imageVector = Icons.Rounded.KeyboardArrowLeft,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primaryContainer,
                    )
                }
            }
            // AnimatedVisibility for the app title
            AnimatedVisibility(
                visible = appContentData.topBarVisibility,
                enter =
                slideInHorizontally(
                    initialOffsetX = {
                        -it * 2
                    },
                ),
                exit =
                slideOutHorizontally(
                    targetOffsetX = {
                        -it * 2
                    },
                ),
            ) {
                // Text composable for the app title
                Text(
                    text = "Debug Desk",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(5.dp),
                )
            }
        }
    }
}

/**
 * Composable function responsible for rendering the bottom navigation bar.
 * It includes dynamic visibility based on [appContentData.topBarVisibility] and composes the
 * [BottomNavigation] composable along with [BottomNavigationItem] for each tab.
 *
 * @param appContentData Data class containing various UI-related state variables.
 * @param navHostController Navigation controller for handling navigation within the app.
 * @param mainViewModel ViewModel containing business logic for the bottom navigation bar.
 * @author Prashant Singh
 *
 */
@OptIn(ExperimentalResourceApi::class)
@Composable
private fun BottomTabNavigationBar(
    appContentData: AppContentData,
    navHostController: NavHostController,
    mainViewModel: MainViewModel,
) {
    // AnimatedVisibility composable for dynamic visibility
    AnimatedVisibility(
        visible = appContentData.bottomBarVisibility,
        enter =
        slideInVertically(
            initialOffsetY = {
                it / 2
            },
        ),
        exit =
        slideOutVertically(
            targetOffsetY = {
                it / 2
            },
        ),
    ) {
        // BottomNavigation composable for the bottom navigation bar
        BottomNavigation {
            // Iterate through each tab and compose BottomNavigationItem for it
            mainViewModel.bottomBarTabs.forEach { tab ->
                BottomNavigationItem(
                    modifier = Modifier.weight(1f),
                    selected = appContentData.currentSelectedTab == tab,
                    onClick = {
                        // Handle tab click
                        mainViewModel.navigateTab(tab, navHostController)
                    },
                ) { size, tabContentColor ->
                    // Icon composable for each tab
                    Icon(
                        painter = painterResource(tab.text),
                        contentDescription = tab.name,
                        modifier = Modifier.size(size),
                        tint = tabContentColor,
                    )
                }
            }
        }
    }
}

/**
 * Composable function representing a generic Bottom Navigation.
 * It includes a [Surface] with a [Row] layout for the icons.
 *
 * @param modifier Modifier for customization.
 * @param backgroundColor Color of the background.
 * @param contentColor Color of the content.
 * @param elevation Elevation of the surface.
 * @param content RowScope lambda for content composition.
 */
@Composable
private fun BottomNavigation(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = contentColorFor(backgroundColor),
    elevation: Dp = 8.dp,
    content: @Composable RowScope.() -> Unit,
) {
    // Surface composable for the bottom navigation
    Surface(
        color = backgroundColor,
        contentColor = contentColor,
        shadowElevation = elevation,
        tonalElevation = elevation,
        modifier = modifier,
    ) {
        // Row composable for the icons
        Row(
            Modifier.fillMaxWidth().height(56.dp).selectableGroup(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            content = content,
        )
    }
}

/**
 * Composable function representing a Bottom Navigation Item.
 * It includes dynamic animations for icon size and color based on selection state.
 *
 * @param modifier Modifier for customization.
 * @param iconSize Size of the icon.
 * @param activeColor Color when the item is selected.
 * @param inactiveColor Color when the item is not selected.
 * @param selected Whether the item is currently selected.
 * @param enabled Whether the item is enabled.
 * @param interactionSource MutableInteractionSource for interaction handling.
 * @param onClick Lambda for click handling.
 * @param content Lambda for content composition with icon size and color parameters.
 */
@Composable
private fun BottomNavigationItem(
    modifier: Modifier = Modifier,
    iconSize: Int = 24,
    activeColor: Color = MaterialTheme.colorScheme.primaryContainer,
    inactiveColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    selected: Boolean,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit,
    content: @Composable (Dp, Color) -> Unit,
) {
    // Animated state for dynamic icon size
    val contentIconSize by animateDpAsState(
        targetValue =
        if (selected) {
            (iconSize * 1.35).dp
        } else {
            iconSize.dp
        },
        animationSpec =
        TweenSpec(
            durationMillis = 300,
            easing = FastOutSlowInEasing,
        ),
    )

    // Animated state for dynamic tab color
    val tabColor by animateColorAsState(
        targetValue =
        if (selected) {
            activeColor
        } else {
            inactiveColor
        },
        animationSpec =
        TweenSpec(
            durationMillis = 300,
            easing = FastOutSlowInEasing,
        ),
    )

    // Animated state for dynamic tab content color
    val tabContentColor by animateColorAsState(
        targetValue =
        if (selected) {
            inactiveColor
        } else {
            activeColor
        },
        animationSpec =
        TweenSpec(
            durationMillis = 300,
            easing = FastOutSlowInEasing,
        ),
    )

    // Box composable for the bottom navigation item
    Box(
        modifier =
        modifier.height(56.dp).selectable(
            selected = selected,
            onClick = onClick,
            enabled = enabled,
            role = Role.Tab,
            interactionSource = interactionSource,
            indication = rememberRipple(bounded = false, color = LocalContentColor.current),
        ).background(tabColor),
        contentAlignment = Alignment.Center,
    ) {
        // Compose the content with dynamic icon size and color
        content(contentIconSize, tabContentColor)
    }
}

private val Screens.bottomBarVisibility: Boolean
    get() =
        when (this) {
            Screens.Account, Screens.Home, Screens.SavedPost, Screens.Search -> true
            else -> false
        }


