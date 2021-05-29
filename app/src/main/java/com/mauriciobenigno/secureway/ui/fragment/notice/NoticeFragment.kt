package com.mauriciobenigno.secureway.ui.fragment.notice

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.Switch
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mauriciobenigno.secureway.R
import com.mauriciobenigno.secureway.model.*
import com.mauriciobenigno.secureway.ui.activity.PrincipalActivity
import com.mauriciobenigno.secureway.ui.adapter.FaqAdapter
import com.mauriciobenigno.secureway.util.Preferences
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.runOnUiThread
import java.lang.Exception
import java.util.*


class NoticeFragment : Fragment() {

    companion object {
        fun newInstance() = NoticeFragment()
    }

    private lateinit var viewModel: NoticeViewModel
    private lateinit var switch: Switch

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_notice, container, false)

        switch = rootView.findViewById(R.id.switchGeoNotifications)
        switch.isChecked = Preferences(requireContext()).habilitarNotificacoes
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NoticeViewModel::class.java)

        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Preferences(requireContext()).habilitarNotificacoes = true
                (activity as PrincipalActivity).configurarGeoFences()
            } else {
                Preferences(requireContext()).habilitarNotificacoes = false
                (activity as PrincipalActivity).desligarGeoFences()
            }
        }
    }

}