package com.example.audioskinsduoc

data class Usuarios(
    var id: String = "", // Nuevo campo para la identificación
    val email: String = "",
    val nombre: String = "",
    val apellidoPaterno: String = "",
    val apellidoMaterno: String = "",
    val telefono: String = "",
    val direccion: String = ""
)
