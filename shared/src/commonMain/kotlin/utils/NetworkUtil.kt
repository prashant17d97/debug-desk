package utils

import datamodel.model.ApiCallResponse
import datamodel.model.ApiErrorCallResponse
import datamodel.model.NetworkResponse
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import network.netweorkcall.ApiCalls

/**
 * Utility object containing network-related functions.
 */
object NetworkUtil {
    /**
     * Handles the network response by updating the response state and message,
     * and invoking appropriate callbacks based on the success or failure of the response.
     * @param apiCalls The instance of ApiCalls used to update the response state and message.
     * @param onSuccess The callback function to be invoked on successful response.
     * @param onFailure The callback function to be invoked on failed response.
     */
    suspend fun <Generic> NetworkResponse<Generic>.handleResponse(
        apiCalls: ApiCalls,
        onSuccess: suspend (ApiCallResponse<Generic>) -> Unit,
        onFailure: (ApiErrorCallResponse) -> Unit = {},
    ) {
        when (this) {
            is NetworkResponse.Error ->
                this.errorCallResponse?.let {
                    // Handle error response...
                    with(apiCalls) {
                        updateResponseState(it.statusCode)
                        updateResponseMessage(it.errorMessage)
                    }
                    onFailure(it)
                }

            is NetworkResponse.Success -> {
                this.data?.let {
                    // Handle success response...
                    with(apiCalls) {
                        updateResponseState(it.statusCode)
                        updateResponseMessage(it.responseMessage)
                    }
                    onSuccess(it)
                } ?: onFailure(ApiErrorCallResponse())
            }
        }
    }

    /**
     * Parses the data from the HttpResponse body.
     * @return The parsed data of type T.
     */
    suspend inline fun <reified T> HttpResponse.parseData(): T {
        // Parse data from the HttpResponse body...
        return this.body<T>()
    }
}

