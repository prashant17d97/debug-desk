package presentation.account

import clickevents.AppTheme
import main.MainViewModel
import navigation.Screens
import repo.Repository

class AccountViewModel(private val repository: Repository) : MainViewModel() {

    val menus = listOf("About" to Screens.About)

    val appThemeItems =
        listOf(AppTheme.SYSTEM_DEFAULT, AppTheme.DARK, AppTheme.LIGHT)
}


