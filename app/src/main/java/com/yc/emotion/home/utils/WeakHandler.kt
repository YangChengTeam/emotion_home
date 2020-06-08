package com.yc.emotion.home.utils

import android.os.Handler
import android.os.Looper
import android.os.Message
import java.lang.ref.WeakReference

class WeakHandler : Handler {

    interface IHandler {
        fun handleMessage(mess: Message)
    }

    private var weakReference: WeakReference<IHandler>? = null


    constructor(handler: IHandler) : super() {

        weakReference = WeakReference(handler)
    }

    constructor(looper: Looper, handler: IHandler) : super(looper) {

        weakReference = WeakReference(handler)
    }

    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        weakReference?.get()?.handleMessage(msg)

    }
}