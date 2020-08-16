package com.developer2t.mygithubuserfinal.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.developer2t.mygithubuserfinal.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class ViewModelListFollow : ViewModel() {

    private val apiKey = "86055d95f961800427f845e61d5bb262e71c4f8c"
    private val listFollowers = MutableLiveData<ArrayList<User>>()

    companion object {
        const val FOLLOWERS = 100
        const val FOLLOWING = 101
    }

    fun setFollowers(type: Int, query: String) {
        var url: String? = ""

        when (type) {
            FOLLOWING -> url = "https://api.github.com/users/${query}/following"
            FOLLOWERS -> url = "https://api.github.com/users/${query}/followers"
        }

        val list = ArrayList<User>()
        val asyncClient = AsyncHttpClient()

        asyncClient.addHeader("Authorization", apiKey)
        asyncClient.addHeader("User-Agent", "request")
        asyncClient.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                // Koneksi berhasil

                try {
                    // parsing JSON
                    val result = String(responseBody)
                    val jsonArray = JSONArray(result)

                    for (i in 0 until jsonArray.length()) {
                        val users = User()
                        val jsonObject = jsonArray.getJSONObject(i)

                        users.username = jsonObject.getString("login")
                        users.images = jsonObject.getString("avatar_url")
                        users.id = jsonObject.getInt("id")

                        list.add(users)
                    }
                    listFollowers.postValue(list)
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
                // Koneksi gagal
                Log.d("onFailure", error.message.toString())
            }

        })

    }

    internal fun getFollowers(): LiveData<ArrayList<User>> {
        return listFollowers
    }
}