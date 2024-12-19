package com.camc.proyecto_redesiii

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.json.JSONObject
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey


class Registro : AppCompatActivity(){
    var eleccionIniciar: Button? = null
    var eleccionRegistrar: Button? = null

    var layoutIniciar: LinearLayout? = null
    var layoutRegistrar: LinearLayout? = null

    var iniciarSesion: Button? = null
    var registrarme: Button? = null

    val datosSpinner = mutableListOf<String>()
    var Carreras = mutableListOf<Carreras>()
    var spinnerCarrera: Spinner? = null

    //Input
    var nombreEdit: EditText? = null
    var apellidosEdit: EditText? = null
    var idEdit: EditText? = null
    var idEditIniciar: EditText? = null
    var contraEditIniciar: EditText? = null
    var contraEdit: EditText? = null

    var nombre : String? = null
    var apellidos : String? = null
    var idSesion : String? = null
    var ocupacion : String? = null
    var carrera : String? = null
    var idCarrera : Int? = null
    var contraSesion : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.registro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        eleccionIniciar=findViewById(R.id.eleccionIniciar)
        eleccionRegistrar=findViewById(R.id.eleccionRegistrar)
        llenamosSpinner()
        asignarEdits()

        layoutIniciar=findViewById(R.id.layoutIniciar)
        eleccionIniciar!!.setBackgroundColor(ContextCompat.getColor(this, R.color.verde))
        eleccionIniciar!!.setTextColor(ContextCompat.getColor(this, R.color.white))
        eleccionRegistrar!!.setBackgroundColor(ContextCompat.getColor(this, R.color.beige))
        eleccionRegistrar!!.setTextColor(ContextCompat.getColor(this, R.color.black))

        layoutRegistrar=findViewById(R.id.layoutRegistrarme)
        layoutRegistrar!!.isEnabled=false
        layoutRegistrar!!.isVisible=false


        eleccionIniciar!!.setOnClickListener{
            layoutIniciar!!.isEnabled=true
            layoutIniciar!!.isVisible=true
            layoutRegistrar!!.isEnabled=false
            layoutRegistrar!!.isVisible=false
            eleccionIniciar!!.setBackgroundColor(ContextCompat.getColor(this, R.color.verde))
            eleccionIniciar!!.setTextColor(ContextCompat.getColor(this, R.color.white))
            eleccionRegistrar!!.setBackgroundColor(ContextCompat.getColor(this, R.color.beige))
            eleccionRegistrar!!.setTextColor(ContextCompat.getColor(this, R.color.black))
        }

        eleccionRegistrar!!.setOnClickListener{
            layoutRegistrar!!.isEnabled=true
            layoutRegistrar!!.isVisible=true
            layoutIniciar!!.isEnabled=false
            layoutIniciar!!.isVisible=false
            eleccionRegistrar!!.setBackgroundColor(ContextCompat.getColor(this, R.color.verde))
            eleccionRegistrar!!.setTextColor(ContextCompat.getColor(this, R.color.white))
            eleccionIniciar!!.setBackgroundColor(ContextCompat.getColor(this, R.color.beige))
            eleccionIniciar!!.setTextColor(ContextCompat.getColor(this, R.color.black))
        }

        iniciarSesion=findViewById(R.id.iniciar)
        iniciarSesion!!.setOnClickListener(evento)
        registrarme=findViewById(R.id.registrar)
        registrarme!!.setOnClickListener(evento)
    }

    fun llenamosSpinner(){
        RetrofitClient.instance.getCarreras().enqueue(object : retrofit2.Callback<List<Carreras>> {
            override fun onResponse(call: retrofit2.Call<List<Carreras>>, response: retrofit2.Response<List<Carreras>>) {
                if (response.isSuccessful) {
                    val carreras = response.body()
                    for(carrera in carreras!!){
                        datosSpinner.add(carrera.nombre)
                        Carreras.add(Carreras(carrera.id, carrera.nombre))
                    }
                    Log.d("SPINNER", datosSpinner.toString())
                    // Configurar el adaptador después de llenar los datos
                    val adaptadorSpinner = ArrayAdapter(
                        this@Registro,
                        android.R.layout.simple_spinner_item,
                        datosSpinner
                    )
                    adaptadorSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    spinnerCarrera = findViewById(R.id.carrera)
                    spinnerCarrera!!.adapter = adaptadorSpinner
                }
            }
            override fun onFailure(call: retrofit2.Call<List<Carreras>>, t: Throwable) {
                //Log.e("API", "Fallo: ${t.message}")
            }
        })
        /*
        val adaptadorSpinner = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, datosSpinner)
        spinnerCarrera=findViewById(R.id.carrera)
        spinnerCarrera!!.adapter = adaptadorSpinner
        spinnerCarrera!!.setSelection(datosSpinner.indexOf("ING. EN SISTEMAS COMPUTACIONALES"))
         */

        var key = Encriptacion.generarLlave()
        var string = Encriptacion.claveToString(key)
        Log.d("TAG", string)
    }

    fun asignarEdits(){
        nombreEdit=findViewById(R.id.nombres)
        apellidosEdit=findViewById(R.id.apellidos)
        idEdit=findViewById(R.id.id_alumno)
        idEditIniciar=findViewById(R.id.usuarioIniciarSesion)
        spinnerCarrera=findViewById(R.id.carrera)
        contraEdit=findViewById(R.id.contraRegistro)
        contraEditIniciar=findViewById(R.id.contraIniciarSesion)
    }


    private val evento = View.OnClickListener { v ->
        if(v === iniciarSesion){
            idSesion=idEditIniciar!!.text.toString()
            contraSesion=contraEditIniciar!!.text.toString()
            Log.d("ENTRO", "YA")
            RetrofitClient.instance.getUsuarios().enqueue(object : retrofit2.Callback<List<Usuarios>> {
                override fun onResponse(call: retrofit2.Call<List<Usuarios>>, response: retrofit2.Response<List<Usuarios>>) {
                    if (response.isSuccessful) {
                        val usuarios = response.body()
                        Log.d("ENTRO", "SI")

                        var usuarioEncontrado = false
                        var contraseñaCorrecta = false
                        var ocupacion: String = ""
                        var tutor = false

                        // Verificar usuario y contraseña
                        for (usuario in usuarios!!) {
                            Log.d("USUARIOS", usuario.toString())
                            if (usuario.id == idSesion!!.toInt()) {
                                usuarioEncontrado = true
                                ocupacion=usuario.ocupacion
                                if(ocupacion=="tutor"){
                                    tutor = true
                                }else{
                                    if (usuario.password == contraSesion) {
                                        /*if(ocupacion=="tutor"){
                                            tutor = true
                                        }else{

                                        }*/
                                        contraseñaCorrecta = true
                                        Sesion.usuario = idSesion!!.toInt()
                                        val intent = Intent(this@Registro, Navegacion::class.java)
                                        startActivity(intent)
                                        return
                                    }
                                }

                            }
                        }

                        //Si el usuario ingresado es tutor
                        if(tutor && ocupacion=="tutor"){
                            val builder = AlertDialog.Builder(this@Registro)
                            builder.setTitle("Ups")
                            builder.setMessage("Para la administración de tutores deberás de entrar a nuestro sitio web")
                            builder.setPositiveButton("Aceptar") { dialog, _ ->
                                val intent = Intent(this@Registro, Registro::class.java)
                                startActivity(intent)
                            }
                            builder.setCancelable(false)
                            builder.show()
                        }

                        // Si se encuentra el usuario, pero la contraseña es incorrecta
                        if (usuarioEncontrado && !contraseñaCorrecta && ocupacion=="alumno") {
                            val builder = AlertDialog.Builder(this@Registro)
                            builder.setTitle("Ups")
                            builder.setMessage("Parece que la contraseña que ingresaste es incorrecta, inténtalo de nuevo")
                            builder.setPositiveButton("Aceptar") { dialog, _ ->
                                val intent = Intent(this@Registro, Registro::class.java)
                                startActivity(intent)
                            }
                            builder.setCancelable(false)
                            builder.show()
                        }

                        // Si no se encontró ningún usuario con el ID proporcionado
                        if (!usuarioEncontrado) {
                            val builder = AlertDialog.Builder(this@Registro)
                            builder.setTitle("Ups")
                            builder.setMessage("El usuario que ingresaste no existe")
                            builder.setPositiveButton("Aceptar") { dialog, _ ->
                                val intent = Intent(this@Registro, Registro::class.java)
                                startActivity(intent)
                            }
                            builder.setCancelable(false)
                            builder.show()
                        }
                    }
                }

                override fun onFailure(call: retrofit2.Call<List<Usuarios>>, t: Throwable) {
                    Log.e("API", "Fallo: ${t.message}")
                }
            })

        }

        if(v === registrarme){
            nombre=nombreEdit!!.text.toString()
            apellidos=apellidosEdit!!.text.toString()
            idSesion=idEdit!!.text.toString()
            carrera=spinnerCarrera!!.selectedItem.toString()
            ocupacion="alumno"
            contraSesion=contraEdit!!.text.toString()
            for(carreras in Carreras){
                if(carrera==carreras.nombre){
                    idCarrera=carreras.id
                    break
                }
            }
            val usuario = Usuarios(id = idSesion!!.toInt(), id_carrera = idCarrera!!, nombre = nombre!!, apellidos = apellidos!!, ocupacion = ocupacion!!, password = contraSesion!!)
            RetrofitClient.instance.crearUsuario(usuario).enqueue(object : retrofit2.Callback<Usuarios> {
                override fun onResponse(call: retrofit2.Call<Usuarios>, response: retrofit2.Response<Usuarios>) {
                    if (response.isSuccessful) {
                        Sesion.usuario=idSesion!!.toInt()
                        val intent = Intent(this@Registro, Navegacion::class.java)
                        startActivity(intent)
                    }
                }
                override fun onFailure(call: retrofit2.Call<Usuarios>, t: Throwable) {
                    Log.e("API", "Fallo: ${t.message}")
                }
            })

        }
    }
}