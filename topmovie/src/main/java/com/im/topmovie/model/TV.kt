package com.im.topmovie.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class TV(
    var id: String,
    var name: String,
    @SerializedName("first_air_date")
    var firsAirDate: String,
    @SerializedName("vote_average")
    var rate: String,
    @SerializedName("overview")
    var synopsis: String,
    @SerializedName("poster_path")
    var poster: String? = null
): Parcelable
