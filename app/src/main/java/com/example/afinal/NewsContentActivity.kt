package com.example.afinal



import android.content.Context
import android.content.Intent
import android.icu.text.CaseMap
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.afinal.fragments.CommentFragment
import com.example.afinal.fragments.MnewsFragment
import kotlinx.android.synthetic.main.news_page.*
class NewsContentActivity : AppCompatActivity() {

    companion object{
        fun actionStart(context: Context, title: String, content: String){
            val intent = Intent(context, NewsContentActivity::class.java).apply {
                putExtra("news_title", title)
                putExtra("new_content", content)
            }
            context.startActivity(intent)
        }
    }

    val fragment = listOf<Fragment>(MnewsFragment(), CommentFragment())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.news_page)
        val title = intent.getStringExtra("new_title")
        val content = intent.getStringExtra("new_content")
//        if (title != null && content != null){
//            val fragment = newsFragment as NewsFragment
//            fragment.refresh(title,content)
//        }
        vpp.adapter = NewsFragmentPageAdapter(supportFragmentManager)
        tabs.setupWithViewPager(vpp)
        go_back.setOnClickListener {
            finish()
        }
    }

    val selectTitle = listOf<String>("新鲜事","评论")

    inner class NewsFragmentPageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm){
        override fun getPageTitle(position: Int): CharSequence? {
            return selectTitle.get(position)
        }
        override fun getItem(position: Int): Fragment {
            return fragment.get(position)
        }
        override fun getCount(): Int {
            return selectTitle.size
        }

    }
}
