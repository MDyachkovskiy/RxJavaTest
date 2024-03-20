package com.test.application.rxjavatest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val testFlow = flow {
            repeat(100) { i ->
                emit(i)
                delay(10)
            }
        }
        coroutineScope.launch {
            testFlow
                .throttleLast(300L)
                .collect { value ->
                    Log.d("@@@", "Emitted value: $value")
                }
        }
    }
}