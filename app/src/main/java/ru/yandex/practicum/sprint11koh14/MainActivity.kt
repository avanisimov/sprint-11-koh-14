package ru.yandex.practicum.sprint11koh14


import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.Date

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "SPRINT_11"
    }

    private val adapter = NewsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val uri: Uri = Uri.parse("https://myserver.com:5051/api/v1/path?text=android&take=1#last")

        Log.d(TAG, "uri.scheme ${uri.scheme}")
        Log.d(TAG, "uri.host ${uri.host}")
        Log.d(TAG, "uri.authority ${uri.authority}")
        Log.d(TAG, "uri.pathSegments ${uri.pathSegments}")
        Log.d(TAG, "uri.lastPathSegment ${uri.lastPathSegment}")
        Log.d(TAG, "uri.queryParameterNames ${uri.queryParameterNames}")
        Log.d(TAG, "uri.getQueryParameter(\"text\") ${uri.getQueryParameter("text")}")
        Log.d(TAG, "uri.fragment ${uri.fragment}")


        val itemsRv: RecyclerView = findViewById(R.id.items)
        itemsRv.adapter = adapter

        val classs : Class<Date> = Date::class.java

        val str = "asdasd"
        val strClass : Class<String> = str.javaClass
        strClass.fields.forEach { field ->

        }
        classs.methods.forEach {
            it.name
            it.annotations
        }
        classs.fields.forEach {
            val fieldType: Class<*> = it.type
            if (fieldType == classs) {
                CustomDateTypeAdapter()
            }
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com/avanisimov/sprint-11-koh-14/")
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .registerTypeAdapter(Date::class.java, CustomDateTypeAdapter())
                        .registerTypeAdapter(NewsItem::class.java, NewsItemAdapter())
                        .create()
                )
            )
            .build()
        val serverApi = retrofit.create(Sprint11ServerApi::class.java)

        serverApi.getNews1()
            .enqueue(object : Callback<NewsResponse> {
                override fun onResponse(
                    call: Call<NewsResponse>,
                    response: Response<NewsResponse>
                ) {
                    Log.i(TAG, "onResponse: ${response.body()}")
                    adapter.items = response.body()?.data?.items?.filter {
                        it !is NewsItem.Unknown
                    } ?: emptyList()
                }

                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure: $t")
                }

            })
    }
}

// https://raw.githubusercontent.com/avanisimov/sprint-11-koh-14/master/jsons/news_1.json

interface Sprint11ServerApi {


    @GET("master/jsons/news_2.json")
    fun getNews1(): Call<NewsResponse>
}
