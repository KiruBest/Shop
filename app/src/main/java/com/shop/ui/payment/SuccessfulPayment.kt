package com.shop.ui.payment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shop.R


class SuccessfulPayment: Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment_successful, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val handler = Handler()
        handler.postDelayed({
            requireActivity().onBackPressed()
        },3000)
        super.onViewCreated(view, savedInstanceState)
    }

}