package com.camc.proyecto_redesiii

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAsesoriaSoli(private val asesorias: List<AsesoriaSolicitada>, private val listener: OnItemClickListener) : RecyclerView.Adapter<RecyclerAsesoriaSoli.AsesoriaSoliViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(asesoria: AsesoriaSolicitada)
    }

    inner class AsesoriaSoliViewHolder(itemView: View) : RecyclerView. ViewHolder(itemView) {
        var titulo : TextView = itemView.findViewById(R.id.titulo_asesoria)
        var hora : TextView = itemView.findViewById(R.id.hora_asesoria)
        var lugar : TextView = itemView.findViewById(R.id.lugar_asesoria)
        var profesor: TextView = itemView.findViewById(R.id.profesor_asesoria)
        fun bind(asesoria: AsesoriaSolicitada) {
            titulo.text=asesoria.materia
            hora.text=asesoria.horarioInicio
            lugar.text="Lugar: Por definir"
            profesor.text="Profesor: Por definir"
            itemView.setOnClickListener {
                listener.onItemClick(asesoria)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsesoriaSoliViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_asesoria,
            parent, false)
        return AsesoriaSoliViewHolder(view)
    }

    override fun onBindViewHolder(holder: AsesoriaSoliViewHolder, position: Int) {
        val asesoria = asesorias[position]
        holder.bind(asesoria)
    }

    override fun getItemCount(): Int {
        return asesorias.size
    }
}