package com.camc.proyecto_redesiii

data class Usuarios(
    val idAlumno: Int,
    val Nombre: String,
    val ApellidoPaterno: String,
    val ApellidoMaterno: String,
    val idCarrera: Int,
    val Password: String,
    val Semestre: Int,
    val Ocupacion: String
)

data class UsuariosGet(
    val id: Int,
    val Nombre: String,
    val ocupacion: String,
    val apellidos: String,
    val password: String,
    val Semestre: Int
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