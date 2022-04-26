package com.shop.presentation.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.shop.databinding.FragmentRegisterBinding
import com.shop.presentation.ui.register.viewmodel.RegisterFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : Fragment() {
    //Смотреть в AuthFragment
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val vm by viewModel<RegisterFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        //Привязка слушателей к кнопке
        initButtonsClick()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initButtonsClick() {
        //Клик по крестику
        binding.close.setOnClickListener {
            (requireContext() as AuthActivity).supportFragmentManager.popBackStack()
        }

        //Привязывается действия по клику на кнопку Подтвердить регистрацию
        binding.buttonReg.setOnClickListener {
            when {
                //Необходимые проверки
                binding.mail.text.isEmpty() -> {
                    Toast.makeText(requireContext(),
                        "Введите почту",
                        Toast.LENGTH_SHORT).show()
                }
                binding.pwd.text.isEmpty() -> {
                    Toast.makeText(requireContext(),
                        "Введите пароль",
                        Toast.LENGTH_SHORT).show()
                }
                binding.pwd.text.toString().length < 6 -> {
                    Toast.makeText(requireContext(),
                        "Пароль должен иметь минимум 6 символов",
                        Toast.LENGTH_SHORT).show()
                }
                binding.pwd.text.toString() != binding.rePwd.text.toString() -> {
                    Toast.makeText(requireContext(),
                        "Пароли не совпадают",
                        Toast.LENGTH_SHORT).show()
                }
                //Если всё норм
                binding.mail.text.isNotEmpty() &&
                        binding.pwd.text.isNotEmpty() &&
                        binding.pwd.text.toString() == binding.rePwd.text.toString() -> {
                    vm.result.observe(requireActivity()) {
                        if (it) {
                            Toast.makeText(requireContext(), "Аккаунт создан", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    //Вызывается метод создания аккаунта
                    vm.createAccount(binding.mail.text.toString(), binding.pwd.text.toString())
                }
            }
        }
    }
}