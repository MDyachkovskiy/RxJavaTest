package com.test.application.rxjavatest

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

fun <T> Flow<T>.throttleFirst(time: Long) : Flow<T> = flow {
    var lastEmitTime = 0L
    collect{ value ->
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastEmitTime >= time) {
            lastEmitTime = currentTime
            emit(value)
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
fun <T> Flow<T>.throttleLast(time: Long) : Flow<T> = channelFlow {
    val values = produce(capacity = Channel.CONFLATED) {
        collect { value ->
            send(value)
        }
    }
    var lastEmitTime = System.currentTimeMillis()

    launch {
        while (isActive) {
            val currentTime = System.currentTimeMillis()
            val nextEmitTime = lastEmitTime + time

            if (currentTime < nextEmitTime) {
                delay(nextEmitTime - currentTime)
            }

            values.tryReceive().getOrNull()?.let { latestValue ->
                send(latestValue)
                lastEmitTime = System.currentTimeMillis()
            }
        }
    }
}


