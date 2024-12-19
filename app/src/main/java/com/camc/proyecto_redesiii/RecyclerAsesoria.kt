package com.camc.proyecto_redesiii

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAsesoria(private val asesorias: List<Asesorias>, private val listener: OnItemClickListener) : RecyclerView.Adapter<RecyclerAsesoria.AsesoriaViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(asesoria: Asesorias)
    }

    inner class AsesoriaViewHolder(itemView: View) : RecyclerView. ViewHolder(itemView) {
        var titulo : TextView = itemView.findViewById(R.id.titulo_asesoria)
        var hora : TextView = itemView.findViewById(R.id.hora_asesoria)
        var lugar : TextView = itemView.findViewById(R.id.lugar_asesoria)
        var profesor: TextView = itemView.findViewById(R.id.profesor_asesoria)
        fun bind(asesoria: Asesorias) {
            titulo.text=asesoria.materiaNombre
            hora.text="Hora: "+asesoria.horario_inicio
            lugar.text="Lugar: "+asesoria.lugarNombre
            profesor.text=asesoria.maestroNombre
            itemView.setOnClickListener {
                listener.onItemClick(asesoria)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsesoriaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_asesoria,
            parent, false)
        return AsesoriaViewHolder(view)
    }

    override fun onBindViewHolder(holder: AsesoriaViewHolder, position: Int) {
        val asesoria = asesorias[position]
        holder.bind(asesoria)
    }

    override fun getItemCount(): Int {
        return asesorias.size
    }
}