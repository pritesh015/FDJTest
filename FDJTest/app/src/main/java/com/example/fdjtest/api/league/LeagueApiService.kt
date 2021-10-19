package com.example.fdjtest.api.league

import com.example.fdjtest.api.models.ResponseLeague
import com.example.fdjtest.api.models.ResponseTeam
import com.example.fdjtest.api.models.ResponseTeamsInLeague
import retrofit2.http.GET
import retrofit2.http.Query

interface LeagueApiService {

    @GET("all_leagues.php")
    suspend fun getAllLeagues(): ResponseLeague

    @GET("search_all_teams.php?")
    suspend fun getTeamsInLeague(@Query("l") l: String): ResponseTeamsInLeague

    @GET("searchteams.php?")
    suspend fun getTeamInfo(@Query("t") t: String): ResponseTeam
}