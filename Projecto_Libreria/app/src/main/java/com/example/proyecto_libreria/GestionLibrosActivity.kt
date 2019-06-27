package com.example.proyecto_libreria

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toolbar
import java.util.jar.Manifest

class GestionLibrosActivity : AppCompatActivity() {
    private var permission= arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion_libros)
        var toolbar: androidx.appcompat.widget.Toolbar= findViewById(R.id.toolbar)
        this.setSupportActionBar(toolbar)
        val actionBar = supportActionBar


        // Set toolbar title/app title
        actionBar!!.title = "Hello APP"

        // Set action bar/toolbar sub title
        actionBar.subtitle = "App subtitle"

        // Set action bar elevation
        actionBar.elevation = 4.0F

        // Display the app icon in action bar/toolbar
        actionBar.setDisplayShowHomeEnabled(true)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.action_locales -> {
               irIntentRespuesta()
                return true
            }
//            R.id.action_copy -> {
//                text_view.text = "Copy"
//                return true
//            }
//            R.id.action_paste -> {
//                text_view.text = "Paste"
//                return true
//            }
//            R.id.action_new -> {
//                text_view.text = "New"
//                return true
//            }
        }
        return super.onOptionsItemSelected(item)
    }
    fun irIntentRespuesta(){
        val intent= Intent(
            this, SeleccionLocalesActivity::class.java
        )

        startActivity(intent);

    }
}
