package com.im.layarngaca21.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Movie(
    var id: String,
    var title: String,
    @SerializedName("release_date")
    var releaseDate: String,
    @SerializedName("vote_average")
    var rate: String,
    @SerializedName("overview")
    var synopsis: String,
    @SerializedName("poster_path")
    var poster: String? = null
): Parcelable
