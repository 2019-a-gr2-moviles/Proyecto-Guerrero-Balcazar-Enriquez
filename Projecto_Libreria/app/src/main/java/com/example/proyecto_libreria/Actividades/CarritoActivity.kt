package com.example.proyecto_libreria.Actividades

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beust.klaxon.Klaxon
import com.example.proyecto_libreria.Adaptadores.AdaptadorCarrito
import com.example.proyecto_libreria.Clases.Factura
import com.example.proyecto_libreria.Clases.LibroCatalogo
import com.example.proyecto_libreria.R
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import kotlinx.android.synthetic.main.activity_carrito.*
import java.text.SimpleDateFormat
import java.util.*

class CarritoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)




        iniciarRVLibros(CatalogoActivity.listaCarrito, this, rv_carrito)
        var resultadoRedondeado=Math.round(CatalogoActivity.totalPagar * 100) / 100.0
        txt_totalPagar.text= resultadoRedondeado.toString()
        //"%.2f".format(CatalogoActivity.objetoCompartido.totalPagar).toDouble()

// or

      //  txt_totalPagar.text="%.2f".format(CatalogoActivity.objetoCompartido.totalPagar).toDouble().toString()
        btn_catalogo.setOnClickListener {
            irCatalogo()
        }
        btn_comprar.setOnClickListener {
            dialogFactura()
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
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun dialogFactura() {

        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        builder.setTitle("With EditText")
        val dialogLayout = inflater.inflate(R.layout.dialog_factura, null)

        val totalPagar = dialogLayout.findViewById<TextView>(R.id.dialog_factura_totalPagar)
        val numeroTarjeta = dialogLayout.findViewById<TextView>(R.id.dialog_factura_inputNumTarjeta)

        totalPagar.setText(CatalogoActivity.totalPagar.toString())



        builder.setView(dialogLayout)

        builder.setPositiveButton("Finalizar compra") { dialogInterface, i ->
            guardarFactura(numeroTarjeta.text.toString())

            Toast.makeText(applicationContext, "COMPRA REALIZADA", Toast.LENGTH_SHORT).show()

        }
        builder.show()
    }
    fun guardarFactura(numeroTarjeta:String){
        var urlFactura="${MainActivity.url}/factura"
        var x=Date()


        val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

        Log.i("http", "FECHA!!!!!!!!!: ${formatter.format(x)}")


        val bodyJson = """
              {
                "numeroTarjeta": "${numeroTarjeta}",
                "fecha" : "${formatter.format(x)}",
                "total": ${CatalogoActivity.totalPagar},
                "idUsuario": ${MainActivity.idUsuario}
              }
            """
        urlFactura
            .httpPost().body(bodyJson)
            .responseString { request, response, result ->
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                        Log.i("http", "rEQUEST: ${request}")
                    }
                    is Result.Success -> {
                        var res= result.get()
                        Log.i("http", "TODO BIIIEN ${res.toString()}")
                        val facturaParseada = Klaxon().parse<Factura>(res)
                        CatalogoActivity.listaCarrito.forEach {
                            crearDetalles(facturaParseada!!.id!!, it.id!!, it.cantidad!!)
                        }
                        Log.i("http", "TODO BIIIEN")
                    }
                }
            }
    }
    fun crearDetalles(idFactura: Int, idLibro:Int, cantidad:Int){
        var urlDetalle="${MainActivity.url}/detalle"
        val bodyJson = """
              {
                "idLibro": ${idLibro},
                "idFactura" : ${idFactura},
                "cantidad": ${cantidad}

              }
            """
        urlDetalle
            .httpPost().body(bodyJson)
            .responseString { request, response, result ->
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                        Log.i("http", "ERROR DETALLE: ${request}")
                    }
                    is Result.Success -> {
                        var res= result.get()

                        Log.i("http", "TODO BIIIEN")
                    }
                }
            }

    }
    fun masLibro(indice: Int){
        CatalogoActivity.listaCarrito[indice].cantidad++
        CatalogoActivity.totalPagar += CatalogoActivity.listaCarrito[indice].precio
        var resultadoRedondeado=Math.round(CatalogoActivity.totalPagar * 100) / 100.0
        txt_totalPagar.text= resultadoRedondeado.toString()
        iniciarRVLibros(CatalogoActivity.listaCarrito, this, rv_carrito)
    }
    fun menosLibro(indice: Int){
        if (CatalogoActivity.listaCarrito[indice].cantidad != 0) {
            CatalogoActivity.listaCarrito[indice].cantidad--
            CatalogoActivity.totalPagar -= CatalogoActivity.listaCarrito[indice].precio
            var resultadoRedondeado = Math.round(CatalogoActivity.totalPagar * 100) / 100.0
            txt_totalPagar.text = resultadoRedondeado.toString()
            iniciarRVLibros(CatalogoActivity.listaCarrito, this, rv_carrito)
        }
    }
    fun irCatalogo(){
        val intent= Intent(
            this, CatalogoActivity::class.java
        )
        intent.putExtra("opcion", "recargar" )
        startActivity(intent);
        finish()
    }
    fun iniciarRVLibros(lista: List<LibroCatalogo>, actividad: CarritoActivity, recycler_view: RecyclerView) {
        val adaptadorPlato = AdaptadorCarrito(lista, actividad, recycler_view)
        rv_carrito.adapter = adaptadorPlato
        rv_carrito.itemAnimator = DefaultItemAnimator()
        //Nos falta el layout manager
        rv_carrito.layoutManager = LinearLayoutManager(this)
        adaptadorPlato.notifyDataSetChanged()
    }
    fun deleteCarrito(indice: Int){

        var libroCarrito= CatalogoActivity.listaCarrito[indice]
        CatalogoActivity.totalPagar = CatalogoActivity.totalPagar -(libroCarrito.precio*libroCarrito.cantidad)
        var resultadoRedondeado=Math.round(CatalogoActivity.totalPagar * 100) / 100.0
        txt_totalPagar.text= resultadoRedondeado.toString()
        CatalogoActivity.listaLibros.add(libroCarrito)
        CatalogoActivity.listaCarrito.remove(libroCarrito)
        iniciarRVLibros(CatalogoActivity.listaCarrito, this, rv_carrito)
    }
}
