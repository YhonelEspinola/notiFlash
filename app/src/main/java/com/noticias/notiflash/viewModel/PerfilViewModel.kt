package com.noticias.notiflash.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PerfilViewModel: ViewModel() {

    private val _nombreU = MutableLiveData<String?>()
    private  val _correoU = MutableLiveData<String?>()
    private val _errorMesage = MutableLiveData<String?>()

    val nombreU: LiveData<String?> get() = _nombreU
    val correoU: LiveData<String?> get() = _correoU

    private val db  = FirebaseFirestore.getInstance().collection("usuarios")

    fun leerInformacion(){
        val userId = FirebaseAuth.getInstance().uid ?: return

        db.document(userId).get()
            .addOnSuccessListener { document ->
                val nombre = document.getString("username")
                val correo = document.getString("email")
                _nombreU.value = nombre
                _correoU.value = correo

            }
            .addOnFailureListener{exception ->
                _errorMesage.value = "Error al obtener la información del usuario"
            }

    }


}