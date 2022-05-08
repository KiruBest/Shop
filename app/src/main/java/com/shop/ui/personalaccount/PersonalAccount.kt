package com.shop.ui.personalaccount

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shop.R
import com.shop.databinding.FragmentPersonalAccountBinding
import com.shop.firebase.UserDatabase
import com.shop.models.User
import com.shop.ui.main.MainActivity

const val REQUEST_IMAGE_GET = 1

class PersonalAccount : Fragment() {

    private var _binding: FragmentPersonalAccountBinding? = null
    private val binding get() = _binding!!

    private val userDatabase = UserDatabase.instance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPersonalAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        User.currentUser?.let { informationHolding(it) }

        binding.saveChangeButton.setOnClickListener {
            changeUserData()
            Toast.makeText(requireContext(), "Изменения сохранены", Toast.LENGTH_SHORT).show()
        }

        binding.accountLoginDetailsButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                //Таким образом делается анимация, можно сделать её красивее
                //.setCustomAnimations(R.animator.slide_in_bottom, R.animator.slide_in_top)
                //Замена fragment
                .replace(R.id.fragmentContainerView, AccountLoginDetails())
                //Фрагмент добавляется в backStack,
                .addToBackStack("")
                //Грубо говоря применяется изменеие
                .commit()
        }

        binding.accountImageButton.setOnClickListener {
            changeProfilePhoto()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as MainActivity).binding.sort.visibility = Button.VISIBLE
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).binding.sort.visibility = Button.GONE
    }

    @SuppressLint("SetTextI18n")
    private fun informationHolding(user: User) {
        if (user.photo != "") Glide.with(requireActivity()).load(user.photo)
            .into(binding.accountImage)
        binding.loginEditText.setText(user.login)

        when {
            user.name != "" && user.lastname != "" -> {
                binding.fioEditText.setText("${user.lastname} ${user.name}")
            }
            user.lastname != "" -> binding.fioEditText.setText(user.lastname)
        }

        binding.deliveryStreetText.setText(user.street)
        binding.deliveryHouseText.setText(user.home)
        binding.deliveryEntranceText.setText(user.entrance)
        binding.deliveryFlatText.setText(user.flat)
        user.age?.let { binding.ageText.setText(it.toString()) }
    }

    //TODO(Здесь логика смены фото профиля)
    private fun changeProfilePhoto() {
        openGallery()
    }

    private fun changeUserData() {
        User.currentUser?.let {
            val fio = binding.fioEditText.text.toString().split(" ")
            Log.d("fio", fio.toString())

            when (fio.size) {
                2 -> {
                    it.lastname = fio[0]
                    it.name = fio[1]
                }
                1 -> {
                    it.lastname = fio[0]
                    it.name = ""
                }
                else -> {
                    it.lastname = ""
                    it.name = ""
                }
            }

            it.login = binding.loginEditText.text.toString()
            it.street = binding.deliveryStreetText.text.toString()
            it.home = binding.deliveryHouseText.text.toString()
            it.entrance = binding.deliveryEntranceText.text.toString()
            it.flat = binding.deliveryFlatText.text.toString()
            if (binding.ageText.text.toString() != "") it.age =
                binding.ageText.text.toString().toInt()
        }


        (requireActivity() as MainActivity).userDatabase.writeUser(
            Firebase.auth.currentUser?.uid.toString(),
            User.currentUser!!
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK && data != null) {
            data.data?.let { userPhoto ->
                userDatabase.saveProfilePhoto(Firebase.auth.uid.toString(), userPhoto) { url ->
                    User.currentUser?.let {
                        it.photo = url
                        Glide.with(requireContext()).load(it.photo).into(binding.accountImage)
                        requireActivity().invalidateOptionsMenu()
                    }
                }
            }



            Toast.makeText(requireContext(), "Не забудьте сохранить изменения", Toast.LENGTH_SHORT)
                .show()
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun openGallery() {
        val openGalleryIntent = Intent()
        openGalleryIntent.action = Intent.ACTION_GET_CONTENT
        openGalleryIntent.type = "image/*"
        if (openGalleryIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(openGalleryIntent, REQUEST_IMAGE_GET)
        }
    }
}