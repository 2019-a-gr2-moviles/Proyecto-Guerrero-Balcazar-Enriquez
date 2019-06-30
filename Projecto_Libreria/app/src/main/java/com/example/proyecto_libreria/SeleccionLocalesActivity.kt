package com.example.proyecto_libreria

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_seleccion_locales.*

class SeleccionLocalesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccion_locales)
        btn_local1.setOnClickListener {
            irIntentRespuesta()
        }
    }
    fun irIntentRespuesta(){
        val intent= Intent(
            this, MapsActivity::class.java
        )

        startActivity(intent);

    }

}
