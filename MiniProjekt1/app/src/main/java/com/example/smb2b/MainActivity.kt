package com.example.smb2b

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.smb2b.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth


        binding.btRegister.setOnClickListener {
            auth.createUserWithEmailAndPassword(
                binding.etLogin.text.toString(),
                binding.etPass.text.toString())
                .addOnSuccessListener {
                    Toast.makeText(this,"witaj nowy użytkowniku",Toast.LENGTH_SHORT)
                        .show()
                }
        }

        binding.btLogin.setOnClickListener {
            auth.signInWithEmailAndPassword(
                binding.etLogin.text.toString(),
                binding.etPass.text.toString())
                .addOnSuccessListener {
                    startActivity(Intent(this, DrugieActivity::class.java))
                }
        }
    }



    fun opclick(view: View) {
        //Toast.makeText(this, "klik1", Toast.LENGTH_SHORT).show()
        val intentOptionActivity = Intent(this, OptionActivity::class.java)
        startActivity(intentOptionActivity)
    }
}