package com.noticias.notiflash.activity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.noticias.notiflash.R
import com.noticias.notiflash.adapter.NoticiasAdapter
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

        val searchView = view.findViewById<SearchView>(R.id.search)


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




        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { noticiasAdapter.filtrar(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { noticiasAdapter.filtrar(it) }
                return true
            }
        })

        noticiaViewModel.listarNoticias()
    }


    companion object{
        fun newInstance() : InicioFragment = InicioFragment()
    }
}