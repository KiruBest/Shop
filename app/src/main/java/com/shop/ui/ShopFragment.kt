package com.shop.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.shop.R
import com.shop.adapters.ProductAdapter
import com.shop.databinding.FragmentShopBinding

class ShopFragment : Fragment() {
    private var _binding: FragmentShopBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentShopBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.productRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.productRecyclerView.adapter = productAdapter
        setCategoryClick()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        //TODO Здесь Должен Быть твой код!
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setCategoryClick() {
        binding.man.setOnClickListener {
            categoryClick(it as TextView)
        }

        binding.woman.setOnClickListener {
            categoryClick(it as TextView)
        }

        binding.child.setOnClickListener {
            categoryClick(it as TextView)
        }
    }

    private fun categoryClick(textView: TextView) {
        if (textView.isActivated) {
            textView.isActivated = false
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            addProductCategory(textView)
        } else {
            textView.isActivated = true
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            deleteProductCategory(textView)
        }
    }

    private fun addProductCategory(textView: TextView) {
        when(textView.text){
            //TODO Логика смены категорий "" ->
        }
    }

    private fun deleteProductCategory(textView: TextView) {
        when(textView.text){
            //TODO Логика смены категорий "" ->
        }
    }

    companion object {
        val productAdapter = ProductAdapter()
    }
}