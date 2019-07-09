package com.example.proyecto_libreria

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beust.klaxon.Klaxon
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import kotlinx.android.synthetic.main.activity_carrito.*

import kotlinx.android.synthetic.main.activity_catalogo.*
import kotlinx.android.synthetic.main.content_catalogo.*

class CatalogoActivity : AppCompatActivity() {
    companion object objetoCompartido {
        var listaLibros = ArrayList<LibroCatalogo>()
        var listaCarrito = ArrayList<LibroCatalogo>()
        var totalPagar = 0.0

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalogo)


        var opcion = intent.getStringExtra("opcion")

        if (opcion == "recargar") {
            iniciarRVLibros(objetoCompartido.listaLibros, this, rv_libros)
        } else {
            objetoCompartido.listaLibros = ArrayList<LibroCatalogo>()
            cargarLibros()
        }
        var toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        this.setSupportActionBar(toolbar)
        val actionBar = supportActionBar//barra de acción


        // Set toolbar title/app title
        actionBar!!.title = "Bienvenido"

        // Set action bar/toolbar sub title
        actionBar.subtitle = "Catálogo"

        // Set action bar elevation
        actionBar.elevation = 4.0F

        // Display the app icon in action bar/toolbar
        actionBar.setDisplayShowHomeEnabled(true)
        btnc_carrito.setOnClickListener {
            irCarrito()
        }


    }

    fun irCarrito() {
        val intent = Intent(
            this, CarritoActivity::class.java
        )

        startActivity(intent);
    }
    fun irGestionLibros() {
        val intent = Intent(
            this, GestionLibrosActivity::class.java
        )

        startActivity(intent);
    }

    fun addCarrito(indice: Int) {
        var libroCarrito = objetoCompartido.listaLibros[indice]
        if (libroCarrito.cantidad != 0) {

            objetoCompartido.totalPagar = objetoCompartido.totalPagar + (libroCarrito.precio*libroCarrito.cantidad)

            objetoCompartido.listaCarrito.add(libroCarrito)
            objetoCompartido.listaLibros.remove(libroCarrito)
            iniciarRVLibros(objetoCompartido.listaLibros, this, rv_libros)

        }else{
            Toast.makeText(applicationContext, "No ha seleccionado la cantidad", Toast.LENGTH_SHORT).show()
        }
    }

    fun masLibro(indice: Int) {

        objetoCompartido.listaLibros[indice].cantidad++
        iniciarRVLibros(objetoCompartido.listaLibros, this, rv_libros)
    }

    fun menosLibro(indice: Int) {
        if (objetoCompartido.listaLibros[indice].cantidad != 0) {
            objetoCompartido.listaLibros[indice].cantidad--
            iniciarRVLibros(objetoCompartido.listaLibros, this, rv_libros)
        }

    }

    fun cargarLibros() {
        val url = "${MainActivity.objetoCompartido.url}/libro"
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
                        libroParseada!!.forEach {
                            objetoCompartido.listaLibros.add(
                                LibroCatalogo(
                                    it.id, it.titulo, it.autor, it.isbn, it.edicion, it.editorial,
                                    it.precio, it.estado, it.idioma, 0
                                )
                            )
                        }



                        runOnUiThread {
                            iniciarRVLibros(objetoCompartido.listaLibros, this, rv_libros)
                        }

                    }
                }
            }


    }

    fun iniciarRVLibros(lista: List<LibroCatalogo>, actividad: CatalogoActivity, recycler_view: RecyclerView) {
        val adaptadorPlato = AdaptadorCatalogo(lista, actividad, recycler_view)
        rv_libros.adapter = adaptadorPlato
        rv_libros.itemAnimator = DefaultItemAnimator()
        //Nos falta el layout manager
        rv_libros.layoutManager = LinearLayoutManager(this)
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
                if(MainActivity.objetoCompartido.permisoAdmin){
                    irGestionLibros()
                }else{
                    Toast.makeText(applicationContext, "Opción sólo disponible para usuarios de tipo ADMINISTRADOR", Toast.LENGTH_SHORT).show()
                }
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

    fun irIntentRespuesta() {
        val intent = Intent(
            this, MapsActivity::class.java
        )

        startActivity(intent);

    }


}
