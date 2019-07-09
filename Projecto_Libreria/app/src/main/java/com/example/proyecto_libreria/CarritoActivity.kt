package com.example.proyecto_libreria

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_carrito.*
import kotlinx.android.synthetic.main.content_catalogo.*

class CarritoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)
        iniciarRVLibros(CatalogoActivity.objetoCompartido.listaCarrito, this, rv_carrito)
        btn_catalogo.setOnClickListener {
            irCatalogo()
        }
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
        CatalogoActivity.listaLibros.add(libroCarrito)
        CatalogoActivity.listaCarrito.remove(libroCarrito)
        iniciarRVLibros(CatalogoActivity.listaCarrito, this, rv_carrito)
    }
}
