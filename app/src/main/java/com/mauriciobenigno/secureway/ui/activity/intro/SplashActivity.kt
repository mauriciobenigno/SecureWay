package com.mauriciobenigno.secureway.ui.activity.intro

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mauriciobenigno.secureway.ui.activity.PrincipalActivity
import com.mauriciobenigno.secureway.R

class SplashActivity : AppCompatActivity() , Runnable {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val handler = Handler()
        handler.postDelayed(this, 1000)

        configuracoesIniciais()
    }

    override fun run() {
        val intent = Intent(this, PrincipalActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        this.finish()
    }

    private fun configuracoesIniciais() {
        Firebase.auth.setLanguageCode("pt")
    }
}