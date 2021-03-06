package com.example.projetocidadeinteligente.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nota_table")

class Nota(
    // Int? = null so when creating instance id has not to be passed as argument
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    @ColumnInfo(name = "titulo") var titulo: String,
    @ColumnInfo(name = "descricao") var descricao: String
)