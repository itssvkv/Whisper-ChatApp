package com.itssvkv.chatapp.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.itssvkv.chatapp.R
import com.itssvkv.chatapp.databinding.FragmentSendOtpBinding
import com.itssvkv.chatapp.utils.Common.TAG
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SendOtpFragment : Fragment() {
    private var binding: FragmentSendOtpBinding? = null
    @Inject
    lateinit var bundle: Bundle
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSendOtpBinding.inflate(inflater, container, false)
        sendOtp()
        return binding?.root
    }

    private fun sendOtp(){
        binding?.sendBtn?.setOnClickListener {
            val codeCountry = binding?.countryNumber?.text?.toString()
            val phone = binding?.numberEt?.text?.toString()
            bundle.putString("phone", "$codeCountry$phone")
            Log.d(TAG, "sendOtp: $codeCountry$phone")
            this@SendOtpFragment.findNavController().navigate(R.id.sendFragmentToVerifyOtpFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}