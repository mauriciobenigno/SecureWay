package com.mauriciobenigno.secureway.ui.fragment.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.rtoshiro.util.format.SimpleMaskFormatter
import com.github.rtoshiro.util.format.text.MaskTextWatcher
import com.mauriciobenigno.secureway.R
import com.mauriciobenigno.secureway.ui.activity.autenticacao.OnCommunicateInterface


class AutenticacaoFragment : Fragment() {

    var onCommunicate: OnCommunicateInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_autenticacao, container, false)
        val edtApelido = view.findViewById(R.id.edtApelido) as EditText
        val edtNumero = view.findViewById(R.id.edtNumero) as EditText
        val button = view.findViewById(R.id.btnLogin) as Button

        val mtw = MaskTextWatcher(edtNumero, SimpleMaskFormatter("(NN) NNNNN-NNNN"))
        edtNumero.addTextChangedListener(mtw);

        button.setOnClickListener {
            if(edtNumero.text.isNotEmpty()){
                val textoFiltrado = edtNumero.text.toString().replace("[^\\d.]".toRegex(), "")
                onCommunicate?.onClickLogin(edtApelido.text.toString(), textoFiltrado)
            }
            else {
                Toast.makeText(requireContext(), "Preencha o numero", Toast.LENGTH_SHORT).show()
            }
        }
        // Inflate the layout for this fragment
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            onCommunicate = context as OnCommunicateInterface
        } catch (e: Exception) {
            //Log.e("onAttach", e.toString())
        }
    }

}