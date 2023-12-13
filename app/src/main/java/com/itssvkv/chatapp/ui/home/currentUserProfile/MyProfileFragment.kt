package com.itssvkv.chatapp.ui.home.currentUserProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.itssvkv.chatapp.R
import com.itssvkv.chatapp.databinding.FragmentSettingsBinding
import com.itssvkv.chatapp.models.UserDataInfo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MyProfileFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val myProfileViewModel by viewModels<MyProfileViewModel>()
    private var currentUserInfo: UserDataInfo? = null
    val binding get() = _binding!!

    @Inject
    lateinit var bundle: Bundle


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        init()
        return _binding?.root
    }

    private fun init() {
        getUserInfo()
        setupUserInfo()
        initClicks()
    }

    private fun getUserInfo() {
        lifecycleScope.launch {
            myProfileViewModel.getCurrentUserFromSharedPref(requireContext())
        }
    }

    private fun setupUserInfo() {
        myProfileViewModel.currentUserDataInfo.observe(viewLifecycleOwner) { userInfo ->
            currentUserInfo = userInfo
            Glide.with(binding.userPhotoIv.context)
                .load(userInfo.profilePhoto)
                .into(binding.userPhotoIv)
            binding.usernameTv.text = userInfo.name
            binding.userStatusTv.text = userInfo.status
            binding.userUsernameTv.text = userInfo.username

        }
    }

    private fun initClicks() {
        binding.backIv.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
        binding.editProfileBottom.setOnClickListener {
            bundle.putSerializable("currentUserInfo", currentUserInfo)
            parentFragment?.parentFragment?.findNavController()
                ?.navigate(R.id.homeFragmentToUpdateFragment)
        }
    }

}