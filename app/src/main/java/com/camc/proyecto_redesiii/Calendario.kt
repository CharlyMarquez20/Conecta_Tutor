package com.camc.proyecto_redesiii

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.DateTime
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.CalendarScopes
import com.google.api.services.calendar.model.Event
import com.google.api.services.calendar.model.EventDateTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Calendario : AppCompatActivity() {
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var signInButton: SignInButton
    private lateinit var calendarService: Calendar

    var asignamos = false

    companion object {
        private const val RC_SIGN_IN = 9001
        private const val SCOPE = CalendarScopes.CALENDAR
        private const val TAG = "Calendario"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.calendario)
        // Configurar el padding de la vista principal
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar la configuración de Google Sign In
        inicializar()

        // Verificar si ya hay una sesión activa
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            Log.d(TAG, "Usuario ya está conectado")
            setupCalendarApi(account)
        }
    }

    private fun inicializar() {
        // Configurar Google Sign In con los scopes necesarios
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(Scope(SCOPE))
            .build()

        // Inicializar el cliente de Google Sign In
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Configurar el botón de inicio de sesión
        signInButton = findViewById<SignInButton>(R.id.signInButton).apply {
            setSize(SignInButton.SIZE_STANDARD)
            setOnClickListener {
                startActivityForResult(googleSignInClient.signInIntent, RC_SIGN_IN)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                handleSignInResult(task)
            } catch (e: Exception) {
                Log.e(TAG, "Error en onActivityResult: ${e.message}")
                //Toast.makeText(this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            Log.d(TAG, "Inicio de sesión exitoso")
            //Toast.makeText(this, "Sesión iniciada correctamente", Toast.LENGTH_SHORT).show()

            // Configurar el servicio de calendario
            setupCalendarApi(account)

            var entro = false

            RetrofitClient.instance.getAsesoriasUsuarios(Sesion.usuario).enqueue(object : retrofit2.Callback<List<Asesorias>> {
                override fun onResponse(call: retrofit2.Call<List<Asesorias>>, response: retrofit2.Response<List<Asesorias>>) {
                    if (response.isSuccessful) {
                        entro=true
                        val asesorias = response.body()
                        Log.d("ASESORIAS", asesorias!!.size.toString())
                        if(asesorias.size!=0){
                            // Crear evento de prueba
                            for(asesoria in asesorias){
                                crearEventoPrueba(asesoria.materia, asesoria.docente,
                                    asesoria.fecha_inicio, asesoria.horario_inicio, asesoria.horario_fin,
                                    asesoria.dias, asesoria.lugar)
                            }
                            asignamos=true
                            if(asignamos){
                                val builder = AlertDialog.Builder(this@Calendario)
                                builder.setTitle("Excelente!!")
                                builder.setMessage("Los eventos han sido añadidos con éxito")
                                builder.setPositiveButton("Aceptar") { dialog, _ ->
                                    val intent = Intent(this@Calendario, Navegacion::class.java)
                                    startActivity(intent)
                                }
                                builder.setCancelable(false)
                                builder.show()
                            }
                        }else{
                            sinAsesorias()
                        }
                    }else{
                        sinAsesorias()
                    }
                }
                override fun onFailure(call: retrofit2.Call<List<Asesorias>>, t: Throwable) {
                    Log.e("API", "Fallo: ${t.message}")
                }
            })

        } catch (e: ApiException) {
            Log.e(TAG, "Error de inicio de sesión: code=${e.statusCode}")
            //Toast.makeText(this, "Error de inicio de sesión: ${e.statusCode}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sinAsesorias(){
        val builder = AlertDialog.Builder(this@Calendario)
        builder.setTitle("Ups")
        builder.setMessage("No tienes asesorias para crear eventos")
        builder.setPositiveButton("Aceptar") { dialog, _ ->
            val intent = Intent(this@Calendario, Navegacion::class.java)
            startActivity(intent)
        }
        builder.setCancelable(false)
        builder.show()
    }

    private fun setupCalendarApi(account: GoogleSignInAccount) {
        try {
            val credential = GoogleAccountCredential.usingOAuth2(
                applicationContext,
                listOf(SCOPE)
            ).setSelectedAccount(account.account)

            calendarService = Calendar.Builder(
                AndroidHttp.newCompatibleTransport(),
                GsonFactory.getDefaultInstance(),
                credential
            )
                .setApplicationName("ConectaTutor")
                .build()

            Log.d(TAG, "Servicio de calendario configurado correctamente")
        } catch (e: Exception) {
            Log.e(TAG, "Error al configurar el servicio de calendario: ${e.message}")
            //Toast.makeText(this, "Error al configurar el calendario", Toast.LENGTH_SHORT).show()
        }
    }

    private fun crearEventoPrueba(materia: String, profesor: String, fecha: String, inicio: String, fin: String,
                                  dias: String, lugar: String) {
        Log.d("CALENDARIO", "crearEventoPrueba: $materia, $profesor, $fecha, $inicio, $$fin, $dias, $lugar")
        createEvent(
            titulo = "Asesoría de $materia",
            descripcion = "Asesoria impartida por el profesor(a) $profesor, los días $dias en $lugar",
            //fechaInicio = "2024-12-15T10:00:00-06:00",
            //fechaFin = "2024-12-15T11:00:00-06:00",
            fechaInicio = fecha+"T"+inicio+"-06:00",
            fechaFin = fecha+"T"+fin+"-06:00",
            onSuccess = { link ->
                Log.d(TAG, "Evento creado exitosamente: $link")
                //Toast.makeText(this, "Evento creado: $link", Toast.LENGTH_LONG).show()
            },
            onError = { error ->
                Log.e(TAG, "Error al crear evento: ${error.message}")
                //Toast.makeText(this, "Error al crear evento: ${error.message}", Toast.LENGTH_LONG).show()
                error.printStackTrace()
            }
        )
    }

    private fun createEvent(
        titulo: String,
        descripcion: String,
        fechaInicio: String,
        fechaFin: String,
        zonaHoraria: String = "America/Los_Angeles",
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val event = Event().apply {
                    summary = titulo
                    this.description = descripcion

                    start = EventDateTime().apply {
                        dateTime = DateTime(fechaInicio)
                        timeZone = zonaHoraria
                    }

                    end = EventDateTime().apply {
                        dateTime = DateTime(fechaFin)
                        timeZone = zonaHoraria
                    }
                }

                val createdEvent = calendarService.events().insert("primary", event).execute()

                withContext(Dispatchers.Main) {
                    onSuccess(createdEvent.htmlLink)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError(e)
                }
            }
        }
    }
}