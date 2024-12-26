package com.noticias.notiflash.viewModel

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.common.internal.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegistrarUsuarioModel:ViewModel() {

    private lateinit var firebaseAuth: FirebaseAuth

    val userRegisterStatus = MutableLiveData<Boolean>()
    val usuarioError = MutableLiveData<String>()
    val correoError = MutableLiveData<String>()
    val passwordError = MutableLiveData<String>()
    val cPasswordError = MutableLiveData<String>()
    val mensajeError = MutableLiveData<String>()

    private fun registrarUsuario(usuario:String, correo:String, password:String){
       firebaseAuth = FirebaseAuth.getInstance()
       firebaseAuth.createUserWithEmailAndPassword(correo,password)
           .addOnCompleteListener() { task ->
               if (task.isSuccessful){
                     insertarInfoBD(usuario,correo)
               }else{
                        userRegisterStatus.value = false
                        mensajeError.value = "Error al registrar: ${task.exception?.message}"
               }
           }
    }


    private fun insertarInfoBD(usuario :String, correo: String){
        val db = FirebaseFirestore.getInstance()



        val data = hashMapOf(
            "username" to usuario,
            "email" to correo
        )

        db.collection("usuarios").document().set(data)
            .addOnSuccessListener {
                userRegisterStatus.value = true
            }
            .addOnFailureListener {
                userRegisterStatus.value = false
                mensajeError.value = "Error al registrar: ${it.message}"
            }


    }

     fun validarDatos(usuario : String, correo: String, password: String , cPassword:String){
        if (usuario.isEmpty()){
            usuarioError.value = "Ingrese un Usuario"
            userRegisterStatus.value = false
        }else if (correo.isEmpty()){
            correoError.value = "Ingrese su Correo"
            userRegisterStatus.value = false
        }else if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            correoError.value = "Correo no valido"
            userRegisterStatus.value = false
        }else if (password.isEmpty()){
            passwordError.value = "Ingrese una contraseña"
            userRegisterStatus.value = false
        }else if (password.length < 6){
            passwordError.value = "Contraseña muy corta, ingrese 6 o mas caracteres"
            userRegisterStatus.value = false
        }else if (cPassword.isEmpty()){
            cPasswordError.value = "Repita la Contraseña"
            userRegisterStatus.value = false
        }else if (password != cPassword){
            cPasswordError.value = "Contraseña no coincide"
            userRegisterStatus.value = false
        }else{
            registrarUsuario(usuario,correo,password)
        }

    }


}