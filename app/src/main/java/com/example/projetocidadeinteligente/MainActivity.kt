package com.example.projetocidadeinteligente

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projetocidadeinteligente.adapters.NotaAdapter
import com.example.projetocidadeinteligente.entities.Nota
import com.example.projetocidadeinteligente.viewModel.NotaViewModel

class MainActivity : AppCompatActivity(), CellClickListener
{
    private lateinit var notaViewModel: NotaViewModel
    private val newNotaActivityRequestCode = 1
    private val editNotaActivityRequestCode = 2
    private var adapter: NotaAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // recycler view
        val recyclerView = findViewById<RecyclerView>(R.id.listaNotas)
        adapter = NotaAdapter(this, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // view model
        notaViewModel = ViewModelProvider(this).get(NotaViewModel::class.java)
        notaViewModel.allNotas.observe(this, Observer { notas ->
        // Update the cached copy of the words in the adapter.
            notas?.let { adapter!!.setNotas(it) }
        })

        val sharedPref: SharedPreferences = getSharedPreferences(
            getString(R.string.sharedPref), Context.MODE_PRIVATE)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean
    {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK)
        {
            if(requestCode == newNotaActivityRequestCode)
            {
                val pTitulo = data?.getStringExtra(AddNota.EXTRA_REPLY_TITULO)
                val pDescricao = data?.getStringExtra(AddNota.EXTRA_REPLY_DESCRICAO)

                if (pTitulo!= null && pDescricao != null) {
                    val nota = Nota(titulo = pTitulo, descricao = pDescricao)
                    notaViewModel.insert(nota)
                }
            }
            else
            if(requestCode == editNotaActivityRequestCode)
            {
                val pTitulo = data?.getStringExtra(EditNota.EXTRA_REPLY_TITULO)
                val pDescricao = data?.getStringExtra(EditNota.EXTRA_REPLY_DESCRICAO)
                if (pTitulo!= null && pDescricao != null) {
                    val nota = Nota(id = adapter!!.selectedNota?.id, titulo = pTitulo, descricao =  pDescricao)
                    notaViewModel.updateNota(nota)
                }
            }
        }
        else
        {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        return when (item.itemId)
        {
            R.id.optionAdd ->
            {
                val intent = Intent(this@MainActivity, AddNota::class.java)
                startActivityForResult(intent, newNotaActivityRequestCode)
                true
            }
            R.id.optionRemove ->
            {
                adapter!!.selectedNota?.id?.let { notaViewModel.deleteByID(it) }
                true
            }
            R.id.optionEdit ->
            {
                val titulo: String = adapter!!.selectedNota?.titulo.toString()
                val descricao: String = adapter!!.selectedNota?.descricao.toString()
                val intent = Intent(this@MainActivity, EditNota::class.java)
                intent.putExtra("titulo", titulo)
                intent.putExtra("descricao", descricao)
                startActivityForResult(intent, editNotaActivityRequestCode)
                true
            }
            R.id.optionRemoveAll ->
            {
                notaViewModel.deleteAll()
                true
            }
            /*
            R.id.optionSelectM ->
            {
                true
            }
            */
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCellClickListener(data: Nota) {
        //Toast.makeText(this, data.id.toString(), Toast.LENGTH_SHORT).show()
    }
}