package com.example.afinal.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.afinal.R
import com.example.afinal.info.Collectinfo
import com.example.afinal.info.Detailinfo
import com.example.afinal.network.HttpUtil
import kotlinx.android.synthetic.main.fragment_collect.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONArray
import java.io.IOException
import java.io.InputStream
import java.net.URL


class CollectFragment :Fragment(){

    val show_collect = "http://${ip}:8080/TestWeb/buff/show_collect.jsp"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        address =  show_collect + "?username=" + fname

        return inflater.inflate(R.layout.fragment_collect, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        HttpUtil.sendByOkHttp(address,object : Callback {

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body!!.string()
                val result = parseJson(responseData)

                activity?.runOnUiThread {

                    val layoutManager = LinearLayoutManager(activity)
                    cStuffRecyclerView.layoutManager = layoutManager
                    val adapter = DetailAdapter(result)
                    cStuffRecyclerView.adapter = adapter
                }

            }
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

        })


    }

    inner class DetailAdapter(val detailList: List<Collectinfo>): RecyclerView.Adapter<DetailAdapter.ViewHolder>(){


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.show_collect,parent,false)
            val holder = ViewHolder(view)


            return holder
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val info = detailList[position]

            holder.sname.text = info.sname

            var drawable: Drawable? = null
            var handler: Handler = Handler{
                if (it.what == 1){
                    holder.img.setImageDrawable(drawable)
                }
                false
            }
            Thread(Runnable {
                drawable = LoadImageFromWebOperations(info.img)
                val msg = Message()
                msg.what = 1
                handler.sendMessage(msg)
            }).start()
        }

        override fun getItemCount() = detailList.size

        inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){

            val img: ImageView = view.findViewById(R.id.img)
            val sname : TextView = view.findViewById(R.id.sname)

        }

        fun  LoadImageFromWebOperations(url:String) : Drawable
        {
            var iss: InputStream = URL(url).getContent() as InputStream;
            var d: Drawable = Drawable.createFromStream(iss, "src name");
            return d;
        }

    }

    private fun parseJson(jsonStr:String): List<Collectinfo>{
        val builder = StringBuilder()
        val detailList = ArrayList<Collectinfo>()
        try{
            //json数组
            val jsonArray = JSONArray(jsonStr)

            for(i in 0 until jsonArray.length()){
                //元素取出
                val jsonObject = jsonArray.getJSONObject(i)
                val sname = jsonObject.getString("sname")
                val img = jsonObject.getString("img")
                val username = jsonObject.getString("username")

                detailList.add(Collectinfo(sname, img, username))
            }
        }
        catch (e:Exception){
            e.printStackTrace()
        }
        return detailList

    }

}