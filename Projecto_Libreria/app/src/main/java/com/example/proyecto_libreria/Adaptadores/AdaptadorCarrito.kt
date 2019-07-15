package com.example.proyecto_libreria.Adaptadores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_libreria.Actividades.CarritoActivity
import com.example.proyecto_libreria.Clases.LibroCatalogo
import com.example.proyecto_libreria.R

class AdaptadorCarrito(private val listaLibros: List<LibroCatalogo>,

                       private val contexto: CarritoActivity,
                       private val recyclerView: RecyclerView
) : RecyclerView.Adapter<AdaptadorCarrito.MyViewHolder>() {
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var titulo: TextView
        var isbn: TextView
        var autor: TextView
        //        var edicion: TextView
//        var editorial: TextView
        var precio: TextView
        var numProductos: TextView
        var botonMas: ImageButton
        var botonMenos: ImageButton
        var idSeleccionado: Int
        var indice: Int
        var botonAddCarrito: ImageButton

        //        var estado: TextView
//        var idioma: TextView
        init {
            titulo = view.findViewById(R.id.txt_titulo) as TextView
            isbn = view.findViewById(R.id.txt_isbn) as TextView
            autor = view.findViewById(R.id.txt_autor) as TextView
            precio = view.findViewById(R.id.txt_precio) as TextView
            numProductos = view.findViewById(R.id.txti_numproductos) as TextView
            botonMas = view.findViewById(R.id.btn_mas) as ImageButton
            botonMenos = view.findViewById(R.id.btn_menos) as ImageButton
            botonAddCarrito = view.findViewById(R.id.btn_addCarrito) as ImageButton


            indice = -1
            idSeleccionado = -1
            botonAddCarrito.setOnClickListener {
                contexto.deleteCarrito(indice)
            }
            botonMas.setOnClickListener {
                contexto.masLibro(indice)
            }
            botonMenos.setOnClickListener {
                contexto.menosLibro(indice)
            }
//            botonAddCarrito.setOnClickListener {
//                contexto.addCarrito(indice)
//            }


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
        myViewHolder.isbn.text = libro.isbn
        myViewHolder.precio.text = libro.precio.toString()
        myViewHolder.numProductos.text = libro.cantidad.toString()
        myViewHolder.indice = position
    }
}
