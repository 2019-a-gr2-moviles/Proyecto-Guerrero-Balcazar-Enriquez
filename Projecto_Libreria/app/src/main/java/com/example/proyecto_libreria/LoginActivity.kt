package com.example.proyecto_libreria

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btn_ingresar.setOnClickListener {
            irIntentCatalogo()
        }

    }
    fun irIntentCatalogo(){
        val intent= Intent(
            this, CatalogoActivity::class.java
        )

        startActivity(intent);

    }

}
