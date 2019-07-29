package com.example.proyecto_libreria.Adaptadores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_libreria.Actividades.DetalleActivity
import com.example.proyecto_libreria.Clases.Detalle
import com.example.proyecto_libreria.R
class AdaptadorLibroDetalle
    (
    private val listaDetalles: List<Detalle>,
    private val contexto: DetalleActivity,
    private val recyclerView: RecyclerView
) : RecyclerView.Adapter<AdaptadorLibroDetalle.MyViewHolder>() {
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var titulo: TextView
        var isbn: TextView
        var autor: TextView
        var btnverLibro: ImageButton

        //        var edicion: TextView
//        var editorial: TextView

        var idSeleccionado: Int
        var indice: Int


        //        var estado: TextView
//        var idioma: TextView
        init {
            titulo = view.findViewById(R.id.layout_libro_detalle_titulo) as TextView
            autor = view.findViewById(R.id.layout_libro_detalle_autor) as TextView
            isbn = view.findViewById(R.id.layout_libro_detalle_isbn) as TextView
            btnverLibro = view.findViewById(R.id.btn_verLibro) as ImageButton

            indice = -1
            idSeleccionado = -1
            btnverLibro.setOnClickListener {
                contexto.dialogLibro(listaDetalles[indice].idLibro)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.layout_libro_detalle,
                parent,
                false
            )

        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listaDetalles.size
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, position: Int) {
        val detalle = listaDetalles[position]
        myViewHolder.titulo.text = detalle.idLibro.titulo
        myViewHolder.autor.text = detalle.idLibro.autor
        myViewHolder.isbn.text = detalle.idLibro.isbn
        myViewHolder.indice = position
        myViewHolder.idSeleccionado = detalle.idLibro.id!!
    }
}


