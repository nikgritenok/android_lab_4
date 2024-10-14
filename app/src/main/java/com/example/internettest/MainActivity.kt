package com.example.internettest

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    private val tag = "Flickr cats"
    private val okHttpClient = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnHttpURLConnection: Button = findViewById(R.id.btnHTTP)
        val btnOkHttp: Button = findViewById(R.id.btnOkHTTP)

        btnHttpURLConnection.setOnClickListener {
            fetchFlickrDataHttpURLConnection()
        }

        btnOkHttp.setOnClickListener {
            fetchFlickrDataOkHttp()
        }
    }

    private fun fetchFlickrDataHttpURLConnection() {
        CoroutineScope(Dispatchers.IO).launch {
            val responseData = getFlickrDataHttpURLConnection()
            withContext(Dispatchers.Main) {
                if (responseData.isNotEmpty()) {
                    Log.i("Flickr HttpURLConnection", responseData)
                } else {
                    Toast.makeText(this@MainActivity, "No data received", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun fetchFlickrDataOkHttp() {
        CoroutineScope(Dispatchers.IO).launch {
            val responseData = getFlickrDataOkHttp()
            withContext(Dispatchers.Main) {
                if (responseData.isNotEmpty()) {
                    Log.i("Flickr OkCats", responseData)
                } else {
                    Toast.makeText(this@MainActivity, "No data received", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getFlickrDataHttpURLConnection(): String {
        var responseData = ""
        try {
            val url = URL("https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=ff49fcd4d4a08aa6aafb6ea3de826464&tags=cat&format=json&nojsoncallback=1")
            val urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.requestMethod = "GET"
            urlConnection.connectTimeout = 5000
            urlConnection.readTimeout = 5000

            val responseCode = urlConnection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inStream = BufferedReader(InputStreamReader(urlConnection.inputStream))
                val responseBuilder = StringBuilder()
                var inputLine: String?

                while (inStream.readLine().also { inputLine = it } != null) {
                    responseBuilder.append(inputLine)
                }
                inStream.close()
                responseData = responseBuilder.toString()
            } else {
                Log.e(tag, "Request not successful: $responseCode")
            }
        } catch (e: IOException) {
            Log.e(tag, "Request failed", e)
        }
        return responseData
    }

    private fun getFlickrDataOkHttp(): String {
        var responseData = ""
        try {
            val request = Request.Builder()
                .url("https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=ff49fcd4d4a08aa6aafb6ea3de826464&tags=cat&format=json&nojsoncallback=1")
                .build()

            val response: Response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                responseData = response.body?.string() ?: ""
            } else {
                Log.e(tag, "Request not successful: ${response.code}")
            }
        } catch (e: Exception) {
            Log.e(tag, "Request failed", e)
        }
        return responseData
    }
}