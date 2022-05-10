package com.shop.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shop.R
import com.shop.adapters.ProductAdapter
import com.shop.databinding.FragmentShopBinding
import com.shop.firebase.ProductDatabase
import com.shop.models.BasketProduct
import com.shop.models.FavoriteProduct
import com.shop.models.Product
import com.shop.ui.admin.ADMIN_ID
import com.shop.ui.admin.AddProductActivity
import com.shop.ui.productpage.ProductPageActivity

class ShopFragment : Fragment() {
    private var _binding: FragmentShopBinding? = null
    private val binding get() = _binding!!

    private val productDatabase = ProductDatabase.instance()

    var productRecyclerView: RecyclerView? = null

    var shopProducts = mutableListOf<Product>()

    private var currentCategory: TextView? = null

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
        if (Firebase.auth.currentUser?.uid == ADMIN_ID) binding.buttonPlus.isVisible = true

        initRecyclerView()

        initAddProduct()

        setCategoryClick()

        setLastCategory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecyclerView() {
        val productAdapter = ProductAdapter(Product.products,
            onItemClick = { product ->
                val intent = Intent(requireContext(), ProductPageActivity::class.java)
                intent.putExtra("product", product)
                requireActivity().startActivity(intent)
            },

            onBasketClick = { productID, uid ->
                BasketProduct.products.forEach {
                    if (it.id == productID) {
                        productDatabase.dropProductFromBasket(uid, productID) {
                            Toast.makeText(
                                requireContext(),
                                "Объект удален из корзины",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        return@ProductAdapter
                    }
                }

                productDatabase.addToBasket(productID, uid, 1)
                Toast.makeText(
                    requireContext(),
                    "Объект добавлен в корзину",
                    Toast.LENGTH_SHORT
                ).show()
            },

            onFavoriteClick = { productID, uid ->
                FavoriteProduct.products.forEach {
                    if (it.id == productID) {
                        productDatabase.dropProductFromFavorite(uid, productID) {
                            Toast.makeText(
                                requireContext(),
                                "Объект удален из корзины",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        return@ProductAdapter
                    }
                }

                productDatabase.addToFavorite(productID, uid)
                Toast.makeText(
                    requireContext(),
                    "Объект добавлен в избранное",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )

        //Grid layout позволяет располагать товары в 2 столбца
        val gridLayoutManager = object : GridLayoutManager(requireContext(), 2) {
            //Запрет скролла самого view, это для красоты и удобства
            override fun canScrollVertically(): Boolean {
                return false
            }
        }

        productRecyclerView = binding.productRecyclerView

        //Менеджер и адаптер привязывается к RecyclerView
        productRecyclerView?.apply {
            layoutManager = gridLayoutManager
            adapter = productAdapter
        }
    }

    private fun initAddProduct() {
        binding.buttonPlus.setOnClickListener {
            startActivity(Intent(requireContext(), AddProductActivity::class.java))
        }
    }

    private fun setLastCategory() {
        when (currentCategory?.text.toString()) {
            requireActivity().getString(R.string.categoryChild) -> categoryActivated(binding.child)
            requireActivity().getString(R.string.categoryMan) -> categoryActivated(binding.man)
            requireActivity().getString(R.string.categoryWoman) -> categoryActivated(binding.woman)
        }
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

    private fun categoryActivated(textView: TextView) {
        currentCategory = textView
        currentCategory?.isActivated = true
        currentCategory?.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        addProductCategory(currentCategory!!)
    }

    private fun categoryUnActivated() {
        currentCategory?.isActivated = false
        currentCategory?.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        deleteProductCategory(currentCategory!!)
    }

    //Тут выбранные пункты красятся в черный, а не выбранные в белый
    private fun categoryClick(textView: TextView) {
        when {
            currentCategory == null -> {
                categoryActivated(textView)
            }

            textView != currentCategory -> {
                categoryUnActivated()
                categoryActivated(textView)
            }

            textView == currentCategory -> {
                categoryUnActivated()
                currentCategory = null
            }
        }
    }

    private fun addProductCategory(textView: TextView) {
        productRecyclerView?.adapter?.let {

            when (val category: String = textView.text.toString()) {
                requireActivity().getString(R.string.categoryChild) -> getProductFromCategory(
                    category,
                    (it as ProductAdapter)
                )

                requireActivity().getString(R.string.categoryMan) -> getProductFromCategory(
                    category,
                    (it as ProductAdapter)
                )

                requireActivity().getString(R.string.categoryWoman) -> getProductFromCategory(
                    category,
                    (it as ProductAdapter)
                )
            }
        }
    }

    private fun deleteProductCategory(textView: TextView) {
        productRecyclerView?.adapter?.let {

            when (textView.text.toString()) {
                requireActivity().getString(R.string.categoryChild) -> dropProductFromCategory(
                    (it as ProductAdapter)
                )

                requireActivity().getString(R.string.categoryMan) -> dropProductFromCategory(
                    (it as ProductAdapter)
                )

                requireActivity().getString(R.string.categoryWoman) -> dropProductFromCategory(
                    (it as ProductAdapter)
                )
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getProductFromCategory(category: String, adapter: ProductAdapter) {
        val categoryProducts = mutableListOf<Product>()

        shopProducts.forEach { product ->
            if (product.category == category) {
                categoryProducts.add(product)
            }
        }

        adapter.products.clear()
        adapter.products.addAll(categoryProducts)
        adapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun dropProductFromCategory(adapter: ProductAdapter) {
        adapter.products.clear()
        adapter.products.addAll(shopProducts)
        adapter.notifyDataSetChanged()
    }
}