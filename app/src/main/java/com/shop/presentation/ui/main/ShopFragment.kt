package com.shop.presentation.ui.main

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.shop.R
import com.shop.databinding.FragmentShopBinding
import com.shop.domain.models.Product
import com.shop.domain.models.ProductArray
import com.shop.presentation.adapters.ProductAdapter
import com.shop.presentation.ui.main.viewmodel.MainActivityViewModel

class ShopFragment : Fragment() {
    private var _binding: FragmentShopBinding? = null
    private val binding get() = _binding!!

    private var productArray: ProductArray = ProductArray(mutableListOf<Product>())

    private var productAdapter: ProductAdapter = ProductAdapter(productArray)

    private val activityVM by activityViewModels<MainActivityViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentShopBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Grid layout позволяет располагать товары в 2 столбца
        val gridLayoutManager = object : GridLayoutManager(requireContext(), 2) {
            //Запрет скролла самого view, это для красоты и удобства
            override fun canScrollVertically(): Boolean {
                return false
            }
        }

        activityVM.productsLiveData.observe(requireActivity()) {
            productArray.products.clear()
            productArray.products.addAll(it.products)
            productAdapter.notifyDataSetChanged()
            Log.d(TAG, productArray.products.toString())
        }

        //Получение всех товаров
        activityVM.getProducts()

        //Менеджер и адаптер привязывается к RecyclerView
        binding.productRecyclerView.layoutManager = gridLayoutManager
        binding.productRecyclerView.adapter = productAdapter
        setCategoryClick()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //Обработка кликов сортировки по категориям
    //Сама логика будет в addProductCategory и deleteProductCategory
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

    //Тут выбранные пункты красятся в черный, а не выбранные в белый
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
        when (textView.text) {
            //TODO(Логика добавления товара категории) "" ->)
        }
    }

    private fun deleteProductCategory(textView: TextView) {
        when (textView.text) {
            //TODO (Логика удаления товара категории) "" ->
        }
    }
}