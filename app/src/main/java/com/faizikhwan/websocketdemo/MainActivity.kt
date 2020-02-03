package com.faizikhwan.websocketdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket


class MainActivity : AppCompatActivity(), EchoWebSocketListener.OnResponse {

    lateinit var client: OkHttpClient
    lateinit var webSocket: WebSocket
    var message = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        client = OkHttpClient()
        setupWebSocket()
        initComponent()
    }

    override fun OnReceiveMessage(message: String) {
        this.message += message + "\n"

        runOnUiThread {
            tv_output.text = this.message
        }
    }

    fun initComponent() {
        btn_send.setOnClickListener {
            if (et_message.text.toString().isNotEmpty()) {
                handleSendMessage(et_message.text.toString())
                et_message.text.clear()
            }
        }

        btn_close.setOnClickListener {
            webSocket.close(EchoWebSocketListener.NORMAL_CLOSURE_STATUS, "reason")
        }
    }

    fun setupWebSocket() {
        val request: Request = Request.Builder().url("ws://echo.websocket.org").build()
        val listener = EchoWebSocketListener(this)
        webSocket = client.newWebSocket(request, listener)
        client.dispatcher.executorService.shutdown()
    }

    fun handleSendMessage(message: String) {
        webSocket.send(message)
    }

}
