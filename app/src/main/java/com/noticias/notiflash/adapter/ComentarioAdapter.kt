package com.noticias.notiflash.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.noticias.notiflash.model.Comentario
import com.noticias.notiflash.viewHolder.ComentarioViewHolder


class ComentarioAdapter: RecyclerView.Adapter<ComentarioViewHolder>(){

    private var list = emptyList<Comentario>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComentarioViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ComentarioViewHolder(inflater,parent)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ComentarioViewHolder, position: Int) {
        val evento=list[position]
        holder.bind(evento)
    }

    fun setDatos(datos: List<Comentario>) {
        list = datos
        notifyDataSetChanged()
    }

}