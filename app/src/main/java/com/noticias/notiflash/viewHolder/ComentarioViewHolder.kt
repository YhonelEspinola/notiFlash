package com.noticias.notiflash.viewHolder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.noticias.notiflash.R
import com.noticias.notiflash.model.Comentario

class ComentarioViewHolder (inflater: LayoutInflater, viewGroup: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_comentario, viewGroup, false)) {

    private var textUsuario: TextView?=null
    private var textComentario: TextView?=null

    init {
        textUsuario= itemView.findViewById(R.id.username)
        textComentario= itemView.findViewById(R.id.comentario)
    }

    fun bind(comentario: Comentario) {
        textUsuario?.text = comentario.userEmail
        textComentario?.text = comentario.comentario
    }

}