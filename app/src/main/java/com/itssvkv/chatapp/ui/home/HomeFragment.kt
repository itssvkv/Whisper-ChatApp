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
    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
//        getDataFromFirebase()
//        observeOnFirebaseData()
        setupNavigation()
        return binding?.root
    }


    private fun setupNavigation() {
        val hostFragment =
            childFragmentManager.findFragmentById(R.id.homeFragmentContainerView) as NavHostFragment
        val navController = hostFragment.navController
        binding?.bottomNavigation?.setupWithNavController(navController)
    }
//    private fun getDataFromFirebase() {
//        lifecycleScope.launch {
//            homeViewModel.getDataFromFirebase()
//        }
//    }
//
//    private fun observeOnFirebaseData() {
//        lifecycleScope.launch {
//            homeViewModel.userInfo.observe(viewLifecycleOwner) { task ->
//                val name = task.result.get("name") as String
//                val image = task.result.get("profilePhoto")
//                binding?.helloMessage?.text = resources.getString(
//                    R.string.helloMessage, name
//                )
//                Glide.with(requireContext()).load(image).into(binding?.image!!)
//            }
//        }
//    }
}