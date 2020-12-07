package com.example.projetocidadeinteligente.api

import retrofit2.Call
import retrofit2.http.*

interface EndPoints {
    @GET("/utilizadorWhereNI/{nome}/{password}")
    fun getUtilizador(@Query("nome") nome: String, @Query("password") password: String): Call<Utilizador>
}