package com.developer2t.mygithubuserfinal.database


import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.developer2t.mygithubuserfinal"
    const val SCHEME = "content"

    internal class UserColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "userfav"
            const val ID = "_id"
            const val PHOTO = "images"
            const val NAME = "name"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY).appendPath(TABLE_NAME).build()


        }
    }
}