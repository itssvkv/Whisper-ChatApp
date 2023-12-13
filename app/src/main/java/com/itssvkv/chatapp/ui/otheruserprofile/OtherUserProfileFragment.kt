package com.itssvkv.chatapp.ui.otheruserprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.itssvkv.chatapp.R
import com.itssvkv.chatapp.databinding.FragmentOtherUserProfileBinding
import com.itssvkv.chatapp.models.UserDataInfo
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OtherUserProfileFragment : Fragment() {

    private var _binding: FragmentOtherUserProfileBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var bundle: Bundle
    private lateinit var userDataInfo: UserDataInfo
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOtherUserProfileBinding.inflate(inflater, container, false)
        userDataInfo = bundle.getSerializable("otherUserProfile") as UserDataInfo
        init()
        return _binding?.root
    }

    private fun init() {
        initClicks()
        setupTheUserProfile()
    }

    private fun initClicks() {
        binding.backIv.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
        binding.userChatIcon.setOnClickListener {
            bundle.putSerializable("otherUserProfile", userDataInfo)
            this@OtherUserProfileFragment.findNavController()
                .navigate(R.id.otherUserProfileFragmentToRoomChatFragment)
        }
    }

    private fun setupTheUserProfile() {
        Glide.with(binding.userPhotoIv.context)
            .load(userDataInfo.profilePhoto)
            .apply(RequestOptions().dontTransform())
            .into(binding.userPhotoIv)
        binding.usernameTv.text = userDataInfo.name
        binding.userUsernameTv.text = userDataInfo.username
        binding.userStatusTv.text = userDataInfo.status

    }

}