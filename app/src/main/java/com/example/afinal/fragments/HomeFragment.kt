package com.example.afinal.fragments

import android.app.Application
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.afinal.R
import com.example.afinal.info.Stuffinfo
import com.example.afinal.network.HttpUtil
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_mine.*
import kotlinx.android.synthetic.main.fragment_stuff.*
import kotlinx.android.synthetic.main.stuff_elem.view.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONArray
import java.io.IOException
import java.io.InputStream
import java.lang.Exception
import java.net.URL

class HomeFragment:Fragment() {

    val add_collect = "http://${ip}:8080/TestWeb/buff/add_collect.jsp"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_home,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        HttpUtil.sendByOkHttp(get_all_stuff,object : Callback {
//
//            override fun onResponse(call: Call, response: Response) {
//                val responseData = response.body!!.string()
//                val result = parseJson(responseData)
//
//                activity?.runOnUiThread {
//                    val layoutManager = LinearLayoutManager(activity)
//                    StuffRecyclerView.layoutManager = layoutManager
//                    val adapter = StuffAdapter(result)
//                    StuffRecyclerView.adapter = adapter
//                }
//
//            }
//            override fun onFailure(call: Call, e: IOException) {
//                e.printStackTrace()
//            }
//
//
//        })

        val layoutManager = LinearLayoutManager(activity)
        StuffRecyclerView.layoutManager = layoutManager
        val adapter = StuffAdapter(getStuff())
        StuffRecyclerView.adapter = adapter

    }

    fun newInstance(name:String,price:String,img:String):DetailFragment{
        val detailFragment = DetailFragment()
        val args = Bundle()
        args.putString("name",name)
        args.putString("price",price)
        args.putString("img",img)
        detailFragment.arguments = args
        return detailFragment
    }

    fun getStuff():List<Stuffinfo>{
        val stuffList = ArrayList<Stuffinfo>()
        stuffList.add(Stuffinfo("AK47",160,"https://g.fp.ps.netease.com/market/file/5aa0c2f820e3db746eb9156cfLYRZTKi"))
        stuffList.add(Stuffinfo("AWP",111160,"https://g.fp.ps.netease.com/market/file/5aa2658eee4c0f391fed5cc0aF92RC2s"))
        stuffList.add(Stuffinfo("DesertEagle",1120,"https://g.fp.ps.netease.com/market/file/5f2cdf9b2786fdc5d7c9fedflDzy6QJ402"))
        stuffList.add(Stuffinfo("M4A4",288,"https://g.fp.ps.netease.com/market/file/5c89e2b28b7427af9ca10943BCqiQQ2p02"))
        stuffList.add(Stuffinfo("M9",10350,"https://g.fp.ps.netease.com/market/file/5aa0b4206f0494f822b2c2dbQZvbuaRL"))
        stuffList.add(Stuffinfo("Balisong",4665,"https://g.fp.ps.netease.com/market/file/5a9fc2adadce5f3c3cff7208B6rVobpc"))

        return stuffList
    }

    inner class StuffAdapter(val stuffList: List<Stuffinfo>): RecyclerView.Adapter<StuffAdapter.ViewHolder>(){

        inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
            val name:TextView = view.findViewById(R.id.name)
            val price:TextView = view.findViewById(R.id.price)
            val img:ImageView = view.findViewById(R.id.img)
            //val seller:TextView = view.findViewById(R.id.seller)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.stuff_elem,parent,false)
            val holder = ViewHolder(view)
            holder.itemView.setOnClickListener {
                val position = holder.adapterPosition
//                Toast.makeText(parent.context, fname, Toast.LENGTH_SHORT).show()
                val detailFragment = DetailFragment().newInstance(stuffList[position].sname,stuffList[position].price.toString(),stuffList[position].img)

                activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container,detailFragment)?.commit()

            }

            holder.itemView.collect.setOnClickListener {
                val position = holder.adapterPosition


                if(flag == 1){

                    address = add_collect + "?username=" + fname + "&sname=" + stuffList[position].sname + "&img=" + stuffList[position].img

                    HttpUtil.sendByOkHttp(address,object: Callback {
                        override fun onResponse(call: Call, response: Response) {

                            activity?.runOnUiThread {
                                Toast.makeText(parent.context,"收藏成功",Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call, e: IOException) {
                            e.printStackTrace()
                        }
                    })
                }
                else{
                    Toast.makeText(parent.context, "请先登录", Toast.LENGTH_SHORT).show()
                }

            }

            return holder
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val info = stuffList[position]
            holder.name.text = info.sname
            holder.price.text = "参考价: ¥" + info.price.toString()
//            holder.img.setImageResource(info.img)
            //holder.seller.text = info.seller

            var drawable: Drawable? = null
            var handler: Handler = Handler{
                if (it.what == 1){
                    holder.img.setImageDrawable(drawable);
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
        override fun getItemCount() = stuffList.size

    }
    fun  LoadImageFromWebOperations(url:String) :Drawable
    {
        var iss: InputStream = URL(url).getContent() as InputStream
        var d:Drawable  = Drawable.createFromStream(iss, "src name")
        return d
    }

//    private fun parseJson(jsonStr:String): List<Stuffinfo>{
//        val builder = StringBuilder()
//        val stuffList = ArrayList<Stuffinfo>()
//        try{
//            //json数组
//            val jsonArray = JSONArray(jsonStr)
//
//            for(i in 0 until jsonArray.length()){
//                //元素取出
//                val jsonObject = jsonArray.getJSONObject(i)
//                val sname = jsonObject.getString("sname")
//                val price = jsonObject.getInt("price")
//                val img = jsonObject.getString("img")
//                builder.append("$sname--$price--$img")
//
//                stuffList.add(Stuffinfo(sname, price, img))
//            }
//        }
//        catch (e:Exception){
//            e.printStackTrace()
//        }
//        return stuffList
//
//    }

}