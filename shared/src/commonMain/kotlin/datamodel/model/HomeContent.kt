package datamodel.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import network.apiendpoints.ApiEndpointConstants.ID

@Serializable
data class HomeContent(
    @SerialName(ID)
    val _id: String = "",
    @SerialName("copyright")
    val copyright: String,
    @SerialName("logo")
    val logo: String,
    @SerialName("noData")
    val noData: String,
    @SerialName("notFound")
    val notFound: String,
    @SerialName("loading")
    val loading: String,
    @SerialName("someError")
    val someError: String,
    @SerialName("siteTitle")
    val siteTitle: String,
    @SerialName("socialLinks")
    val socialLinks: List<SocialLink>,
)
