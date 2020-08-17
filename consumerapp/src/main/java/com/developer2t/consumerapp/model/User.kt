package com.developer2t.consumerapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Suppress("PLUGIN_WARNING")
@Parcelize
class User : Parcelable {
    var username: String? = null
    var name: String? = null
    var images: String? = null
    var id: Int? = 0
    var company: String? = null
    var location: String? = null
    var follower: Int? = 0
    var followings: Int? = 0
    var repo: Int? = 0
}