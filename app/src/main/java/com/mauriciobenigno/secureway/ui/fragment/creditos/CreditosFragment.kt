package com.mauriciobenigno.secureway.ui.fragment.creditos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mauriciobenigno.secureway.R
import com.mauriciobenigno.secureway.ui.fragment.faq.FaqFragment

class CreditosFragment : Fragment() {

    companion object {
        fun newInstance() = FaqFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_creditos, container, false)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
}