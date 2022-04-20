package com.example.afinal.network

import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

object HttpUtil {
    fun sendByHttpUrlConn(address:String, listener: HttpCallbackListener){
        thread {
            var connection: HttpURLConnection? = null
            try {

                val url = URL(address)
                connection = url.openConnection() as HttpURLConnection //打开链接
                connection.connectTimeout = 8000 //8000ms 连接超时
                connection.readTimeout = 8000 // 读取超时


                val input = connection.inputStream
                val builder = StringBuilder()
                val reader = BufferedReader(InputStreamReader(input))
                reader.use {
                    reader.forEachLine {
                        builder.append(it).append("\n")
                    }
                }
                listener.onFinish(builder.toString())

            } catch (e: Exception) {
                e.printStackTrace()
                listener.onError(e)
            } finally {
                connection?.disconnect()
            }

        }
    }


    fun sendByOkHttp(address:String, callback:okhttp3.Callback){
        val client = OkHttpClient()

        //构建请求
        val request = Request.Builder()
            .url(address)
            .build()
        //执行
        client.newCall(request).enqueue(callback)
    }
}