package com.example.fdjtest.models

import com.example.fdjtest.Utils.ApiState
import com.example.fdjtest.api.league.LeagueApiService
import com.example.fdjtest.api.models.ResponseLeague
import com.example.fdjtest.api.models.ResponseTeam
import com.example.fdjtest.api.models.ResponseTeamsInLeague
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class LeagueRepository(private val leagueApi: LeagueApiService) {

    suspend fun getAllLeagueName(): Flow<ApiState<ResponseLeague>> {
        return flow {
            val league = leagueApi.getAllLeagues()

            emit(ApiState.success(league))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getTeamsInLeague(league: String): Flow<ApiState<ResponseTeamsInLeague>> {
        return flow {
            val teams = leagueApi.getTeamsInLeague(league)

            emit(ApiState.success(teams))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getTeamInfo(teamName: String): Flow<ApiState<ResponseTeam>> {
        return flow {
            val teamInfo = leagueApi.getTeamInfo(teamName)

            emit(ApiState.success(teamInfo))
        }.flowOn(Dispatchers.IO)
    }
}