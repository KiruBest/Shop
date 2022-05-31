package com.shop.ui.payment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import com.redmadrobot.inputmask.MaskedTextChangedListener
import com.shop.R
import com.shop.databinding.FragmentPaymentBinding
import java.sql.Time
import java.util.*

class PaymentFragment : Fragment() {

    private lateinit var binding: FragmentPaymentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = requireArguments()
        binding.textViewPaymentEndSumValue.text = bundle.getString("sum")

        binding.editViewCardNumber.addTextChangedListener(
            MaskedTextChangedListener(
                "[0000] [0000] [0000] [0000]",
                binding.editViewCardNumber
            )
        )

        binding.editViewMonth.addTextChangedListener(
            MaskedTextChangedListener(
                "[00]{/}[00]]",
                binding.editViewMonth
            )
        )

        binding.editViewCVV.addTextChangedListener(
            MaskedTextChangedListener(
                "[000]",
                binding.editViewCVV
            )
        )

        binding.buttonPaymentPay.setOnClickListener {
            val date = binding.editViewMonth.text.split("/")
            val time = Calendar.getInstance()
            val year = time.get(Calendar.YEAR) - 2000
            val month = time.get(Calendar.MONTH) + 1

            Log.i("TAGDATE", date[1].toInt().toString())


            when {
                binding.editViewCardNumber.text.length != 19 -> Toast.makeText(
                    requireContext(),
                    "Неверный номер карты",
                    Toast.LENGTH_SHORT
                )
                    .show()
                binding.editViewMonth.text.length != 5 || date[0].toInt() > 12 || date[0].toInt() < month || date[1].toInt() < year -> Toast.makeText(
                    requireContext(),
                    "Неверная дата",
                    Toast.LENGTH_SHORT
                )
                    .show()
                binding.editViewCVV.text.length != 3 -> Toast.makeText(
                    requireContext(),
                    "Неверный код CVC/CVV",
                    Toast.LENGTH_SHORT
                )
                    .show()
                binding.editViewNameCardholder.text.isEmpty() -> Toast.makeText(
                    requireContext(),
                    "Неверно указаны имя и фамилия",
                    Toast.LENGTH_SHORT
                )
                    .show()
                else -> {
                    val fragment = PaymentFragmentWait()
                    requireActivity().supportFragmentManager.beginTransaction().addToBackStack(null)
                        .replace(R.id.fragmentContainerView, fragment).commit()
                }
            }

        }
        binding.buttonPaymentBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }


}