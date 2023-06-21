package io.github.janbarari.mvi

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlin.reflect.KClass

interface ScreenController<A : Any, S : Any, E : Any, R : Reducer<S>> {
    val state: StateFlow<S>
    val effect: Flow<E>
    suspend fun action(action: A)
}

class DefaultScreenController<A : Any, S : Any, E : Any, R : Reducer<S>>(
    private val effectHandler: EffectHandler<E>,
    initialState: S,
    private val actionHandlers: Set<Pair<KClass<out A>, ActionHandler<*, S, E, R>>>
) : ScreenController<A, S, E, R> {

    private val _state = MutableStateFlow(initialState)
    override val state: StateFlow<S>
        get() = _state
    override val effect: Flow<E>
        get() = effectHandler.effects

    override suspend fun action(action: A) {
        (actionHandlers
            .find { it.first == action::class }
            ?.second as? ActionHandler<A, S, E, R>)
            ?.handle(action, _state.value) { effect ->
                effectHandler.handleEffect(effect)
            }?.cancellable()
            ?.catch {
                if (BuildConfig.DEBUG) {
                    Log.e("Controller", it.message ?: "unknown error")
                }
            }
            ?.collect { reducer ->
                _state.update { reducer.reduce(_state.value) }
            }
    }
}