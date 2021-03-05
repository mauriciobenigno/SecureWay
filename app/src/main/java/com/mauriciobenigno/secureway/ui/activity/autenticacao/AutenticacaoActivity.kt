package com.mauriciobenigno.secureway.ui.activity.autenticacao

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.mauriciobenigno.secureway.R
import com.mauriciobenigno.secureway.ui.fragment.login.AutenticacaoFragment
import com.mauriciobenigno.secureway.ui.fragment.login.ConfirmaAutenticacaoFragment

class AutenticacaoActivity : AppCompatActivity(), OnCommunicateInterface {

    private var fragmentAtual: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_autenticacao)

        // Inicializar Framents
        fragmentAtual = AutenticacaoFragment()
    }

    override fun onResume() {
        super.onResume()
        if (fragmentAtual != null) {
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            ft.replace(R.id.container_frame_auth, fragmentAtual!!)
            ft.commit()
        }
    }

    override fun onClickLogin(nome: String?,numero: String?) {

        val novoFragment: Fragment = ConfirmaAutenticacaoFragment()

        val bundle = Bundle()
        bundle.putString("apelido", nome)
        bundle.putString("numero", numero)
        novoFragment.arguments = bundle

        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.replace(R.id.container_frame_auth, novoFragment)
        ft.commit()
    }
}

interface OnCommunicateInterface {
    fun onClickLogin(nome: String?, numero: String?)
}