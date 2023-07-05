package com.xxmrk888ytxx.threads1

import android.content.Context
import android.widget.Toast
import androidx.compose.material.ScaffoldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ActivityViewModel constructor() : ViewModel() {

    val label = MutableStateFlow(0L)

    private val printCount = MutableStateFlow(0)

    private val startSessionTime = System.currentTimeMillis()

    private var isStarted = false

    fun start(
        context: Context,
        uiScope: CoroutineScope,
        scaffoldState: ScaffoldState
    ) {
        if(isStarted) return

        isStarted = true

        viewModelScope.launch(Dispatchers.Default) {
            while (isActive) {
                label.update { System.currentTimeMillis() - startSessionTime }

                delay(1000)
            }
        }

        viewModelScope.launch(Dispatchers.Default) {
            while (isActive) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context,label.value.toString(),Toast.LENGTH_SHORT).show()
                }

                printCount.update { it + 1 }

                delay(10000)
            }
        }

        viewModelScope.launch(Dispatchers.Default) {
            printCount.collect() {
                if(it % 4 == 0 && it != 0) {
                    uiScope.launch {
                        scaffoldState.snackbarHostState.showSnackbar("Surprise")
                    }
                }
            }
        }
    }
}