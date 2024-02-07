package presentation.widgets.shared

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import core.log.Logcat

/**
 * Composable function for displaying web content in a WebView.
 * This function loads the specified HTML content into a WebView and displays it.
 * It also enables JavaScript and handles URL loading requests.
 * Note: This function is intended for use in Android.
 *
 * @param modifier The modifier to be applied to the WebView.
 * @param htmlContent The HTML content to be loaded into the WebView.
 *
 * @author Prashant Singh
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
actual fun WebViewContainer(
    modifier: Modifier,
    htmlContent: String,
) {
    // Check if the system is in dark theme
    val isDarkTheme by rememberUpdatedState(newValue = isSystemInDarkTheme())
    // Retrieve the current context
    val context = LocalContext.current
    // Create a mutable state for the WebView
    val webView by remember { mutableStateOf(WebView(context)) }

    // Load the HTML content into the WebView when the dark theme changes
    LaunchedEffect(isDarkTheme) {
        Logcat.d("AndroidWebView", "HtmlContent:---> $htmlContent")
        webView.loadDataWithBaseURL(
            null,
            htmlContent,
            "text/html",
            "UTF-8",
            null,
        )
    }

    // Create an AndroidView to display the WebView
    AndroidView(
        factory = {
            webView.apply {
                // Configure WebView settings
                settings.apply {
                    setBackgroundColor(0x00000000)
                    // Enable JavaScript
                    javaScriptEnabled = true
                }
                // Configure WebViewClient to handle URL loading requests
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?,
                    ): Boolean {
                        // Intercept URL loading requests
                        val url = request?.url.toString()

                        // Customize the handling of different URLs
                        return if (url.startsWith("https://www.debugdesk.in")) {
                            // Open the URL in the same WebView
                            view?.loadUrl(url)
                            true
                        } else {
                            // Open the URL in an external browser
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            ContextCompat.startActivity(context, intent, null)
                            true
                        }
                    }
                }
            }
        },
        update = { webView ->
            // Ensure that the WebView's lifecycle is managed properly
            val lifecycleObserver = WebViewLifecycleObserver(webView)
            (context as? LifecycleOwner)?.lifecycle?.addObserver(lifecycleObserver)
        },
        modifier = Modifier.fillMaxWidth(),
    )
}

/**
 * Lifecycle observer for managing the lifecycle of a WebView.
 */
class WebViewLifecycleObserver(private val webView: WebView) : LifecycleEventObserver {
    override fun onStateChanged(
        source: LifecycleOwner,
        event: Lifecycle.Event,
    ) {
        when (event) {
            Lifecycle.Event.ON_START -> webView.onResume()
            Lifecycle.Event.ON_STOP -> webView.onPause()
            Lifecycle.Event.ON_DESTROY -> {
                webView.removeAllViews()
                webView.destroy()
            }
            else -> Unit
        }
    }
}
