//package com.example.eleme.adapter
//
//import android.content.Context
//import android.content.Intent
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.daimajia.slider.library.SliderLayout
//import com.daimajia.slider.library.SliderTypes.TextSliderView
//import com.example.afinal.R
//import com.example.afinal.info.NewsInfo
//import com.example.eleme.R
//import com.example.eleme.data.NewsInfo
//import com.example.eleme.myeleme.SellerStore
//
//
//class FindRvAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
//
//    companion object{
//        val TYPE_SLIDER = 0
//        val TYPE_NEWS = 1
//    }
//    //不同position对应不同类型
//    override fun getItemViewType(position: Int): Int {
//        if(position == 0){
//            return TYPE_SLIDER
//        }else{
//            return TYPE_NEWS
//        }
//    }
//
//    var mDatas : List<NewsInfo> = ArrayList()
//
//    fun setData(data: List<NewsInfo>){
//        this.mDatas = data
//        notifyDataSetChanged()
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        val viewType = getItemViewType(position)
//        when(viewType){
//            TYPE_SLIDER -> (holder as SliderHolder).bindData("for test")
//            TYPE_NEWS -> (holder as NewsHolder).bindData(mDatas[position-1])
//        }
//    }
//
//    override fun getItemCount(): Int {
//        if(mDatas.size > 0){
//            return mDatas.size + 1
//        }else{
//            return 0
//        }
//    }
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        when(viewType){
//            TYPE_SLIDER -> return SliderHolder(View.inflate(context, R.layout.fd_sld,null))
//            TYPE_NEWS -> return NewsHolder(View.inflate(context, R.layout.fd_news,null))
//            else -> return SliderHolder(View.inflate(context, R.layout.fd_empty,null))
//        }
//    }
//
//    var url_maps : HashMap<String,String> = HashMap()
//
//    inner class SliderHolder(item : View) : RecyclerView.ViewHolder(item){
//        val sliderLayout : SliderLayout
//        init {
//            sliderLayout = item.findViewById(R.id.slider)
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
//                url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg")
//                url_maps.put(
//                    "Game of Thrones",
//                    "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg"
//                )
//                for((key, value) in url_maps){
//                    var textSliderView : TextSliderView = TextSliderView(context)
//                    textSliderView.description(key).image(value)
//                    sliderLayout.addSlider(textSliderView)
//                }
//            }
//        }
//    }
//
//    inner class NewsHolder(item : View) : RecyclerView.ViewHolder(item){
//
//        init {
//
//
//            item.setOnClickListener{
//                val intent: Intent = Intent(context, SellerStore::class.java )
////            var hasSelectInfo = false
////            val count = queryClickInfoBySrid(item.Srid.toInt())
////            if (count > 0) {
////                hasSelectInfo = true
////            }
////            intent.putExtra("seller", seller)
////            intent.putExtra("hasSelectInfo", hasSelectInfo)
//                context?.startActivity(intent)
//            }
//        }
//        fun bindData(seller: NewsInfo){
//
//        }
//    }
//}