package com.developer2t.consumerapp.helper

import android.database.Cursor
import com.developer2t.consumerapp.database.DatabaseContract
import com.developer2t.consumerapp.model.User


object MappingHelper {

    fun mapUserCursorToArrayList(userCursor: Cursor?): ArrayList<User> {
        val userList = ArrayList<User>()

        userCursor?.apply {
            while (moveToNext()) {
                val user = User()
                user.id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns.ID))
                user.username = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.NAME))
                user.images = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.PHOTO))

                userList.add(user)
            }
        }

        return userList
    }
}