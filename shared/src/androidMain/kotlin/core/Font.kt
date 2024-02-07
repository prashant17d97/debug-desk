package core

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import java.io.IOException


/*
@Composable
actual fun GetFont(
    id: FontResource,
    fontWeight: FontWeight,
    fontStyle: FontStyle
): Font? {
    Font
    return Font(resId = id.fontResourceId, weight = fontWeight, style = fontStyle)
}


actual class String(private val context: Context) {
    actual fun get(id: StringResource, args: List<Any>): kotlin.String {
        return if (args.isEmpty()) {
            StringDesc.Resource(id).toString(context = context)
        } else {
            id.format(*args.toTypedArray()).toString(context = context)
        }
    }
}*/

/*
actual class Drawable(private val context: Context) {
    actual fun getDrawable(id: ImageResource): Drawable? {
        return id.getDrawable(context = context)
    }
}*/

/**
 * Actual implementation to load a font resource for Android.
 * @param res The path to the font resource.
 * @param fontWeight The weight of the font (e.g., FontWeight.Normal, FontWeight.Bold).
 * @param fontStyle The style of the font (e.g., FontStyle.Normal, FontStyle.Italic).
 * @return The Font object representing the loaded font.
 *
 * @author Prashant Singh
 */
@SuppressLint("DiscouragedApi")
@Composable
actual fun font(res: String, fontWeight: FontWeight, fontStyle: FontStyle): Font {
    val context = LocalContext.current
    // Retrieve the resource identifier for the font
    val fontResourceId: Int = context.resources.getIdentifier(fontNameFromPath(res), "font", context.packageName)
    return Font(resId = fontResourceId, weight = fontWeight, style = fontStyle)
}

/**
 * Extracts the font name from the provided path.
 * @param path The path to the font resource.
 * @return The name of the font file.
 */
private fun fontNameFromPath(path: String): String {
    val parts = path.split("/")
    val fileName = parts.last()
    return fileName.substringBeforeLast(".")
}

/**
 * Interface representing a resource that can be read.
 */
interface Resource {
    suspend fun readBytes(): ByteArray
}

/**
 * Abstract implementation of the Resource interface for Android.
 * @param path The path to the resource.
 */
internal abstract class AbstractResourceImpl(val path: String) : Resource {
    /**
     * Checks if this resource is equal to another object.
     * @param other The other object to compare.
     * @return True if the resources have the same path, false otherwise.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return if (other is AbstractResourceImpl) {
            path == other.path
        } else {
            false
        }
    }

    /**
     * Computes the hash code of this resource.
     * @return The hash code value.
     */
    override fun hashCode(): Int {
        return path.hashCode()
    }
}

/**
 * Android-specific implementation of the Resource interface.
 * @param path The path to the resource.
 */
private class AndroidResourceImpl(path: String) : AbstractResourceImpl(path) {
    /**
     * Reads the bytes of the resource.
     * @return The byte array representing the resource content.
     * @throws MissingResourceException if the resource is not found.
     */
    override suspend fun readBytes(): ByteArray {
        val classLoader = Thread.currentThread().contextClassLoader
            ?: (::AndroidResourceImpl.javaClass.classLoader)
        val resource = classLoader.getResourceAsStream(path)
        if (resource != null) {
            return resource.readBytes()
        } else {
            throw MissingResourceException(path)
        }
    }
}

/**
 * Exception indicating that a resource is missing.
 * @param path The path to the missing resource.
 */
internal class MissingResourceException(path: String) :
    IOException("Missing resource with path: $path")

/**
 * Factory function to create a resource for the provided path.
 * @param path The path to the resource.
 * @return The resource object.
 */
fun resource(path: String): Resource = AndroidResourceImpl(path)
