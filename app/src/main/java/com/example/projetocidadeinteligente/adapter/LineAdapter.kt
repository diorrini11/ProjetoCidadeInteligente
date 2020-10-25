package com.example.projetocidadeinteligente.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projetocidadeinteligente.R
import com.example.projetocidadeinteligente.dataclasses.Nota
import kotlinx.android.synthetic.main.recyclerline.view.*

class LineAdapter(val list: ArrayList<Nota>):RecyclerView.Adapter<LineViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LineViewHolder {

        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.recyclerline, parent, false);
        return LineViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LineViewHolder, position: Int) {
        val currentNote = list[position]

        holder.titulo.text = currentNote.titulo
        holder.descricao.text = currentNote.descricao
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class LineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
{
    val titulo = itemView.titulo
    val descricao = itemView.descricao
}