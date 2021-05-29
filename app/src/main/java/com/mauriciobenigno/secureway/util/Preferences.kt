package com.mauriciobenigno.secureway.util

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

class Preferences (contexto: Context) {

    companion object {
        const val NOME_ARQUIVO_PREFENCIAS = "SW_PREFS"
        private const val TUTORIAL = "SW_TUTORIAL"
        private const val NOTICES = "SW_NOTIFICACOES"
        private const val GEOFENCES = "SW_CERCAS"

    }

    private var sharedPreferences: SharedPreferences =
        contexto.getSharedPreferences(NOME_ARQUIVO_PREFENCIAS, Context.MODE_PRIVATE)


    fun limparPreferencias(){
        sharedPreferences.edit().clear().apply()
    }


    var completouTutorial: Boolean
        get() = sharedPreferences.getBoolean(TUTORIAL, false)
        set(completouTutorial) { this.sharedPreferences.edit().putBoolean(TUTORIAL, completouTutorial).apply() }

    var habilitarNotificacoes: Boolean
        get() = sharedPreferences.getBoolean(NOTICES, false)
        set(habilitarNotificacoes) { this.sharedPreferences.edit().putBoolean(NOTICES, habilitarNotificacoes).apply() }

    var salvarGeoCercaAtual: ArrayList<String>
        get() = Gson().fromJson(sharedPreferences.getString(GEOFENCES, null), ArrayList::class.java) as ArrayList<String>
        set(salvarGeoCercaAtual) { this.sharedPreferences.edit().putString(GEOFENCES, Gson().toJson(salvarGeoCercaAtual)).apply() }

}