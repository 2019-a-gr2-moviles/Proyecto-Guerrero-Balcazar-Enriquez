package com.example.proyecto_libreria.Actividades

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.proyecto_libreria.R
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import kotlinx.android.synthetic.main.activity_registro_usuario.*

class RegistroUsuarioActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {


    var itemsSpinner = arrayOf("Administrador","Cliente");
    var spinner: Spinner? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_usuario);

        /**Esto es para los datos del spinner**/
        spinner = this.spinnerTipoUsuario
        spinner!!.setOnItemSelectedListener(this)

        // Create an ArrayAdapter using a simple spinner layout and languages array
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, itemsSpinner)
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        spinner!!.setAdapter(aa)
        /*************************************/


        btn_RegistrarUsuario.setOnClickListener {
            registrarUsuario();
        }


    }

    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {
        //textView_msg!!.text = "Selected : "+languages[position]
    }

    override fun onNothingSelected(arg0: AdapterView<*>) {

    }


    fun registrarUsuario(){


        if(!txtregistroUser_nombre.text.toString().isEmpty() &&
            !txtregistroUser_apellido.text.toString().isEmpty() &&
            !txtregistroUser_cedula.text.toString().isEmpty() &&
            !txtregistroUser_user.text.toString().isEmpty() &&
            !txtregistroUser_password.text.toString().isEmpty()){


            var nombreUsuario=txtregistroUser_nombre.text.toString();
            var apellidoUsuario=txtregistroUser_apellido.text.toString();
            var cedulaUsuario=txtregistroUser_cedula.text.toString();
            var usernameUsuario=txtregistroUser_user.text.toString();
            var contraseniaUsuario=txtregistroUser_password.text.toString();

            var index=spinner!!.selectedItemPosition;
            var idTipoUsuario=index+1;

            val usuario = """

                {
                  "nombre": "${nombreUsuario}",
                  "apellido": "${apellidoUsuario}",
                  "cedula":"${cedulaUsuario}",
                  "username": "${usernameUsuario}",
                  "contrasenia": "${contraseniaUsuario}",
                  "idTipo": ${idTipoUsuario}
                }
        """.trimIndent()

            Log.i("http","${usuario}")

            var urlCrearUsuario="${MainActivity.objetoCompartido.url}/usuario"

    Log.i("http","$urlCrearUsuario");
            urlCrearUsuario
              .httpPost().body(usuario)
                .responseString { request, response, result ->
                    when(result){

                        is Result.Failure ->{
                            val ex = result.getException()
                            Log.i("http", "Error: ${ex.message}")
                        }

                        is Result.Success ->{
                            val usuarioString= result.get();

                            Log.i("http","${usuarioString}");

                            runOnUiThread {
                                txtregistroUser_nombre.setText("");
                                txtregistroUser_apellido.setText("");
                                txtregistroUser_cedula.setText("");
                                txtregistroUser_user.setText("");
                                txtregistroUser_password.setText("");
                            }

                        }

                    }

                }


        }else{
            Toast.makeText(applicationContext, "LLene todos los campos!" , Toast.LENGTH_LONG).show();
        }


    }
}
