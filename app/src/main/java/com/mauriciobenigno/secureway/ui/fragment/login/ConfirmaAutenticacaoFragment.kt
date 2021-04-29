package com.mauriciobenigno.secureway.ui.fragment.login

import android.app.ProgressDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mauriciobenigno.secureway.R
import com.mauriciobenigno.secureway.ui.MapViewModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.concurrent.TimeUnit


class ConfirmaAutenticacaoFragment : Fragment() {

    lateinit var viewModel : AuthViewModel

    private var storedVerificationId: String? = null
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null
    private var mAuth = FirebaseAuth.getInstance()


    private var edtCode1: EditText? = null
    private var edtCode2: EditText? = null
    private var edtCode3: EditText? = null
    private var edtCode4: EditText? = null
    private var edtCode5: EditText? = null
    private var edtCode6: EditText? = null

    private var txtEditarNumero: TextView? = null

    private var btnValidarSMS: Button? = null

    private var vNome = ""
    private var vNumero = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_confirma_autenticacao, container, false)

        edtCode1 = view.findViewById(R.id.edtCode1) as EditText
        edtCode2 = view.findViewById(R.id.edtCode2) as EditText
        edtCode3 = view.findViewById(R.id.edtCode3) as EditText
        edtCode4 = view.findViewById(R.id.edtCode4) as EditText
        edtCode5 = view.findViewById(R.id.edtCode5) as EditText
        edtCode6 = view.findViewById(R.id.edtCode6) as EditText

        txtEditarNumero = view.findViewById(R.id.txtEditarNumero) as TextView

        btnValidarSMS = view.findViewById(R.id.btnValidarSMS)

        configurarInputs()

        val bundle = arguments

        if(bundle!= null){
            bundle.getString("apelido")?.let {
                vNome = it
            }
            bundle.getString("numero")?.let {
                vNumero = it
                enviarCodigoVerificacao("+55${it}")
            }
        }

        txtEditarNumero!!.setOnClickListener {
            val ft: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            ft.replace(R.id.container_frame_auth, AutenticacaoFragment())
            ft.commit()
        }

        btnValidarSMS?.setOnClickListener {
            val codigoSms = "${edtCode1!!.text}${edtCode2!!.text}${edtCode3!!.text}${edtCode4!!.text}${edtCode5!!.text}${edtCode6!!.text}"
            verificarCodigoManualmente(codigoSms)
        }

        return view
    }

    private fun verificarCodigoManualmente(codigoSms: String){
        val credencial = PhoneAuthProvider.getCredential(storedVerificationId!!, codigoSms)
        signInWithPhoneAuthCredential(credencial)
    }

    private fun enviarCodigoVerificacao(numero: String){
        val options = PhoneAuthOptions.newBuilder(Firebase.auth)
            .setPhoneNumber(numero)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(mCallbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }


    private var mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            credential.smsCode?.let { preencheCodigo(it) }
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            var mensagem = "Erro desconhecido."
            try{
                if (e is FirebaseAuthInvalidCredentialsException) {
                   mensagem = e.message.toString()
                } else if (e is FirebaseTooManyRequestsException) {
                   mensagem = "Foram feitas várias tentativas de login com esse numero!\nTente novamente mais tarde!"
                }
                else{
                   mensagem = e.message.toString()
                }
                MaterialAlertDialogBuilder(requireContext())
                   .setTitle("Ocorreu um erro!")
                   .setMessage(mensagem)
                   .setPositiveButton("OK") { _, _ ->
                       activity?.finish()
                   }
                   .show()
            }
            catch (e: Exception){
                Log.e("ERROR_AUTH", mensagem)
            }
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            storedVerificationId = verificationId
            resendToken = token
        }
    }

    private fun preencheCodigo(codeSms: String){
        edtCode1?.setText(codeSms[0].toString())
        edtCode2?.setText(codeSms[1].toString())
        edtCode3?.setText(codeSms[2].toString())
        edtCode4?.setText(codeSms[3].toString())
        edtCode5?.setText(codeSms[4].toString())
        edtCode6?.setText(codeSms[5].toString())
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {

        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = task.result?.user

                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(vNome)
                        .build()

                    Firebase.auth.currentUser!!.updateProfile(profileUpdates)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {

                            }
                        }


                    val progressDialog = ProgressDialog(requireContext())
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                    progressDialog.setCancelable(true)

                    doAsync {

                        uiThread {
                            progressDialog.setTitle("Autenticação realizada com sucesso!")
                            progressDialog.setMessage("Baixando adjetivos!")
                            progressDialog.show()
                        }

                        try {

                            viewModel.syncFetchAdjetivosFromServer()

                            uiThread {
                                progressDialog.setMessage("Baixando seus reportes!")
                                progressDialog.show()
                            }

                            viewModel.getReportsByUser("55${vNumero}")

                            uiThread {
                                progressDialog.setMessage("Baixando suas zonas!")
                                progressDialog.show()
                            }

                            viewModel.syncFetchZonasFromServer()

                            uiThread {
                                progressDialog.dismiss()
                                MaterialAlertDialogBuilder(requireContext())
                                    .setTitle("Sucesso!")
                                    .setMessage("Autenticação realizada e dados baixados com sucesso!")
                                    .setPositiveButton("OK") { _, _ ->
                                        progressDialog.dismiss()
                                        activity?.finish()
                                    }
                                    .show()
                            }
                        }
                        catch (e: Exception) {
                            uiThread {
                                progressDialog.dismiss()
                                MaterialAlertDialogBuilder(requireContext())
                                    .setTitle("Atenção!")
                                    .setMessage("Autenticação realizada porém ocorreu um problema a baixar os dados!")
                                    .setPositiveButton("OK") { _, _ ->
                                        activity?.finish()
                                    }
                                    .show()
                            }
                        }

                    }

                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(requireContext(), "Código Inválido!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun configurarInputs(){

        edtCode1?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!p0.isNullOrEmpty()) edtCode2!!.requestFocus()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        edtCode2?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!p0.isNullOrEmpty()) edtCode3!!.requestFocus()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        edtCode3?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!p0.isNullOrEmpty()) edtCode4!!.requestFocus()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        edtCode4?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!p0.isNullOrEmpty()) edtCode5!!.requestFocus()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        edtCode5?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!p0.isNullOrEmpty()) edtCode6!!.requestFocus()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

}