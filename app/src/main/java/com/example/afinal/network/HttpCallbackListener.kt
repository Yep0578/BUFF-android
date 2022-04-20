package com.example.afinal.network

import java.lang.Exception
import java.net.CacheResponse

interface HttpCallbackListener {
    fun  onFinish(response: String)

    fun  onError(e: Exception)


}