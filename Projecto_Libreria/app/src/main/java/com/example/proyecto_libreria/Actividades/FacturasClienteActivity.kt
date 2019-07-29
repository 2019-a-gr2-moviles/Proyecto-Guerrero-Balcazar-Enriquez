package com.example.proyecto_libreria.Actividades

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beust.klaxon.Klaxon
import com.example.proyecto_libreria.Adaptadores.AdaptadorCatalogo
import com.example.proyecto_libreria.Adaptadores.AdaptadorFactura
import com.example.proyecto_libreria.Clases.Factura
import com.example.proyecto_libreria.Clases.LibroCatalogo
import com.example.proyecto_libreria.R
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import kotlinx.android.synthetic.main.activity_facturas_cliente.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_catalogo.*

class FacturasClienteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facturas_cliente)
        cargarLibros()
    }

    fun iniciarRVFacturas(lista: List<Factura>, actividad: FacturasClienteActivity, recycler_view: RecyclerView) {
        val adaptadorPlato =
            AdaptadorFactura(lista, actividad, recycler_view)
        rv_facturas_cliente.adapter = adaptadorPlato
        rv_facturas_cliente.itemAnimator = DefaultItemAnimator()
        rv_facturas_cliente.layoutManager = LinearLayoutManager(this)
        adaptadorPlato.notifyDataSetChanged()
    }

    fun irADetalleActivity(idFactura: Int) {

        val intent = Intent(
            this, DetalleActivity::class.java
        )
        intent.putExtra("idFactura", idFactura)
        startActivity(intent);
    }


    fun cargarLibros() {
        val url = "${MainActivity.url}/factura?idUsuario=${MainActivity.objetoCompartido.idUsuario}"
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

                        var facturaParseada = Klaxon().parseArray<Factura>(data)

                        runOnUiThread {
                            iniciarRVFacturas(facturaParseada!!, this, rv_facturas_cliente)
                        }

                    }
                }
            }

    }

}
