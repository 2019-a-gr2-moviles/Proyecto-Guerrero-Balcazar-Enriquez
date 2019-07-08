package com.example.proyecto_libreria

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdaptadorCatalogo(private val listaLibros: List<LibroCatalogo>,

private val contexto: CatalogoActivity,
private val recyclerView: RecyclerView, private val opcion: Int) : RecyclerView.Adapter<AdaptadorCatalogo.MyViewHolder>() {
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var titulo: TextView
        var isbn: TextView
        var autor: TextView
//        var edicion: TextView
//        var editorial: TextView
        var precio: TextView
//        var estado: TextView
//        var idioma: TextView
        init {
            titulo= view.findViewById(R.id.txt_titulo) as TextView
            isbn= view.findViewById(R.id.txt_isbn) as TextView
            autor= view.findViewById(R.id.txt_autor) as TextView
            precio= view.findViewById(R.id.txt_precio) as TextView
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.layout_catalogo,
                parent,
                false
            )

        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
       return listaLibros.size
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, position: Int) {
        val libro = listaLibros[position]
        myViewHolder.titulo.text = libro.titulo
        myViewHolder.autor.text = libro.autor
        myViewHolder.precio.text = libro.precio.toString()
    }


}