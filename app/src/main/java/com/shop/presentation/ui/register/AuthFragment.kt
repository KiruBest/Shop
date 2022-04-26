package com.shop.presentation.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.shop.R
import com.shop.databinding.FragmentAuthBinding
import com.shop.domain.firebase.BooleanCallback
import com.shop.presentation.ui.register.viewmodel.AuthFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthFragment : Fragment() {

    //Во фрагменте binding создается немного иным способом
    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    private val vm by viewModel<AuthFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        //Привязывается действия по нажатию на кнопку
        setButtonClickListener()
    }

    //Метод вызывается при закрытии фрагмента
    //Binding уничтожается для того, чтобы не уходила утечка памяти
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setButtonClickListener() {
        //Кнопке Войти привязывается действие
        //Проверяются все поля, возможно стоит их поправить немного
        binding.buttonAuth.setOnClickListener {
            when {
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

                //Если все поля заполнены, то осуществляем авторизацию и смену activity
                //В данном случае размывается отвественность данного класса, но зато просто
                //Можно отрефакторить, создать новые классы, разбить логику
                //По крайней мере как-нибудь избавиться от вызова методов activity
                //Но можно и так оставить, работает же
                binding.mail.text.isNotEmpty() &&
                        binding.pwd.text.isNotEmpty() -> {
                    //Вызывается метод авторизации
                    vm.auth(binding.mail.text.toString(),
                        binding.pwd.text.toString(),
                        object : BooleanCallback {
                            override fun onCallback(status: Boolean) {
                                //Меняется текущее activity
                                if (status) {
                                    (requireContext() as AuthActivity).setActivity()
                                } else Toast.makeText(requireContext(),
                                    "Неверный логин или пароль",
                                    Toast.LENGTH_SHORT).show()
                            }
                        })

                }
            }
        }

        //По клику на кнопку Регистрация открывается fragment регистрации
        binding.buttonReg.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                //Таким образом делается анимация, можно сделать её красивее
                .setCustomAnimations(R.animator.slide_in_bottom, R.animator.slide_in_top)
                //Замена fragment
                .replace(R.id.fragmentContainerView, RegisterFragment())
                //Фрагмент добавляется в backStack,
                .addToBackStack("")
                //Грубо говоря применяется изменеие
                .commit()
        }
    }
}