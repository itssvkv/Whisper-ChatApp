package com.itssvkv.chatapp.ui.userprofile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.itssvkv.chatapp.databinding.FragmentUpdateUserInfoBinding
import com.itssvkv.chatapp.models.UserDataInfo
import com.itssvkv.chatapp.utils.Common.TAG
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UpdateUserInfoFragment : Fragment() {
    private var _binding: FragmentUpdateUserInfoBinding? = null
    private var resultLauncher: ActivityResultLauncher<Intent>? = null
    private var imgResult: Uri? = null
    private val imgIntent by lazy { Intent() }
    private val binding get() = _binding!!
    private val updateUserInfoViewModel by viewModels<UpdateUserInfoViewModel>()

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
        binding.userPhotoIv.setOnClickListener {
            resultLauncher?.launch(imgIntent)
        }
        binding.updateButton.setOnClickListener {
            updateUserInfoViewModel.updateCurrentUserInfo(
                name = binding.updateNameEt.text.toString().trim(),
                userName = binding.updateUsernameEt.text.toString().trim(),
                status = binding.updateStatusEt.text.toString().trim(),
                context = requireContext()
            )
        }

        updateUserInfoViewModel.makeToast = {
            when(it){
                com.itssvkv.chatapp.ui.userprofile.UpdateUserInfoViewModel.TOASTS.SUCCESS-> {
                    Toast.makeText(requireContext(), "Upload succeed", Toast.LENGTH_SHORT).show()
                    this@UpdateUserInfoFragment.findNavController().popBackStack()
                }
                com.itssvkv.chatapp.ui.userprofile.UpdateUserInfoViewModel.TOASTS.FAILURE-> {
                    Toast.makeText(requireContext(), "Upload failed", Toast.LENGTH_SHORT).show()
                    this@UpdateUserInfoFragment.findNavController().popBackStack()
                }
            }
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
                    binding.userPhotoIv.apply {
                        Log.d(TAG, "selectImageFormGallery: $imgResult")
                        setImageURI(imgResult)
                        updateUserInfoViewModel.uploadImageToFirebase(imgResult!!)
                    }
                }
            }
    }

    private fun setupUserInfo() {
        Glide.with(binding.userPhotoIv.context).load(currentUserInfo?.profilePhoto)
            .apply(RequestOptions().dontTransform())
            .into(binding.userPhotoIv)
        binding.updateNameEt.setText(currentUserInfo?.name)
        binding.updateUsernameEt.setText(currentUserInfo?.username)
        binding.updateStatusEt.setText(currentUserInfo?.status)
        binding.userPhone.text = currentUserInfo?.phone
    }

}