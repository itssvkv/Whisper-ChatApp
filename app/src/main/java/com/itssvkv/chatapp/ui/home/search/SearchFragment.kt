package com.itssvkv.chatapp.ui.home.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.itssvkv.chatapp.R
import com.itssvkv.chatapp.databinding.FragmentSearchBinding
import com.itssvkv.chatapp.models.UserDataInfo
import com.itssvkv.chatapp.ui.home.adapters.SearchResultAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private var searchJob: Job? = null

    private val searchViewModel by viewModels<SearchViewModel>()

    @Inject
    lateinit var searchResultAdapter: SearchResultAdapter

    @Inject
    lateinit var bundle: Bundle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.searchResultRecycler.adapter = searchResultAdapter
        binding.animationView.visibility = View.GONE
        binding.searchAnimation.visibility = View.VISIBLE
        init()
        return _binding?.root
    }

    private fun init() {
        sendSearchQuery()
        initClicks()
        goToOtherUserProfileFragment()
    }

    private fun goToOtherUserProfileFragment(){
        searchResultAdapter.onUserClickListener = { userDataInfo ->
            bundle.putSerializable("otherUserProfile", userDataInfo)
            parentFragment?.parentFragment?.findNavController()
                ?.navigate(R.id.homeFragmentToOtherUserProfileFragment)
        }
    }

    private fun initClicks() {
        binding.backIv.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
    }


    private fun sendSearchQuery() {
        binding.deleteIcon.setOnClickListener {
            binding.searchEt.setText("")
        }
        watchSearch(
            action = { searchInput ->
                setupBaseChatsAdapter(searchInput)
                binding.animationView.visibility = View.GONE
                binding.searchResultRecycler.visibility = View.VISIBLE
                binding.searchAnimation.visibility = View.GONE
            },
            emptyAction = {
                binding.animationView.visibility = View.GONE
                binding.searchResultRecycler.visibility = View.GONE
                binding.searchAnimation.visibility = View.VISIBLE
            },
            loading = {
                binding.searchResultRecycler.visibility = View.GONE
                binding.animationView.visibility = View.VISIBLE
                binding.searchAnimation.visibility = View.GONE
            },
            duration = 1000
        )
    }

    override fun onResume() {
        super.onResume()
        binding.animationView.visibility = View.GONE
    }

    private fun setupBaseChatsAdapter(searchText: String) {
        lifecycleScope.launch {
            searchViewModel.sendSearchQuery(searchText = searchText).addOnSuccessListener { query ->
                binding.animationView.visibility = View.GONE
                searchResultAdapter.submitList(query.toObjects(UserDataInfo::class.java))
            }
        }
    }

    private fun watchSearch(
        action: ((String) -> Unit)? = null,
        emptyAction: (() -> Unit)? = null,
        loading: (() -> Unit)? = null,
        duration: Long
    ) {

        binding.searchEt.doAfterTextChanged { text ->
            searchJob?.cancel()
            if (text.toString().isNotEmpty()) {
                binding.deleteIcon.visibility = View.VISIBLE
            } else {
                binding.deleteIcon.visibility = View.GONE
            }
            loading?.invoke()

            searchJob = if(text.toString().isEmpty()) {
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
