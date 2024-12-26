package com.noticias.notiflash.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.noticias.notiflash.model.Noticia
import com.noticias.notiflash.viewHolder.ListarNoticiasViewHolder

class NoticiasAdapter:RecyclerView.Adapter<ListarNoticiasViewHolder>() {
    private var list = emptyList<Noticia>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListarNoticiasViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ListarNoticiasViewHolder(inflater,parent)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ListarNoticiasViewHolder, position: Int) {
        val noticia=list[position]
        holder.bind(noticia)
    }

    fun setDatos(datos: List<Noticia>) {
        list = datos
        notifyDataSetChanged()
    }
}