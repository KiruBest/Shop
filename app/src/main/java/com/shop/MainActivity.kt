package com.shop

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.shop.database.DB
import com.shop.databinding.ActivityMainBinding
import com.shop.ui.BasketFragment
import com.shop.ui.ShopFragment

class MainActivity : AppCompatActivity() {
    //binding используется для тогоо, чтобы убрать необходимость инициализировать View в коде
    private lateinit var binding: ActivityMainBinding
    private lateinit var currentNavMenu: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initDataFromDb()
        toolbarInit()
        initTopNav()
        initFirstFragment()
        initSortSpinner()
    }

    override fun onStart() {
        super.onStart()
        //TODO Пиши код сюда
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun toolbarInit() {
        binding.mainToolbar.title = getString(R.string.main_toolbar_title)
        binding.mainToolbar.setTitleTextAppearance(this, R.style.toolbar_text)
        setSupportActionBar(binding.mainToolbar)
    }

    private fun initDataFromDb() {
        DB.instance.getProduct()
    }

    @SuppressLint("NewApi")
    private fun initFirstFragment() {
        supportFragmentManager.beginTransaction().add(R.id.fragmentContainerView, ShopFragment())
            .commit()
        currentNavMenu = binding.navShop
        currentNavMenu.isActivated = true
        currentNavMenu.setTextColor(getColor(R.color.white))
    }

    private fun initTopNav() {
        binding.navShop.setOnClickListener {
            navClick(it as TextView, ShopFragment())
        }

        binding.navBasket.setOnClickListener {
            navClick(it as TextView, BasketFragment())
        }

        binding.navFavorite.setOnClickListener {
            navClick(it as TextView, BasketFragment())
        }

        binding.sort.setOnClickListener {
            binding.sortSpinner.performClick()
        }
    }

    @SuppressLint("ResourceType", "UseCompatLoadingForDrawables")
    private fun initSortSpinner() {
        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(this,
            R.array.sort_category, R.layout.support_simple_spinner_dropdown_item)
        binding.sortSpinner.adapter = adapter
    }

    @SuppressLint("NewApi")
    private fun navClick(textView: TextView, fragment: Fragment) {
        if (currentNavMenu == textView) return
        currentNavMenu.isActivated = false
        currentNavMenu.setTextColor(getColor(R.color.black))
        currentNavMenu = textView
        currentNavMenu.isActivated = true
        currentNavMenu.setTextColor(getColor(R.color.white))

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .commit()
    }

    fun changeActionBar(title: String) {
        binding.mainToolbar.title = title
        setSupportActionBar(binding.mainToolbar)
    }
}