package com.faizikhwan.websocketdemo

import android.content.Context
import android.util.Log
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString


class EchoWebSocketListener(val context: Context) : WebSocketListener() {

    val TAG = "EchoWebSocketListener"
    lateinit var callback: OnResponse

    companion object {
        val NORMAL_CLOSURE_STATUS = 1000
    }

    interface OnResponse {
        fun OnReceiveMessage(message: String)
    }

    init {
        if (context is OnResponse) callback = context
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        Log.d(TAG, "Receiving : $text")
        callback.OnReceiveMessage("Receiving : $text")
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        super.onMessage(webSocket, bytes)
        Log.d(TAG, "Receiving bytes : ${bytes.hex()}")
        callback.OnReceiveMessage("Receiving bytes : ${bytes.hex()}")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
        Log.d(TAG, "Closing : $code | reason: $reason")
        callback.OnReceiveMessage("Closing : $code | reason: $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        Log.d(TAG, "Error: ${t.message}")
        callback.OnReceiveMessage("Error: ${t.message}")
    }
}