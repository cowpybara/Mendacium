package com.example.mendacium.model

// LOCAL  = un solo teléfono que se pasa entre jugadores (offline, MotorResolucion en Android)
// ONLINE = cada jugador en su propio teléfono (WebSocket; el backend resuelve)
enum class GameMode { LOCAL, ONLINE }
