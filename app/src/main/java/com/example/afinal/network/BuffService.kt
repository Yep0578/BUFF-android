package com.example.afinal.network

import com.example.afinal.info.Userinfo
import retrofit2.Call
import retrofit2.http.GET

interface BuffService {
    @GET("get_all_user.jsp")
    fun getAllUser():Call<List<Userinfo>>

}