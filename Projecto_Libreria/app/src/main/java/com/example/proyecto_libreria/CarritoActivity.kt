package com.example.proyecto_libreria

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beust.klaxon.Klaxon
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import kotlinx.android.synthetic.main.activity_carrito.*
import kotlinx.android.synthetic.main.content_catalogo.*
import java.text.SimpleDateFormat
import java.util.*

class CarritoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)



        iniciarRVLibros(CatalogoActivity.objetoCompartido.listaCarrito, this, rv_carrito)
        var resultadoRedondeado=Math.round(CatalogoActivity.objetoCompartido.totalPagar * 100) / 100.0
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
    }
    fun dialogFactura() {

        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        builder.setTitle("With EditText")
        val dialogLayout = inflater.inflate(R.layout.dialog_factura, null)

        val totalPagar = dialogLayout.findViewById<TextView>(R.id.dialog_factura_totalPagar)
        val numeroTarjeta = dialogLayout.findViewById<TextView>(R.id.dialog_factura_inputNumTarjeta)

        totalPagar.setText(CatalogoActivity.objetoCompartido.totalPagar.toString())



        builder.setView(dialogLayout)

        builder.setPositiveButton("Finalizar compra") { dialogInterface, i ->
            guardarFactura(numeroTarjeta.text.toString())

            Toast.makeText(applicationContext, "COMPRA REALIZADA", Toast.LENGTH_SHORT).show()

        }
        builder.show()
    }
    fun guardarFactura(numeroTarjeta:String){
        var urlFactura="${MainActivity.objetoCompartido.url}/factura"
        var x=Date()


        val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

        Log.i("http", "FECHA!!!!!!!!!: ${formatter.format(x)}")


        val bodyJson = """
              {
                "numeroTarjeta": "${numeroTarjeta}",
                "fecha" : "${formatter.format(x)}",
                "total": ${CatalogoActivity.objetoCompartido.totalPagar},
                "idUsuario": ${MainActivity.objetoCompartido.idUsuario}
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
                        CatalogoActivity.objetoCompartido.listaCarrito.forEach {
                            crearDetalles(facturaParseada!!.id!!, it.id!!, it.cantidad!!)
                        }
                        Log.i("http", "TODO BIIIEN")
                    }
                }
            }
    }
    fun crearDetalles(idFactura: Int, idLibro:Int, cantidad:Int){
        var urlDetalle="${MainActivity.objetoCompartido.url}/detalle"
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
        CatalogoActivity.objetoCompartido.totalPagar+=CatalogoActivity.listaCarrito[indice].precio
        var resultadoRedondeado=Math.round(CatalogoActivity.objetoCompartido.totalPagar * 100) / 100.0
        txt_totalPagar.text= resultadoRedondeado.toString()
        iniciarRVLibros(CatalogoActivity.objetoCompartido.listaCarrito, this, rv_carrito)
    }
    fun menosLibro(indice: Int){
        CatalogoActivity.listaCarrito[indice].cantidad++
        CatalogoActivity.objetoCompartido.totalPagar-=CatalogoActivity.listaCarrito[indice].precio
        var resultadoRedondeado=Math.round(CatalogoActivity.objetoCompartido.totalPagar * 100) / 100.0
        txt_totalPagar.text= resultadoRedondeado.toString()
        iniciarRVLibros(CatalogoActivity.objetoCompartido.listaCarrito, this, rv_carrito)
    }
    fun irCatalogo(){
        val intent= Intent(
            this, CatalogoActivity::class.java
        )
        intent.putExtra("opcion", "recargar" )
        startActivity(intent);
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
        CatalogoActivity.totalPagar= CatalogoActivity.totalPagar-(libroCarrito.precio*libroCarrito.cantidad)
        var resultadoRedondeado=Math.round(CatalogoActivity.objetoCompartido.totalPagar * 100) / 100.0
        txt_totalPagar.text= resultadoRedondeado.toString()
        CatalogoActivity.listaLibros.add(libroCarrito)
        CatalogoActivity.listaCarrito.remove(libroCarrito)
        iniciarRVLibros(CatalogoActivity.listaCarrito, this, rv_carrito)
    }
}
