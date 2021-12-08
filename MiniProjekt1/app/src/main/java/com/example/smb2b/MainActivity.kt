package com.example.smb2b

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.smb2b.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }



    fun click(view: View) {
        //Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
        val intentDrugieActivity = Intent(this, DrugieActivity::class.java)
        startActivity(intentDrugieActivity)
    }

    fun opclick(view: View) {
        //Toast.makeText(this, "klik1", Toast.LENGTH_SHORT).show()
        val intentOptionActivity = Intent(this, OptionActivity::class.java)
        startActivity(intentOptionActivity)
    }
}