package com.example.newsapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.helper.DateHelper
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.util.Date

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val layoutManager = LinearLayoutManager(this)
        binding?.rvNews?.layoutManager = layoutManager


        getBerita()
    }

    private fun getBerita() {
        binding?.progressBar?.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = BuildConfig.API_URL

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                binding?.progressBar?.visibility = View.GONE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                showToast("DEDEO PAYAHHHH")
                showToast(errorMessage)
            }

            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                binding?.progressBar?.visibility = View.GONE
                val result = responseBody?.let { String(it) }
                Log.d(TAG, "onSuccess: $result")

                val listBerita = ArrayList<Berita>()
                try {
                    val jsonObject = JSONObject(result)
                    val jsonArray = jsonObject.getJSONArray("articles")

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject2 = jsonArray.getJSONObject(i)
                        val judul = jsonObject2.getString("title")
                        val content = jsonObject2.getString("content")
                        val author = jsonObject2.getString("author")
                        val publishAt = jsonObject2.getString("publishedAt")

                        val image = jsonObject2.getString("urlToImage")
                        val media = jsonObject2.getJSONObject("source").getString("name")

                        val berita = Berita(judul, content, author, media, publishAt, image)
                        listBerita.add(berita)
                    }
                    val adapter = NewsAdapter(listBerita)
                    adapter.setOnItemClickCallback(object : NewsAdapter.OnItemClickCallback {
                        override fun onItemClicked(data: Berita) {
                            val intent = Intent(this@MainActivity, DetailBerita::class.java)
                            intent.putExtra(DetailBerita.EXTRA_NEWS, data)
                            startActivity(intent)
                        }
                    })

                    binding?.rvNews?.adapter = adapter

                } catch (e: Exception) {
                    showToast(e.message ?: "")
                    e.printStackTrace()
                }
            }
        })


    }

    private fun showToast(msg: String) {
        Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}