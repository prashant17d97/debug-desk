package presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import core.NavHostController
import core.ResourcePath.Drawable.logo
import kotlinx.coroutines.delay
import main.MainViewModel
import navigation.Screens
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject

/**
 * Composable function for the splash screen.
 *
 * @param navHostController The navigation host controller to navigate to the home screen.
 *
 * @author Prashant Singh
 */
@OptIn(ExperimentalResourceApi::class)
@Composable
fun Splash(navHostController: NavHostController) {
    val mainViewModel: MainViewModel = koinInject()

    // Launch effect to delay for a certain time, change status bar color, get core details,
    // and navigate to the home screen
    LaunchedEffect(Unit) {
        delay(1000)
        mainViewModel.changeStaticStatusBarColor(applyStaticColor = false)
        mainViewModel.getCoreDetails()
        delay(200)
        navHostController.navigate(
            route = Screens.Home,
            popInclusive = true,
        )
    }

    // Surface with a black background containing the app logo centered on the screen
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                modifier = Modifier.size(260.dp),
                painter = painterResource(logo),
                contentDescription = "Splash Logo",
            )
        }
    }
}

