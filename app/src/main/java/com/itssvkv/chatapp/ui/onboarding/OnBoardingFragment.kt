package com.itssvkv.chatapp.ui.onboarding

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.itssvkv.chatapp.R
import com.itssvkv.chatapp.databinding.FragmentOnBoardingBinding
import com.itssvkv.chatapp.ui.onboarding.pager.PagerAdapter
import com.itssvkv.chatapp.utils.Common.TAG
import com.itssvkv.chatapp.utils.sharedpref.SharedPrefCommon.IS_FIRST_TIME
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OnBoardingFragment : Fragment() {

    private var binding: FragmentOnBoardingBinding? = null
    private lateinit var adapter: PagerAdapter
    private val viewModel by viewModels<OnBoardingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
        binding?.onBoardingContinueTv?.visibility = View.INVISIBLE
        init()
        return binding?.root
    }

    private fun init() {
        initAdapter()
        setNextBtnVisibility()
        initNextBtn()
        initSkipTV()
    }

    private fun initAdapter() {
        adapter = PagerAdapter(this)
        binding?.let {
            it.onBoardingPager.adapter = adapter
        }
    }

    private fun setNextBtnVisibility() {
        binding?.onBoardingPager?.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                if (position == 0 || position == 1) {
                    Log.d(TAG, "onPageScrolled: $position")
                    binding?.onBoardingNextTv?.visibility = View.VISIBLE
                    binding?.onBoardingSkipTv?.visibility = View.VISIBLE
                    binding?.onBoardingContinueTv?.visibility = View.INVISIBLE

                } else {
                    Log.d(TAG, "onPageScrolled: $position")
                    binding?.onBoardingNextTv?.visibility = View.INVISIBLE
                    binding?.onBoardingSkipTv?.visibility = View.INVISIBLE
                    binding?.onBoardingContinueTv?.visibility = View.VISIBLE

                }
            }
        })
    }

    private fun initNextBtn() {
        binding?.onBoardingNextTv?.setOnClickListener {
            when (binding?.onBoardingPager?.currentItem) {
                0 -> {
                    binding?.onBoardingPager?.currentItem = 1
                }

                1 -> {
                    binding?.onBoardingPager?.currentItem = 2
                }
            }
        }
    }

    private fun initSkipTV() {
        binding?.onBoardingSkipTv?.setOnClickListener {
            this@OnBoardingFragment.findNavController()
                .navigate(R.id.onBoardingFragmentToAuthFragment)
            lifecycleScope.launch {
                viewModel.saveToPref(requireContext(), IS_FIRST_TIME, false)
            }
        }
        binding?.onBoardingContinueTv?.setOnClickListener {
            this@OnBoardingFragment.findNavController()
                .navigate(R.id.onBoardingFragmentToAuthFragment)
            lifecycleScope.launch {
                viewModel.saveToPref(requireContext(), IS_FIRST_TIME, false)
            }
        }
    }

}