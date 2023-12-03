package com.itssvkv.chatapp.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.itssvkv.chatapp.R
import com.itssvkv.chatapp.bottomsheets.createuser.CreateUserBottomSheet
import com.itssvkv.chatapp.databinding.FragmentVerifyOtpBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class VerifyOtpFragment : Fragment() {
    private var binding: FragmentVerifyOtpBinding? = null
    private val verifyOtpViewModel by viewModels<VerifyOtpViewModel>()
    private val createUserBottomSheet by lazy { CreateUserBottomSheet() }

    @Inject
    lateinit var bundle: Bundle
    private var phoneNumber: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVerifyOtpBinding.inflate(inflater, container, false)
        phoneNumber = bundle.getString("phone")
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sendMessage()
        setInProgress()
        singIn()
    }

    private fun sendMessage() {
        lifecycleScope.launch {
            verifyOtpViewModel.sendMessage(
                activity = requireActivity(),
                phoneNumber = phoneNumber
            )
        }
    }

    private fun setInProgress() {
        verifyOtpViewModel.isProgressTrue = {
            binding?.progressBar?.visibility = View.VISIBLE

        }
        verifyOtpViewModel.isProgressFalse = {
            binding?.progressBar?.visibility = View.GONE
        }
    }


    private fun singIn() {
        binding?.verifyBtn?.setOnClickListener {
            binding?.progressBar?.visibility = View.VISIBLE
            val code = binding?.numberEt?.text.toString()
            verifyOtpViewModel.verifyOtp(code)
        }
        verifyOtpViewModel.showToast = {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        verifyOtpViewModel.createUser = {
            createUserBottomSheet.show(requireActivity().supportFragmentManager, null)
        }
        createUserBottomSheet.goToNextFragment = {
            this@VerifyOtpFragment.findNavController()
                .navigate(R.id.verifyOtpFragmentToHomeFragment)
        }

    }
}

