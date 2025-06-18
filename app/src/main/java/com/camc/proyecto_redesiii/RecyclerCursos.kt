package com.camc.proyecto_redesiii

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerCursos(private val cursos: List<Asesorias>, private val listener: OnItemClickListener) : RecyclerView.Adapter<RecyclerCursos.CursoViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(item: Asesorias)
    }

    inner class CursoViewHolder(itemView: View) : RecyclerView. ViewHolder(itemView) {
        var titulo : TextView = itemView.findViewById(R.id.titulo_asesoria)
        var dias : TextView = itemView.findViewById(R.id.dias_curso)
        var hora : TextView = itemView.findViewById(R.id.hora_curso)
        fun bind(asesoria: Asesorias) {
            titulo.text=asesoria.materia
            dias.text="Dias: "+asesoria.dias
            hora.text="Hora: "+asesoria.horario_inicio
            itemView.setOnClickListener {
                listener.onItemClick(asesoria)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CursoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_cursos,
            parent, false)
        return CursoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CursoViewHolder, position: Int) {
        val producto = cursos[position]
        holder.bind(producto)
    }

    override fun getItemCount(): Int {
        return cursos.size
    }
}