package com.noticias.notiflash.activity.fragment

import android.os.Bundle
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
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import com.noticias.notiflash.R
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

    private val db = FirebaseFirestore.getInstance()

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

        comentarioNoticia = view.findViewById(R.id.txtComentario)
        btnComentar = view.findViewById(R.id.btnComentar)

        val userID = arguments?.getString("userID")

        val titulo = arguments?.getString("titulo")
        val descripcion = arguments?.getString("descripcion")
        val fecha = arguments?.getString("fecha")
        val ubicacion = arguments?.getString("ubicacion")
        val imagenURL = arguments?.getString("imagenURL")
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