package main

import clickevents.ClickEvent
import clickevents.DataStoreEvent
import core.BottomBarTab
import core.BottomBarTab.Account
import core.BottomBarTab.BookMark
import core.BottomBarTab.Home
import core.BottomBarTab.Search
import core.NavHostController
import core.datastore.repo.DataStoreRepository
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import navigation.Screens
import network.netweorkcall.ApiCalls
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import repo.Repository

/**
 * ViewModel class that manages the state and logic related to the main screen of the application.
 * It provides access to the current selected bottom tab, controls the visibility of the back button,
 * and handles navigation actions. This class can be extended by other ViewModels to reuse its
 * properties and functions.
 *
 * This ViewModel interacts with various components such as repositories, data stores, and API calls
 * to fetch and update necessary data. It utilizes StateFlow and MutableStateFlow to manage the state
 * of bottom tab selection and back button visibility.
 *
 * @property repository The repository responsible for fetching data from an external source.
 * @property dataStoreRepository The repository managing application data using data stores.
 * @property apiCalls The API calls instance for managing API interactions.
 * @property responseState StateFlow providing read-only access to the response state data.
 * @property responseMessage StateFlow providing read-only access to the response message data.
 * @property appTheme StateFlow providing read-only access to the current application theme.
 * @property isUsingSystemPalette StateFlow providing read-only access to whether the system palette is in use.
 * @property currentBottomBarTab StateFlow providing read-only access to the current selected bottom tab.
 * @property backButtonVisibility StateFlow providing read-only access to the visibility of the back button.
 * @property bottomBarTabs List of available bottom tabs in the application.
 * @property applyStaticColor StateFlow providing read-only access to whether static color is applied to the status bar.
 *
 * @param _currentBottomBarTab MutableStateFlow representing the current selected bottom tab.
 * @param _backButtonVisibility MutableStateFlow representing the visibility of the back button.
 * @param _applyStaticColor MutableStateFlow representing whether static color is applied to the status bar.
 *
 * @constructor Initializes the ViewModel, setting up necessary observations and dependencies.
 *
 * @author Prashant Singh
 * @see BottomBarTab
 * @see Screens
 * @see KoinComponent
 * @see ViewModel
 */

open class MainViewModel : ViewModel(), KoinComponent {
    private val repository: Repository by inject()
    private val dataStoreRepository: DataStoreRepository by inject()
    private val apiCalls: ApiCalls by inject()
    val responseState = repository.responseState
    val responseMessage = repository.responseMessage
    val appTheme = dataStoreRepository.appTheme

    fun getCoreDetails() {
        viewModelScope.launch {
            dataStoreRepository.getTheme()
            dataStoreRepository.getSystemPalette()
        }
    }

    val isUsingSystemPalette = dataStoreRepository.useSystemPalette

    // MutableStateFlow for the current selected bottom tab
    private val _currentBottomBarTab: MutableStateFlow<BottomBarTab> = MutableStateFlow(Home)

    // StateFlow for providing read-only access to the current selected bottom tab
    val currentBottomBarTab: StateFlow<BottomBarTab> = _currentBottomBarTab

    // MutableStateFlow for the visibility of the back button
    private val _backButtonVisibility: MutableStateFlow<Boolean> = MutableStateFlow(true)

    // StateFlow for providing read-only access to the visibility of the back button
    val backButtonVisibility: StateFlow<Boolean> = _backButtonVisibility

    // List of available bottom tabs in the application
    val bottomBarTabs =
        listOf(Home, Search, BookMark, Account)

    private val _applyStaticColor: MutableStateFlow<Boolean> = MutableStateFlow(true)

    val applyStaticColor: StateFlow<Boolean> = _applyStaticColor

    fun changeStaticStatusBarColor(applyStaticColor: Boolean) {
        _applyStaticColor.tryEmit(applyStaticColor)
    }

    /**
     * Initializes the ViewModel. It sets up the observation of the current bottom tab to
     * automatically update the visibility of the back button.
     */
    init {
        // observedBottomBarTab()
    }

    /**
     * Observes changes in the current bottom tab to update the visibility of the back button.
     */
    private fun observedBottomBarTab() {
        viewModelScope.launch {
            currentBottomBarTab.collect {
                updateBackButtonVisibility(it != Home)
            }
        }
    }

    /**
     * Updates the current selected bottom tab and triggers a navigation action based on the
     * selected tab.
     *
     * @param tab The target bottom tab to navigate to.
     * @param navHostController The navigation controller responsible for handling navigation actions.
     */
    fun navigateTab(
        tab: BottomBarTab,
        navHostController: NavHostController,
    ) {
        val (screen, popInclusive) =
            when (tab) {
                Home -> Screens.Home to true
                Search -> Screens.Search to false
                BookMark -> Screens.SavedPost to false
                Account -> Screens.Account to false
            }
        navHostController.navigate(route = screen, popInclusive = popInclusive)
        update(tab)
    }

    /**
     * Updates the current selected bottom tab and performs a pop-up action in the navigation stack.
     *
     * @param navHostController The navigation controller responsible for handling navigation actions.
     */
    fun updateTabNPopUp(navHostController: NavHostController) {
        navHostController.popUp()
        val tab =
            when (navHostController.currentStack.value) {
                Screens.Account -> Account
                Screens.Home -> Home
                Screens.SavedPost -> BookMark
                Screens.Search -> Search
                else -> null
            }
        tab?.let { update(tab) }
    }

    /**
     * Updates the current selected bottom tab based on the given tab.
     *
     * @param tab The new current selected bottom tab.
     */
    private fun update(tab: BottomBarTab) {
        _currentBottomBarTab.tryEmit(tab)
    }

    /**
     * Updates the visibility of the back button.
     *
     * @param isVisible True if the back button should be visible, false otherwise.
     */
    private fun updateBackButtonVisibility(isVisible: Boolean) {
        _backButtonVisibility.tryEmit(isVisible)
    }

    override fun onCleared() {
        super.onCleared()
        apiCalls.loaded()
    }

    fun clearMessage() {
        apiCalls.clearMessage()
    }

    fun updateResponseState() {
        apiCalls.loaded()
    }

    /**
     * Handles click events triggered within the application. Depending on the type of click event,
     * it performs various actions such as navigation, saving or removing posts, saving themes,
     * or toggling the use of the system palette.
     *
     * This function is asynchronous and launches a coroutine in the viewModelScope to handle the
     * click event asynchronously.
     *
     * @param clickEvent The ClickEvent object representing the type of click action triggered.
     * @param navHostController The navigation controller responsible for handling navigation actions.
     */
    fun handleClickEvent(
        clickEvent: ClickEvent,
        navHostController: NavHostController,
    ) {
        viewModelScope.launch {
            when (clickEvent) {
                is ClickEvent.NavigateScreen ->
                    // If the click event is to navigate to a screen, navigates to the specified screen
                    // with the provided arguments and navigation options.
                    navHostController.navigate(
                        route = clickEvent.screen,
                        argumentString = clickEvent.argument,
                        popInclusive = clickEvent.popInclusive,
                        navigatingForward = clickEvent.navigatingForward,
                    )

                is ClickEvent.SaveOrRemovePost -> {
                    // If the click event involves saving or removing a post,
                    // performs the respective action based on the event type.
                    when (clickEvent.event) {
                        DataStoreEvent.SAVE ->
                            dataStoreRepository.savePost(clickEvent.data)

                        DataStoreEvent.REMOVE ->
                            dataStoreRepository.removePost(clickEvent.data)
                    }
                }

                is ClickEvent.SaveTheme ->
                    // If the click event is to save the theme, updates the application's theme
                    // using the data store repository.
                    dataStoreRepository.saveTheme(clickEvent.appTheme)

                is ClickEvent.UseSystemPalette ->
                    // If the click event is to toggle the use of the system palette,
                    // updates the system palette preference using the data store repository.
                    dataStoreRepository.saveSystemPalette(clickEvent.isUsingSystemPalette)
            }
        }
    }
}
