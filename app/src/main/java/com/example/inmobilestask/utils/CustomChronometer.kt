package com.example.inmobilestask.utils

import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet

import android.widget.Chronometer

// my custom chronometer class to override native methods and manipulate them as needed
class CustomChronometer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Chronometer(context, attrs, defStyleAttr) {
    private var timeWhenStopped: Long = 0

//    var isRunning : Boolean = false

    override fun start() {
        base = SystemClock.elapsedRealtime() + timeWhenStopped
        super.start()
    }

    override fun stop() {
//        isRunning = false
        super.stop()
        timeWhenStopped = base - SystemClock.elapsedRealtime()
    }

    fun reset() {
        stop()
        base = SystemClock.elapsedRealtime()
        timeWhenStopped = 0
    }

    var currentTime: Long
        get() = timeWhenStopped
        set(time) {
            timeWhenStopped = time
            base = SystemClock.elapsedRealtime() + timeWhenStopped
        }

}