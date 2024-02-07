package presentation.widgets.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.interop.UIKitView
import androidx.compose.ui.unit.dp
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIScreen
import platform.UIKit.UIViewController
import platform.WebKit.WKNavigationDelegateProtocol
import platform.WebKit.WKWebView
import platform.WebKit.javaScriptEnabled
import utils.getColor

/**
 * WebViewContainer composable function for iOS platform.
 * This function loads and displays web-based content (HTML) within a WKWebView.
 * @param modifier The modifier for the WebView container.
 * @param htmlContent The HTML content to be displayed in the WebView.
 * @param background The background color of the WebView container.
 * @param webView The WKWebView used to display the HTML content.
 *
 * @author Prashant Singh
 */
@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun WebViewContainer(
    modifier: Modifier,
    htmlContent: String,
) {
    // Retrieve the color scheme from MaterialTheme
    val colorScheme: ColorScheme = MaterialTheme.colorScheme

    // Retrieve the background color
    val background: Color by rememberUpdatedState(colorScheme.background)

    // Create a WKWebView to display the HTML content
    val webView: WKWebView by remember {
        mutableStateOf(
            WKWebView(frame = UIScreen.mainScreen.bounds).also {
                it.opaque = false
                it.backgroundColor = background.getColor()
                it.configuration.preferences.javaScriptEnabled = true
            },
        )
    }

    // Create a UIKitView to host the WKWebView
    UIKitView(
        modifier = modifier
            .height(800.dp)
            .background(Color.Transparent), // Ensure WebView is transparent
        background = Color.Transparent,
        factory = {
            // Initialize WebViewContentController with the WKWebView
            WebViewContentController(
                wkWebView = webView,
            ).view
        },
        update = {
            // Load the HTML content into the WebView
            webView.loadHTMLString(htmlContent, null)
        },
        interactive = true,
    )
}

/**
 * WebViewContentController class for iOS platform.
 * This class controls the behavior of a WKWebView used to display HTML content.
 * @param wkWebView The WKWebView instance to be controlled.
 */
class WebViewContentController(private val wkWebView: WKWebView) :
    UIViewController(nibName = null, bundle = null), WKNavigationDelegateProtocol {

    override fun loadView() {
        // Set the navigation delegate and disable bouncing for the WKWebView
        wkWebView.navigationDelegate = this
        wkWebView.scrollView.bounces = false
        view = wkWebView
    }
}
