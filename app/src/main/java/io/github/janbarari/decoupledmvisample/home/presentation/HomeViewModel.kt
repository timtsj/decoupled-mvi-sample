package io.github.janbarari.decoupledmvisample.home.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.janbarari.decoupledmvisample.home.aser.GetTempActionHandler
import io.github.janbarari.decoupledmvisample.home.aser.GetWeatherActionHandler
import io.github.janbarari.decoupledmvisample.home.aser.HomeAction
import io.github.janbarari.decoupledmvisample.home.aser.HomeEffect
import io.github.janbarari.decoupledmvisample.home.aser.HomeEffectHandler
import io.github.janbarari.decoupledmvisample.home.aser.HomeReducer
import io.github.janbarari.decoupledmvisample.home.aser.HomeState
import io.github.janbarari.mvi.DefaultScreenController
import io.github.janbarari.mvi.ScreenController
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    homeEffectHandler: HomeEffectHandler,
    private val getWeatherActionHandler: GetWeatherActionHandler,
    private val getTempActionHandler: GetTempActionHandler
) : ViewModel(),
    ScreenController<HomeAction, HomeState, HomeEffect, HomeReducer> by DefaultScreenController(
        homeEffectHandler,
        HomeState.Default,
        setOf(
            HomeAction.LoadWeatherButtonClicked::class to getWeatherActionHandler,
            HomeAction.LoadTempButtonClicked::class to getTempActionHandler
        )
    )
