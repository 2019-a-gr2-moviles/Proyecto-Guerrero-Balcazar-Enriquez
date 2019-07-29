package com.example.proyecto_libreria.Adaptadores

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_libreria.Actividades.CatalogoActivity
import com.example.proyecto_libreria.Actividades.FacturasClienteActivity
import com.example.proyecto_libreria.Clases.Factura
import com.example.proyecto_libreria.Clases.LibroCatalogo
import com.example.proyecto_libreria.R

class AdaptadorFactura(private val listaFacturas: List<Factura>,

private val contexto: FacturasClienteActivity,
private val recyclerView: RecyclerView) : RecyclerView.Adapter<AdaptadorFactura.MyViewHolder>() {
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var fecha: TextView
        var total: TextView
        var botonIrDetalle: ImageButton
        var idSeleccionado: Int
        var indice: Int


        //        var estado: TextView
//        var idioma: TextView
        init {
            fecha = view.findViewById(R.id.layout_factura_fecha) as TextView
            total = view.findViewById(R.id.layout_factura_total) as TextView
            botonIrDetalle= view.findViewById(R.id.btn_irDetalle) as ImageButton
            indice = -1
            idSeleccionado = -1
            botonIrDetalle.setOnClickListener {
                contexto.irADetalleActivity(listaFacturas[indice].id!!)
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.layout_facturas_cliente,
                parent,
                false
            )

        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listaFacturas.size
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, position: Int) {
        val factura = listaFacturas[position]
        myViewHolder.fecha.text= factura.fecha
        var resultadoRedondeado=Math.round(factura.total * 100) / 100.0
        myViewHolder.total.text= resultadoRedondeado.toString()
//        myViewHolder.titulo.text = libro.titulo
//        myViewHolder.autor.text = libro.autor
//        myViewHolder.isbn.text = libro.isbn
//        myViewHolder.precio.text = libro.precio.toString()
//        myViewHolder.numProductos.text = libro.cantidad.toString()
        myViewHolder.indice = position
        myViewHolder.idSeleccionado= factura.id!!

    }


}