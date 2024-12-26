package com.noticias.notiflash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.noticias.notiflash.adapter.NoticiasAdapter
import com.noticias.notiflash.model.Noticia
import com.noticias.notiflash.viewModel.NoticiasViewModel

class InicioFragment:Fragment() {

    private lateinit var noticiaViewModel : NoticiasViewModel
    private lateinit var noticiasAdapter: NoticiasAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return  inflater.inflate(R.layout.fragment_inicio,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noticiaViewModel= ViewModelProvider(this)[NoticiasViewModel::class.java]

        val recycler = view.findViewById<RecyclerView>(R.id.recyclerNoticias)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        noticiasAdapter = NoticiasAdapter()
        recycler.adapter = noticiasAdapter

        noticiaViewModel.listarNoticia.observe(viewLifecycleOwner) { noticias ->
            if (noticias.isEmpty()) {
                println("No se encontraron noticias")
            } else {
                println("Noticias encontradas: ${noticias.size}")
            }
            noticiasAdapter.setDatos(noticias)
        }

        // Llamar a la función para listar noticias
        noticiaViewModel.listarNoticias()


    }


    companion object{
        fun newInstance() : InicioFragment = InicioFragment()
    }
}