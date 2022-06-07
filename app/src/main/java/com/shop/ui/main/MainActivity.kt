package com.shop.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shop.R
import com.shop.adapters.BasketRecyclerViewAdapter
import com.shop.adapters.FavoriteRecyclerViewAdapter
import com.shop.adapters.ProductAdapter
import com.shop.databinding.ActivityMainBinding
import com.shop.firebase.ProductDatabase
import com.shop.firebase.UserDatabase
import com.shop.models.BasketProduct
import com.shop.models.FavoriteProduct
import com.shop.models.Product
import com.shop.models.User
import com.shop.sort.Sorting
import com.shop.ui.admin.ADMIN_ID
import com.shop.ui.chat.AdminChatActivity
import com.shop.ui.chat.ChatActivity
import com.shop.ui.personalaccount.PersonalAccount
import com.shop.ui.productpage.ProductPageActivity
import com.shop.ui.register.AuthActivity


class MainActivity : AppCompatActivity() {
    private lateinit var shopFragment: ShopFragment
    private lateinit var basketFragment: BasketFragment
    private lateinit var favoriteFragment: FavoriteFragment

    private lateinit var searchAdapter: ProductAdapter

    private val productDatabase = ProductDatabase.instance()

    //Хранится ссылка на объект, который работает с Firebase, скорее всего так лучше не делать
    val userDatabase = UserDatabase.instance()

    //binding используется для тогоо, чтобы убрать необходимость инициализировать View в коде
    lateinit var binding: ActivityMainBinding

    //Здесь хранится текущий и предыдущий пункт меню
    private var currentNav: TextView? = null
    private lateinit var previousNav: TextView

    @SuppressLint("NotifyDataSetChanged")
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

        //Добавление из базы данных
        initDataFromDatabase()

        //Здесь теоретически будут пункты сортировки, но я думаю надо будет сделать по-другому
        initSortSpinner()

        searchAdapter = ProductAdapter(
            mutableListOf(),
            onItemClick = { product ->
                val intent = Intent(this, ProductPageActivity::class.java)
                intent.putExtra("product", product)
                startActivity(intent)
            },

            onBasketClick = { productID, uid ->
                BasketProduct.products.forEach {
                    if (it.id == productID) {
                        productDatabase.dropProductFromBasket(uid, productID) {
                            Toast.makeText(
                                this,
                                "Объект удален из корзины",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        return@ProductAdapter
                    }
                }

                Product.products.forEach {
                    if (it.id == productID) {
                        MaterialAlertDialogBuilder(this)
                            .setTitle(resources.getString(R.string.pick_size))
                            .setItems(it.sizes.toTypedArray()) { _, _ ->
                                productDatabase.addToBasket(productID, uid, 1)
                                Toast.makeText(
                                    this,
                                    "Объект добавлен в корзину",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            .show()
                        return@ProductAdapter
                    }
                }
            },

            onFavoriteClick = { productID, uid ->
                FavoriteProduct.products.forEach {
                    if (it.id == productID) {
                        productDatabase.dropProductFromFavorite(uid, productID) {
                            Toast.makeText(
                                this,
                                "Объект удален из корзины",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        return@ProductAdapter
                    }
                }

                productDatabase.addToFavorite(productID, uid)
                Toast.makeText(
                    this,
                    "Объект добавлен в избранное",
                    Toast.LENGTH_SHORT
                ).show()
            })

        binding.searchRecyclerView.apply {
            adapter = searchAdapter
            layoutManager = GridLayoutManager(this@MainActivity, 2)
        }

        binding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                return
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                return
            }

            override fun afterTextChanged(p0: Editable?) {
                val text = p0.toString()
                if (text == "") {
                    binding.searchRecyclerView.visibility = View.GONE
                    binding.scrollView.visibility = View.VISIBLE
                } else {
                    binding.searchRecyclerView.visibility = View.VISIBLE
                    binding.scrollView.visibility = View.GONE

                    val searchProducts = mutableListOf<Product>()

                    Product.products.forEach { product ->
                        if (product.title.contains(text, true) || product.description.contains(
                                text,
                                true
                            )
                        ) {
                            searchProducts.add(product)
                        }
                    }

                    searchAdapter.filter(searchProducts)
                }
            }

        })
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
                deleteCurrentNavMenu()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, PersonalAccount()).commit()
            }

            //Выход из аккаунта
            R.id.sign_out -> {
                userDatabase.signOut()
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
            }
            R.id.chat -> {
                val intent: Intent = if (Firebase.auth.currentUser?.uid == ADMIN_ID) {
                    Intent(this, AdminChatActivity::class.java)
                } else {
                    Intent(this, ChatActivity::class.java)
                }
                startActivity(intent)
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
        Firebase.auth.currentUser?.uid?.let { uid ->
            userDatabase.readCurrentUser(uid) { currentUser ->
                User.currentUser = currentUser
                invalidateOptionsMenu()
            }
        }
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
        basketFragment = BasketFragment()

        favoriteFragment = FavoriteFragment()

        shopFragment = ShopFragment()

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

        if (currentNav == null) {
            currentNav = textView
        }

        var fragment: Fragment? = null
        previousNav = currentNav!!
        currentNav = textView
        previousNav.isActivated = false
        currentNav?.isActivated = true

        //В зависимости от текста на копке устанавливается нужный фрагмент
        when (textView.text) {
            getString(R.string.catalog) -> fragment = shopFragment
            getString(R.string.basket) -> fragment = basketFragment
            getString(R.string.favorite) -> fragment = favoriteFragment
        }

        //Также как в AuthFragment производится смена fragment
        fragment?.let { it ->
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_in_right)
                .replace(R.id.fragmentContainerView, it)
                .commit()
        }
    }

    private fun deleteCurrentNavMenu() {
        currentNav?.isActivated = false
        currentNav = null
    }

    //Здесь для выпадающего списка по клику на Сотрировка создаются пункты
    //Возможно стоит переделать
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
                val sorting = Sorting()
                val temp = mutableListOf<Product>()

                //Сравниваем значения полученное при нажатии с каждым элементом спинера
                when (text) {
                    sortName[1] -> {
                        shopFragment.productRecyclerView?.adapter?.let {
                            temp.clear()
                            temp.addAll((it as ProductAdapter).products)
                            it.products.clear()
                            it.products.addAll(sorting.sortNameDec(temp))
                            it.notifyItemRangeChanged(0, it.itemCount)

                            shopFragment.shopProducts =
                                sorting.sortNameDec(shopFragment.shopProducts).toMutableList()
                        }

                        basketFragment.basketRecyclerView?.adapter?.let {
                            temp.clear()
                            temp.addAll((it as BasketRecyclerViewAdapter).products)
                            it.products.clear()
                            it.products.addAll(sorting.sortNameDec(temp))
                            it.notifyItemRangeChanged(0, it.itemCount)
                        }

                        favoriteFragment.favoriteRecyclerView?.adapter?.let {
                            temp.clear()
                            temp.addAll((it as FavoriteRecyclerViewAdapter).products)
                            it.products.clear()
                            it.products.addAll(sorting.sortNameDec(temp))
                            it.notifyItemRangeChanged(0, it.itemCount)
                        }
                    }
                    sortName[0] -> {
                        shopFragment.productRecyclerView?.adapter?.let {
                            temp.clear()
                            temp.addAll((it as ProductAdapter).products)
                            it.products.clear()
                            it.products.addAll(sorting.sortName(temp))
                            it.notifyItemRangeChanged(0, it.itemCount)

                            shopFragment.shopProducts =
                                sorting.sortName(shopFragment.shopProducts).toMutableList()
                        }

                        basketFragment.basketRecyclerView?.adapter?.let {
                            temp.clear()
                            temp.addAll((it as BasketRecyclerViewAdapter).products)
                            it.products.clear()
                            it.products.addAll(sorting.sortName(temp))
                            it.notifyItemRangeChanged(0, it.itemCount)
                        }

                        favoriteFragment.favoriteRecyclerView?.adapter?.let {
                            temp.clear()
                            temp.addAll((it as FavoriteRecyclerViewAdapter).products)
                            it.products.clear()
                            it.products.addAll(sorting.sortName(temp))
                            it.notifyItemRangeChanged(0, it.itemCount)
                        }
                    }
                    sortName[2] -> {
                        shopFragment.productRecyclerView?.adapter?.let {
                            temp.clear()
                            temp.addAll((it as ProductAdapter).products)
                            it.products.clear()
                            it.products.addAll(sorting.sortPrice(temp))
                            it.notifyItemRangeChanged(0, it.itemCount)

                            shopFragment.shopProducts =
                                sorting.sortPrice(shopFragment.shopProducts).toMutableList()
                        }

                        basketFragment.basketRecyclerView?.adapter?.let {
                            temp.clear()
                            temp.addAll((it as BasketRecyclerViewAdapter).products)
                            it.products.clear()
                            it.products.addAll(sorting.sortPrice(temp))
                            it.notifyItemRangeChanged(0, it.itemCount)
                        }

                        favoriteFragment.favoriteRecyclerView?.adapter?.let {
                            temp.clear()
                            temp.addAll((it as FavoriteRecyclerViewAdapter).products)
                            it.products.clear()
                            it.products.addAll(sorting.sortPrice(temp))
                            it.notifyItemRangeChanged(0, it.itemCount)
                        }
                    }
                    sortName[3] -> {
                        shopFragment.productRecyclerView?.adapter?.let {
                            temp.clear()
                            temp.addAll((it as ProductAdapter).products)
                            it.products.clear()
                            it.products.addAll(sorting.sortPriceDec(temp))
                            it.notifyItemRangeChanged(0, it.itemCount)

                            shopFragment.shopProducts =
                                sorting.sortPriceDec(shopFragment.shopProducts).toMutableList()
                        }

                        basketFragment.basketRecyclerView?.adapter?.let {
                            temp.clear()
                            temp.addAll((it as BasketRecyclerViewAdapter).products)
                            it.products.clear()
                            it.products.addAll(sorting.sortPriceDec(temp))
                            it.notifyItemRangeChanged(0, it.itemCount)
                        }

                        favoriteFragment.favoriteRecyclerView?.adapter?.let {
                            temp.clear()
                            temp.addAll((it as FavoriteRecyclerViewAdapter).products)
                            it.products.clear()
                            it.products.addAll(sorting.sortPriceDec(temp))
                            it.notifyItemRangeChanged(0, it.itemCount)
                        }
                    }
                }
            }

            //Используется, когда ни на что не кликнули
            override fun onNothingSelected(parent: AdapterView<*>?) {
                return
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initDataFromDatabase() {
        binding.progressBar.visibility = ProgressBar.VISIBLE
        productDatabase.readProduct { products ->
            Product.products.clear()
            Product.products.addAll(products)
            shopFragment.shopProducts.clear()
            shopFragment.shopProducts.addAll(products)
            shopFragment.productRecyclerView?.adapter?.notifyDataSetChanged()

            Firebase.auth.currentUser?.uid?.let {
                productDatabase.getFromFavorite(it) { products ->
                    FavoriteProduct.products.clear()
                    FavoriteProduct.products.addAll(products)
                    favoriteFragment.favoriteRecyclerView?.adapter?.notifyDataSetChanged()
                    shopFragment.productRecyclerView?.adapter?.notifyDataSetChanged()
                }

                productDatabase.getFromBasket(it) { products ->
                    BasketProduct.products.clear()
                    BasketProduct.products.addAll(products)
                    basketFragment.basketRecyclerView?.adapter?.notifyDataSetChanged()
                    shopFragment.productRecyclerView?.adapter?.notifyDataSetChanged()
                }
            }

            binding.progressBar.visibility = ProgressBar.GONE
        }
    }
}
