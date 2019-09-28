package com.im.layarngaca21.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class TV(
    var id: String,
    var name: String? = null,
    @SerializedName("first_air_date")
    var firsAirDate: String? = null,
    @SerializedName("vote_average")
    var rate: String,
    @SerializedName("overview")
    var synopsis: String? = null,
    @SerializedName("poster_path")
    var poster: String? = null
): Parcelable
