package com.example.proyecto_libreria

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification
import android.util.Log

import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        identificarIdioma()

        login_btn_ingresar.setOnClickListener {
            irACatalogoActivity()
        }
    }

    fun irACatalogoActivity() {
        val intent= Intent(
            this, CatalogoActivity::class.java
        )
        startActivity(intent);
    }


    fun identificarIdioma() {
        val languageIdentifier: FirebaseLanguageIdentification = FirebaseNaturalLanguage.getInstance().languageIdentification
        val y:String="ls"
        val x= languageIdentifier.identifyLanguage("hello")
            .addOnSuccessListener { languageCode ->
                if (languageCode !== "und") {
                    Log.i("1234", languageCode)
                } else {
                   Log.i("1234", languageCode)
                }
            }
    }
}
