package com.example.afinal.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.afinal.R
import com.example.afinal.info.Userinfo
import com.example.afinal.network.HttpUtil
import kotlinx.android.synthetic.main.fragment_mine.*
import okhttp3.*
import org.json.JSONArray
import java.io.IOException
import java.lang.Exception

var flag :Int = 0
val ip = "10.150.14.143"
lateinit var fname:String
lateinit var address:String

class MineFragment: Fragment() {


    val get_all_user = "http://${ip}:8080/TestWeb/buff/get_all_user.jsp"
    val get_all_stuff = "http://${ip}:8080/TestWeb/buff/get_all_stuff.jsp"
    val get_one_stuff = "http://${ip}:8080/TestWeb/buff/get_one_stuff.jsp"
    val get_one_user = "http://${ip}:8080/TestWeb/buff/get_one_user.jsp"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)

        val view: View = inflater.inflate(R.layout.fragment_mine, container, false)


        if (arguments != null) {
            fname = arguments!!.getString("name").toString()
            val username: TextView = view.findViewById(R.id.username)
            username.text = fname
            val address1 = get_one_user + "?username=" + fname
            address = address1
            }



            return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        login.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container,LoginFragment())?.commit()
//            username.visibility = View.VISIBLE
//            login.visibility = View.INVISIBLE
        }

        if (flag == 1)
        {
            username.visibility = View.VISIBLE
            login.visibility = View.INVISIBLE
            address = get_one_user + "?username=" + fname

                HttpUtil.sendByOkHttp(address,object : Callback {

                    override fun onResponse(call: Call, response: Response) {
                        val responseData = response.body!!.string()
                        val result = parseJson2(responseData)

                        activity?.runOnUiThread {
                            username.text = fname
                            money.text = "¥" + result[0].money.toString()
                            count.text = result[0].count.toString()
                        }

                    }
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                    }

                })

        }

        charge.setOnClickListener {
            if (flag == 1)
                activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container,ChargeFragment())?.commit()
            else
                Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show()
        }

        collect.setOnClickListener {
            if (flag == 1)
                activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container,CollectFragment())?.commit()
            else
                Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show()
        }

        quit.setOnClickListener {

            if( flag == 1){
                flag = 0
                activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container,HomeFragment())?.commit()

            }
            else
                Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show()
        }

        bh.setOnClickListener {
            if (flag == 1)
                activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container,BhistoryFragment())?.commit()
            else
                Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show()
        }

        sh.setOnClickListener {
            if (flag == 1)
                activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container,ShistoryFragment())?.commit()
            else
                Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show()
        }

    }

    fun newInstance(name:String):MineFragment{
        val mineFragment = MineFragment()
        val args = Bundle()
        args.putString("name",name)

        mineFragment.arguments = args
        return mineFragment
    }

    private fun parseJson(jsonStr:String): String{
        val builder = StringBuilder()
        try{
            //json数组
            val jsonArray = JSONArray(jsonStr)
            for(i in 0 until jsonArray.length()){
                //元素取出
                val jsonObject = jsonArray.getJSONObject(i)
                val sname = jsonObject.getString("sname")
                val price = jsonObject.getInt("price")
                val img = jsonObject.getString("img")
                builder.append("$sname--$price--$img")
            }
        }
        catch (e:Exception){
            e.printStackTrace()
        }
        return builder.toString()

    }
    private fun parseJson2(jsonStr:String): List<Userinfo>{
        val builder = StringBuilder()
        val detailList = ArrayList<Userinfo>()
        try{
            //json数组
            val jsonArray = JSONArray(jsonStr)

            for(i in 0 until jsonArray.length()){
                //元素取出
                val jsonObject = jsonArray.getJSONObject(i)
                val username = jsonObject.getString("username")
                val password = jsonObject.getString("password")
                val userid = jsonObject.getInt("userid")
                val money = jsonObject.getInt("money")
                val count = jsonObject.getInt("count")

                detailList.add(Userinfo(userid, username, password, money, count))
            }
        }
        catch (e:Exception){
            e.printStackTrace()
        }
        return detailList

    }

}