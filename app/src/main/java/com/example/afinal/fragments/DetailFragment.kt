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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.afinal.R
import com.example.afinal.info.Detailinfo
import com.example.afinal.network.HttpUtil
import kotlinx.android.synthetic.main.detail_elem.view.*
import kotlinx.android.synthetic.main.fragment_detail.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONArray
import java.io.IOException
import java.io.InputStream
import java.net.URL

class DetailFragment : Fragment() {

    val get_all_user = "http://${ip}:8080/TestWeb/buff/get_all_user.jsp"
    val get_all_stuff = "http://${ip}:8080/TestWeb/buff/get_all_stuff.jsp"
    val get_one_stuff = "http://${ip}:8080/TestWeb/buff/get_one_stuff.jsp"
    val buy_stuff = "http://${ip}:8080/TestWeb/buff/buy_stuff.jsp"
    val detailList = ArrayList<Detailinfo>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view : View = inflater.inflate(R.layout.fragment_detail, container, false)
        if (arguments != null) {
            val name = arguments!!.getString("name").toString()
            val price = arguments!!.getString("price")
            val img = arguments!!.getString("img").toString()

            val nametext:TextView = view.findViewById(R.id.name)
            val pricetext:TextView = view.findViewById(R.id.price)
            val imgview:ImageView = view.findViewById(R.id.img)

            nametext.text = name
            pricetext.text = "参考价: " + price

            address = get_one_stuff + "?sname=" + name

            var drawable: Drawable? = null
            var handler: Handler = Handler{
                if (it.what == 1){
                    view.img.setImageDrawable(drawable)
                }
                false
            }
            Thread(Runnable {
                drawable = LoadImageFromWebOperations(img)
                val msg = Message()
                msg.what = 1
                handler.sendMessage(msg)
            }).start()

        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        HttpUtil.sendByOkHttp(address,object : Callback {

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body!!.string()
                val result = parseJson(responseData)

                activity?.runOnUiThread {

                    val layoutManager = LinearLayoutManager(activity)
                    DetailRecyclerView.layoutManager = layoutManager
                    val adapter = DetailAdapter(result)
                    DetailRecyclerView.adapter = adapter
                }

            }
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

        })


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

    fun  LoadImageFromWebOperations(url:String) :Drawable
    {
        var iss: InputStream = URL(url).getContent() as InputStream
        var d:Drawable  = Drawable.createFromStream(iss, "src name")
        return d
    }


    inner class DetailAdapter(val detailList: List<Detailinfo>): RecyclerView.Adapter<DetailAdapter.ViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.detail_elem,parent,false)
            val holder = ViewHolder(view)
            holder.itemView.setOnClickListener {
                val position = holder.adapterPosition
                Toast.makeText(parent.context,detailList[position].img, Toast.LENGTH_SHORT).show()
                //activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container,DetailFragment())?.commit()
            }

            holder.itemView.buy.setOnClickListener {
                if(flag == 1){

                    val position = holder.adapterPosition
                    address = buy_stuff + "?username=" + fname + "&stuffid=" + detailList[position].stuffid.toString() + "&money=" + detailList[position].price.toString()

                    HttpUtil.sendByOkHttp(address,object : Callback {

                        override fun onResponse(call: Call, response: Response) {
                            val responseData = response.body!!.string()
                            val result = parseJson2(responseData)

                            activity?.runOnUiThread {
                                //                            if (result == "1")
//                            {
//                                Toast.makeText(activity,"购买成功",Toast.LENGTH_SHORT).show()
//                                activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container,HomeFragment())?.commit()
//                            }
//                            else
//                                Toast.makeText(activity,"余额不足",Toast.LENGTH_SHORT).show()

                                activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container,HomeFragment())?.commit()

                            }

                        }
                        override fun onFailure(call: Call, e: IOException) {
                            e.printStackTrace()
                        }

                    })
                }
                else {
                    Toast.makeText(parent.context, "请先登录", Toast.LENGTH_SHORT).show()
                }


            }

            return holder
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val info = detailList[position]

            holder.seller.text = "卖家: " + info.seller
            holder.broken.text = "磨损: " + info.broken
            holder.price.text = "¥" + info.price.toString()

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
            val price: TextView = view.findViewById(R.id.price)
            val img: ImageView = view.findViewById(R.id.img)
            val seller: TextView = view.findViewById(R.id.seller)
            val broken: TextView = view.findViewById(R.id.broken)
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

    private fun parseJson2(jsonStr:String): String{
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

//    private fun parseJson(jsonStr:String): String{
//        val builder = StringBuilder()
//        try{
//            //json数组
//            val jsonArray = JSONArray(jsonStr)
//            for(i in 0 until jsonArray.length()){
//                //元素取出
//                val jsonObject = jsonArray.getJSONObject(i)
//                val sname = jsonObject.getString("sname")
//                val price = jsonObject.getInt("price")
//                val img = jsonObject.getString("img")
//                val seller = jsonObject.getString("seller")
//                val broken = jsonObject.getString("broken")
//                val flag = jsonObject.getInt("flag")
//                builder.append("$sname")
//            }
//        }
//        catch (e:Exception){
//            e.printStackTrace()
//        }
//        return builder.toString()
//
//    }

}