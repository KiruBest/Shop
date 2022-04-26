package com.shop.presentation.ui.register

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shop.R
import com.shop.databinding.ActivityAuthBinding
import com.shop.presentation.ui.main.MainActivity

class AuthActivity : AppCompatActivity() {
    //Используется вместо явного объявления элементов view
    //То есть вместо findViewBy(R.id...)
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        //Таким образом создается binding.
        //Для других activity или fragment меняется ActivityAuthBinding
        binding = ActivityAuthBinding.inflate(layoutInflater)

        //Если пользователь уже ранее вошел в аккаунт, то сразу запускается MainActivity
        //В Firebase есть объект auth, в котором хранится текущий пользователь
        if (Firebase.auth.currentUser != null){
            setActivity()
        }

        //С помощью supportFragmentManager можно устанавливать фрагмент в fragmentContainerView
        //То есть можно во время работы приложения можно менять фрагменты с помощью beginTransaction
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainerView, AuthFragment())
            .commit()
    }

    //Метод отвечает за обработку кнопки назад
    override fun onBackPressed() {
        //Соответсвно если нажимается кнопка назад, то fragment меняется на предыдущий
        //либо приложение сворачивается
        supportFragmentManager.popBackStack()
    }

    fun setActivity() {
        //Стандартное действие для смены текущего activity
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}