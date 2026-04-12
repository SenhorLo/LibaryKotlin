package com.example.trabalhofinal

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.trabalhofinal.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Redireciona para a tela de login ao iniciar
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
