package com.shop.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shop.adapters.FavoriteRecyclerViewAdapter
import com.shop.databinding.FragmentFavoriteBinding
import com.shop.firebase.ProductDatabase
import com.shop.models.BasketProduct
import com.shop.models.FavoriteProduct
import com.shop.ui.productpage.ProductPageActivity

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val productDatabase = ProductDatabase.instance()

    var favoriteRecyclerView: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emptyListCheck()

        favoriteRecyclerView = binding.recyclerViewFavorite

        favoriteRecyclerView?.apply {
            adapter = FavoriteRecyclerViewAdapter(
                FavoriteProduct.products,

                onItemClick = { product ->
                    val intent = Intent(requireContext(), ProductPageActivity::class.java)
                    intent.putExtra("product", product)
                    requireActivity().startActivity(intent)
                },

                onCloseClick = { productID ->
                    productDatabase.dropProductFromFavorite(
                        Firebase.auth.currentUser?.uid.toString(),
                        productID
                    ) { status ->
                        if (status) {
                            for (i in 0 until FavoriteProduct.products.size) {
                                if (FavoriteProduct.products[i].id == productID) {
                                    adapter?.notifyItemRemoved(i)
                                    adapter?.notifyDataSetChanged()
                                    FavoriteProduct.products.removeAt(i)
                                    break
                                }
                            }

                            emptyListCheck()

                            Toast.makeText(
                                requireContext(),
                                "Объект удален из избранного",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
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

                            return@FavoriteRecyclerViewAdapter
                        }
                    }

                    productDatabase.addToBasket(productID, uid, 1)
                    Toast.makeText(
                        requireContext(),
                        "Объект добавлен в корзину",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )

            layoutManager = object : LinearLayoutManager(requireContext()) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        }
    }

    private fun emptyListCheck() {
        if(FavoriteProduct.products.isEmpty()) {
            binding.textViewEmptyWarning.visibility = TextView.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}