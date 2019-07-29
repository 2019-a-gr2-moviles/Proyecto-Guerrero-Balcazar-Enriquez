package com.example.proyecto_libreria.Adaptadores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_libreria.Actividades.CarritoActivity
import com.example.proyecto_libreria.Actividades.GestionLibrosActivity
import com.example.proyecto_libreria.Clases.Libro
import com.example.proyecto_libreria.Clases.LibroCatalogo
import com.example.proyecto_libreria.R

class AdaptadorLibro(
    private val listaLibros: List<Libro>,
    private val contexto: GestionLibrosActivity,
    private val recyclerView: RecyclerView
) : RecyclerView.Adapter<AdaptadorLibro.MyViewHolder>() {
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var titulo: TextView
        var isbn: TextView
        var autor: TextView
        var btnEditar: ImageButton
        var btnEliminar: ImageButton
        //        var edicion: TextView
//        var editorial: TextView

        var idSeleccionado: Int
        var indice: Int


        //        var estado: TextView
//        var idioma: TextView
        init {
            titulo = view.findViewById(R.id.layout_libro_titulo) as TextView
            autor = view.findViewById(R.id.layout_libro_autor) as TextView
            isbn = view.findViewById(R.id.layout_libro_isbn) as TextView
            btnEditar= view.findViewById(R.id.layout_libro_btn_editar) as ImageButton
            btnEliminar= view.findViewById(R.id.layout_libro_btn_borrar) as ImageButton
            indice = -1
            idSeleccionado = -1
            btnEditar.setOnClickListener {
                contexto.dialogLibro(listaLibros[indice], 0)
            }
            btnEliminar.setOnClickListener {
                contexto.eliminarLibro(idSeleccionado)
            }



        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.layout_gestion_libros,
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
        myViewHolder.indice = position
        myViewHolder.idSeleccionado= libro.id!!
    }
}

