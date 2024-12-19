package com.camc.proyecto_redesiii

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Navegacion : AppCompatActivity() {
    //FOOTER
    var salir: ImageButton? = null
    var solicitar: ImageButton? = null
    var cuenta: ImageButton? = null
    var calendario: ImageButton? = null
    //

    var recyclerAsesoria: RecyclerView? = null
    var recyclerCurso: RecyclerView? = null

    var leyenda: ConstraintLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.navegacion)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //FOOTER
        salir=findViewById(R.id.salir)
        salir!!.setOnClickListener(evento)
        solicitar=findViewById(R.id.solicitar)
        solicitar!!.setOnClickListener(evento)
        cuenta=findViewById(R.id.cuenta)
        cuenta!!.setOnClickListener(evento)
        calendario=findViewById(R.id.calendario)
        calendario!!.setOnClickListener(evento)
        //

        leyenda=findViewById(R.id.sinAsesorias)
        recyclerAsesoria=findViewById(R.id.asesorias)
        recyclerAsesoria!!.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerAsesoria!!.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerCurso=findViewById(R.id.cursos)
        recyclerCurso!!.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerCurso!!.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        if(Sesion.usuario!=0){
            // Obtenemos asesorias
            RetrofitClient.instance.getAsesoriasUsuarios(Sesion.usuario).enqueue(object : retrofit2.Callback<List<Asesorias>> {
                override fun onResponse(call: retrofit2.Call<List<Asesorias>>, response: retrofit2.Response<List<Asesorias>>) {
                    if (response.isSuccessful) {
                        val asesorias = response.body()
                        if(asesorias!!.size!=0){
                            recyclerAsesoria!!.visibility=View.VISIBLE
                            leyenda!!.isEnabled=false
                            leyenda!!.visibility=View.GONE
                            val adaptador = RecyclerAsesoria(asesorias!!, object : RecyclerAsesoria.OnItemClickListener {
                                override fun onItemClick(asesoria: Asesorias) {
                                    Log.d("RecyclerAsesoria", "Clic en ${asesoria.materiaNombre}")
                                    val intent: Intent = Intent(this@Navegacion, DetallesAsesoria::class.java)
                                    intent.putExtra("id", asesoria.id_asesoria)
                                    intent.putExtra("materia", asesoria.materiaNombre)
                                    intent.putExtra("lugar", asesoria.lugarNombre)
                                    intent.putExtra("dias", asesoria.dias)
                                    intent.putExtra("dia", asesoria.fecha_inicio)
                                    intent.putExtra("hora", asesoria.horario_inicio)
                                    intent.putExtra("profesor", asesoria.maestroNombre)
                                    intent.putExtra("boton", "asesoria")
                                    startActivity(intent)
                                }
                            })
                            recyclerAsesoria!!.adapter = adaptador
                        }else{
                            recyclerAsesoria!!.isEnabled=false
                            recyclerAsesoria!!.isVisible=false
                            recyclerAsesoria!!.visibility=View.GONE
                            leyenda!!.visibility=View.VISIBLE
                        }
                    }
                }
                override fun onFailure(call: retrofit2.Call<List<Asesorias>>, t: Throwable) {
                    //Log.e("API", "Fallo: ${t.message}")
                }
            })

            // Obtenemos cursos
            RetrofitClient.instance.getCursos(Sesion.usuario).enqueue(object : retrofit2.Callback<List<Asesorias>> {
                override fun onResponse(call: retrofit2.Call<List<Asesorias>>, response: retrofit2.Response<List<Asesorias>>) {
                    if (response.isSuccessful) {
                        val asesorias = response.body()
                        val adaptador = RecyclerCursos(asesorias!!, object : RecyclerCursos.OnItemClickListener {
                            override fun onItemClick(asesoria: Asesorias) {
                                val intent: Intent = Intent(this@Navegacion, DetallesAsesoria::class.java)
                                intent.putExtra("id", asesoria.id_asesoria)
                                intent.putExtra("materia", asesoria.materiaNombre)
                                intent.putExtra("lugar", asesoria.lugarNombre)
                                intent.putExtra("dias", asesoria.dias)
                                intent.putExtra("dia", asesoria.fecha_inicio)
                                intent.putExtra("hora", asesoria.horario_inicio)
                                intent.putExtra("profesor", asesoria.maestroNombre)
                                intent.putExtra("boton", "curso")
                                startActivity(intent)
                            }
                        })
                        recyclerCurso!!.adapter = adaptador
                    }
                }
                override fun onFailure(call: retrofit2.Call<List<Asesorias>>, t: Throwable) {
                    //Log.e("API", "Fallo: ${t.message}")
                }
            })
        }



    }

    private val evento = View.OnClickListener { v ->
        //FOOTER
        if(v === salir){
            Sesion.usuario=0
            val intent = Intent(this@Navegacion, Registro::class.java)
            startActivity(intent)
        }
        if(v === solicitar){
            val intent = Intent(this@Navegacion, Solicitar::class.java)
            startActivity(intent)
        }
        if(v === cuenta){
            val intent = Intent(this@Navegacion, Usuario::class.java)
            startActivity(intent)
        }
        if(v === calendario){
            val intent = Intent(this@Navegacion, Calendario::class.java)
            startActivity(intent)
        }
        //
    }
}