package me.martichou.unswayedphotos.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RMine(
    @field:Json(name = "realname") val realname: String,
    @field:Json(name = "fakedname") val fakedname: String,
    @field:Json(name = "created_at") val createdAt: String
) : Parcelable