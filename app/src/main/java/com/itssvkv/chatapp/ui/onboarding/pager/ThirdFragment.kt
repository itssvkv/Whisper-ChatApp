package com.itssvkv.chatapp.ui.onboarding.pager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.itssvkv.chatapp.databinding.FragmentThirdBinding


class ThirdFragment : Fragment() {
    private var binding: FragmentThirdBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentThirdBinding.inflate(layoutInflater)
        return binding?.root
    }

}