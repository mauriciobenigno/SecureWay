package com.mauriciobenigno.secureway.util

import android.content.Context
import android.content.SharedPreferences

class Preferences (contexto: Context) {

    companion object {
        const val NOME_ARQUIVO_PREFENCIAS = "SW_PREFS"
        private const val TUTORIAL = "SW_TUTORIAL"

    }

    private var sharedPreferences: SharedPreferences =
        contexto.getSharedPreferences(NOME_ARQUIVO_PREFENCIAS, Context.MODE_PRIVATE)


    fun limparPreferencias(){
        sharedPreferences.edit().clear().apply()
    }


    var completouTutorial: Boolean
        get() = sharedPreferences.getBoolean(TUTORIAL, false)
        set(completouTutorial) { this.sharedPreferences.edit().putBoolean(TUTORIAL, completouTutorial).apply() }

}