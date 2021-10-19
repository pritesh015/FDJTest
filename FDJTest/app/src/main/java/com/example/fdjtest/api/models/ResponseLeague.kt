package com.example.fdjtest.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseLeague(
    @field:Json(name = "leagues")
    val leagueList: List<ResponseLeagueList> = listOf()
)

@JsonClass(generateAdapter = true)
data class ResponseLeagueList(
    @field:Json(name = "idLeague")
    val leagueID: Int,

    @field:Json(name = "strLeague")
    val leagueName: String? = null,

    @field:Json(name = "strSport")
    val sport: String? = null,

    @field:Json(name = "strLeagueAlternate")
    val LeagueAlternate: String? = null
)
