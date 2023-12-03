package com.itssvkv.chatapp.ui.userprofile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.itssvkv.chatapp.bottomsheets.updatename.UpdateNameBottomSheet
import com.itssvkv.chatapp.bottomsheets.updatestatus.UpdateStatusBottomSheet
import com.itssvkv.chatapp.databinding.FragmentUpdateUserInfoBinding
import com.itssvkv.chatapp.models.UserDataInfo
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UpdateUserInfoFragment : Fragment() {
    private var _binding: FragmentUpdateUserInfoBinding? = null
    private val updateNameBottomSheet by lazy { UpdateNameBottomSheet() }
    private val updateStatusBottomSheet by lazy { UpdateStatusBottomSheet() }
    private var resultLauncher: ActivityResultLauncher<Intent>? = null
    private var imgResult: Uri? = null
    private val imgIntent by lazy { Intent() }
    private val binding get() = _binding!!

    @Inject
    lateinit var bundle: Bundle
    private var currentUserInfo: UserDataInfo? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateUserInfoBinding.inflate(inflater, container, false)
        currentUserInfo = bundle.getSerializable("currentUserInfo") as UserDataInfo?
        init()
        return _binding?.root
    }

    private fun init() {
        setupUserInfo()
        initClicks()
        selectImageFormGallery()
    }

    private fun initClicks() {
        binding.backIv.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
        binding.nameLayout.setOnClickListener {
            updateNameBottomSheet.show(requireActivity().supportFragmentManager, null)
        }
        binding.aboutLayout.setOnClickListener {
            updateStatusBottomSheet.show(requireActivity().supportFragmentManager, null)
        }

        binding.profileIV.setOnClickListener {
            resultLauncher?.launch(imgIntent)
        }
    }

    private fun selectImageFormGallery() {
        imgIntent.apply {
            action = Intent.ACTION_GET_CONTENT
            type = "image/*"
        }
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                if (it.resultCode == Activity.RESULT_OK){
                    imgResult = it.data?.data
                    binding.profileIV.apply {
                        setImageURI(imgResult)
                    }
                }
            }
    }

    private fun setupUserInfo() {
        Glide.with(binding.profileIV.context).load(currentUserInfo?.profilePhoto)
            .into(binding.profileIV)
        binding.usernameTv.text = currentUserInfo?.name
        binding.aboutUserTv.text = currentUserInfo?.status
        binding.userPhoneTv.text = currentUserInfo?.phone
    }

}