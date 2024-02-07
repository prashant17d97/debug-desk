package network.sealeds

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import network.sealeds.ApiResponse.EmptyBodySuccess
import network.sealeds.ApiResponse.Error
import network.sealeds.ApiResponse.Idle
import network.sealeds.ApiResponse.Success

/**
 * Represents the response structure for API calls, providing different states based on the response status.
 * Uses Kotlinx.serialization annotations for JSON serialization.
 * @param Generic The type of data contained in the response.
 * @property Idle Represents the initial state when no API call has been made.
 * @property Success Represents a successful API call with data.
 * @property EmptyBodySuccess Represents a successful API call with an empty body.
 * @property Error Represents an error state in API call.
 *
 * @author Prashant Singh
 */
@Serializable
sealed class ApiResponse<out Generic> {
    // Represents the initial state when no API call has been made.
    @Serializable
    @SerialName("idle")
    data object Idle : ApiResponse<Nothing>()

    /**
     * Represents a successful API call with data.
     * @param response The response data of type [Generic].
     * @param responseMessage A message accompanying the response, if any.
     * @param statusCode The HTTP status code of the response.
     * */
    @Serializable
    @SerialName("success")
    data class Success<Generic>(
        val response: Generic?,
        val responseMessage: String?,
        val statusCode: Int?,
    ) : ApiResponse<Generic>()

    // Represents a successful API call with an empty body.
    @Serializable
    @SerialName("emptyBodySuccess")
    data class EmptyBodySuccess(
        val responseMessage: String?,
        val statusCode: Int?,
    ) : ApiResponse<String>()

    /**
     *  Represents an error state in API call.
     * @param errorMessage The error message accompanying the error state.
     * @param statusCode The HTTP status code of the error response.
     */
    @Serializable
    @SerialName("error")
    data class Error(
        val errorMessage: String?,
        val statusCode: Int?,
    ) : ApiResponse<Nothing>()
}
