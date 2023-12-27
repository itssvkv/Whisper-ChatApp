package com.itssvkv.chatapp.ui.otheruserprofile

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.itssvkv.chatapp.R
import com.itssvkv.chatapp.databinding.FragmentOtherUserProfileBinding
import com.itssvkv.chatapp.models.UserDataInfo
import com.itssvkv.chatapp.ui.home.adapters.OneUserPostsAdapter
import com.itssvkv.chatapp.utils.Common
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OtherUserProfileFragment : Fragment() {

    private var _binding: FragmentOtherUserProfileBinding? = null
    private val binding get() = _binding!!
    private val otherUserProfileViewModel by viewModels<OtherUserProfileViewModel>()
    private lateinit var oneUserPostsAdapter: OneUserPostsAdapter
    private lateinit var staggeredGridLayoutManager: StaggeredGridLayoutManager

    @Inject
    lateinit var bundle: Bundle
    private lateinit var userDataInfo: UserDataInfo
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOtherUserProfileBinding.inflate(inflater, container, false)
        userDataInfo = bundle.getSerializable("otherUserProfile") as UserDataInfo
        otherUserProfileViewModel.getOtherUserInfo(userInfo = userDataInfo)
        oneUserPostsAdapter = OneUserPostsAdapter()
        init()
        return _binding?.root
    }

    private fun init() {
        initClicks()
        setupTheUserProfile()
        makeToasts()
        setDataToOneUserPostAdapter()
        setupOneUserPostsRecycler()
        showOrHideLoadingAnimation()
        showOrHideNoResultAnimation()
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

        binding.followButton.setOnClickListener {
            otherUserProfileViewModel.addOnFollowingInCurrentUserInfo(
                userInfo = userDataInfo,
                requireContext()
            )
            otherUserProfileViewModel.addOnFollowersInOtherUserInfo(userInfo = userDataInfo)
        }
    }

    private fun makeToasts() {
        otherUserProfileViewModel.makeToast = {
            when (it) {
                OtherUserProfileViewModel.TOASTS.SUCCESS -> {
                    Toast.makeText(requireContext(), "follow added", Toast.LENGTH_SHORT).show()
                }

                OtherUserProfileViewModel.TOASTS.FAILURE -> {
                    Toast.makeText(requireContext(), "follow failed", Toast.LENGTH_SHORT).show()
                }
            }
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
        if (userDataInfo.following == null) {
            binding.numFollowing.text =
                resources.getString(R.string.num_following, 0)
        } else {
            binding.numFollowing.text =
                resources.getString(R.string.num_following, userDataInfo.following!!.size)
        }

        if (userDataInfo.followers == null) {
            binding.numFollowers.text =
                resources.getString(R.string.num_followers, 0)
        } else {

            binding.numFollowers.text =
                resources.getString(R.string.num_followers, userDataInfo.followers!!.size)
        }


    }


    private fun setDataToOneUserPostAdapter() {
        otherUserProfileViewModel.allPostsForOneUserLiveData.observe(viewLifecycleOwner) {
            setNumberOfPosts(it.size)
            Log.d(Common.TAG, "setupOneUserPostsRecycler: $it")
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

    private fun showOrHideLoadingAnimation() {
        otherUserProfileViewModel.loadingAnimLiveData.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    binding.loadingAnimation.visibility = View.VISIBLE
                    binding.userProfilePostsRecycler.visibility = View.GONE
                }

                false -> {
                    binding.loadingAnimation.visibility = View.GONE
                    binding.userProfilePostsRecycler.visibility = View.VISIBLE
                }
            }
        }
    }
    private fun showOrHideNoResultAnimation() {
        otherUserProfileViewModel.noResultAnimLiveData.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    binding.noResultAnimation.visibility = View.VISIBLE
                    binding.userProfilePostsRecycler.visibility = View.GONE
                }

                false -> {
                    binding.noResultAnimation.visibility = View.GONE
                    binding.userProfilePostsRecycler.visibility = View.VISIBLE
                }
            }
        }
    }

}
