package com.noticias.notiflash.viewHolder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.noticias.notiflash.R
import com.noticias.notiflash.activity.fragment.DetalleNoticiaFragment
import com.noticias.notiflash.model.Noticia
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Locale

class ListarNoticiasViewHolder(inflater: LayoutInflater, viewGroup: ViewGroup):
RecyclerView.ViewHolder(inflater.inflate(R.layout.item_noticias, viewGroup, false)) {

    private var imagenNoticia : ImageView?=null
    private var titulo : TextView?=null
    private var descripcion : TextView?=null
    private var fecha : TextView?=null
    private var ubicacion : TextView?=null
    private var item_noticia : CardView?=null


    init {
        imagenNoticia = itemView.findViewById(R.id.btnImagen)
        titulo = itemView.findViewById(R.id.textTitulo)
        descripcion = itemView.findViewById(R.id.textDescripcion)
        fecha = itemView.findViewById(R.id.textFecha)
        ubicacion = itemView.findViewById(R.id.textUbicacion)
        item_noticia = itemView.findViewById(R.id.cardNoticia)
    }

    fun bind(noticia: Noticia) {
        imagenNoticia?.let {
            Picasso.get()
                .load(noticia.imagenURL)
                .placeholder(R.drawable.place)
                .into(it)
        }
        titulo?.text = noticia.titulo
        descripcion?.text = noticia.descripcion
        fecha?.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(noticia.fecha.toDate())
        ubicacion?.text = noticia.ubicacion

        item_noticia?.setOnClickListener {
            val fragment = DetalleNoticiaFragment.newInstance().apply {
                arguments = Bundle().apply {
                    putString("titulo", noticia.titulo)
                    putString("descripcion", noticia.descripcion)
                    putString("fecha", SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(noticia.fecha.toDate()))
                    putString("ubicacion", noticia.ubicacion)
                    putString("imagenURL", noticia.imagenURL)
                    putString("userID", noticia.userID)
                }
            }

            val fragmentManager = (itemView.context as AppCompatActivity).supportFragmentManager
            fragmentManager.beginTransaction().apply {
                replace(R.id.fragment_menu, fragment)
                addToBackStack(null)
                commit()
            }
        }

    }
}