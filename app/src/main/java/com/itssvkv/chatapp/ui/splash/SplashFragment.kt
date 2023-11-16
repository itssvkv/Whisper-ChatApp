package com.itssvkv.chatapp.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.itssvkv.chatapp.R
import com.itssvkv.chatapp.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private var binding: FragmentSplashBinding? = null
    private val viewModel by viewModels<SplashViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        viewModel.init(requireContext())
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
    }

    private fun observer() {
        lifecycleScope.launch {
            viewModel.isFirstTimeLiveData.observe(viewLifecycleOwner) { isFirstTime ->
                when (isFirstTime) {
                    true -> this@SplashFragment.findNavController()
                        .navigate(R.id.splashFragmentToOnBoardingFragment)

                    false -> viewModel.isUserLiveData.observe(viewLifecycleOwner) { isUser ->
                        when (isUser) {
                            true -> this@SplashFragment.findNavController()
                                .navigate(R.id.splashFragmentToHomeFragment)

                            false -> this@SplashFragment.findNavController()
                                .navigate(R.id.splashFragmentToAuthFragment)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
