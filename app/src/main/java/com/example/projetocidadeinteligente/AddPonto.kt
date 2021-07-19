package com.example.projetocidadeinteligente

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.projetocidadeinteligente.api.EndPoints
import com.example.projetocidadeinteligente.api.Ponto
import com.example.projetocidadeinteligente.api.ServiceBuilder
import com.example.projetocidadeinteligente.api.Tipo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddPonto : AppCompatActivity() {

    private lateinit var tituloText: EditText
    private lateinit var LatLongText: EditText
    private lateinit var tipoSpinner: Spinner
    private lateinit var tipos: List<Tipo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ponto)

        tituloText = findViewById(R.id.tituloEdit)
        LatLongText = findViewById(R.id.latLongEdit)
        tipoSpinner = findViewById(R.id.tipoSpinner)

        val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter(
            this, android.R.layout.simple_spinner_dropdown_item)


        LatLongText.setText(intent.getStringExtra("LATLONG"))

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getAllTipos()

        call.enqueue(object : Callback<List<Tipo>> {
            override fun onResponse(call: Call<List<Tipo>>, response: Response<List<Tipo>>) {
                if (response.isSuccessful) {
                    tipos = response.body()!!

                    for (tipo in tipos) {
                        spinnerAdapter.add(tipo.nome)
                    }
                    tipoSpinner.setAdapter(spinnerAdapter)
                }
            }
            override fun onFailure(call: Call<List<Tipo>>, t: Throwable)
            {
                Toast.makeText(this@AddPonto, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

        val button = findViewById<Button>(R.id.saveButton)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(tituloText.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                replyIntent.putExtra(ID, tituloText.text.toString())
                replyIntent.putExtra(LATLONG, LatLongText.text.toString())
                replyIntent.putExtra(TIPO, (tipoSpinner.selectedItemPosition + 1).toString())
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val ID = "com.example.android.titulo"
        const val LATLONG = "com.example.android.latLong"
        const val TIPO = "com.example.android.tipo"
    }
}