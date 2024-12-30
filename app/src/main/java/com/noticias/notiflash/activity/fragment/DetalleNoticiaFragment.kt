package com.noticias.notiflash.activity.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import com.noticias.notiflash.R
import com.noticias.notiflash.adapter.ComentarioAdapter
import com.noticias.notiflash.viewModel.ComentarioViewModel
import com.squareup.picasso.Picasso

class DetalleNoticiaFragment:Fragment() {

    private lateinit var tituloNoticia : TextView
    private lateinit var descripcionNoticia : TextView
    private lateinit var fechaNoticia : TextView
    private lateinit var ubicacionNoticia : TextView
    private lateinit var imagenNoticia : ImageView
    private lateinit var userNoticia : TextView
    private lateinit var comentarioNoticia : TextInputEditText
    private lateinit var btnComentar : AppCompatButton
    private lateinit var recyclerComentarios : RecyclerView


    private val db = FirebaseFirestore.getInstance()
    private lateinit var comentarioViewModel: ComentarioViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detalle_noticia, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tituloNoticia = view.findViewById(R.id.texttituloNoticia)
        descripcionNoticia = view.findViewById(R.id.textDescripcion)
        fechaNoticia = view.findViewById(R.id.textFecha)
        ubicacionNoticia = view.findViewById(R.id.textUbicacion)
        imagenNoticia = view.findViewById(R.id.imgNoticia)
        userNoticia = view.findViewById(R.id.textAutor)


        comentarioViewModel = ViewModelProvider(this).get(ComentarioViewModel::class.java)
        comentarioNoticia = view.findViewById(R.id.txtComentario)
        btnComentar = view.findViewById(R.id.btnComentar)
        recyclerComentarios = view.findViewById(R.id.rvComentarios)


        val userID = arguments?.getString("userID")

        val titulo = arguments?.getString("titulo")
        val descripcion = arguments?.getString("descripcion")
        val fecha = arguments?.getString("fecha")
        val ubicacion = arguments?.getString("ubicacion")
        val imagenURL = arguments?.getString("imagenURL")
        val noticiaID = arguments?.getString("noticiaID")?: ""


        userID?.let{
            db.collection("usuarios").document(it).get()
                .addOnSuccessListener { document ->
                    if(document != null && document.exists()){
                        val username = document.getString("username")
                        userNoticia.text = username ?: "Usuario desconocido"
                    }else{
                        userNoticia.text = "Usuario desconocido"
                    }
                }
                .addOnFailureListener{
                    userNoticia.text = "Error al cargar el usuario"
                }
        } ?: run {
            userNoticia.text = "Usuario no especificado"
        }


        tituloNoticia?.text = titulo
        descripcionNoticia?.text = descripcion
        fechaNoticia?.text = fecha
        ubicacionNoticia?.text = ubicacion
        userNoticia?.text = userID

        imagenURL?.let {
            Picasso.get()
                .load(it)
                .placeholder(R.drawable.place)
                .into(imagenNoticia)
        }

        btnComentar.setOnClickListener{
            val comentario = comentarioNoticia.text.toString()

            if(comentario.isEmpty()){
                showError("El comentario no puede estar vacío")
                return@setOnClickListener
            }
            Log.d("DetalleNoticiaFragment", "NoticiaID: $noticiaID")

            comentarioViewModel.crearComentario(comentario,noticiaID){exito, message ->
                if(exito){
                    showSuccess(message)
                    comentarioNoticia.text?.clear()
                    comentarioViewModel.listarComentarios(noticiaID)
                }else{
                    showError(message)
                }
            }

        }
        val adapterC = ComentarioAdapter()
        recyclerComentarios.adapter = adapterC
        recyclerComentarios.layoutManager = LinearLayoutManager(context)

        comentarioViewModel.listarComentarios(noticiaID)
        comentarioViewModel.listComentario.observe(viewLifecycleOwner){
            Log.d("DetalleNoticiaFragment", "Comentarios cargados: ${it.size}")
            if(it.isNotEmpty()){
                adapterC.setDatos(it)
            }
        }

    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showSuccess(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
    companion object {
        fun newInstance(): DetalleNoticiaFragment {
            return DetalleNoticiaFragment()
        }
    }
}