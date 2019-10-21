package com.im.layarngaca21.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable


@Entity
@Parcelize
data class Favorite (
    @PrimaryKey(autoGenerate = false)
    var id: String,
    var title: String,
    var date: String,
    var rate: String,
    var synopsis: String,
    var poster: String? = null,
    var category: String

): Parcelable