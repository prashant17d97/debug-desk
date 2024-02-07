package datamodel.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class NetworkResponse<Generic>(
    val data: ApiCallResponse<Generic>? = null,
    val errorCallResponse: ApiErrorCallResponse? = null,
) {
    @SerialName("success")
    class Success<Generic>(apiCallResponse: ApiCallResponse<Generic>?) : NetworkResponse<Generic>(
        data = apiCallResponse,
    )

    @SerialName("error")
    class Error<Generic>(errorCallResponse: ApiErrorCallResponse?) : NetworkResponse<Generic>(
        errorCallResponse = errorCallResponse,
    )
}
