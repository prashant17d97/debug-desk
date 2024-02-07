package main

import clickevents.AppTheme
import core.BottomBarTab
import navigation.Screens

/**
 * Represents the data related to the content of the application, including the current screen,
 * selected bottom tab, visibility of UI elements such as back button, top bar, and bottom bar,
 * application theme, and system palette preference.
 *
 * @param screens The current screen being displayed in the application.
 * @param currentSelectedTab The currently selected bottom tab in the application.
 * @param backButtonVisibility Indicates the visibility of the back button in the UI. Default is true.
 * @param topBarVisibility Indicates the visibility of the top bar in the UI. Default is true.
 * @param bottomBarVisibility Indicates the visibility of the bottom bar in the UI. Default is true.
 * @param appTheme The current theme of the application.
 * @param isUsingSystemPalette Indicates whether the application is configured to use the system palette.
 *
 * @constructor Creates an instance of [AppContentData] with the provided parameters.
 *
 * @author Prashant Singh
 */
data class AppContentData(
    val screens: Screens,
    val currentSelectedTab: BottomBarTab,
    val backButtonVisibility: Boolean = true,
    val topBarVisibility: Boolean = true,
    val bottomBarVisibility: Boolean = true,
    val appTheme: AppTheme,
    val isUsingSystemPalette: Boolean
)

