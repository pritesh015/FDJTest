package com.example.fdjtest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fdjtest.Utils.ApiState
import com.example.fdjtest.Utils.Status
import com.example.fdjtest.api.league.LeagueApiService
import com.example.fdjtest.api.models.ResponseTeamInfo
import com.example.fdjtest.api.models.ResponseTeamsInLeague
import com.example.fdjtest.api.models.ResponseTeamsInLeagueDetails
import com.example.fdjtest.models.LeagueRepository
import com.example.fdjtest.network.RetrofitInit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    private val repository = LeagueRepository(RetrofitInit.builder().create(LeagueApiService::class.java))

    val leagueNameState = MutableStateFlow(ApiState(Status.LOADING, mutableListOf(""), ""))
    val teamsInLeagueState = MutableStateFlow(ApiState(Status.LOADING, mutableListOf<ResponseTeamsInLeagueDetails>(), ""))
    val teamInfoState = MutableStateFlow(ApiState(Status.LOADING, mutableListOf<ResponseTeamInfo>(), ""))

    fun getLeagueName() {
        leagueNameState.value = ApiState.loading()

        viewModelScope.launch {
            repository.getAllLeagueName()
                .catch {
                    leagueNameState.value = ApiState.error(it.message.toString())
                }
                .collect {
                    val teamListName: MutableList<String> = mutableListOf()

                    it.data?.leagueList?.map { response ->
                        teamListName.add(response.leagueName.toString())
                    }
                    leagueNameState.value = ApiState.success(teamListName)
                }
        }
    }

    fun getTeamsInLeague(league: String) {
        teamsInLeagueState.value = ApiState.loading()

        viewModelScope.launch {
            repository.getTeamsInLeague(league)
                .catch {
                    teamsInLeagueState.value = ApiState.error(it.message.toString())
                }
                .collect {
                    teamsInLeagueState.value = ApiState.success(it.data?.teamsList?.toMutableList())
                }
        }
    }

    fun getTeamInfo(teamName: String) {
        teamInfoState.value = ApiState.loading()

        viewModelScope.launch {
            repository.getTeamInfo(teamName)
                .catch {
                    teamInfoState.value = ApiState.error(it.message.toString())
                }
                .collect {
                    teamInfoState.value = ApiState.success(it.data?.teamInfo?.toMutableList())
                }
        }
    }
}