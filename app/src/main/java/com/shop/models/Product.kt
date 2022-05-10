package com.shop.models

import android.os.Parcel
import android.os.Parcelable

//Класс хранит информацию о конкретном товаре
data class Product(
    var id: String = "",
    val brand: Int = -1,
    val category: String = "",
    val description: String = "",
    var photo: String = "",
    val price: Float = -1f,
    val title: String = "",
    var count: Int = 1,
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readFloat(),
        parcel.readString()!!,
        parcel.readInt()
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.writeString(id)
        p0?.writeInt(brand)
        p0?.writeString(category)
        p0?.writeString(description)
        p0?.writeString(photo)
        p0?.writeFloat(price)
        p0?.writeString(title)
        p0?.writeInt(count)
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }

        var products: MutableList<Product> = mutableListOf()
    }
}
