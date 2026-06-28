package com.example.mendacium.ui.service

import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.StompClient as KrossbowStompClient
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient

// Conexión WebSocket de bajo nivel (patrón singleton, como RetrofitClient).
// El backend declara el endpoint /ws; con el context-path /api la URL es /api/ws.
object StompClient {

    private const val WS_URL = "ws://10.0.2.2:8080/api/ws"

    private var session: StompSession? = null

    suspend fun conectar(): StompSession {
        session?.let { return it }
        val nueva = KrossbowStompClient(OkHttpWebSocketClient()).connect(WS_URL)
        session = nueva
        return nueva
    }

    suspend fun desconectar() {
        session?.disconnect()
        session = null
    }
}
