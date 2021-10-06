package com.example.androidtest.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Breed(
    val id: Int,
    val name: String,
    val bred_for: String?,
    val breed_group: String?,
    @SerializedName("life_span")
    val lifeSpan: String?,
    val temperament: String?,
    val origin: String?,
    @SerializedName("country_code")
    val countryCode: String?,
    @SerializedName("reference_image_id")
    val referenceImageId: String?
) : Parcelable