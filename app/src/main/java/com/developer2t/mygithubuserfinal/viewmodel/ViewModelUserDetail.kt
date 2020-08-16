package com.developer2t.mygithubuserfinal.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.developer2t.mygithubuserfinal.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class ViewModelUserDetail : ViewModel() {

    private val detailUsers = MutableLiveData<User>()

    fun setDetailUser(query: String) {

        val apiKey = "86055d95f961800427f845e61d5bb262e71c4f8c"

        val client = AsyncHttpClient()
        client.addHeader("Authorization",apiKey)
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/${query}"

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                // Jika koneksi berhasil
                val result = String(responseBody)
                try {
                    val responseObjects = JSONObject(result)
                    val detail = User()

                    detail.username = responseObjects.getString("login")
                    detail.name = responseObjects.getString("name")
                    detail.images = responseObjects.getString("avatar_url")
                    detail.images = responseObjects.getString("avatar_url")
                    detail.company = responseObjects.getString("company")
                    detail.location = responseObjects.getString("location")
                    detail.repo = responseObjects.getString("public_repos").toInt()
                    detail.follower = responseObjects.getString("followers").toInt()
                    detail.followings = responseObjects.getString("following").toInt()

                    detailUsers.postValue(detail)
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

    internal fun getDetailUser(): MutableLiveData<User> {
        return detailUsers
    }
}