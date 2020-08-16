package com.developer2t.mygithubuserfinal.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.developer2t.mygithubuserfinal.model.User
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class UserViewModel : ViewModel() {

    companion object {
        const val API_KEY = "86055d95f961800427f845e61d5bb262e71c4f8c"
        const val LIST = 100
        const val SEARCH = 101
    }

    private val listUser = MutableLiveData<ArrayList<User>>()

    fun setUsers(type: Int, query: String = "") {
        var url: String? = null

        when (type) {
            LIST -> url = "https://api.github.com/search/users?q=sutris"
            SEARCH -> url = "https://api.github.com/search/users?q=${query}"
        }

        val listItem = ArrayList<User>()
        val asyncClient = AsyncHttpClient()
        asyncClient.addHeader("Authorization", API_KEY)
        asyncClient.addHeader("User-Agent", "request")
        asyncClient.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                try {
                    // parsing JSON
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("items")

                    for (i in 0 until list.length()) {
                        val userItem = User()
                        val jsonObject = list.getJSONObject(i)

                        userItem.images = jsonObject.getString("avatar_url")
                        userItem.username = jsonObject.getString("login")
                        userItem.id = jsonObject.getInt("id")
                        listItem.add(userItem)
                    }
                    listUser.postValue(listItem)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }

            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                Log.d("onFailure", error.message.toString())
            }

        })
    }

    internal fun getUsers(): LiveData<ArrayList<User>> {
        return listUser
    }

}