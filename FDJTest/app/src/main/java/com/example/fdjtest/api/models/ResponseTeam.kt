package com.example.fdjtest.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class ResponseTeam(
    @field:Json(name = "teams")
    val teamInfo: List<ResponseTeamInfo> = listOf()
)

@JsonClass(generateAdapter = true)
data class ResponseTeamInfo(
    @field:Json(name = "strTeam")
    val teamName: String? = null,

    @field:Json(name = "strTeamBanner")
    val teamBanner: String? = null,

    @field:Json(name = "strCountry")
    val country: String? = null,

    @field:Json(name = "strLeague")
    val teamLeague: String? = null,

    @field:Json(name = "strDescriptionEN")
    val teamDescription: String? = null
)