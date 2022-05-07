package com.shop.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shop.R
import com.shop.adapters.ProductAdapter
import com.shop.databinding.FragmentShopBinding
import com.shop.firebase.GetProductCallback
import com.shop.firebase.ProductDatabase
import com.shop.models.BasketProduct
import com.shop.models.Product
import com.shop.ui.admin.ADMIN_ID
import com.shop.ui.admin.AddProductActivity
import com.shop.ui.productpage.ProductPageActivity

class ShopFragment : Fragment() {
    private var _binding: FragmentShopBinding? = null
    private val binding get() = _binding!!

    val progressBar: ProgressBar by lazy { binding.progressBar }

    private val productDatabase = ProductDatabase.instance()

    lateinit var productAdapter: ProductAdapter

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

        progressBar.visibility = ProgressBar.VISIBLE
        productDatabase.readProduct(object : GetProductCallback {
            @SuppressLint("NotifyDataSetChanged")
            override fun callback(products: MutableList<Product>) {
                Product.products.clear()
                Product.products.addAll(products)
                productAdapter.notifyDataSetChanged()
                Log.d("productArray", Product.products.toString())
                progressBar.visibility = ProgressBar.GONE
            }
        })

        initAddProduct()

        initRecyclerView()
        setCategoryClick()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecyclerView() {
        val productRecyclerView = binding.productRecyclerView

        productAdapter =
            ProductAdapter(
                Product.products, object : ProductAdapter.OnItemClickListener {
                    override fun onItemClick(product: Product) {
                        val intent = Intent(requireContext(), ProductPageActivity::class.java)

                        intent.putExtra("product", product)

                        requireActivity().startActivity(intent)
                    }

                    override fun onBasketClick(productID: String, uid: String) {
                        BasketProduct.products.forEach {
                            if (it.id == productID) {
                                Toast.makeText(requireContext(),
                                    "Объект уже в корзине",
                                    Toast.LENGTH_SHORT)
                                    .show()
                                return@onBasketClick
                            }
                        }
                        productDatabase.addToBasket(productID, uid, 1)
                        Toast.makeText(requireContext(),
                            "Объект добавлен в корзину",
                            Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onFavoriteClick(productID: String, uid: String) {
                        BasketProduct.products.forEach {
                            if (it.id == productID) {
                                //Toast.makeText(requireContext(), "Объект уже в корзине", Toast.LENGTH_SHORT).show()
                                return@onFavoriteClick
                            }
                        }
                    }
                }
            )

        //Grid layout позволяет располагать товары в 2 столбца
        val gridLayoutManager = object : GridLayoutManager(requireContext(), 2) {
            //Запрет скролла самого view, это для красоты и удобства
            override fun canScrollVertically(): Boolean {
                return false
            }
        }

        //Менеджер и адаптер привязывается к RecyclerView
        productRecyclerView.layoutManager = gridLayoutManager
        productRecyclerView.adapter = productAdapter
        productAdapter.notifyDataSetChanged()
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

    private fun initAddProduct() {
        binding.buttonPlus.setOnClickListener {
            startActivity(Intent(requireContext(), AddProductActivity::class.java))
        }
    }

    private fun addProductCategory(textView: TextView) {
        when (textView.text) {

        }
    }

    private fun deleteProductCategory(textView: TextView) {
        when (textView.text) {
            //TODO (Логика удаления товара категории) "" ->
        }
    }
}