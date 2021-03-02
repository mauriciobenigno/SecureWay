package com.mauriciobenigno.secureway.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mauriciobenigno.secureway.R
import java.util.concurrent.TimeUnit

class ConfirmaAutenticacaoFragment : Fragment() {

    private var storedVerificationId: String? = null
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null
    private var mAuth = FirebaseAuth.getInstance()


    private var edtCode1: EditText? = null
    private var edtCode2: EditText? = null
    private var edtCode3: EditText? = null
    private var edtCode4: EditText? = null
    private var edtCode5: EditText? = null
    private var edtCode6: EditText? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        configurarInputs()

        val bundle = arguments

        if(bundle!= null){
            bundle.getString("numero")?.let {
                enviarCodigoVerificacao(it)
            }
        }

        return view
    }


    private fun enviarCodigoVerificacao(numero: String){
        val options = PhoneAuthOptions.newBuilder(Firebase.auth)
            .setPhoneNumber(numero)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity())                 // Activity (for callback binding)
            .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }


    private var mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
           // Log.d(TAG, "onVerificationCompleted:$credential")
            credential.smsCode?.let { preencheCodigo(it) }
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            //Log.w(TAG, "onVerificationFailed", e)

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                // ...
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                // ...
            }

            // Show a message and update the UI
            // ...
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            //Log.d(TAG, "onCodeSent:$verificationId")

            // Save verification ID and resending token so we can use them later
            storedVerificationId = verificationId
            resendToken = token

            // ...
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
                    // Sign in success, update UI with the signed-in user's information
                    //Log.d(TAG, "signInWithCredential:success")

                    val user = task.result?.user

                    Toast.makeText(requireContext(), "sucesso ao validar SMS", Toast.LENGTH_SHORT).show()

                } else {
                    // Sign in failed, display a message and update the UI
                   // Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(requireContext(), "Erro ao validar SMS", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun configurarInputs(){

        edtCode1?.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(!p0.isNullOrEmpty()) edtCode2!!.requestFocus()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        edtCode2?.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(!p0.isNullOrEmpty()) edtCode3!!.requestFocus()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        edtCode3?.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(!p0.isNullOrEmpty()) edtCode4!!.requestFocus()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        edtCode4?.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(!p0.isNullOrEmpty()) edtCode5!!.requestFocus()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        edtCode5?.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(!p0.isNullOrEmpty()) edtCode6!!.requestFocus()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })



    }

}