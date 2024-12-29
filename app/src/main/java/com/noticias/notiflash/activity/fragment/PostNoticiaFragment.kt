package com.noticias.notiflash.activity.fragment

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.noticias.notiflash.R
import com.noticias.notiflash.viewModel.NoticiasViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import com.google.firebase.Timestamp
import com.noticias.notiflash.InicioFragment

class PostNoticiaFragment: Fragment() {

    private lateinit var viewModel: NoticiasViewModel

    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){ uri ->
        if(uri != null){
            imgNoticia?.setImageURI(uri)
            selectedImageUri = uri
        }else{
            Log.i("ari","img no seleccionada")
        }
    }

    private lateinit var btnImagen: AppCompatButton
    private lateinit var imgNoticia: ImageView
    private var selectedImageUri: Uri? = null
    private val storage = Firebase.storage


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_publicar_noticia, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[NoticiasViewModel::class.java]

        btnImagen = view.findViewById(R.id.btnSeleccionarImagen)
        imgNoticia = view.findViewById(R.id.imgNoticia)

        val buttonPost = view.findViewById<AppCompatButton>(R.id.btnPublicarNoticia)
        val tituloNoticia = view.findViewById<TextInputEditText>(R.id.txtTitulo)
        val descripcionNoticia = view.findViewById<TextInputEditText>(R.id.edtDescripcion)
        val ubicacionReferencial = view.findViewById<TextInputEditText>(R.id.edtUbicacion)


        btnImagen.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        buttonPost.setOnClickListener {
            val titulo = tituloNoticia.text.toString().trim()
            val descripcion = descripcionNoticia.text.toString().trim()
            val ubicacion = ubicacionReferencial.text.toString().trim()
            val fecha = Timestamp.now()

            if (titulo.isNotBlank() && descripcion.isNotBlank() && ubicacion.isNotBlank() && selectedImageUri != null) {
                uploadImageAndCreateNews(selectedImageUri!!, titulo, descripcion, fecha, ubicacion)
            } else {
                showError("Complete todos los campos correctamente")
            }
        }


    }

    private fun uploadImageAndCreateNews(
        uri: Uri,
        titulo: String,
        descripcion: String,
        fecha : Timestamp = Timestamp.now(),
        ubicacion: String
    ) {
        val storageRef = storage.reference.child("noticias/${System.currentTimeMillis()}.jpg")
        storageRef.putFile(uri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { imageUrl ->

                    viewModel.crearNoticia(titulo, descripcion, fecha, ubicacion, imageUrl.toString()) { result ->
                        if (result) {
                            showSuccess("Noticia publicada")
                            requireActivity().supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_menu, InicioFragment.newInstance())
                                .commit()
                        } else {
                            showError("Error al publicar noticia")
                        }
                    }
                }
            }
            .addOnFailureListener {
                showError("Error al subir la imagen")
            }
    }




    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showSuccess(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object{
        fun newInstance(): PostNoticiaFragment = PostNoticiaFragment()
    }


}