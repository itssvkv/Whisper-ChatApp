package com.itssvkv.chatapp.ui.createuser

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.itssvkv.chatapp.R
import com.itssvkv.chatapp.databinding.FragmentCreateUserBinding
import com.itssvkv.chatapp.models.UserDataInfo
import com.itssvkv.chatapp.ui.MainActivity
import com.itssvkv.chatapp.utils.Common
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CreateUserFragment : Fragment() {

    private var _binding: FragmentCreateUserBinding? = null
    private val binding get() = _binding!!
    private val createUserViewModel by viewModels<CreateUserViewModel>()

    private var imgResult: Uri? = null
    private var downloadImage: Uri? = null
    private var phoneNumber: String? = null
    private var resultLauncher: ActivityResultLauncher<Intent>? = null
    private val imgIntent by lazy { Intent() }
    private var userId: String = ""


    @Inject
    lateinit var bundle: Bundle

    private lateinit var userInfo: UserDataInfo
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreateUserBinding.inflate(inflater, container, false)
        binding.progressBar.visibility = View.GONE
        phoneNumber = bundle.getString("phone")
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClicks()
        initImagesClicks()
        selectImage()
        setInProgress()


    }


    private fun initClicks() {
        binding.createBtn.setOnClickListener {
            lifecycleScope.launch {
                userId = createUserViewModel.getUserId()!!
                Log.d(Common.TAG, "initClicks: f${(activity as MainActivity).currentUserId}")
                (activity as MainActivity).currentUserId = userId
                Log.d(Common.TAG, "initClicks: s${(activity as MainActivity).currentUserId}")
                userInfo = UserDataInfo(
                    name = binding.createNameEt.text?.toString()!!,
                    status = binding.createAboutYouEt.text?.toString()!!,
                    phone = phoneNumber,
                    //                    timestamp = System.currentTimeMillis(),
                    profilePhoto = downloadImage.toString(),
                    id = userId,
                    username = binding.createUsernameEt.text.toString()
                )
                Log.d(Common.TAG, "initClicks: $userInfo")
                createUserViewModel.setDataToFirebase(
                    context = requireContext(),
                    userInfo = userInfo
                )
                createUserViewModel.goToNextFragment = {
                    this@CreateUserFragment.findNavController()
                        .navigate(R.id.createUserFragmentToHomeFragment)
                }
            }

        }
    }

    private fun setInProgress() {
        createUserViewModel.setInProgress.observe(viewLifecycleOwner) {
            when (it) {
                true -> binding.progressBar.visibility = View.VISIBLE
                false -> binding.progressBar.visibility = View.GONE
            }
        }
    }


    private fun initImagesClicks() {
        binding.profileIV.setOnClickListener {
            resultLauncher?.launch(imgIntent)
        }
    }

    private fun selectImage() {
        imgIntent.apply {
            action = Intent.ACTION_GET_CONTENT
            type = "image/*"
        }
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { intentResult ->
                if (intentResult.resultCode == Activity.RESULT_OK) {
                    imgResult = intentResult.data?.data
                    binding.profileIV.apply {
                        setImageURI(imgResult)
                        Log.d(Common.TAG, "selectImage: $imgResult")
                        uploadImage(imgResult)
                    }
                }
            }
    }

    private fun uploadImage(imageUri: Uri?) {
        lifecycleScope.launch {
            createUserViewModel.uploadImage(imageUri = imageUri!!)
        }
        createUserViewModel.imageUriLiveData.observe(viewLifecycleOwner) {
            downloadImage = it
        }
        Log.d(Common.TAG, "uploadImageActivity: $downloadImage")

    }

}