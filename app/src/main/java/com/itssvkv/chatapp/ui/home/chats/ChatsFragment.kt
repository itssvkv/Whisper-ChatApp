package com.itssvkv.chatapp.ui.home.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.itssvkv.chatapp.R
import com.itssvkv.chatapp.databinding.FragmentChatsBinding
import com.itssvkv.chatapp.models.UserDataInfo
import com.itssvkv.chatapp.ui.MainActivity
import com.itssvkv.chatapp.ui.home.adapters.BaseChatsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatsFragment : Fragment() {

    private var binding: FragmentChatsBinding? = null
    private var searchJob: Job? = null
    private val chatsViewModel by viewModels<ChatsViewModel>()
    private lateinit var baseChatsAdapter: BaseChatsAdapter
    private var currentUserId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatsBinding.inflate(inflater, container, false)
        currentUserId = (activity as MainActivity).currentUserId
        baseChatsAdapter = BaseChatsAdapter(currentUserId = currentUserId)
        binding?.searchResultRecycler?.adapter = baseChatsAdapter
        binding?.animationView?.visibility = View.GONE
        sendSearchQuery()
        openRootChat()
        // Inflate the layout for this fragment
        return binding?.root
    }

    private fun openRootChat() {
        baseChatsAdapter.onUserClickListener = {
            parentFragment?.parentFragment?.findNavController()?.navigate(R.id.homeFragmentToRoomChatFragment)
        }
    }


    private fun sendSearchQuery() {
        binding?.deleteIcon?.setOnClickListener {
            binding?.searchEt?.setText("")
        }
        binding?.searchEt?.watchSearch(
            action = { searchInput ->
                setupBaseChatsAdapter("+$searchInput")
                binding?.animationView?.visibility = View.GONE
                binding?.chatsRecycler?.visibility = View.GONE
                binding?.searchResultRecycler?.visibility = View.VISIBLE
            },
            emptyAction = {
                binding?.animationView?.visibility = View.GONE
                binding?.searchResultRecycler?.visibility = View.GONE
                binding?.chatsRecycler?.visibility = View.VISIBLE
            },
            loading = {
                binding?.searchResultRecycler?.visibility = View.GONE
                binding?.chatsRecycler?.visibility = View.GONE
                binding?.animationView?.visibility = View.VISIBLE
            },
            duration = 1000
        )
    }

    private fun setupBaseChatsAdapter(searchText: String) {
        lifecycleScope.launch {
            chatsViewModel.sendSearchQuery(searchText = searchText).addOnSuccessListener { query ->
                binding?.animationView?.visibility = View.GONE
                baseChatsAdapter.submitList(query.toObjects(UserDataInfo::class.java))
            }
        }
    }

    private fun EditText.watchSearch(
        action: ((String) -> Unit)? = null,
        emptyAction: (() -> Unit)? = null,
        loading: (() -> Unit)? = null,
        duration: Long
    ) {
        binding?.searchEt?.doAfterTextChanged { text ->
            searchJob?.cancel()
            if (text.toString().isNotEmpty()) {
                binding?.deleteIcon?.visibility = View.VISIBLE
            } else {
                binding?.deleteIcon?.visibility = View.GONE
            }
            loading?.invoke()

            searchJob = if (text.toString().isEmpty()) {
                lifecycleScope.launch {
                    delay(duration)
                    emptyAction?.invoke()
                }
            } else {
                lifecycleScope.launch {
                    delay(duration)
                    action?.invoke(text.toString())
                }
            }
        }
    }
}

