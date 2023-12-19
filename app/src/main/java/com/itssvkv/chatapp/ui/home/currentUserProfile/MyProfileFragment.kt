package com.itssvkv.chatapp.ui.home.currentUserProfile

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.itssvkv.chatapp.R
import com.itssvkv.chatapp.databinding.FragmentSettingsBinding
import com.itssvkv.chatapp.models.UserDataInfo
import com.itssvkv.chatapp.ui.home.adapters.OneUserPostsAdapter
import com.itssvkv.chatapp.utils.Common.TAG
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyProfileFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val myProfileViewModel by viewModels<MyProfileViewModel>()
    private var currentUserInfo: UserDataInfo? = null
    private lateinit var oneUserPostsAdapter: OneUserPostsAdapter
    private lateinit var staggeredGridLayoutManager: StaggeredGridLayoutManager
    val binding get() = _binding!!

    @Inject
    lateinit var bundle: Bundle


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        oneUserPostsAdapter = OneUserPostsAdapter()
        init()
        return _binding?.root
    }

    private fun init() {
        setupUserInfo()
        initClicks()
        setupOneUserPostsRecycler()
        setDataToOneUserPostAdapter()

    }


    private fun setDataToOneUserPostAdapter() {
        myProfileViewModel.allPostsForOneUserLiveData.observe(viewLifecycleOwner) {
            setNumberOfPosts(it.size)
            Log.d(TAG, "setupOneUserPostsRecycler: $it")
            oneUserPostsAdapter.submitList(it)
        }
    }



    private fun setNumberOfPosts(numberOfPosts: Int) {
        binding.numPosts.text = resources.getString(R.string.num_posts, numberOfPosts)
    }


    private fun setupOneUserPostsRecycler() {
//        staggeredGridLayoutManager =
//            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
//        staggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
//        staggeredGridLayoutManager.gapStrategy =
//            StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        binding.userProfilePostsRecycler.apply {
            adapter = oneUserPostsAdapter
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    super.getItemOffsets(outRect, view, parent, state)
                    val space = 10
                    outRect.left = space - 2
                    outRect.right = space - 2
                    outRect.bottom = space
                    outRect.top = space


                }
            })
            setHasFixedSize(true)
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

            if (userInfo.following == null) {
                binding.numFollowing.text =
                    resources.getString(R.string.num_following, 0)
            } else {
                binding.numFollowing.text =
                    resources.getString(R.string.num_following, userInfo.following.size)
            }

            if (userInfo.followers == null) {
                binding.numFollowers.text =
                    resources.getString(R.string.num_followers, 0)
            } else {

                binding.numFollowers.text =
                    resources.getString(R.string.num_followers, userInfo.followers.size)
            }

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