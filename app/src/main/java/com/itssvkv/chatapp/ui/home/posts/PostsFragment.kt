package com.itssvkv.chatapp.ui.home.posts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
        postsAdapter = PostsAdapter(requireContext())
        init()
        return _binding?.root
    }

    private fun init() {
        setupPostsRecycler()
        setDataToPostsAdapter()
        setCurrentUserInfoToAdapter()
        whenLikeIconClicked()
        makeToasts()

    }


    private fun setupPostsRecycler() {
        binding.postsRecycler.adapter = postsAdapter
    }

    private fun setDataToPostsAdapter() {
        postsViewModel.allPostsLiveData.observe(viewLifecycleOwner) { posts ->
            postsAdapter.submitList(posts)
        }
    }

    private fun whenLikeIconClicked(){
        postsAdapter.whenLikeIconClicked={
            postsViewModel.whenLikeIconClicked(it)
        }
    }

    private fun makeToasts(){
        postsViewModel.makeToast={
            when(it){
                PostsViewModel.TOASTS.SUCCESS-> Toast.makeText(requireContext(), "Like add", Toast.LENGTH_SHORT).show()
                PostsViewModel.TOASTS.FAILURE-> Toast.makeText(requireContext(), "Like failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setCurrentUserInfoToAdapter(){
        postsViewModel.currentUserDataInfoLiveData.observe(viewLifecycleOwner){
            postsAdapter.setCurrentUserInfoToAdapter(it)
        }
    }

}


