package com.example.projetocidadeinteligente.api

data class Ponto(
    val id: Int,
    val titulo: String,
    val lat: String,
    val long: String,
    val id_tipo: Int,
    val id_ponto: Int
)
