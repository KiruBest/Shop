package com.shop.ui.personalaccount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shop.databinding.FragmentAccountLoginDetailsBinding
import com.shop.models.User

class AccountLoginDetails : Fragment() {

    private var _binding: FragmentAccountLoginDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAccountLoginDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.emailEditText.setText(User.currentUser?.email.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}