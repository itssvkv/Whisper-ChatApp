package com.itssvkv.chatapp.ui.roomchat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.bumptech.glide.Glide
import com.itssvkv.chatapp.databinding.FragmentRoomChatBinding
import com.itssvkv.chatapp.models.ChatMessage
import com.itssvkv.chatapp.models.UserDataInfo
import com.itssvkv.chatapp.ui.MainActivity
import com.itssvkv.chatapp.ui.home.adapters.ChatMessageAdapter
import com.itssvkv.chatapp.utils.Common.TAG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RoomChatFragment : Fragment() {
    private var _binding: FragmentRoomChatBinding? = null
    private val roomChatViewModel by viewModels<RoomChatViewModel>()

    @Inject lateinit var chatMessageAdapter: ChatMessageAdapter
    private val binding get() = _binding!!

    private var userInfo: UserDataInfo? = null
    private var chatRoomId: String = ""
    private var otherUser: String = ""
    private var currentUserId: String = ""

    @Inject
    lateinit var bundle: Bundle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userInfo = bundle.getSerializable("userInfo") as UserDataInfo?
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRoomChatBinding.inflate(inflater, container, false)
        currentUserId = (activity as MainActivity).currentUserId
        Log.d(TAG, "onCreateView: $currentUserId")
        Log.d(TAG, "onCreateView:${(activity as MainActivity).currentUserId}")
        binding.chatMessageRecycler.adapter = chatMessageAdapter
        return _binding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        Log.d(TAG, "userid: $currentUserId")
        setDataForUser()
        initClicks()
        lifecycleScope.launch {
            chatRoomId = roomChatViewModel
                .getChatRoomId(secondUserId = otherUser)
            Log.d(TAG, "init: $chatRoomId")
            getOrCreateChatRoom(chatRoomId = chatRoomId)
            initChatMessageAdapter()
        }


    }

    private fun initClicks() {
        binding.backIv.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        binding.sendMessageIcon.setOnClickListener {
            if (binding.sendMessageEt.text.toString().trim().isEmpty()) {
                return@setOnClickListener
            }
            lifecycleScope.launch {
                roomChatViewModel.sendMessageToUser(
                    binding.sendMessageEt.text.toString().trim(),
                    chatRoomId
                )
                roomChatViewModel.clearEditText = {
                    binding.sendMessageEt.setText("")
                }
            }
        }
    }


    private fun initChatMessageAdapter() {
        lifecycleScope.launch {
            roomChatViewModel.getAllMessageOfTwoUsers(chatRoomId = chatRoomId)
        }
        roomChatViewModel.query.observe(viewLifecycleOwner) {
            chatMessageAdapter.submitList(it.toObjects(ChatMessage::class.java))
            chatMessageAdapter.registerAdapterDataObserver(object : AdapterDataObserver() {
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    super.onItemRangeInserted(positionStart, itemCount)
                    binding.chatMessageRecycler.smoothScrollToPosition(0)
                }
            })
            Log.d(TAG, "initChatMessageAdapter: ${it.toObjects(ChatMessage::class.java)}")
        }
    }

    private fun getOrCreateChatRoom(chatRoomId: String) {
        lifecycleScope.launch {
            roomChatViewModel.getOrCreateChatRoom(chatRoomId = chatRoomId)
        }
    }

    private fun setDataForUser() {
        otherUser = userInfo?.id!!
        Glide.with(binding.profileIV.context)
            .load(userInfo?.profilePhoto).into(binding.profileIV)
        binding.usernameTv.text = userInfo?.name
    }

}