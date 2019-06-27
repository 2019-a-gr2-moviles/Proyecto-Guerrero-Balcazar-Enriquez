package com.example.proyecto_libreria

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification
import android.support.annotation.NonNull
import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        identificarIdioma()


    }

    fun identificarIdioma() {
        val languageIdentifier: FirebaseLanguageIdentification = FirebaseNaturalLanguage.getInstance().languageIdentification
        val y:String="ls"
        val x= languageIdentifier.identifyLanguage("hola")
            .addOnSuccessListener { languageCode ->
                if (languageCode !== "und") {
                    Log.i("1234", languageCode)
                } else {
                   Log.i("1234", languageCode)
                }
            }

    }
}
