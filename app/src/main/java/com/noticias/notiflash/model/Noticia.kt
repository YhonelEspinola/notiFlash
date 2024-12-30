package com.noticias.notiflash.model

import com.google.firebase.Timestamp

data class Noticia(
    val titulo: String = "",
    val descripcion: String = "",
    val fecha: Timestamp = Timestamp.now(),
    val ubicacion: String = "",
    val imagenURL: String = "",
    val userID: String = "",
    val noticiaID:String = "") {
}
