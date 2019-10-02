package com.im.layarngaca21.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable


@Entity
data class Favorite (
    @PrimaryKey(autoGenerate = false)
    var id: String,
    var title: String,
    var date: String,
    var rate: String,
    var synopsis: String,
    var poster: String,
    var category: String

)