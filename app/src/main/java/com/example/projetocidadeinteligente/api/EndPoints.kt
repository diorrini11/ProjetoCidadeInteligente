package com.example.projetocidadeinteligente.api

import retrofit2.Call
import retrofit2.http.*

interface EndPoints
{
    @GET("/api/utilizadorWhereNI/{nome}/{password}")
    fun getUtilizador(@Path("nome") nome: String, @Path("password") password: String): Call<Utilizador>

    @GET("/api/pontoAll")
    fun getAllPontos(): Call<List<Ponto>>
}