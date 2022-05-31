package com.shop.ui.payment

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shop.R
import java.util.*


class PaymentFragmentWait : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment_wait, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val handler = Handler()
        handler.postDelayed({ requireActivity().supportFragmentManager.popBackStack() },4000)
        super.onViewCreated(view, savedInstanceState)
    }

}