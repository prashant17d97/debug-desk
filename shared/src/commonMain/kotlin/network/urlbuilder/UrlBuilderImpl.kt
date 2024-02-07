package network.urlbuilder

import network.apiendpoints.ApiEndpointConstants.DELIMITER

/**
 * Represents an implementation of the [UrlBuilder] interface for dynamically constructing URLs.
 * Allows adding query parameters, path segments, and building the final URL.
 * @param baseUrl The base URL to which path segments and query parameters will be appended.
 *
 * @author Prashant Singh
 */
class UrlBuilderImpl(private val baseUrl: String) : UrlBuilder {
    private val queryParams = mutableMapOf<String, String>()
    private val pathSegments = mutableListOf<String>()

    /**
     * Adds a single query parameter with its key-value pair to the URL.
     * @param key The key of the query parameter.
     * @param value The value of the query parameter.
     * @return Instance of [UrlBuilderImpl] for method chaining.
     */
    override fun addQueryParam(
        key: String,
        value: String,
    ): UrlBuilderImpl {
        queryParams[key] = value
        return this
    }

    /**
     * Adds multiple query parameters using a map of key-value pairs to the URL.
     * @param keyValueMap The map containing query parameter key-value pairs.
     * @return Instance of [UrlBuilderImpl] for method chaining.
     */
    override fun addQueryParamMap(keyValueMap: Map<String, String>): UrlBuilderImpl {
        keyValueMap.forEach {
            queryParams[it.key] = it.value
        }
        return this
    }

    /**
     * Adds a path segment to the URL.
     * @param path The path segment to be added.
     * @return Instance of [UrlBuilderImpl] for method chaining.
     */
    override fun addPathSegment(path: String): UrlBuilderImpl {
        pathSegments.add(path)
        return this
    }

    /**
     * Constructs the final URL string based on the added query parameters and path segments.
     * @return The final URL string.
     */
    override fun build(): String {
        val urlBuilder = StringBuilder(baseUrl)

        if (pathSegments.isNotEmpty()) {
            urlBuilder.append(DELIMITER)
            urlBuilder.append(pathSegments.joinToString(DELIMITER))
        }

        if (queryParams.isNotEmpty()) {
            urlBuilder.append("?")
            urlBuilder.append(queryParams.entries.joinToString("&") { "${it.key}=${it.value}" })
        }
        clear()
        return urlBuilder.toString()
    }

    /**
     * Clears the stored query parameters and path segments.
     */
    private fun clear() {
        queryParams.clear()
        pathSegments.clear()
    }
}
