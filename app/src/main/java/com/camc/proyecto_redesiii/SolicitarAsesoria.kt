package com.camc.proyecto_redesiii

data class SolicitarAsesoria(
    val idAlumno: Int,
    val dias: List<String>,
    val horario_inicio: String,
    val materia: String
)
