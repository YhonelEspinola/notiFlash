package com.noticias.notiflash.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.noticias.notiflash.model.Noticia
import com.google.firebase.Timestamp

class NoticiasViewModel:ViewModel() {

    private  val db= FirebaseFirestore.getInstance()
    private  val userId = FirebaseAuth.getInstance().uid
    val listarNoticia = MutableLiveData<List<Noticia>>()


    fun crearNoticia(
        titulo: String,
        descripcion: String,
        fecha : Timestamp = Timestamp.now(),
        ubicacion: String,
        imagenURL: String,
        callback: (Boolean) -> Unit
    ) {
        val nuevaNoticiaRef = db.collection("noticias").document()

        val data = hashMapOf(
            "titulo" to titulo,
            "descripcion" to descripcion,
            "fecha" to fecha,
            "ubicacion" to ubicacion,
            "imagenURL" to imagenURL,
            "userID" to userId
        )

        nuevaNoticiaRef.set(data)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    fun listarNoticias(){

        val listaNoticias = arrayListOf<Noticia>()

        db.collection("noticias")
            .orderBy("fecha", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for(document in querySnapshot){
                    val data = document.data
                    val noticiaID = document.id
                    val titulo = data["titulo"] as? String ?: ""
                    val descripcion = data["descripcion"] as? String ?: ""
                    val fecha = data["fecha"] as? Timestamp ?: Timestamp.now()
                    val ubicacion = data["ubicacion"] as? String ?: ""
                    val imagenURL = data["imagenURL"] as? String ?: ""
                    val userID = data["userID"] as? String ?: ""

                    if(document != null){
                        val noticia = Noticia(
                            titulo,
                            descripcion,
                            fecha,
                            ubicacion,
                            imagenURL,
                            userID,
                            noticiaID
                        )
                        listaNoticias.add(noticia)
                    }
                }
                listarNoticia.value = listaNoticias
            }
            .addOnFailureListener { exception ->
                listarNoticia.value = emptyList()
                exception.printStackTrace()
            }
    }

}