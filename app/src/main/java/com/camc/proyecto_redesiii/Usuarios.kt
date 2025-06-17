package com.camc.proyecto_redesiii

data class Usuarios(
    val id: Int,
    val nombre: String,
    val apellidoPaterno: String,
    val apellidoMaterno: String,
    val idCarrera: Int,
    val password: String,
    val semestre: Int,
    val ocupacion: String
)

data class UsuariosGet(
    val id: Int,
    val Nombre: String,
    val ocupacion: String,
    val apellidos: String,
    val password: String,
    val Semestre: Int
)

data class UsuarioUpdate(
    val id: Int,
    val ocupacion: String,
    val password: String
)

data class RespuestaLogin(
    val mensaje: String,
    val usuario: UsuarioResp
)

data class UsuarioResp(
    val id: String,
    val nombre: String,
    val ocupacion: String
)

data class UsuarioIniciarSesion(
    val id: String,
    val password: String
)