package com.itssvkv.chatapp.ui.home.posts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.itssvkv.chatapp.R
import com.itssvkv.chatapp.databinding.FragmentPostsBinding
import com.itssvkv.chatapp.ui.home.adapters.PostsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostsFragment : Fragment() {

    private var _binding: FragmentPostsBinding? = null
    private lateinit var postsAdapter: PostsAdapter
    private val binding get() = _binding!!
    private val postsViewModel by viewModels<PostsViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostsBinding.inflate(inflater, container, false)
        postsAdapter = PostsAdapter()
        init()
        return _binding?.root
    }

    private fun init() {
        getCurrentUserInfo()
        getCurrentUserInfoFormSharedPref()
        initClicks()
        setupPostsRecycler()
        setDataToPostsAdapter()

    }

    private fun getCurrentUserInfo() {
        postsViewModel.getCurrentUserInfo(requireContext())
    }

    private fun getCurrentUserInfoFormSharedPref() {
        postsViewModel.currentUserDataInfo.observe(viewLifecycleOwner) {
            Glide.with(requireContext()).load(it.profilePhoto).into(binding.currentUserIv)
        }
    }

    private fun initClicks() {
        binding.createPostTv.setOnClickListener {
            parentFragment?.parentFragment?.findNavController()
                ?.navigate(R.id.homeFragmentToCreatePostFragment)
        }
    }

    private fun setupPostsRecycler() {
        binding.postsRecycler.adapter = postsAdapter
    }

    private fun setDataToPostsAdapter() {
        postsViewModel.allPostsLiveData.observe(viewLifecycleOwner) { posts ->
            postsAdapter.submitList(posts)
        }
    }

}

