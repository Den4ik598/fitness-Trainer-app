package com.malkinfo.editingrecyclerview.data

import android.os.Parcel
import android.os.Parcelable

class CaloriesData(val id: Int, val name: String, val volume: Int) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeInt(volume)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CaloriesData> {
        override fun createFromParcel(parcel: Parcel): CaloriesData {
            return CaloriesData(parcel)
        }

        override fun newArray(size: Int): Array<CaloriesData?> {
            return arrayOfNulls(size)
        }
    }

}