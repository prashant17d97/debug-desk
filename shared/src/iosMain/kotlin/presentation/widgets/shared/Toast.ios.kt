package presentation.widgets.shared

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import platform.UIKit.NSTextAlignmentCenter
import platform.UIKit.UIApplication
import platform.UIKit.UIFont.Companion.systemFontOfSize
import platform.UIKit.UILabel
import platform.UIKit.UIView
import platform.UIKit.UIViewAnimationOptionCurveEaseOut
import utils.getColor


// Work in Progress
@Composable
actual fun ToastMsg(message: String) {
    val toastLabel =
        UILabel().apply {
            backgroundColor = Color.Black.copy(alpha = 0.6f).getColor()
            textColor = Color.White.getColor()
            textAlignment = NSTextAlignmentCenter
            font = systemFontOfSize(15.0)
            text = message
            alpha = 1.0
            layer.cornerRadius = 10.0
            clipsToBounds = true
            UIApplication.sharedApplication.keyWindow?.addSubview(this)
        }

    UIView.animateWithDuration(
        duration = 2.0,
        delay = 0.1,
        options = UIViewAnimationOptionCurveEaseOut,
        animations = {
            toastLabel.alpha = 0.0
        },
        completion = { _ ->
            toastLabel.removeFromSuperview()
        },
    )
}
