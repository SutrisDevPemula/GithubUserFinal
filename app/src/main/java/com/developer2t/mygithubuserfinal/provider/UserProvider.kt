package com.developer2t.mygithubuserfinal.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.developer2t.mygithubuserfinal.database.DatabaseContract
import com.developer2t.mygithubuserfinal.database.DatabaseContract.AUTHORITY
import com.developer2t.mygithubuserfinal.database.UserHelper

class UserProvider : ContentProvider() {
    companion object {
        private const val TABLE_USER = DatabaseContract.UserColumns.TABLE_NAME
        private val CONTENT_URI_USER = DatabaseContract.UserColumns.CONTENT_URI

        private const val USER = 1
        private const val USER_ID = 2

        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        private lateinit var userHelper: UserHelper

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_USER, USER)
            sUriMatcher.addURI(AUTHORITY, "$TABLE_USER/#", USER_ID)
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (USER) {
            sUriMatcher.match(uri) -> userHelper.insert(values)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI_USER, null)
        return Uri.parse("$CONTENT_URI_USER/$added")
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        var cursor: Cursor? = null
        when (sUriMatcher.match(uri)) {
            USER -> cursor = userHelper.queryAll()
            USER_ID -> cursor = userHelper.queryById(uri.lastPathSegment.toString())
        }
        return cursor
    }

    override fun onCreate(): Boolean {
        userHelper = UserHelper.getInstance(context as Context)
        userHelper.open()

        return true
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val updated: Int = when (USER_ID) {
            sUriMatcher.match(uri) -> userHelper.update(uri.lastPathSegment.toString(), values)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI_USER, null)
        return updated
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (USER_ID) {
            sUriMatcher.match(uri) -> userHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI_USER, null)
        return deleted
    }

    override fun getType(uri: Uri): String? {
        return null
    }
}