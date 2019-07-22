package com.example.proyecto_libreria.Actividades

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification
import android.util.Log
import android.widget.Toast
import com.beust.klaxon.Klaxon
import com.example.proyecto_libreria.Clases.HistorialUsuarioTipo
import com.example.proyecto_libreria.Clases.Usuario
import com.example.proyecto_libreria.R
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result

import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    companion object objetoCompartido {
        //var url = "http://172.29.52.56:1337"
        //var url = "http://172.29.47.25:1337"
        var url = "http://192.168.200.5:1337";
        var nombreUsuario = ""
        var idUsuario =-1
        var permisoAdmin=true


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        identificarIdioma()

        login_btn_ingresar.setOnClickListener {
            autenticar();
        }

        login_btn_registro.setOnClickListener {
            irARegistroActivity();
        }
    }

    fun autenticar() {
        var urlAutenticar= "$url/usuario?username=${login_txtin_usuario.text}&contrasenia=${login_txtin_password.text}"
        var urlPermisos= "$url/usuario?username=${login_txtin_usuario.text}&contrasenia=${login_txtin_password.text}"

        urlAutenticar.httpGet().responseString { request, response, result ->
            when (result) {
                is Result.Failure -> {

                    val ex = result.getException()
                    Log.i("http", "Error: ${ex.message} : ${request.toString()}")

                }
                is Result.Success -> {
                    val data = result.get()
                    Log.i("http", "Error: ${data}: ${request.toString()}")
                    var usuarioParseado = Klaxon().parseArray<Usuario>(data)

                    if(usuarioParseado!!.size!=0){
                        idUsuario =usuarioParseado[0].id!!
                        var urlPermisos= "$url/historialUsuarioTipo?idUsuario=$idUsuario&idTipo=1"

                        urlPermisos.httpGet().responseString{request, response, result ->
                            when(result){
                                is Result.Failure -> {
                                    val ex = result.getException()
                                    Log.i("http", "Error: ${ex.message} : ${request.toString()}")

                                }
                                is Result.Success -> {
                                    val data = result.get()
                                    var permisosParseado= Klaxon().parseArray<HistorialUsuarioTipo>(data)
                                    if(permisosParseado!!.size!=0){
                                        permisoAdmin =true
                                    }else{
                                        permisoAdmin =false
                                    }
                                }

                            }
                        }

                        irACatalogoActivity()

                    }else{
                        Toast.makeText(applicationContext, "Usuario o  contraseña no correctos", Toast.LENGTH_SHORT).show()
                    }

                }
            }

        }
    }

    fun irACatalogoActivity() {
        nombreUsuario = login_txtin_usuario.text.toString()
        val intent = Intent(
            this, CatalogoActivity::class.java
        )
        intent.putExtra("opcion", "inicial");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    fun irARegistroActivity() {

        val intent = Intent(
            this, RegistroUsuarioActivity::class.java
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
    }


    fun identificarIdioma() {
        val languageIdentifier: FirebaseLanguageIdentification =
            FirebaseNaturalLanguage.getInstance().languageIdentification
        val y: String = "ls"
        val x = languageIdentifier.identifyLanguage("hello")
            .addOnSuccessListener { languageCode ->
                if (languageCode !== "und") {
                    Log.i("1234", languageCode)
                } else {
                    Log.i("1234", languageCode)
                }
            }
    }



}
