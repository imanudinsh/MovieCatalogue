package com.im.topmovie.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Trailer(
    var key: String
): Parcelable
