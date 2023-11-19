package com.itssvkv.chatapp.ui.roomchat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.itssvkv.chatapp.databinding.FragmentRoomChatBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoomChatFragment : Fragment() {
    private var binding: FragmentRoomChatBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRoomChatBinding.inflate(inflater, container, false)
        return binding?.root

    }
}