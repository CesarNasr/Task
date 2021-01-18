package com.example.inmobilestask.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Coroutines {
    // acts as a coroutine manager
    fun main(work: suspend (() -> Unit)) = // coroutine running on main dispatcher used for the API call
        CoroutineScope(Dispatchers.Main).launch {
            work()
        }

//    fun io(work: suspend () -> Unit)= // to be used in other cases
//        CoroutineScope(Dispatchers.IO).launch {
//            work()
//        }
}