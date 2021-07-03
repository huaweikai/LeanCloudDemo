package com.example.leanclouddemo

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.viewpager2.adapter.FragmentStateAdapter
import cn.leancloud.LCUser
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login_sign.*

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        buttonTologin.isEnabled=false
        val textWatch=object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //判断不为空
                val t1=loginusername.text.toString().trim().isNotEmpty()
                val t2=loginuserpasswd.text.toString().trim().isNotEmpty()
                buttonTologin.isEnabled=t1 and t2
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        loginusername.addTextChangedListener(textWatch)
        loginuserpasswd.addTextChangedListener(textWatch)
        buttonTologin.setOnClickListener {
            val name=loginusername.text.toString().trim()
            val passwd=loginuserpasswd.text.toString().trim()
            Log.d("TAG", "onActivityCreated: "+name+":"+passwd)
            LCUser.logIn(name,passwd).subscribe(object :Observer<LCUser>{
                override fun onSubscribe(d: Disposable) {}

                override fun onNext(t: LCUser) {
                   val navController= Navigation.findNavController(it)
                    navController.navigate(R.id.action_loginSignFragment_to_wordFragment)
                }

                override fun onError(e: Throwable) {
                    Log.d("TAG", "onError: "+e.message+e.localizedMessage+e.suppressed+e.cause)
//                    Toast.makeText(activity,e.message,Toast.LENGTH_SHORT).show()
                }
                override fun onComplete() {
                }

            })
        }
    }
}