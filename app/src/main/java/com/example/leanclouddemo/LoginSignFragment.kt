package com.example.leanclouddemo

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.adapter.FragmentStateAdapter
import cn.leancloud.LCUser
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login_sign.*

class LoginSignFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_sign, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("TAG", "LANDGonActivityCreated: "+LCUser.getCurrentUser())
        LoginSignPager.apply {
            adapter=object : FragmentStateAdapter(this@LoginSignFragment){
                override fun getItemCount()=2
                override fun createFragment(position: Int)=when(position){
                    0->LoginFragment()
                    else->SignFragment()
                }
            }
        }
        TabLayoutMediator(tablayout,LoginSignPager){ tab: TabLayout.Tab, i: Int ->
            tab.text=when(i){
                0->"登录"
                else->"注册"
            }
        }.attach()
    }
}