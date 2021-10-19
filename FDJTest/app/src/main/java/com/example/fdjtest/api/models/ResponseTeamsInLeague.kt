package com.example.fdjtest.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseTeamsInLeague(
    @field:Json(name = "teams")
    val teamsList: List<ResponseTeamsInLeagueDetails> = listOf()
)

@JsonClass(generateAdapter = true)
data class ResponseTeamsInLeagueDetails(
    @field:Json(name = "strTeam")
    val teamName: String? = null,

    @field:Json(name = "strTeamBadge")
    val teamBadge: String? = null
)