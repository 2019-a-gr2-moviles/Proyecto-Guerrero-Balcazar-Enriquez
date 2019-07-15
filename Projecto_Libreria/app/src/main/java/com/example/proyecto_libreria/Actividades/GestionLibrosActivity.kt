package com.example.proyecto_libreria.Actividades

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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
import com.example.proyecto_libreria.Adaptadores.AdaptadorCatalogo
import com.example.proyecto_libreria.Adaptadores.AdaptadorLibro
import com.example.proyecto_libreria.Clases.Libro
import com.example.proyecto_libreria.Clases.LibroCatalogo
import com.example.proyecto_libreria.R
import com.github.kittinunf.fuel.httpDelete
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.httpPut
import com.github.kittinunf.result.Result
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification
import kotlinx.android.synthetic.main.activity_gestion_libros.*
import kotlinx.android.synthetic.main.content_catalogo.*

class GestionLibrosActivity : AppCompatActivity() {
    companion object companionObjectlibros {
        var lista = listOf<Libro>()
    }

    private var permission = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion_libros)
        cargarLibros()

        btn_nuevoLibro.setOnClickListener {
            dialogLibro(
                Libro(
                    -1, "", "", "", 0, "", 0.0,
                    "", ""
                ), 1
            )
        }
        var toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        this.setSupportActionBar(toolbar)
        val actionBar = supportActionBar


        // Set toolbar title/app title
        actionBar!!.title = "Hello APP"

        // Set action bar/toolbar sub title
        actionBar.subtitle = "App subtitle"

        // Set action bar elevation
        actionBar.elevation = 4.0F

        // Display the app icon in action bar/toolbar
        actionBar.setDisplayShowHomeEnabled(true)

    }

    fun cargarLibros() {
        val url = "${MainActivity.url}/libro"
        var lista = listOf<LibroCatalogo>()
        var listaLibros = ArrayList<LibroCatalogo>()
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

                        var libroParseada = Klaxon().parseArray<Libro>(data)
                        companionObjectlibros.lista = libroParseada!!
                        runOnUiThread {
                            iniciarLibros(companionObjectlibros.lista, this, rv_gestionLibros)
                        }

                    }
                }
            }


    }

    fun guardarLibro(libro: Libro) {
        val url = "${MainActivity.objetoCompartido.url}/libro"
        val bodyJson = """
              {

                "titulo": "${libro.titulo}",
                "autor": "${libro.autor}",
                "edicion" : ${libro.edicion},
                "editorial": "${libro.editorial}",
                "precio": ${libro.precio},
                "isbn": "${libro.isbn}",
                "idioma": "${libro.idioma}",
                "estado": "${libro.estado}"
              }
            """
        url
            .httpPost().body(bodyJson)
            .responseString { request, response, result ->
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                        Log.i("http", "rEQUEST: ${request}")
                    }
                    is Result.Success -> {
                        runOnUiThread {
                            cargarLibros()
                        }

                        Log.i("http", "TODO BIIIEN")
                    }
                }
            }

    }

    fun eliminarLibro(idLibro: Int) {
        val url = "${MainActivity.url}/libro/${idLibro}"
        var lista = listOf<LibroCatalogo>()
        var listaLibros = ArrayList<LibroCatalogo>()
        url
            .httpDelete()
            .responseString { request, response, result ->
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                        Log.i("http", "Error: ${ex.message}")
                    }
                    is Result.Success -> {
                        cargarLibros()

                    }
                }
            }

    }

    fun editarLibro(libro: Libro) {
        val url = "${MainActivity.objetoCompartido.url}/libro/${libro.id}"
        val bodyJson = """
              {

                "titulo": "${libro.titulo}",
                "autor": "${libro.autor}",
                "edicion" : ${libro.edicion},
                "editorial": "${libro.editorial}",
                "precio": ${libro.precio},
                "isbn": "${libro.isbn}",
                "idioma": "${libro.idioma}",
                "estado": "${libro.estado}"
              }
            """
        url
            .httpPut().body(bodyJson)
            .responseString { request, response, result ->
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                        Log.i("http", "rEQUEST: ${request}")
                    }
                    is Result.Success -> {
                        runOnUiThread {
                            cargarLibros()
                        }

                        Log.i("http", "TODO BIIIEN")
                    }
                }
            }
    }

    fun dialogLibro(libro: Libro, opcion: Int) {

        val builder = AlertDialog.Builder(this)

        val inflater = layoutInflater
        builder.setTitle("With EditText")
        val dialogLayout = inflater.inflate(R.layout.dialog_libro, null)

        val titulo = dialogLayout.findViewById<EditText>(R.id.dialog_libro_inputTitulo)
        val autor = dialogLayout.findViewById<EditText>(R.id.dialog_libro_inputAutor)
        val edicion = dialogLayout.findViewById<EditText>(R.id.dialog_libro_inputEdicion)
        val editorial = dialogLayout.findViewById<EditText>(R.id.dialog_libro_inputEditorial)
        val precio = dialogLayout.findViewById<EditText>(R.id.dialog_libro_inputPrecio)
        val estado = dialogLayout.findViewById<Switch>(R.id.dialog_libro_swithEstado)
        val isbn = dialogLayout.findViewById<EditText>(R.id.dialog_libro_inputISBN)
        val idioma = dialogLayout.findViewById<TextView>(R.id.dialog_libro_inputIdioma)
        val txt_idioma = dialogLayout.findViewById<TextView>(R.id.txt_idioma)
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





        builder.setPositiveButton("Guardar") { dialogInterface, i ->
            if (estado.isChecked()) {
                libro.estado = "disponible"

            } else {
                libro.estado = "no disponible"
            }
            libro.titulo = titulo.text.toString()
            libro.autor = autor.text.toString()
            libro.precio = precio.text.toString().toDouble()
            libro.editorial = editorial.text.toString()
            libro.edicion = edicion.text.toString().toInt()
            libro.idioma = identificarIdioma(libro.titulo)
            libro.isbn = isbn.text.toString()
            if (opcion == 1) {

                val languageIdentifier: FirebaseLanguageIdentification =
                    FirebaseNaturalLanguage.getInstance().languageIdentification
                val x = languageIdentifier.identifyLanguage(libro.titulo)
                    .addOnSuccessListener { languageCode ->
                        if (languageCode !== "und") {
                            Log.i("1234", languageCode)
                            libro.idioma = languageCode

                        } else {
                            Log.i("1234", languageCode)
                            libro.idioma = languageCode

                        }
                        guardarLibro(libro)
                    }


            } else {

                val languageIdentifier: FirebaseLanguageIdentification =
                    FirebaseNaturalLanguage.getInstance().languageIdentification
                val x = languageIdentifier.identifyLanguage(libro.titulo)
                    .addOnSuccessListener { languageCode ->
                        if (languageCode !== "und") {
                            Log.i("1234", languageCode)
                            libro.idioma = languageCode

                        } else {
                            Log.i("1234", languageCode)
                            libro.idioma = languageCode

                        }
                        editarLibro(libro)
                    }

            }
            Toast.makeText(applicationContext, "LIBRO GUARDADO CORRECTAMENTE", Toast.LENGTH_SHORT).show()

        }
        if (opcion == 1) {
            idioma.visibility = View.INVISIBLE
            txt_idioma.visibility = View.INVISIBLE

        }

        builder.setView(dialogLayout)
        builder.show()
    }

    fun identificarIdioma(texto: String): String {
        val languageIdentifier: FirebaseLanguageIdentification =
            FirebaseNaturalLanguage.getInstance().languageIdentification
        var idioma = "indefinido"
        val x = languageIdentifier.identifyLanguage(texto)
            .addOnSuccessListener { languageCode ->
                if (languageCode !== "und") {
                    Log.i("1234", languageCode)
                    idioma = languageCode
                } else {
                    Log.i("1234", languageCode)
                    idioma = languageCode
                }
            }
        return idioma

    }

    fun iniciarLibros(lista: List<Libro>, actividad: GestionLibrosActivity, recycler_view: RecyclerView) {
        val adaptadorPlato =
            AdaptadorLibro(lista, actividad, recycler_view)
        rv_gestionLibros.adapter = adaptadorPlato
        rv_gestionLibros.itemAnimator = DefaultItemAnimator()
        //Nos falta el layout manager
        rv_gestionLibros.layoutManager = LinearLayoutManager(this)
        adaptadorPlato.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.action_locales -> {
                irIntentRespuesta()
                return true
            }

            R.id.action_catalogo -> {
                if (MainActivity.permisoAdmin) {
                    irGestionLibros()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Opción sólo disponible para usuarios de tipo ADMINISTRADOR",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return true
            }
            R.id.action_factura -> {
                irIntentFactura()
                return true
            }
//            R.id.action_paste -> {
//                text_view.text = "Paste"
//                return true
//            }
//            R.id.action_new -> {
//                text_view.text = "New"
//                return true
//            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun irIntentFactura() {
        val intent = Intent(
            this, FacturasClienteActivity::class.java
        )

        startActivity(intent);

    }

    fun irGestionLibros() {
        val intent = Intent(
            this, GestionLibrosActivity::class.java
        )
        startActivity(intent);
    }

    fun irIntentRespuesta() {
        val intent = Intent(
            this, MapsActivity::class.java
        )

        startActivity(intent);

    }

}
