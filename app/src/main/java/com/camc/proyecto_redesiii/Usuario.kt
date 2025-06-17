package com.camc.proyecto_redesiii

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import retrofit2.Response

class Usuario : AppCompatActivity()  {
    var editContra: EditText? = null
    var editID: EditText? = null
    var editNombre: EditText? = null
    var textContra: TextView? = null

    var btnAceptar: Button? = null
    var btnModificar: Button? = null
    var btnRegresar: Button? = null

    var nombreCompleto: String? = null
    var nombre: String? = null
    var apellidos: String? = null
    var idCarrera: Int? = null
    var ocupacion: String? = null

    //Clase de SendGrid
    val email = SendGrid()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.usuario)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        textContra=findViewById(R.id.textContra)
        editContra=findViewById(R.id.contra)
        editID=findViewById(R.id.id_alumno)
        editNombre=findViewById(R.id.nombre)
        btnModificar=findViewById(R.id.modificar)
        btnAceptar=findViewById(R.id.aceptar)
        btnModificar!!.setOnClickListener(evento)
        btnAceptar!!.setOnClickListener(evento)

        btnRegresar=findViewById(R.id.btnRegresar)
        btnRegresar!!.setOnClickListener(evento)

        RetrofitClient.instance.getUsuario(Sesion.usuario).enqueue(object : retrofit2.Callback<UsuariosGet> {
            override fun onResponse(call: retrofit2.Call<UsuariosGet>, response: retrofit2.Response<UsuariosGet>) {
                if (response.isSuccessful) {
                    val usuario = response.body()
                    Log.d("TAG", "onResponse: $usuario")
                    nombreCompleto=usuario!!.Nombre+" "+ usuario.apellidos
                    editNombre!!.hint=nombreCompleto!!
                    editNombre!!.isEnabled=false

                    nombre=usuario.Nombre
                    apellidos=usuario.apellidos
                    //idCarrera=usuario.idCarrera
                    ocupacion=usuario.ocupacion
                }
            }
            override fun onFailure(call: retrofit2.Call<UsuariosGet>, t: Throwable) {
                Log.e("API", "Fallo: ${t.message}")
            }
        })

        editID!!.hint=Sesion.usuario.toString()
        editID!!.isEnabled=false
        editNombre!!.isEnabled=false

        if(!Sesion.modificarContra){
            textContra!!.isVisible=false
            editContra!!.isVisible=false
            editContra!!.isEnabled=false
            btnModificar!!.isEnabled=true
            btnModificar!!.isVisible=true
            btnAceptar!!.isEnabled=false
            btnAceptar!!.isVisible=false
        }else{
            textContra!!.isVisible=true
            editContra!!.isVisible=true
            editContra!!.isEnabled=true
            editNombre!!.isEnabled=false
            btnModificar!!.isEnabled=false
            btnModificar!!.isVisible=false
            btnAceptar!!.isEnabled=true
            btnAceptar!!.isVisible=true
            btnRegresar!!.isEnabled=false
            btnRegresar!!.isVisible=false
        }

    }

    private val evento = View.OnClickListener { v ->
        if(v === btnModificar){
            Sesion.modificarContra=true
            val intent = Intent(this@Usuario, Usuario::class.java)
            startActivity(intent)
        }
        if(v === btnAceptar){
            Sesion.modificarContra=false
            val contra = editContra!!.text.toString()

            val usuario = UsuarioUpdate(id = Sesion.usuario, password = contra, ocupacion = ocupacion!!)
            RetrofitClient.instance.actualizarUsuario(usuario).enqueue(object : retrofit2.Callback<UsuarioUpdate> {
                override fun onResponse(call: retrofit2.Call<UsuarioUpdate>, response: retrofit2.Response<UsuarioUpdate>) {
                    if (response.isSuccessful) {
                        email.cambiarContra(Sesion.usuario.toString())
                        val builder = AlertDialog.Builder(this@Usuario)
                        builder.setTitle("Excelente!")
                        builder.setMessage("Tu contraseña ha sido actualizada")
                        builder.setPositiveButton("Aceptar") { dialog, _ ->
                            val intent = Intent(this@Usuario, Navegacion::class.java)
                            startActivity(intent)
                        }
                        builder.setCancelable(false)
                        builder.show()
                        return
                    }
                }
                override fun onFailure(call: retrofit2.Call<UsuarioUpdate>, t: Throwable) {
                    Log.e("API", "Fallo: ${t.message}")
                }
            })

            /*val intent = Intent(this@Usuario, Usuario::class.java)
            startActivity(intent)*/
        }
        if(v === btnRegresar){
            val intent = Intent(this@Usuario, Navegacion::class.java)
            startActivity(intent)
        }
    }

}