package com.example.afinal.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.afinal.R
import com.example.afinal.network.HttpUtil
import kotlinx.android.synthetic.main.fragment_charge.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONArray
import java.io.IOException

class ChargeFragment: Fragment() {

    val update_money = "http://${ip}:8080/TestWeb/buff/update_money.jsp"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_charge, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        charge.setOnClickListener {

            val money = money.text.toString()

            val address1 = update_money + "?username=" + fname + "&money=" + money

            HttpUtil.sendByOkHttp(address1,object: Callback {
                override fun onResponse(call: Call, response: Response) {

                    activity?.runOnUiThread {
                        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container,MineFragment())?.commit()
                    }
                }
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }
            })

        }

        back.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container,MineFragment())?.commit()
        }

    }
    fun newInstance(name:String):ChargeFragment{
        val chargeFragment = ChargeFragment()
        val args = Bundle()
        args.putString("name",name)
        chargeFragment.arguments = args
        return chargeFragment
    }


}