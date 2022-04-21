package com.shop.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.shop.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    //Смотреть в AuthFragment
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
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
                    //Вызывается метод создания аккаунта
                    (requireContext() as AuthActivity).userDatabase
                        .createAccount(binding.mail.text.toString(),
                        binding.pwd.text.toString(), requireActivity())
                    //Окно регистрации меняется на предыдущее
                    (requireContext() as AuthActivity).supportFragmentManager.popBackStack()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}