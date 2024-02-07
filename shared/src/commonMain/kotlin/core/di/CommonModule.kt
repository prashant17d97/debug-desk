package core.di

import core.datastore.DataStore
import core.datastore.repo.DataStoreRepository
import core.datastore.repo.DataStoreRepositoryImpl
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.URLBuilder
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import main.MainViewModel
import network.netweorkcall.ApiCallImpl
import network.netweorkcall.ApiCalls
import network.urlbuilder.UrlBuilder
import network.urlbuilder.UrlBuilderImpl
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import presentation.account.AccountViewModel
import presentation.author.AuthorViewModel
import presentation.category.CategoryViewModel
import presentation.home.HomeViewModel
import presentation.post.PostScreenVIewModel
import presentation.posts.SavedPostViewModel
import presentation.search.SearchViewModel
import repo.CommentProcessor
import repo.CommentProcessorImpl
import repo.Repository
import repo.RepositoryImpl

/**
 * Initializes Koin dependency injection framework with the provided additional modules.
 * It starts Koin with the given modules, including commonModule and platformModule by default.
 *
 * @param additionalModules Optional list of additional Koin modules to be included.
 *
 * @see Module
 * @see startKoin
 *
 * @author Prashant Singh
 */
fun initiateKoin(additionalModules: List<Module>? = null) {
    startKoin {
        modules(additionalModules.orEmpty() + listOf(commonModule, platformModule))
    }
}

/**
 * Koin module containing common dependencies used across platforms.
 * It provides single instances of various components such as HttpClient, DataStore, Repository,
 * ViewModels, etc., required for application functionality.
 *
 * @see Module
 * @see single
 *
 * @property commonModule The Koin module containing common dependencies.
 *
 * @constructor Creates an instance of [commonModule].
 *
 * @author Prashant Singh
 */
private val commonModule =
    module {
        single(named("BASE_URL")) { "https://www.debugdesk.in/api" }
        single {
            HttpClient {
                defaultRequest {
                    url.takeFrom(URLBuilder().takeFrom(get<String>(named("BASE_URL"))))
                }
                install(HttpTimeout) {
                    requestTimeoutMillis = 15_000
                }
                install(ContentNegotiation) {
                    json(
                        Json {
                            ignoreUnknownKeys = true
                            prettyPrint = true
                        },
                    )
                }
                install(Logging) {
                    level = LogLevel.ALL
                    logger =
                        object : Logger {
                            override fun log(message: String) {
                                println(message)
                            }
                        }
                }
            }
        }

        single<UrlBuilder> {
            UrlBuilderImpl(
                baseUrl = get<String>(named("BASE_URL")),
            )
        }

        single<DataStore> {
            DataStore(dataStore = get())
        }

        single<DataStoreRepository> {
            DataStoreRepositoryImpl(dataStore = get())
        }

        single<ApiCalls> {
            ApiCallImpl(
                urlBuilder = get(),
                ktorHttpClient = get(),
            )
        }

        single<Repository> {
            RepositoryImpl(
                apiCalls = get(),
                dataStoreRepository = get(),
            )
        }
        single<CommentProcessor> {
            CommentProcessorImpl(apiCalls = get())
        }

        single<HomeViewModel> {
            HomeViewModel(
                repository = get(),
                dataStoreRepository = get(),
            )
        }
        single<MainViewModel> {
            MainViewModel()
        }

        single<AccountViewModel> {
            AccountViewModel(repository = get())
        }
        single<SearchViewModel> {
            SearchViewModel(
                repository = get(),
                dataStoreRepository = get(),
            )
        }
        single<SavedPostViewModel> {
            SavedPostViewModel(
                repository = get(),
                dataStoreRepository = get(),
            )
        }
        single<PostScreenVIewModel> {
            PostScreenVIewModel(
                repository = get(),
                dataStoreRepository = get(),
                commentProcessor = get(),
            )
        }

        single<CategoryViewModel> {
            CategoryViewModel(
                repository = get(),
                dataStoreRepository = get(),
            )
        }
        single<AuthorViewModel> {
            AuthorViewModel(
                repository = get(),
            )
        }
    }

/**
 * Declares an expect property platformModule to be implemented in platform-specific code.
 * This module provides platform-specific dependencies required for the application.
 *
 * @see Module
 * @see expect
 *
 * @property platformModule The platform-specific Koin module.
 *
 * @constructor Creates an instance of [platformModule].
 *
 * @author Prashant Singh
 */
expect val platformModule: Module
