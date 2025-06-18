package com.camc.proyecto_redesiii

data class Asesorias(
    val id_asesoria: Int,
    val fecha_inicio: String,
    val fecha_fin: String,
    val dias: String,
    val horario_inicio: String,
    val horario_fin: String,
    val estado: String,
    val materia: String,
    val lugar: String,
    val docente: String
)

data class AsesoriasUsr(
    val id_asesoria: Int,
    val fecha_inicio: String,
    val fecha_fin: String,
    val dias: String,
    val horario_inicio: String,
    val horario_fin: String,
    val estado: String
)

data class AsesoriaSolicitada(
    val idAsesoria: Int,
    val fechaInicio: String,
    val fechaFin: String,
    val dias: String,
    val horarioInicio: String,
    val horarioFin: String,
    val estado: String,
    val materia: String
)