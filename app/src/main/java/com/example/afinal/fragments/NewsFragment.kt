package com.example.afinal.fragments

import android.content.Context
import android.content.Intent
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
import com.daimajia.slider.library.SliderLayout
import com.daimajia.slider.library.SliderTypes.TextSliderView
import com.example.afinal.NewsContentActivity
import com.example.afinal.R
import com.example.afinal.info.NewsInfo
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.fragment_sold.*
import kotlinx.android.synthetic.main.news_item.*
import java.io.InputStream
import java.net.URL

class NewsFragment : Fragment() {
    var url_maps : HashMap<String,String> = HashMap()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View = inflater.inflate(R.layout.fragment_news,container,false)
        val sliderlayout  = view.findViewById<SliderLayout>(R.id.slider)
        if (url_maps.size == 0) {
            url_maps.put(
                "Hannibal",
                "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fi5.article.fd.zol-img.com.cn%2Ft_s640x2000%2Fg5%2FM00%2F03%2F0E%2FChMkJ1oyPi-ITU7_AAHWWrQfJ_0AAjKWgNzSsYAAdZy355.jpg&refer=http%3A%2F%2Fi5.article.fd.zol-img.com.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1612660725&t=38520de80e6348aef011cc0ec62673c7"
            )
            url_maps.put(
                "Big Bang Theory",
                "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fi2.hdslb.com%2Fbfs%2Farchive%2Feef748d330565a1cd1ffebea0c30ae95b4197179.jpg&refer=http%3A%2F%2Fi2.hdslb.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1612660675&t=43e129b8e177ee34755ee44aaab1bb21"
            )
            url_maps.put(
                "House of Cards",
                "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1054589146,260834727&fm=26&gp=0.jpg"
            )
            url_maps.put(
                "Game of Thrones",
                "https://gimg2.baidu.com/image_search/src=http%3A%2F%2F08.imgmini.eastday.com%2Fmobile%2F20170929%2F20170929110202_92312c145d010a706b6a7fc64506c9d4_1.jpeg&refer=http%3A%2F%2F08.imgmini.eastday.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1612660759&t=dcc9cf54d00880e394e905817b986e80"
            )
            for ((key, value) in url_maps) {
                var textSliderView: TextSliderView = TextSliderView(context)
                textSliderView.description(key).image(value)
                sliderlayout.addSlider(textSliderView)
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(activity)
        rv_news.layoutManager = layoutManager
        val adapter = NewsAdapter(setdata())
        rv_news.adapter = adapter
    }

    private fun setdata(): List<NewsInfo> {
        val newslist = ArrayList<NewsInfo>()
        newslist.add(NewsInfo(1,"2020年度最佳选手第15名: Brollan印花及库存介绍", "this is context1", "sc1"))
        newslist.add(NewsInfo(2,"2020年度TOP20职业选手：yuurih（第14名）", "this is context2", "sc2"))
        newslist.add(NewsInfo(3,"实至名归，ZywOo当选2020年度法国最佳电竞选手", "this is context3", "sc3"))
        newslist.add(NewsInfo(4,"职业哥高光：“马西西”残局一打五", "this is context4", "sc4"))
        newslist.add(NewsInfo(5,"EHOME俱乐部宣布Sccc离队", "this is context5", "sc5"))
        newslist.add(NewsInfo(6,"选手日常事：那个熟悉的大眼回来了", "this is context6", "pic"))
        newslist.add(NewsInfo(7,"CSGO皮肤鉴赏：X光系列皮肤", "this is context7", "pic"))
        return newslist
    }

    fun refresh(title: String, content: String) {

    }

    inner class NewsAdapter (val newslist : List<NewsInfo>) : RecyclerView.Adapter<NewsAdapter.ViewHolder>(){

        inner class ViewHolder(view :View) : RecyclerView.ViewHolder(view){
            val tv_title : TextView = view.findViewById(R.id.tv_title) // news
            val seller_logo : ImageView = view.findViewById(R.id.seller_logo)
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.news_item, parent, false)
            val holder= ViewHolder(view)
//            holder.itemView.setOnClickListener{
//                val news = newslist[holder.adapterPosition]
//                NewsContentActivity.actionStart(parent.context, news.title, news.content)
//            }

            return holder
        }

        override fun getItemCount(): Int {
            return newslist.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val news = newslist[position]
            holder.tv_title.text =  news.title// holder 中的元素 的属性 的赋值
            checkpicid(holder, position)

//            var drawable: Drawable? = null
//            var handler: Handler = Handler{
//                if (it.what == 1){
//                    seller_logo.setImageDrawable(drawable)
//                }
//                false
//            }
//            Thread(Runnable {
//                drawable = LoadImageFromWebOperations(news.pic)
//                val msg = Message()
//                msg.what = 1
//                handler.sendMessage(msg)
//            }).start()

            holder.itemView.setOnClickListener {
                val intent = Intent(context,NewsContentActivity::class.java)
                startActivity(intent)
            }

        }
        fun  LoadImageFromWebOperations(url:String) :Drawable
        {
            var iss: InputStream = URL(url).getContent() as InputStream;
            var d:Drawable  = Drawable.createFromStream(iss, "src name");
            return d;
        }


        fun checkpicid(holder: ViewHolder, position: Int){
            if (position == 0)
                holder.seller_logo.setImageResource(R.drawable.sc1)
            if (position == 1)
                holder.seller_logo.setImageResource(R.drawable.sc2)
            if (position == 2)
                holder.seller_logo.setImageResource(R.drawable.sc3)
            if (position == 3)
                holder.seller_logo.setImageResource(R.drawable.sc4)
            if (position == 4)
                holder.seller_logo.setImageResource(R.drawable.sc5)
            if (position == 5)
                holder.seller_logo.setImageResource(R.drawable.sc6)
            if (position == 6)
                holder.seller_logo.setImageResource(R.drawable.sc7)
        }
    }

//    var url_maps : HashMap<String,String> = HashMap()
//    inner class SldAdapter() : Adapter<SldAdapter.ViewHolder>(){
//        inner class ViewHolder(view :View) : RecyclerView.ViewHolder(view){
//            val sliderLayout: SliderLayout = view.findViewById(R.id.slider) // slider
//        }
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : NewsAdapter.ViewHolder {
//            val view = LayoutInflater.from(parent.context)
//                .inflate(R.layout.news_item, parent, false)
//            val holder= ViewHolder(view)
//
//        }
//        fun bindData(data: String) {
//            if (url_maps.size == 0) {
//                url_maps.put(
//                    "Hannibal",
//                    "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg"
//                )
//                url_maps.put(
//                    "Big Bang Theory",
//                    "http://tvfiles.alphacoders.com/100/hdclearart-10.png"
//                )
//                url_maps.put(
//                    "House of Cards",
//                    "http://cdn3.nflximg.net/images/3093/2043093.jpg"
//                )
//                url_maps.put(
//                    "Game of Thrones",
//                    "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg"
//                )
//                for ((key, value) in url_maps) {
//                    var textSliderView: TextSliderView = TextSliderView(context)
//                    textSliderView.description(key).image(value)
//                    sliderLayout.addSlider(textSliderView)
//                }
//            }
//        }
//    }
}
