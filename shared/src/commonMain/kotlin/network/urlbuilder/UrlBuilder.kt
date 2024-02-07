package network.urlbuilder

/**
 * Represents an interface for building URLs dynamically.
 * Allows adding query parameters, path segments, and building the final URL.
 * @property addQueryParam Function to add a single query parameter with its key-value pair.
 * @property addQueryParamMap Function to add multiple query parameters using a map of key-value pairs.
 * @property addPathSegment Function to add a path segment to the URL.
 * @property build Function to construct the final URL string.
 * @constructor Creates an instance of [UrlBuilder].
 *
 * @author Prashant Singh
 */
interface UrlBuilder {
    /**
     * Adds a single query parameter with its key-value pair to the URL.
     * @param key The key of the query parameter.
     * @param value The value of the query parameter.
     * @return Instance of [UrlBuilder] for method chaining.
     */
    fun addQueryParam(
        key: String,
        value: String,
    ): UrlBuilder

    /**
     * Adds multiple query parameters using a map of key-value pairs to the URL.
     * @param keyValueMap The map containing query parameter key-value pairs.
     * @return Instance of [UrlBuilder] for method chaining.
     */
    fun addQueryParamMap(keyValueMap: Map<String, String>): UrlBuilder

    /**
     * Adds a path segment to the URL.
     * @param path The path segment to be added.
     * @return Instance of [UrlBuilder] for method chaining.
     */
    fun addPathSegment(path: String): UrlBuilder

    /**
     * Constructs the final URL string based on the added query parameters and path segments.
     * @return The final URL string.
     */
    fun build(): String
}

