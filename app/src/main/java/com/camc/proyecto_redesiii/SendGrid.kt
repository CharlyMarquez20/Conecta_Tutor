package com.camc.proyecto_redesiii

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject

class SendGrid {
    var apiKey: String = ""

    suspend fun api(): String {
        return try {
            val response = RetrofitClient.instance.getApiKey()
            response.apiKey
        } catch (e: Exception) {
            Log.e("API", "Error al obtener API key: ${e.message}")
            ""
        }
    }

    fun inscrito(id: String, materia: String, profesor: String, fecha: String, hora: String,
                  dias: String, lugar: String) {
        CoroutineScope(Dispatchers.IO).launch {
            apiKey = api()
            val fromEmail = "al333812@edu.uaa.mx"
            val toEmail = "al$id@edu.uaa.mx"
            val subject = "Te has inscrito en la asesoria de $materia"
            val content = "La asesoria de $materia será impartida por el profesor(a) $profesor los días $dias con fecha de " +
                    "inicio el día $fecha a las $hora en $lugar. Éxito en tus próximas asesorias!!."

            val client = OkHttpClient()

            // Create the to recipient array
            val toArray = JSONArray().apply {
                put(JSONObject().apply {
                    put("email", toEmail)
                })
            }

            // Create the personalization array
            val personalizationsArray = JSONArray().apply {
                put(JSONObject().apply {
                    put("to", toArray)
                    put("subject", subject)
                })
            }

            // Create the content array
            val contentArray = JSONArray().apply {
                put(JSONObject().apply {
                    put("type", "text/plain")
                    put("value", content)
                })
            }

            // Build the main JSON object
            val json = JSONObject().apply {
                put("personalizations", personalizationsArray)
                put("from", JSONObject().apply { put("email", fromEmail) })
                put("content", contentArray)
            }

            val body = RequestBody.create(
                "application/json".toMediaTypeOrNull(),
                json.toString()
            )

            val request = Request.Builder()
                .url("https://api.sendgrid.com/v3/mail/send")
                .addHeader("Authorization", "Bearer $apiKey")
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build()

            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    Log.d("SendGrid", "Email sent successfully: ${response.code}")
                } else {
                    Log.e("SendGrid", "Failed to send email: ${response.code}, ${response.body?.string()}")
                }
            } catch (e: Exception) {
                Log.e("SendGrid", "Error: ${e.message}", e)
            }
        }
    }

    fun bajaAsesoria(id: String, materia: String) {
        CoroutineScope(Dispatchers.IO).launch {
            apiKey = api()
            val fromEmail = "al333812@edu.uaa.mx"
            val toEmail = "al$id@edu.uaa.mx"
            val subject = "Te has dado de baja en la asesoria de $materia"
            val content = "Esperamos que hayas disfrutado de las asesorias, no olvides que puedes darte de alta nuevamente" +
                    " en cualquier momento."

            val client = OkHttpClient()

            // Create the to recipient array
            val toArray = JSONArray().apply {
                put(JSONObject().apply {
                    put("email", toEmail)
                })
            }

            // Create the personalization array
            val personalizationsArray = JSONArray().apply {
                put(JSONObject().apply {
                    put("to", toArray)
                    put("subject", subject)
                })
            }

            // Create the content array
            val contentArray = JSONArray().apply {
                put(JSONObject().apply {
                    put("type", "text/plain")
                    put("value", content)
                })
            }

            // Build the main JSON object
            val json = JSONObject().apply {
                put("personalizations", personalizationsArray)
                put("from", JSONObject().apply { put("email", fromEmail) })
                put("content", contentArray)
            }

            val body = RequestBody.create(
                "application/json".toMediaTypeOrNull(),
                json.toString()
            )

            val request = Request.Builder()
                .url("https://api.sendgrid.com/v3/mail/send")
                .addHeader("Authorization", "Bearer $apiKey")
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build()

            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    Log.d("SendGrid", "Email sent successfully: ${response.code}")
                } else {
                    Log.e("SendGrid", "Failed to send email: ${response.code}, ${response.body?.string()}")
                }
            } catch (e: Exception) {
                Log.e("SendGrid", "Error: ${e.message}", e)
            }
        }
    }

    fun cambiarContra(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            apiKey = api()
            Log.d("PROBANDO", "cambiarContra: $apiKey")
            val fromEmail = "al333812@edu.uaa.mx"
            val toEmail = "al$id@edu.uaa.mx"
            val subject = "Contraseña actualizada"
            val content = "La contraseña de tu cuenta ha sido actualizada, si fuiste tú, ignora este email. En caso contrario," +
                    " ponte en contacto con los administradores para ver qué ocurre."

            val client = OkHttpClient()

            // Create the to recipient array
            val toArray = JSONArray().apply {
                put(JSONObject().apply {
                    put("email", toEmail)
                })
            }

            // Create the personalization array
            val personalizationsArray = JSONArray().apply {
                put(JSONObject().apply {
                    put("to", toArray)
                    put("subject", subject)
                })
            }

            // Create the content array
            val contentArray = JSONArray().apply {
                put(JSONObject().apply {
                    put("type", "text/plain")
                    put("value", content)
                })
            }

            // Build the main JSON object
            val json = JSONObject().apply {
                put("personalizations", personalizationsArray)
                put("from", JSONObject().apply { put("email", fromEmail) })
                put("content", contentArray)
            }

            val body = RequestBody.create(
                "application/json".toMediaTypeOrNull(),
                json.toString()
            )

            val request = Request.Builder()
                .url("https://api.sendgrid.com/v3/mail/send")
                .addHeader("Authorization", "Bearer $apiKey")
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build()

            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    Log.d("SendGrid", "Email sent successfully: ${response.code}")
                } else {
                    Log.e("SendGrid", "Failed to send email: ${response.code}, ${response.body?.string()}")
                }
            } catch (e: Exception) {
                Log.e("SendGrid", "Error: ${e.message}", e)
            }
        }
    }

    fun cancelarSolicitud(id: String, materia: String) {
        CoroutineScope(Dispatchers.IO).launch {
            apiKey = api()
            val fromEmail = "al333812@edu.uaa.mx"
            val toEmail = "al$id@edu.uaa.mx"
            val subject = "Cancelación de solicitud de asesoría"
            val content = "Se ha cancelado tu solicitud de asesoria para $materia. Puedes checar todos los cursos disponibles en nuestro" +
                    " sitio web o aplicación móvil."

            val client = OkHttpClient()

            // Create the to recipient array
            val toArray = JSONArray().apply {
                put(JSONObject().apply {
                    put("email", toEmail)
                })
            }

            // Create the personalization array
            val personalizationsArray = JSONArray().apply {
                put(JSONObject().apply {
                    put("to", toArray)
                    put("subject", subject)
                })
            }

            // Create the content array
            val contentArray = JSONArray().apply {
                put(JSONObject().apply {
                    put("type", "text/plain")
                    put("value", content)
                })
            }

            // Build the main JSON object
            val json = JSONObject().apply {
                put("personalizations", personalizationsArray)
                put("from", JSONObject().apply { put("email", fromEmail) })
                put("content", contentArray)
            }

            val body = RequestBody.create(
                "application/json".toMediaTypeOrNull(),
                json.toString()
            )

            val request = Request.Builder()
                .url("https://api.sendgrid.com/v3/mail/send")
                .addHeader("Authorization", "Bearer $apiKey")
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build()

            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    Log.d("SendGrid", "Email sent successfully: ${response.code}")
                } else {
                    Log.e("SendGrid", "Failed to send email: ${response.code}, ${response.body?.string()}")
                }
            } catch (e: Exception) {
                Log.e("SendGrid", "Error: ${e.message}", e)
            }
        }
    }
}