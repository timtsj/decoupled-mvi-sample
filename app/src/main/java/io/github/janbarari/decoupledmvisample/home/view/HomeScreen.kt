package io.github.janbarari.decoupledmvisample.home.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.janbarari.decoupledmvisample.home.aser.HomeAction
import io.github.janbarari.decoupledmvisample.home.aser.HomeEffect
import io.github.janbarari.decoupledmvisample.home.aser.HomeReducer
import io.github.janbarari.decoupledmvisample.home.aser.HomeState
import io.github.janbarari.decoupledmvisample.theme.DecoupledMVISampleTheme
import io.github.janbarari.mvi.ScreenController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    controller: ScreenController<HomeAction, HomeState, HomeEffect, HomeReducer>,
) {
    val coroutineScope = rememberCoroutineScope()
    val state by controller.state.collectAsState()

    LaunchedEffect(Unit) {
        controller.effect.collectLatest {
            // Handle effects that require user interface (UI) changes, such as displaying popups or other UI elements
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Munich, Germany", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(48.dp))

        if (state.isLoading) {
            Text(text = "Loading...")
            Spacer(modifier = Modifier.height(72.dp))
        } else if (state.isError) {
            Text(text = "ERROR")
            Spacer(modifier = Modifier.height(72.dp))
        } else {
            Text(text = "Weather")
            Text(text = state.weatherStatus)
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Temp")
            Text(text = state.tempStatus)
            Spacer(modifier = Modifier.height(48.dp))
        }

        if (state.isLoading.not() || state.isError) {
            Button(
                onClick = {
                    coroutineScope.launch {
                        controller.action(HomeAction.LoadWeatherButtonClicked("Munich"))
                    }
                }
            ) {
                Text(text = "Get Weather")
            }

            Button(onClick = {
                coroutineScope.launch {
                    controller.action(HomeAction.LoadTempButtonClicked("Munich"))
                }
            }) {
                Text(text = "Get Temp")
            }
        }
    }

}

@Preview(showSystemUi = true)
@Composable
fun ScreenPreview() {
    class FakeController : ScreenController<HomeAction, HomeState, HomeEffect, HomeReducer> {
        override val state: StateFlow<HomeState> = MutableStateFlow(HomeState.Default)
        override val effect: Flow<HomeEffect> = MutableSharedFlow()
        override suspend fun action(action: HomeAction) = Unit

    }

    DecoupledMVISampleTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            MainScreen(controller = FakeController())
        }
    }
}