package com.itssvkv.chatapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.itssvkv.chatapp.R
import com.itssvkv.chatapp.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupNavigation()
        return binding?.root
    }

    private fun setupNavigation() {
        val hostFragment =
            childFragmentManager.findFragmentById(R.id.homeFragmentContainerView) as NavHostFragment
        val navController = hostFragment.navController
        binding?.bottomNavigation?.setupWithNavController(navController)
    }
}