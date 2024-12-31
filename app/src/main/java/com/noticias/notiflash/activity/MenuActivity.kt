package com.noticias.notiflash.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.noticias.notiflash.activity.fragment.InicioFragment
import com.noticias.notiflash.R
import com.noticias.notiflash.activity.fragment.PerfilFragment
import com.noticias.notiflash.activity.fragment.PostNoticiaFragment

class MenuActivity : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val navView = findViewById<BottomNavigationView>(R.id.nav_view)

        // Cargar el fragmento inicial por defecto
        if (savedInstanceState == null) {
            openFragment(InicioFragment.newInstance())
        }

        navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.itemHome -> {
                    openFragment(InicioFragment.newInstance())
                    true
                }
                R.id.itemPost -> {
                    openFragment(PostNoticiaFragment.newInstance())
                    true
                }
                R.id.itemProfile -> {
                    openFragment(PerfilFragment.newInstance())
                    true
                }

                else -> false
            }
        }
    }

    fun openFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val currentFragment = fragmentManager.findFragmentById(R.id.fragment_menu)
        if (currentFragment != null && currentFragment::class.java == fragment::class.java) {
            return
        }
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_menu, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


}