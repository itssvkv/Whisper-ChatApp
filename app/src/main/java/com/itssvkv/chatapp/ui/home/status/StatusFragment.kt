package com.itssvkv.chatapp.ui.home.status

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.itssvkv.chatapp.R
import com.itssvkv.chatapp.databinding.FragmentStatusBinding


class StatusFragment : Fragment() {

  private var binding: FragmentStatusBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatusBinding.inflate(inflater, container, false)
        return binding?.root
    }

}