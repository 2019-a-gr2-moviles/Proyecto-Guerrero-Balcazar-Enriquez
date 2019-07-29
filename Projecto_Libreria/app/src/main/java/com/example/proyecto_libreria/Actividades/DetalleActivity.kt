package com.example.proyecto_libreria.Actividades

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beust.klaxon.Klaxon
import com.example.proyecto_libreria.Adaptadores.AdaptadorDetalle
import com.example.proyecto_libreria.Adaptadores.AdaptadorLibro
import com.example.proyecto_libreria.Adaptadores.AdaptadorLibroDetalle
import com.example.proyecto_libreria.Clases.Detalle
import com.example.proyecto_libreria.Clases.Factura
import com.example.proyecto_libreria.Clases.Libro
import com.example.proyecto_libreria.R
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification
import kotlinx.android.synthetic.main.activity_detalle.*
import kotlinx.android.synthetic.main.activity_facturas_cliente.*
import kotlinx.android.synthetic.main.activity_gestion_libros.*

class DetalleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle)
        var idFactura = intent.getIntExtra("idFactura", -1)
        cargarDetalles(idFactura)
    }

    fun cargarDetalles(idFactura: Int) {
        val url = "${MainActivity.url}/detalle?idFactura=${idFactura}"
        var lista = listOf<Factura>()
        url
            .httpGet()
            .responseString { request, response, result ->
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                        Log.i("http", "Error: ${ex.message}")
                    }
                    is Result.Success -> {
                        val data = result.get()
                        Log.i("http", "Data: ${data}")

                        var detalleParseada = Klaxon().parseArray<Detalle>(data)

                        runOnUiThread {
                            iniciarLibros(detalleParseada!!, this, rv_libros_detalle)
                        }
                        val urlFactura = "${MainActivity.url}/factura?id=${idFactura}"
                        urlFactura.httpGet()
                            .responseString { request, response, result ->
                                when (result) {
                                    is Result.Failure -> {
                                        val ex = result.getException()
                                        Log.i("http", "Error: ${ex.message}")
                                    }
                                    is Result.Success -> {

                                        val data = result.get()
                                        Log.i("http", "Data: ${data}")

                                        var facturaParseada = Klaxon().parseArray<Factura>(data)
                                        runOnUiThread {
                                            txt_fecha.text= facturaParseada!![0].fecha.toString()
                                            txt_total.text= facturaParseada!![0].total.toString()
                                        }


                                    }
                                }
                            }



                    }
                }
            }


    }

    fun iniciarLibros(
        lista: List<Detalle>, actividad: DetalleActivity, recycler_view: RecyclerView
    ) {
        val adaptadorPlato =
            AdaptadorLibroDetalle(lista, actividad, recycler_view)
        rv_libros_detalle.adapter = adaptadorPlato
        rv_libros_detalle.itemAnimator = DefaultItemAnimator()
        //Nos falta el layout manager
        rv_libros_detalle.layoutManager = LinearLayoutManager(this)
        adaptadorPlato.notifyDataSetChanged()
    }

    fun dialogLibro(libro: Libro) {

        val builder = AlertDialog.Builder(this)

        val inflater = layoutInflater
        builder.setTitle("Información")
        val dialogLayout = inflater.inflate(R.layout.dialog_libro, null)
        val titulo = dialogLayout.findViewById<EditText>(R.id.dialog_libro_inputTitulo)
        val autor = dialogLayout.findViewById<EditText>(R.id.dialog_libro_inputAutor)
        val edicion = dialogLayout.findViewById<EditText>(R.id.dialog_libro_inputEdicion)
        val editorial = dialogLayout.findViewById<EditText>(R.id.dialog_libro_inputEditorial)
        val precio = dialogLayout.findViewById<EditText>(R.id.dialog_libro_inputPrecio)
        val estado = dialogLayout.findViewById<Switch>(R.id.dialog_libro_swithEstado)
        val isbn = dialogLayout.findViewById<EditText>(R.id.dialog_libro_inputISBN)
        val idioma = dialogLayout.findViewById<TextView>(R.id.dialog_libro_inputIdioma)
        idioma.setText(libro.idioma)
        titulo.setText(libro.titulo)
        autor.setText(libro.autor)
        edicion.setText(libro.edicion.toString())
        editorial.setText(libro.editorial)
        precio.setText(libro.precio.toString())
        isbn.setText(libro.isbn)
        idioma.setText(libro.idioma)
        if (libro.estado == "disponible") {
            estado.isChecked = true
        } else {
            estado.isChecked = false
        }

        builder.setPositiveButton("LISTO") { dialogInterface, i ->

        }
        builder.setView(dialogLayout)
        builder.show()
    }
}
