package com.camc.proyecto_redesiii

import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface APIService {
    //DEFINICION DE SERVICIOS PARA USUARIOS
    @GET("usuarios")
    fun getUsuarios(): Call<List<Usuarios>>

    @GET("usr/{id}")
    fun getUsuario(@Path("id") id: Int): Call<Usuarios>

    @POST("usuarios")
    fun crearUsuario(@Body usuario: Usuarios): Call<Usuarios>

    @POST("baja") //Mandar id de asesoria y id de alumno
    fun bajaAsesoria(@Body datos: AltaBaja): Call<AltaBaja>

    @POST("alta") //Mandar id de asesoria y id de alumno
    fun altaAsesoria(@Body datos: AltaBaja): Call<AltaBaja>

    @PUT("usuarios")
    fun actualizarUsuario(@Body usuario: Usuarios): Call<Usuarios>


    //DEFINICION DE SERVICIOS PARA ASESORIAS
    @GET("asesorias")
    fun getAsesorias(): Call<List<Asesorias>>

    @GET("asesorias/alumno/{id}")
    fun getAsesoriasUsuarios(@Path("id") id: Int): Call<List<Asesorias>>

    @GET("asesorias/{id}")
    fun getCursos(@Path("id") id: Int): Call<List<Asesorias>>

    @POST("asesoria")
    fun solicitarAsesoria(@Body asesoria: SolicitarAsesoria): Call<SolicitarAsesoria>

    @GET("asesoriasSolicitadas/{id}")
    fun getAsesoriasSolicitadas(@Path("id") id: Int): Call<List<Asesorias>>

    @PUT("cancelarasesoria")
    fun cancelarAsesoria(@Body id: AsesoriaCancelada): Call<AsesoriaCancelada>

    //DEFINICION DE USUARIOS PARA MATERIAS
    @GET("materias/{id}")
    fun getMaterias(@Path("id") id: Int): Call<List<Materias>>

    //DEFINICION DE CARRERAS
    @GET("carreras")
    fun getCarreras(): Call<List<Carreras>>


}