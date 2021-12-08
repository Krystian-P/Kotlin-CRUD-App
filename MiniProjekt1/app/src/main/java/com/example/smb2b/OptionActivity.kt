package com.example.smb2b


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.smb2b.databinding.ActivityMainBinding
import com.example.smb2b.databinding.ActivityOptionBinding

class OptionActivity : AppCompatActivity() {

    lateinit var binding: ActivityOptionBinding
    lateinit var sp: SharedPreferences
    val colorKey = "currentColor"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOptionBinding.inflate(layoutInflater)

        sp = getPreferences(Context.MODE_PRIVATE)

        when (sp.getString(colorKey, "green")) {
            "blue" ->  binding.root.setBackgroundColor(Color.BLUE)
            "red" ->  binding.root.setBackgroundColor(Color.DKGRAY)
            "white" ->  binding.root.setBackgroundColor(Color.WHITE)
        }



        setContentView(binding.root)
        val bt1t: Button = binding.bt1t
        bt1t.setOnClickListener { binding.textView.setTextSize(12F)
            binding.textView2.setTextSize(12F) }
        val bt2t: Button = binding.bt2t
        bt2t.setOnClickListener { binding.textView.setTextSize(14F)
            binding.textView2.setTextSize(14F) }
        val bt3t: Button = binding.bt3t
        bt3t.setOnClickListener { binding.textView.setTextSize(16F)
            binding.textView2.setTextSize(16F) }

        }
    fun onClick(view: View) {
        when (view.id) {
            R.id.bt1c -> {
                sp.edit().putString(colorKey, "blue").apply()
            }

            R.id.bt2c -> {
                sp.edit().putString(colorKey, "red").apply()
            }

            R.id.bt3c -> {
                sp.edit().putString(colorKey, "white").apply()
            }
        }
        val intent = intent // from getIntent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        finish()
        startActivity(intent)
    }
}