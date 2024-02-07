package presentation.about

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import core.NavHostController
import presentation.widgets.AppColumn

@Composable
fun About(navHostController: NavHostController) {

    AppColumn {
        Text("About")
    }
}