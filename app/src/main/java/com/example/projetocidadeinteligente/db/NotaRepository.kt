package com.example.projetocidadeinteligente.db

import androidx.lifecycle.LiveData
import com.example.projetocidadeinteligente.dao.NotaDao
import com.example.projetocidadeinteligente.entities.Nota

class NotaRepository(private val notaDao: NotaDao) {
    val allNotas: LiveData<List<Nota>> = notaDao.getAllNotas()

    suspend fun insert(nota: Nota) {
        notaDao.insert(nota)
    }

    suspend fun deleteAll(){
        notaDao.deleteAll()
    }

    suspend fun deleteByID(id: Int){
        notaDao.deleteByID(id)
    }

    suspend fun updateNota(nota: Nota) {
        notaDao.updateNota(nota)
    }

    suspend fun updateDescricao(id: Int, descricao: String){
        notaDao.updateDescricao(id, descricao)
    }
}