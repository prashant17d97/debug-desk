import androidx.compose.ui.window.ComposeUIViewController

/**
 * Creates a ComposeUIViewController for the main view controller of the application.
 * This function initializes the Compose UI by setting up the root Composable function `App()`.
 * @return A ComposeUIViewController displaying the main UI of the application.
 */
fun MainViewController() = ComposeUIViewController { App() }

