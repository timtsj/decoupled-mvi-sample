package io.github.janbarari.decoupledmvisample.home.aser

import io.github.janbarari.decoupledmvisample.home.presentation.state.TempState
import io.github.janbarari.decoupledmvisample.home.presentation.state.WeatherState
import io.github.janbarari.mvi.Reducer

sealed class HomeReducer(
    reducer: Reducer<HomeState>
): Reducer<HomeState> by reducer {

    object OnLoading: HomeReducer(
        {
            it.copy(
                isLoading = true,
                isError = false,
            )
        }
    )

    data class OnWeatherLoaded(
        val weatherState: WeatherState
    ): HomeReducer(
        {
            it.copy(
                isLoading = false,
                weatherStatus = weatherState.result,
                isError = false,
            )
        }
    )

    data class OnTempLoaded(
        val tempState: TempState
    ): HomeReducer(
        {
            it.copy(
                isLoading = false,
                tempStatus = tempState.result,
                isError = false,
            )
        }
    )

    object Error : HomeReducer({
        it.copy(
            isError = true,
            isLoading = false,
        )
    })
}
