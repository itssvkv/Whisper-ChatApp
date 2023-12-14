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
import com.itssvkv.chatapp.ui.home.adapters.SearchResultAdapter
import com.itssvkv.chatapp.ui.home.adapters.RecentChatAdapter
import com.itssvkv.chatapp.utils.Common.TAG
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChatsFragment : Fragment() {
    private var binding: FragmentChatsBinding? = null
    private val chatsViewModel by viewModels<ChatsViewModel>()

    @Inject
    lateinit var searchResultAdapter: SearchResultAdapter

    @Inject
    lateinit var recentChatAdapter: RecentChatAdapter

    @Inject
    lateinit var bundle: Bundle
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatsBinding.inflate(inflater, container, false)
        binding?.chatsRecycler?.adapter = recentChatAdapter
        setupRecentChatAdapter()
        openRootChat()
        // Inflate the layout for this fragment
        return binding?.root
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

}



