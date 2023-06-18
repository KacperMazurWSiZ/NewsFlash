package com.example.newsflash

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.newsflash.databinding.FragmentHomeBinding
import org.json.JSONException

class Home : Fragment(), OnNewsClick {



    private lateinit var binding: FragmentHomeBinding
    private lateinit var mAdaptor: NewsAdaptor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mAdaptor = NewsAdaptor(this)
        recyclerView.adapter = mAdaptor

        fetchnews()
    }

    private fun fetchnews() {
        val queue = Volley.newRequestQueue(requireContext())
        val url = "https://saurav.tech/NewsAPI/top-headlines/category/health/in.json"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    val newsJsonArray = response.getJSONArray("articles")
                    val newsArray = ArrayList<News>()
                    for (i in 0 until newsJsonArray.length()) {
                        val newsJsonObject = newsJsonArray.getJSONObject(i)
                        val author = newsJsonObject.getString("author")
                        if (author == "null") {
                            val news = News(
                                newsJsonObject.getString("title"),
                                "Author: Unknown",
                                newsJsonObject.getString("url"),
                                newsJsonObject.getString("urlToImage")
                            )
                            newsArray.add(news)
                        }
                        else {
                            val news = News(
                                newsJsonObject.getString("title"),
                                author,
                                newsJsonObject.getString("url"),
                                newsJsonObject.getString("urlToImage")
                            )
                            newsArray.add(news)
                        }
                    }

                    mAdaptor.updateData(newsArray)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener {
                // Obsługa błędu żądania
            }
        )
        queue.add(jsonObjectRequest)
    }

    companion object {
        fun newInstance(): Home {
            return Home()
        }
    }

    override fun onClicked(news: News) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(requireContext(), Uri.parse(news.url))
    }
}


