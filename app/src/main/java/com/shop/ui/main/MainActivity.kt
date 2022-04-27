package com.shop.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shop.R
import com.shop.databinding.ActivityMainBinding
import com.shop.firebase.GetUserCallback
import com.shop.firebase.UserDatabase
import com.shop.models.Product
import com.shop.models.User
import com.shop.sort.Sorting
import com.shop.ui.register.AuthActivity
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper


class MainActivity : AppCompatActivity() {
    private val shopFragment by lazy { ShopFragment() }
    private val basketFragment by lazy { BasketFragment() }
    private val favoriteFragment by lazy { FavoriteFragment() }

    //Хранится ссылка на объект, который работает с Firebase, скорее всего так лучше не делать
    private val userDatabase = UserDatabase.instance()

    //binding используется для тогоо, чтобы убрать необходимость инициализировать View в коде
    private lateinit var binding: ActivityMainBinding

    //Здесь хранится текущий и предыдущий пункт меню
    private lateinit var currentNav: TextView
    private lateinit var previousNav: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Инициализация текущего пользователя, ниже будет подробнее
        initUser()

        //Инициализация actionBar и ShopFragment
        initToolbar()
        initFirstFragment()

        //Создается навигация по верхним кнопкам
        initTopNav()

        //Здесь теоретически будут пункты сортировки, но я думаю надо будет сделать по-другому
        initSortSpinner()

        //Это просто для красоты, добавляется overscroll
        OverScrollDecoratorHelper.setUpOverScroll(binding.scrollView)
    }

    //Таким образом в actionBar добавляется меню
    //Т.е. Чат, профиль и пункты по нажатию на фото профиля
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //Тут с помощью Glide меняется фото пользователя, которое в actionBar
    //Этот метод также вызывается с помощью другого метода invalidateOptionsMenu()
    //Применение этого метода в классе UserDatabase
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        Glide.with(this).asDrawable().load(User.currentUser?.photo.toString())
            .override(100)
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?,
                ) {
                    menu?.findItem(R.id.account)?.icon = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    //Хз нафиг оно надо
                }
            })
        return super.onPrepareOptionsMenu(menu)
    }

    //Тут назначаются listeners для всех пунктов меню
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile -> {
                //TODO(Переход на страницу профиля)
                Toast.makeText(
                    this,
                    User.currentUser?.toString(),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

            //Выход из аккаунта
            R.id.sign_out -> {
                userDatabase.signOut()
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
            }
            R.id.chat -> {
                //TODO(Тут реализовать чат)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //Клик по кнопке назад
    //Просто устанавливается экран с каталогом и если backStack пуст(а он пуст),
    // то приложение сворачивается
    override fun onBackPressed() {
        changeCurrentNavItem(binding.navShop)
    }

    private fun initUser() {
        //Поиск текущего пользователя в БД
        userDatabase.readCurrentUser(Firebase.auth.currentUser?.uid.toString(),
            object : GetUserCallback {
                override fun callback(currentUser: User) {
                    User.currentUser = currentUser
                    invalidateOptionsMenu()
                }
            })
    }

    private fun initToolbar() {
        //Устанавливается текст и его стиль в actionBar
        binding.mainToolbar.title = getString(R.string.main_toolbar_title)
        binding.mainToolbar.setTitleTextAppearance(this, R.style.toolbar_text)
        setSupportActionBar(binding.mainToolbar)
    }

    private fun initFirstFragment() {
        //При открытии приложения каталог устанавливается выбранным
        //Т.е. заполняется черным
        binding.navShop.isActivated = true
        currentNav = binding.navShop

        supportFragmentManager.beginTransaction().add(R.id.fragmentContainerView, shopFragment)
            .commit()
    }

    private fun initTopNav() {
        //Привязываются действия к кнопка
        //Соответсвенно по нажатию меняется fragment на соответствующий пункту навигации
        //Так реализована навигация
        binding.navShop.setOnClickListener {
            changeCurrentNavItem(it as TextView)
        }

        binding.navBasket.setOnClickListener {
            changeCurrentNavItem(it as TextView)
        }

        binding.navFavorite.setOnClickListener {
            changeCurrentNavItem(it as TextView)
        }

        //По клику на Сортировка появляеся список
        binding.sort.setOnClickListener {
            binding.sortSpinner.performClick()
        }
    }

    //Логика смены страниц
    private fun changeCurrentNavItem(textView: TextView) {
        if (currentNav == textView) return
        var fragment: Fragment? = null
        previousNav = currentNav
        currentNav = textView
        previousNav.isActivated = false
        currentNav.isActivated = true

        //В зависимости от текста на копке устанавливается нужный фрагмент
        when (textView.text) {
            getString(R.string.catalog) -> fragment = shopFragment
            getString(R.string.basket) -> fragment = basketFragment
            getString(R.string.favorite) -> fragment = favoriteFragment
        }

        //Также как в AuthFragment производится смена fragment
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_in_right)
            .replace(R.id.fragmentContainerView, fragment!!)
            .commit()
    }

    //Здесь для выпадающего списка по клику на Сотрировка создаются пункты
    //Возможно стоит переделать
    @SuppressLint("ResourceType", "UseCompatLoadingForDrawables")
    private fun initSortSpinner() {
        //Назначение адаптера - преобразовать один вид информации в другой,
        // без вмешательства в исходное состояние информации.

        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this,
            R.array.sort_category, R.layout.support_simple_spinner_dropdown_item
        )
        //Установка адаптера
        binding.sortSpinner.adapter = adapter
        binding.sortSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            //Функция для клика
            @SuppressLint("NotifyDataSetChanged")
            override fun onItemSelected(
                parent: AdapterView<*>?,
                itemSelected: View?, selectedItemPosition: Int, selectedId: Long,
            ) {
                // Получаем все элементы из спиннера
                val sortName = resources.getStringArray(R.array.sort_category)
                //Получаем текст из нажатого textview
                val text: String = binding.sortSpinner.selectedItem.toString()
                val s = Sorting()

                //Сравниваем значения полученное при нажатии с каждым элементом спинера
                when (text) {
                    sortName[0] -> {
                        Product.products = s.sortName(Product.products).toMutableList()
                    }
                    sortName[1] -> {
                        Product.products = s.sortNameDec(Product.products).toMutableList()
                    }
                    sortName[2] -> {
                        Product.products = s.sortPrice(Product.products).toMutableList()
                    }
                    sortName[3] -> {
                        Product.products = s.sorPriceDec(Product.products).toMutableList()
                    }
                }

                Log.d("categoryProducts", Product.products.toString())

                shopFragment.productAdapter.notifyDataSetChanged()
            }

            //Используется, когда ни на что не кликнули
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }


}
