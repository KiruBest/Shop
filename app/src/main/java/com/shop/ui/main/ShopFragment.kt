package com.shop.ui.main

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
import com.shop.firebase.IProductDatabase
import com.shop.firebase.ProductDatabase
import com.shop.models.Product

class ShopFragment : Fragment() {
    private var _binding: FragmentShopBinding? = null
    private val binding get() = _binding!!

    //Хранит ссылку на обхект для работы с БД
    //Работает с товарами
    private var productDatabase: IProductDatabase = ProductDatabase.instance()

    private lateinit var productAdapter: ProductAdapter

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

        //Grid layout позволяет располагать товары в 2 столбца
        val gridLayoutManager = object : GridLayoutManager(requireContext(), 2) {
            //Запрет скролла самого view, это для красоты и удобства
            override fun canScrollVertically(): Boolean {
                return false
            }
        }

        //Менеджер и адаптер привязывается к RecyclerView
        binding.productRecyclerView.layoutManager = gridLayoutManager
        binding.productRecyclerView.adapter = productAdapter
        setCategoryClick()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productAdapter = ProductAdapter()
    }

    override fun onStart() {
        //Получение всех товаров
        Product.products =
            productDatabase.readProduct(binding.progressBar, productAdapter)
        super.onStart()
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