package com.itssvkv.chatapp.ui.createpost

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.itssvkv.chatapp.databinding.FragmentCreatePostBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreatePostFragment : Fragment() {
    private var _binding: FragmentCreatePostBinding? = null
    private val createPostViewModel by viewModels<CreatePostViewModel>()
    private val binding get() = _binding!!
    private var imgResult: Uri? = null
    private var downloadImage: Uri? = null
    private var resultLauncher: ActivityResultLauncher<Intent>? = null
    private val imgIntent by lazy { Intent() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreatePostBinding.inflate(inflater, container, false)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        init()
        return _binding?.root
    }

    private fun init() {
        getCurrentUserInfoFromSharedPref()
        setCurrentUserInfo()
        initClicks()
        selectImage()

    }

    private fun getCurrentUserInfoFromSharedPref() {
        createPostViewModel.getCurrentUserInfoFormSharedPref(requireContext())
    }

    private fun setCurrentUserInfo() {
        createPostViewModel.currentUserDataInfoLiveData.observe(viewLifecycleOwner) {
            binding.currentUserName.text = it.name
            Glide.with(binding.currentUserIv.context)
                .load(it.profilePhoto)
                .into(binding.currentUserIv)
        }
    }

    private fun initClicks() {
        binding.backIv.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
        binding.choosePhotoIv.setOnClickListener {
            resultLauncher?.launch(imgIntent)
        }
        binding.postTv.setOnClickListener {
            createPostOnFirebase()
        }
    }

    private fun selectImage() {
        imgIntent.apply {
            action = Intent.ACTION_GET_CONTENT
            type = "image/*"
        }
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    imgResult = it?.data?.data
                    imgResult?.let { imgUri ->
                        binding.postPhoto.setImageURI(imgUri)
                        createPostViewModel.uploadImageToFirebase(imgUri)
                    }

                }
            }
    }

    private fun createPostOnFirebase() {

        createPostViewModel.setPostDataToFirebase(
            caption = binding.createPostEt.text.toString()
        )

        createPostViewModel.closeCreatePostFragment = {
            findNavController().popBackStack()
        }
        createPostViewModel.makeToast = {
            Toast.makeText(requireContext(), "Your post successfully uploaded", Toast.LENGTH_SHORT)
                .show()
        }
    }

}