package com.noticias.notiflash.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.noticias.notiflash.model.Comentario
import com.google.firebase.firestore.Query


class ComentarioViewModel:ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val userEmail = FirebaseAuth.getInstance().currentUser?.email
    val listComentario = MutableLiveData<List<Comentario>>()

    fun crearComentario(comentario:String,noticiaID:String,callback:(Boolean,String)->Unit){
        val data = hashMapOf(
            "comentario" to comentario,
            "noticiaID" to noticiaID,
            "userEmail" to userEmail,
            "fecha" to Timestamp.now()
        )

        db.collection("comentarios")
            .add(data)
            .addOnSuccessListener {
                callback(true,"Comentario añadido.")
            }
            .addOnFailureListener { e ->
                callback(false,"Error al añadir comentario: ${e.message}")
            }
    }

    fun listarComentarios(noticiaID:String){
        val comentarios = arrayListOf<Comentario>()

        db.collection("comentarios")
            .whereEqualTo("noticiaID",noticiaID)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for(document in querySnapshot){
                    val comentario = document.getString("comentario") ?: ""
                    val userEmail = document.getString("userEmail") ?: ""

                    if(document != null){
                        val modelo = Comentario(comentario, userEmail)
                        comentarios.add(modelo)
                    }
                }
                listComentario.value = comentarios
            }
    }

}