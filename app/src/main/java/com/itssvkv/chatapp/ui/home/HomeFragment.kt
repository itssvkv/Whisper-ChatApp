package com.itssvkv.chatapp.ui.home

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.itssvkv.chatapp.R
import com.itssvkv.chatapp.data.local.repository.FirebaseRepository
import com.itssvkv.chatapp.databinding.FragmentHomeBinding
import com.itssvkv.chatapp.utils.Common.TAG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null

    @Inject
    lateinit var firebaseRepo: FirebaseRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        init()
        return binding?.root
    }

    private fun init() {
        lifecycleScope.launch {
            firebaseRepo.currentUserDetails().get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val name = task.result.get("name") as String
                    val image = task.result.get("profilePhoto") as String
                    Log.d(TAG, "init: $name")
                    val helloMessage = resources.getString(R.string.helloMessage, name)
                    binding?.helloMessage?.text = helloMessage
                    Glide.with(requireContext()).load(image).into(binding?.image!!)
                }
            }
        }
    }


}