package com.camc.proyecto_redesiii

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject

class Solicitar : AppCompatActivity() {
    var regresar: Button? = null
    var solicitar: Button? = null

    val datosHoras = listOf("07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00")
    var datosMaterias = mutableListOf<String>()

    var spinnerHora: Spinner? = null
    var spinnerMateria: Spinner? = null

    var lun: CheckBox? = null
    var mar: CheckBox? = null
    var mie: CheckBox? = null
    var jue: CheckBox? = null
    var vie: CheckBox? = null
    var dias: RadioGroup? = null

    var diasString = mutableListOf<String>()
    var materia: String? = null
    var hora: String? = null

    var recyclerAsesoria: RecyclerView? = null
    var leyenda: ConstraintLayout? = null
    var contenedorRecycler: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.solicitar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        regresar=findViewById(R.id.btnRegresar)
        regresar!!.setOnClickListener(evento)
        solicitar=findViewById(R.id.solicitar)
        solicitar!!.setOnClickListener(evento)

        leyenda=findViewById(R.id.sinAsesorias)
        contenedorRecycler=findViewById(R.id.conAsesorias)
        recyclerAsesoria=findViewById(R.id.asesorias)
        recyclerAsesoria!!.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerAsesoria!!.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        dias = findViewById(R.id.dias)
        lun = findViewById(R.id.lunes)
        mar = findViewById(R.id.martes)
        mie = findViewById(R.id.miercoles)
        jue = findViewById(R.id.jueves)
        vie = findViewById(R.id.viernes)

        lun!!.setOnClickListener { v ->
            if (lun!!.isChecked) {
                diasString.add("Lunes")
            } else {
                diasString.remove("Lunes")
            }
            Log.d("YO", diasString.toString())
        }

        mar!!.setOnClickListener { v ->
            if (mar!!.isChecked) {
                diasString.add("Martes")
            } else {
                diasString.remove("Martes")
            }
            Log.d("YO", diasString.toString())
        }

        mie!!.setOnClickListener { v ->
            if (mie!!.isChecked) {
                diasString.add("Miércoles")
            } else {
                diasString.remove("Miércoles")
            }
            Log.d("YO", diasString.toString())
        }

        jue!!.setOnClickListener { v ->
            if (jue!!.isChecked) {
                diasString.add("Jueves")
            } else {
                diasString.remove("Jueves")
            }
            Log.d("YO", diasString.toString())
        }

        vie!!.setOnClickListener { v ->
            if (vie!!.isChecked) {
                diasString.add("Viernes")
            } else {
                diasString.remove("Viernes")
            }
            Log.d("YO", diasString.toString())
        }

        RetrofitClient.instance.getMaterias(Sesion.usuario).enqueue(object : retrofit2.Callback<List<Materias>> {
            override fun onResponse(call: retrofit2.Call<List<Materias>>, response: retrofit2.Response<List<Materias>>) {
                if (response.isSuccessful) {
                    val materias = response.body()
                    for(materia in materias!!){
                        datosMaterias.add(materia.Nombre)
                        Log.d("datos", "$datosMaterias")
                        llenamosSpinner()
                    }
                }
            }
            override fun onFailure(call: retrofit2.Call<List<Materias>>, t: Throwable) {
                //Log.e("API", "Fallo: ${t.message}")
            }
        })

        RetrofitClient.instance.getAsesoriasSolicitadas(Sesion.usuario).enqueue(object : retrofit2.Callback<List<AsesoriaSolicitada>> {
            override fun onResponse(call: retrofit2.Call<List<AsesoriaSolicitada>>, response: retrofit2.Response<List<AsesoriaSolicitada>>) {
                if (response.isSuccessful) {
                    val asesorias = response.body()
                    if(asesorias!!.size!=0){
                        contenedorRecycler!!.visibility=View.VISIBLE
                        recyclerAsesoria!!.visibility=View.VISIBLE
                        leyenda!!.isEnabled=false
                        leyenda!!.visibility=View.GONE
                        val adaptador = RecyclerAsesoriaSoli(asesorias!!, object : RecyclerAsesoriaSoli.OnItemClickListener {
                            override fun onItemClick(asesoria: AsesoriaSolicitada) {
                                Log.d("RecyclerAsesoria", "Clic en ${asesoria.materia}")
                                val intent: Intent = Intent(this@Solicitar, DetallesAsesoria::class.java)
                                intent.putExtra("id", asesoria.idAsesoria)
                                intent.putExtra("materia", asesoria.materia)
                                intent.putExtra("lugar", "Por definir")
                                intent.putExtra("dias", "Por definir")
                                intent.putExtra("hora", "Por definir")
                                intent.putExtra("profesor", "Por definir")
                                intent.putExtra("boton", "asesoria")
                                startActivity(intent)
                            }
                        })
                        recyclerAsesoria!!.adapter = adaptador
                    }else{
                        recyclerAsesoria!!.isEnabled=false
                        recyclerAsesoria!!.isVisible=false
                        recyclerAsesoria!!.visibility=View.GONE
                        contenedorRecycler!!.visibility=View.GONE
                        leyenda!!.visibility=View.VISIBLE
                    }
                }
            }
            override fun onFailure(call: retrofit2.Call<List<AsesoriaSolicitada>>, t: Throwable) {
                //Log.e("API", "Fallo: ${t.message}")
            }
        })
    }

    fun llenamosSpinner(){
        val adaptadorSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_item, datosHoras)
        spinnerHora=findViewById(R.id.hora)
        spinnerHora!!.adapter = adaptadorSpinner
        spinnerHora!!.setSelection(datosHoras.indexOf("07:00"))
        val adaptadorSpinnerMat = ArrayAdapter(this, android.R.layout.simple_spinner_item, datosMaterias.toList())
        spinnerMateria=findViewById(R.id.materia)
        spinnerMateria!!.adapter = adaptadorSpinnerMat
    }

    private val evento = View.OnClickListener { v ->
        if(v === regresar){
            val intent = Intent(this@Solicitar, Navegacion::class.java)
            startActivity(intent)
        }
        if(v === solicitar){
            val diasMandar = diasString.joinToString(", ")
            val vectorDias = diasMandar.split(", ")
            Log.d("TAG", "vector: $vectorDias")
            materia=spinnerMateria!!.selectedItem.toString()
            hora=spinnerHora!!.selectedItem.toString()
            val asesoria = SolicitarAsesoria(dias = vectorDias, horario_inicio = hora!!, materia = materia!!, idAlumno = Sesion.usuario)
            RetrofitClient.instance.solicitarAsesoria(asesoria).enqueue(object : retrofit2.Callback<SolicitarAsesoria> {
                override fun onResponse(call: retrofit2.Call<SolicitarAsesoria>, response: retrofit2.Response<SolicitarAsesoria>) {
                    if (response.isSuccessful) {
                        Log.d("YO", "AQUI SI")
                        val builder = AlertDialog.Builder(this@Solicitar)
                        builder.setTitle("Excelente!")
                        builder.setMessage("Solicitud en proceso, los tutores podrán aceptar tu solicitud próximamente")
                        builder.setPositiveButton("Aceptar") { dialog, _ ->
                            val intent = Intent(this@Solicitar, Navegacion::class.java)
                            startActivity(intent)
                        }
                        builder.setCancelable(false)
                        builder.show()
                    }
                }
                override fun onFailure(call: retrofit2.Call<SolicitarAsesoria>, t: Throwable) {
                    Log.e("API", "Fallo: ${t.message}")
                }
            })
        }
    }
}