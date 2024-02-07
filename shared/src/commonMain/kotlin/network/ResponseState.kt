package network

/**
 * Represents the state of an API call response.
 * Possible states include:
 * - [Loaded]: Indicates that the API call has successfully loaded data.
 * - [Loading]: Indicates that the API call is currently in progress.
 * - [NoData]: Indicates that the API call returned no data.
 * - [NotFound]: Indicates that the requested resource was not found.
 * - [SomeErrorOccurred]: Indicates that an error occurred during the API call.
 * @author Prashant Singh.
 */
enum class ResponseState {
    Loaded,
    Loading,
    NoData,
    NotFound,
    SomeErrorOccurred,
}

