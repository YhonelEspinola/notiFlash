package com.noticias.notiflash.activity.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.noticias.notiflash.R
import com.noticias.notiflash.activity.LoginActivity
import com.noticias.notiflash.viewModel.PerfilViewModel

class PerfilFragment : Fragment() {

    private var firebaseAuth: FirebaseAuth? = null

    private  lateinit var viewModel: PerfilViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        viewModel = ViewModelProvider(requireActivity()).get(PerfilViewModel::class.java)

        val miPerfil : LinearLayout = view.findViewById(R.id.miPerfil)

        val misNoticias : LinearLayout = view.findViewById(R.id.misNoticias)

        val cerrarSesion : AppCompatButton = view.findViewById(R.id.logout_button)

        val nombreUsuario = view.findViewById<TextView>(R.id.user_name)

        viewModel.nombreU.observe(viewLifecycleOwner, Observer {nombre ->
            nombreUsuario.text = nombre
        })

        viewModel.leerInformacion()

        miPerfil.setOnClickListener {

        }
        cerrarSesion.setOnClickListener {
            cerrarSesion()
        }

    }

    private fun openSubFragment(newFragment: Fragment) {
        val transaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_menu, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun cerrarSesion(){
        firebaseAuth!!.signOut()
        startActivity(Intent(activity, LoginActivity::class.java))
        activity?.finish()
    }


    companion object{
        fun newInstance() : PerfilFragment = PerfilFragment()
    }

}