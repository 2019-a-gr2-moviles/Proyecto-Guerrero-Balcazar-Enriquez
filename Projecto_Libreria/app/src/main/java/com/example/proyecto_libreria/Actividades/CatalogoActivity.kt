package com.example.proyecto_libreria.Actividades

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beust.klaxon.Klaxon
import com.example.proyecto_libreria.Adaptadores.AdaptadorCatalogo
import com.example.proyecto_libreria.Clases.Libro
import com.example.proyecto_libreria.Clases.LibroCatalogo
import com.example.proyecto_libreria.R
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result

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
            iniciarRVLibros(listaLibros, this, rv_catalogo)
        } else {
            listaLibros = ArrayList<LibroCatalogo>()
            listaCarrito = ArrayList<LibroCatalogo>()
            totalPagar=0.0
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
        btn_buscarLibro.setOnClickListener {
            buscarLibros()
        }


    }
    fun generarFactura(){

    }


    fun irCarrito() {
        val intent = Intent(
            this, CarritoActivity::class.java
        )

        startActivity(intent);
        finish()
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
    fun irIntentFactura() {
        val intent = Intent(
            this, FacturasClienteActivity::class.java
        )

        startActivity(intent);

    }

    fun addCarrito(indice: Int) {
        var libroCarrito = listaLibros[indice]
        if (libroCarrito.cantidad != 0) {

            totalPagar = totalPagar + (libroCarrito.precio*libroCarrito.cantidad)

            listaCarrito.add(libroCarrito)
            listaLibros.remove(libroCarrito)
            iniciarRVLibros(listaLibros, this, rv_catalogo)

        }else{
            Toast.makeText(applicationContext, "No ha seleccionado la cantidad", Toast.LENGTH_SHORT).show()
        }
    }

    fun masLibro(indice: Int) {

        listaLibros[indice].cantidad++
       // CatalogoActivity.totalPagar += CatalogoActivity.listaCarrito[indice].precio
        iniciarRVLibros(listaLibros, this, rv_catalogo)
    }

    fun menosLibro(indice: Int) {
        if (listaLibros[indice].cantidad != 0) {
            listaLibros[indice].cantidad--
            iniciarRVLibros(listaLibros, this, rv_catalogo)
        }

    }
    fun buscarLibros(){
        var listaFiltrada= objetoCompartido.listaLibros.filter {
            it.titulo.contains(txt_buscarLibro.text.toString())
        }
        iniciarRVLibros(listaFiltrada, this, rv_catalogo)
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
                        libroParseada!!.forEach {
                            objetoCompartido.listaLibros.add(
                                LibroCatalogo(
                                    it.id, it.titulo, it.autor, it.isbn, it.edicion, it.editorial,
                                    it.precio, it.estado, it.idioma, 0
                                )
                            )
                        }
                        runOnUiThread {
                            iniciarRVLibros(objetoCompartido.listaLibros, this, rv_catalogo)
                        }

                    }
                }
            }


    }

    fun iniciarRVLibros(lista: List<LibroCatalogo>, actividad: CatalogoActivity, recycler_view: RecyclerView) {
        val adaptadorPlato =
            AdaptadorCatalogo(lista, actividad, recycler_view)
        rv_catalogo.adapter = adaptadorPlato
        rv_catalogo.itemAnimator = DefaultItemAnimator()
        //Nos falta el layout manager
        rv_catalogo.layoutManager = LinearLayoutManager(this)
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
                if(MainActivity.permisoAdmin){
                    irGestionLibros()
                }else{
                    Toast.makeText(applicationContext, "Opción sólo disponible para usuarios de tipo ADMINISTRADOR", Toast.LENGTH_SHORT).show()
                }
                return true
            }
            R.id.action_factura -> {
               irIntentFactura()
                return true
            }
//            R.id.action_new -> {
//                text_view.text = "New"
//                return true
//            }
        }
        return super.onOptionsItemSelected(item)
    }




}
