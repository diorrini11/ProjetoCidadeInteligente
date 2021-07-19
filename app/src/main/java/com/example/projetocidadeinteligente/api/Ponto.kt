package com.example.projetocidadeinteligente.api

data class Ponto(
    val id: Int,
    val titulo: String,
    val lati: String,
    val longi: String,
    val tipo_id: Int,
    val utilizador_id: Int
)
