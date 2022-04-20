package com.example.afinal.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.afinal.R
import com.example.afinal.info.Userinfo
import com.example.afinal.network.HttpUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_mine.view.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONArray
import java.io.IOException

class LoginFragment : Fragment() {



    val get_all_user = "http://${ip}:8080/TestWeb/buff/login.jsp"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        confirm.setOnClickListener {

            flag = 1
            val username = username.text.toString()
            val password = password.text.toString()
            val address = get_all_user+"?username="+username+"&password="+password

            HttpUtil.sendByOkHttp(address,object: Callback {
                override fun onResponse(call: Call, response: Response) {

                    val responseData = response.body!!.string()
                    val result = parseJson(responseData)

                    activity?.runOnUiThread {
                        if (result == "1" || result == "2")
                        {
                            val mineFragment = MineFragment().newInstance(username)
                            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container,mineFragment)?.commit()
                        }
                        else
                        {
                            Toast.makeText(activity,"密码错误",Toast.LENGTH_SHORT).show()
                        }

                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }
            })

        }

    }
    private fun parseJson(jsonStr:String): String{
        val builder = StringBuilder()
        try{
            //json数组
            val jsonArray = JSONArray(jsonStr)
            for(i in 0 until jsonArray.length()){
                //元素取出
                val jsonObject = jsonArray.getJSONObject(i)
                val result = jsonObject.getString("result")
                builder.append("$result")
            }
        }
        catch (e:Exception){
            e.printStackTrace()
        }
        return builder.toString()

    }
}