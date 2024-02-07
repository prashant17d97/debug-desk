package presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Brush.Companion.horizontalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import core.Resources
import core.Size
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.resource

/**
 * CommonElements object contains utility functions and values commonly used across the application.
 */
object CommonElements {

    /**
     * Composable function for a custom button with customizable text, appearance, and click behavior.
     *
     * @param text The text displayed on the button.
     * @param fillMaxWith Whether the button should fill the maximum width available.
     * @param gradient The gradient brush for the button background.
     * @param onClick The callback invoked when the button is clicked.
     */
    @Composable
    fun CustomButton(
        text: String,
        fillMaxWith: Boolean = false,
        gradient: Brush =
            horizontalGradient(
                colors =
                listOf(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                    MaterialTheme.colorScheme.primary,
                ),
            ),
        onClick: () -> Unit,
    ) {
        Button(
            colors =
            ButtonDefaults.buttonColors(
                Color.Transparent,
            ),
            shape = cornerShape,
            contentPadding = PaddingValues(),
            onClick = onClick,
        ) {
            Box(
                modifier =
                Modifier
                    .background(gradient)
                    .then(
                        if (fillMaxWith) {
                            Modifier
                                .fillMaxWidth(0.9f)
                                .height(height = Size.Button.height)
                        } else {
                            Modifier.size(width = Size.Button.width, height = Size.Button.height)
                        },
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }

    // Common values

    val cornerShapeValue = 15.dp
    val elevationValue = 10.dp

    val cornerShape = RoundedCornerShape(cornerShapeValue)

    /**
     * Modifier extension property for applying shadow elevation to a composable.
     */
    val Modifier.shadowElevation: Modifier
        get() = this.shadow(elevation = elevationValue, ambientColor = ambientColor)

    /**
     * Modifier extension property for applying global corner clipping to a composable.
     */
    val Modifier.globalClip: Modifier
        get() = this.clip(shape = cornerShape)

    /**
     * Modifier extension property for applying border to a composable.
     */
    val Modifier.border: Modifier
        @Composable
        get() = this.border(
            width = 2.dp,
            color = MaterialTheme.colorScheme.primary,
            shape = cornerShape
        )

    // Ambient color for shadow
    private val ambientColor = Color(0x125A6CEA)

    /**
     * Function to retrieve Lottie JSON string from resources or URL.
     *
     * @param path The path to the Lottie JSON resource or URL.
     * @return The Lottie JSON string.
     */
    @OptIn(ExperimentalResourceApi::class)
    @Composable
    fun lottieResource(path: String): LottieJson {
        var lottie: String by remember { mutableStateOf("") }
        LaunchedEffect(Unit) {
            lottie =
                if (path.contains("http")) path else Resources.getJsonResource(resource(path).readBytes())
        }
        return lottie
    }
}

/**
 * Type alias for Lottie JSON string.
 */
typealias LottieJson = String

