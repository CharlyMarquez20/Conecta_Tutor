package com.camc.proyecto_redesiii

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible

class DetallesAsesoria : AppCompatActivity() {
    var materia : TextView? = null
    var lugar : TextView? = null
    var dias : TextView? = null
    var hora : TextView? = null
    var profesor : TextView? = null

    var regresar: Button? = null
    var baja: Button? = null
    var inscribir: Button? = null

    var idAsesoria: Int? = null

    //Variables para mandar email
    var profesorText: String? = null
    var materiaText: String? = null
    var lugarText: String? = null
    var diasText: String? = null
    var horaText: String? = null
    var diaInicioText: String? = null

    //Clase de SendGrid
    val email = SendGrid()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.item_detalles)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Textos
        materia=findViewById(R.id.materia)
        lugar=findViewById(R.id.lugar)
        dias=findViewById(R.id.dias)
        hora=findViewById(R.id.hora)
        profesor=findViewById(R.id.profesor)

        //Botones
        regresar=findViewById(R.id.btnRegresar)
        regresar!!.setOnClickListener(evento)
        baja=findViewById(R.id.btnBaja)
        baja!!.setOnClickListener(evento)
        baja!!.isEnabled=false
        baja!!.isVisible=false
        inscribir=findViewById(R.id.btnInscribir)
        inscribir!!.setOnClickListener(evento)
        inscribir!!.isEnabled=false
        inscribir!!.isVisible=false

        if (intent.extras != null) {
            idAsesoria = intent.getIntExtra("id", 0)
            val boton = intent.getStringExtra("boton")
            materiaText = intent.getStringExtra("materia")
            lugarText = intent.getStringExtra("lugar")
            diasText = intent.getStringExtra("dias")
            diaInicioText = intent.getStringExtra("dia")
            horaText = intent.getStringExtra("hora")
            profesorText = intent.getStringExtra("profesor")
            if(profesorText==null){
                materia!!.text=materiaText
                lugar!!.text=""
                dias!!.text=diasText
                hora!!.text=horaText
                profesor!!.text=""
                baja!!.isEnabled=true
                baja!!.isVisible=true
                baja!!.text="Eliminar solicitud"
            }else{
                materia!!.text=materiaText
                lugar!!.text=lugarText
                dias!!.text=diasText
                hora!!.text=horaText
                profesor!!.text=profesorText
                if(boton=="asesoria"){
                    baja!!.isEnabled=true
                    baja!!.isVisible=true
                }else if(boton=="curso"){
                    inscribir!!.isEnabled=true
                    inscribir!!.isVisible=true
                }
            }
        }


    }

    private val evento = View.OnClickListener { v ->
        if(v === regresar){
            if(profesorText!=null){
                val intent = Intent(this@DetallesAsesoria, Navegacion::class.java)
                startActivity(intent)
            }else{
                val intent = Intent(this@DetallesAsesoria, Solicitar::class.java)
                startActivity(intent)
            }
        }
        if(v === baja){
            if(profesorText!=null){
                val baja = AltaBaja(idAsesoria = idAsesoria!!, idUsuario = Sesion.usuario)

                RetrofitClient.instance.bajaAsesoria(baja).enqueue(object : retrofit2.Callback<AltaBaja> {
                    override fun onResponse(call: retrofit2.Call<AltaBaja>, response: retrofit2.Response<AltaBaja>) {
                        if (response.isSuccessful) {
                            email.bajaAsesoria(Sesion.usuario.toString(), materia = materiaText!!)
                            val builder = AlertDialog.Builder(this@DetallesAsesoria)
                            builder.setTitle("Excelente!")
                            builder.setMessage("Te has dado de baja de este curso")
                            builder.setPositiveButton("Aceptar") { dialog, _ ->
                                val intent = Intent(this@DetallesAsesoria, Navegacion::class.java)
                                startActivity(intent)
                            }
                            builder.setCancelable(false)
                            builder.show()
                            return
                        }
                    }
                    override fun onFailure(call: retrofit2.Call<AltaBaja>, t: Throwable) {
                        Log.e("API", "Fallo: ${t.message}")
                    }
                })
            }else{
                val cancelar = AsesoriaCancelada(idAsesoria = idAsesoria!!)
                Log.d("TAG", cancelar.toString())

                RetrofitClient.instance.cancelarAsesoria(cancelar).enqueue(object : retrofit2.Callback<AsesoriaCancelada> {
                    override fun onResponse(call: retrofit2.Call<AsesoriaCancelada>, response: retrofit2.Response<AsesoriaCancelada>) {
                        if (response.isSuccessful) {
                            email.cancelarSolicitud(Sesion.usuario.toString(), materia = materiaText!!)
                            val builder = AlertDialog.Builder(this@DetallesAsesoria)
                            builder.setTitle("Excelente!")
                            builder.setMessage("Has eliminado tu solicitud de este curso")
                            builder.setPositiveButton("Aceptar") { dialog, _ ->
                                val intent = Intent(this@DetallesAsesoria, Solicitar::class.java)
                                startActivity(intent)
                            }
                            builder.setCancelable(false)
                            builder.show()
                            return
                        }
                    }
                    override fun onFailure(call: retrofit2.Call<AsesoriaCancelada>, t: Throwable) {
                        Log.e("API", "Fallo: ${t.message}")
                    }
                })
            }

        }
        if(v === inscribir){
            val alta = AltaBaja(idAsesoria = idAsesoria!!, idUsuario = Sesion.usuario)
            Log.d("ALTA", alta.toString())

            RetrofitClient.instance.altaAsesoria(alta).enqueue(object : retrofit2.Callback<AltaBaja> {
                override fun onResponse(call: retrofit2.Call<AltaBaja>, response: retrofit2.Response<AltaBaja>) {
                    if (response.isSuccessful) {
                        email.inscrito(id = Sesion.usuario.toString(), materia = materiaText!!, profesor = profesorText!!, fecha = diaInicioText!!,
                            hora = horaText!!, dias = diasText!!, lugar = lugarText!!)
                        val builder = AlertDialog.Builder(this@DetallesAsesoria)
                        builder.setTitle("Excelente!")
                        builder.setMessage("Te has inscrito en este curso")
                        builder.setPositiveButton("Aceptar") { dialog, _ ->
                            val intent = Intent(this@DetallesAsesoria, Navegacion::class.java)
                            startActivity(intent)
                        }
                        builder.setCancelable(false)
                        builder.show()
                        return
                    }
                }
                override fun onFailure(call: retrofit2.Call<AltaBaja>, t: Throwable) {
                    Log.e("API", "Fallo: ${t.message}")
                }
            })
        }
    }
}