package com.example.lensfood1


import android.os.Parcel
import android.os.Parcelable

data class DataCam(
    var dataTitle: String,
    var dataDesc: String,
    var score: Float,
    var karbohidrat: Int,
    var kalori: Int,
    var lemak: Double,
    var protein: Int,
    var durasiBerlari: Int,
    var durasiSitUp: Int,
    var durasiWorkout: Int,
    var durasiSkipping: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readFloat(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(dataTitle)
        parcel.writeString(dataDesc)
        parcel.writeFloat(score)
        parcel.writeInt(karbohidrat)
        parcel.writeInt(kalori)
        parcel.writeDouble(lemak)
        parcel.writeInt(protein)
        parcel.writeInt(durasiBerlari)
        parcel.writeInt(durasiSitUp)
        parcel.writeInt(durasiWorkout)
        parcel.writeInt(durasiSkipping)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DataCam> {
        override fun createFromParcel(parcel: Parcel): DataCam {
            return DataCam(parcel)
        }

        override fun newArray(size: Int): Array<DataCam?> {
            return arrayOfNulls(size)
        }
    }
}
