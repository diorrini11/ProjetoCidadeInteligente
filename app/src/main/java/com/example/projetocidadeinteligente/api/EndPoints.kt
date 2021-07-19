package com.example.projetocidadeinteligente.api

import retrofit2.Call
import retrofit2.http.*

interface EndPoints
{
    @GET("/api/utilizadorWhereNI/{nome}/{password}")
    fun getUtilizador(@Path("nome") nome: String, @Path("password") password: String): Call<Utilizador>

    @GET("/api/pontoAll")
    fun getAllPontos(): Call<List<Ponto>>

    @GET("/api/tipoAll")
    fun getAllTipos(): Call<List<Tipo>>

    @FormUrlEncoded
    @POST("/api/addPonto")
    fun addPonto(@Field("titulo") titulo: String, @Field("lati") lat: String,
                @Field("longi") long: String, @Field("tipo_id") tipo: String,
                @Field("utilizador_id") id: Int): Call<Ponto>
}