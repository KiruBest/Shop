package com.shop.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shop.adapters.BasketRecyclerViewAdapter
import com.shop.databinding.FragmentBasketBinding
import com.shop.firebase.ProductDatabase
import com.shop.models.BasketProduct
import com.shop.ui.productpage.ProductPageActivity

class BasketFragment : Fragment() {
    private var _binding: FragmentBasketBinding? = null
    private val binding get() = _binding!!

    private val productDatabase = ProductDatabase.instance()

    var basketRecyclerView: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentBasketBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emptyListCheck()

        val basketRecyclerViewAdapter = BasketRecyclerViewAdapter(BasketProduct.products,
            onItemClick = { product ->
                val intent = Intent(requireContext(), ProductPageActivity::class.java)
                intent.putExtra("product", product)
                requireActivity().startActivity(intent)
            },

            onDeleteClick = { productID ->
                productDatabase.dropProductFromBasket(
                    Firebase.auth.currentUser?.uid.toString(),
                    productID
                ) { status ->
                    if (status) {
                        for (i in 0 until BasketProduct.products.size) {
                            if (BasketProduct.products[i].id == productID) {
                                basketRecyclerView?.adapter?.notifyItemRemoved(i)
                                basketRecyclerView?.adapter?.notifyDataSetChanged()
                                BasketProduct.products.removeAt(i)
                                break
                            }
                        }

                        sum()

                        emptyListCheck()

                        Toast.makeText(
                            requireContext(),
                            "Объект удален из корзины",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        )

        basketRecyclerView = binding.basketRecyclerView

        basketRecyclerView?.apply {
            layoutManager = object : LinearLayoutManager(requireContext()) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            adapter = basketRecyclerViewAdapter
        }

        sum()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    fun sum() {
        binding.textViewSum.apply {
            var sum = 0f
            BasketProduct.products.forEach {
                sum += it.count * it.price
            }

            text = "$sum₽"
        }
    }

    private fun emptyListCheck() {
        if(BasketProduct.products.isEmpty()) {
            binding.textViewEmptyWarning.visibility = TextView.VISIBLE
            binding.constraintLayoutBottom.visibility = ConstraintLayout.GONE
        }
    }
}