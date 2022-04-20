package com.example.afinal.fragments

import android.annotation.SuppressLint
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
import com.example.afinal.info.Detailinfo
import com.example.afinal.network.HttpUtil
import kotlinx.android.synthetic.main.fragment_stuff.*
import kotlinx.android.synthetic.main.mine_elem.view.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONArray
import java.io.IOException
import java.io.InputStream
import java.net.URL

class StuffFragment :Fragment(){

    val get_my_stuff = "http://${ip}:8080/TestWeb/buff/get_my_stuff.jsp"
    val sold_stuff = "http://${ip}:8080/TestWeb/buff/sold_stuff.jsp"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View = inflater.inflate(R.layout.fragment_stuff,container,false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        if (flag == 1){
            address = get_my_stuff + "?seller=" + fname
            HttpUtil.sendByOkHttp(address,object : Callback {

                override fun onResponse(call: Call, response: Response) {
                    val responseData = response.body!!.string()
                    val result = parseJson(responseData)

                    activity?.runOnUiThread {

                        val layoutManager = LinearLayoutManager(activity)
                        mStuffRecyclerView.layoutManager = layoutManager
                        val adapter = DetailAdapter(result)
                        mStuffRecyclerView.adapter = adapter
                    }

                }
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

            })
        }



    }

//    fun getDetail():List<Detailinfo>{
//        val detailList = ArrayList<Detailinfo>()
//
//        detailList.add(Detailinfo(1,"AK47",200,"https://g.fp.ps.netease.com/market/file/5aa2658eee4c0f391fed5cc0aF92RC2s","Yep","0.00000017",0))
//        detailList.add(Detailinfo(2,"AK47",80,"https://g.fp.ps.netease.com/market/file/5aa0c2f820e3db746eb9156cfLYRZTKi","Yep","0.203000",0))
//
//        return detailList
//    }

    inner class DetailAdapter(val detailList: List<Detailinfo>): RecyclerView.Adapter<DetailAdapter.ViewHolder>(){


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.mine_elem,parent,false)
            val holder = ViewHolder(view)

            holder.itemView.soldbtn.setOnClickListener {
                val position = holder.adapterPosition

                address = sold_stuff + "?stuffid=" + detailList[position].stuffid.toString()

                HttpUtil.sendByOkHttp(address,object : Callback {

                    override fun onResponse(call: Call, response: Response) {

                        activity?.runOnUiThread {
                            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container,HomeFragment())?.commit()
                        }

                    }
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                    }

                })

            }

            return holder
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val info = detailList[position]

            holder.sname.text = info.sname
            holder.broken.text = "磨损: " + info.broken


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

        inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){

            val img: ImageView = view.findViewById(R.id.img)
            val broken: TextView = view.findViewById(R.id.broken)
            val sname :TextView = view.findViewById(R.id.sname)
        }

        fun  LoadImageFromWebOperations(url:String) :Drawable
        {
            var iss: InputStream = URL(url).getContent() as InputStream;
            var d:Drawable  = Drawable.createFromStream(iss, "src name");
            return d;
        }

    }

    private fun parseJson(jsonStr:String): List<Detailinfo>{
        val builder = StringBuilder()
        val detailList = ArrayList<Detailinfo>()
        try{
            //json数组
            val jsonArray = JSONArray(jsonStr)

            for(i in 0 until jsonArray.length()){
                //元素取出
                val jsonObject = jsonArray.getJSONObject(i)
                val stuffid = jsonObject.getInt("stuffid")
                val sname = jsonObject.getString("sname")
                val price = jsonObject.getInt("price")
                val img = jsonObject.getString("img")
                val seller = jsonObject.getString("seller")
                val broken = jsonObject.getString("broken")
                val flag = jsonObject.getInt("flag")

                detailList.add(Detailinfo(stuffid,sname, price, img, seller, broken, flag))
            }
        }
        catch (e:Exception){
            e.printStackTrace()
        }
        return detailList

    }

}