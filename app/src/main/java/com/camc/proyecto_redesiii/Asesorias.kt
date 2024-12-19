package com.camc.proyecto_redesiii

data class Asesorias(
    val id_asesoria: Int,
    val fecha_inicio: String,
    val fecha_fin: String,
    val dias: String,
    val horario_inicio: String,
    val horario_fin: String,
    val estado: String,
    val materiaNombre: String,
    val maestroNombre: String,
    val lugarNombre: String
)
