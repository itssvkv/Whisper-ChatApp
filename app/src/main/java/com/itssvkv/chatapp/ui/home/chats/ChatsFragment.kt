package com.itssvkv.chatapp.ui.home.chats

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.itssvkv.chatapp.R
import com.itssvkv.chatapp.databinding.FragmentChatsBinding
import com.itssvkv.chatapp.models.ChatRoom
import com.itssvkv.chatapp.ui.home.adapters.RecentChatAdapter
import com.itssvkv.chatapp.utils.Common.TAG
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChatsFragment : Fragment() {
    private var _binding: FragmentChatsBinding? = null
    private val binding get() = _binding!!
    private val chatsViewModel by viewModels<ChatsViewModel>()

    @Inject
    lateinit var recentChatAdapter: RecentChatAdapter

    @Inject
    lateinit var bundle: Bundle
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatsBinding.inflate(inflater, container, false)
        binding.chatsRecycler.adapter = recentChatAdapter
        setupRecentChatAdapter()
        openRootChat()
        initClicks()
        showOrHideLoadingAnimation()
        showOrHideNoChatAnimation()
        // Inflate the layout for this fragment
        return _binding?.root
    }

    private fun initClicks() {
        binding.backIv.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
    }

    private fun setupRecentChatAdapter() {
        chatsViewModel.query.observe(viewLifecycleOwner) { query ->
            if (query != null) {
                Log.d(TAG, "setupRecentChatAdapter: ${query.toObjects(ChatRoom::class.java)}")
                recentChatAdapter.submitList(query.toObjects(ChatRoom::class.java))
            }
        }
    }

    private fun openRootChat() {
        recentChatAdapter.onChatClickedListener = { userDataInfo ->
            bundle.putSerializable("otherUserProfile", userDataInfo)
            parentFragment?.parentFragment?.findNavController()
                ?.navigate(R.id.homeFragmentToRoomChatFragment)
        }
    }


    override fun onResume() {
        super.onResume()
        chatsViewModel.updateChatRooms()
    }


    private fun showOrHideLoadingAnimation() {
        chatsViewModel.loadingAnimLiveData.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    binding.loadingAnimation.visibility = View.VISIBLE
                    binding.chatsRecycler.visibility = View.GONE
                }

                false -> {
                    binding.loadingAnimation.visibility = View.GONE
                    binding.chatsRecycler.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showOrHideNoChatAnimation() {
        chatsViewModel.noChatAnimLiveData.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    binding.emptyMailAnimation.visibility = View.VISIBLE
                    binding.chatsRecycler.visibility = View.GONE
                }

                false -> {
                    binding.emptyMailAnimation.visibility = View.GONE
                    binding.chatsRecycler.visibility = View.VISIBLE
                }
            }
        }
    }
}



