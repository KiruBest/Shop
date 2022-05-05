package com.shop.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shop.adapters.BasketRecyclerViewAdapter
import com.shop.databinding.FragmentBasketBinding
import com.shop.firebase.BooleanProductCallback
import com.shop.firebase.GetProductCallback
import com.shop.firebase.ProductDatabase
import com.shop.models.BasketProduct
import com.shop.models.Product
import com.shop.ui.productpage.ProductPageActivity

class BasketFragment : Fragment() {
    private var _binding: FragmentBasketBinding? = null
    private val binding get() = _binding!!

    lateinit var adapter: BasketRecyclerViewAdapter

    private val productDatabase = ProductDatabase.instance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentBasketBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productDatabase.getFromBasket(
            uid = Firebase.auth.currentUser?.uid.toString(),
            callback = object : GetProductCallback {
                @SuppressLint("NotifyDataSetChanged")
                override fun callback(products: MutableList<Product>) {
                    BasketProduct.products.clear()
                    BasketProduct.products.addAll(products)
                }
            }
        )

        adapter = BasketRecyclerViewAdapter(
            products = BasketProduct.products,
            object : BasketRecyclerViewAdapter.OnItemClickListener {
                override fun onItemClick(product: Product) {
                    val intent = Intent(requireContext(), ProductPageActivity::class.java)

                    intent.putExtra("product", product)

                    requireActivity().startActivity(intent)
                }

                override fun onDeleteClick(productID: String) {
                    productDatabase.dropProductFromBasket(Firebase.auth.currentUser?.uid.toString(),
                        productID,
                        object : BooleanProductCallback {
                            @SuppressLint("NotifyDataSetChanged")
                            override fun callback(status: Boolean) {
                                if (status) {
                                    Toast.makeText(requireContext(),
                                        "Объект удален из корзины",
                                        Toast.LENGTH_SHORT).show()
                                    adapter.notifyDataSetChanged()
                                }
                            }
                        }
                    )
                }
            }
        )

        binding.basketRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.basketRecyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}