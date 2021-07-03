package com.example.leanclouddemo

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import cn.leancloud.LCUser
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_sign.*
import java.util.*


class SignFragment : Fragment() {
    val mbackstck:Deque<NavDestination> = ArrayDeque()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        buttonTosign.isEnabled=false
        //输入监听
        val textWatch=object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //判断不为空
                val t1=addusername.text.toString().trim().isNotEmpty()
                val t2=adduserpasswd.text.toString().trim().isNotEmpty()
                buttonTosign.isEnabled=t1 and t2
            }
            override fun afterTextChanged(s: Editable?) {}
        }
        addusername.addTextChangedListener(textWatch)
        adduserpasswd.addTextChangedListener(textWatch)
        buttonTosign.setOnClickListener {
            val name=addusername.text.toString().trim()
            val passwd=adduserpasswd.text.toString().trim()
            LCUser().apply {
                username=name
                password=passwd
                signUpInBackground().subscribe(object :Observer<LCUser>{
                    override fun onSubscribe(d: Disposable) {
                    }
                    override fun onNext(t: LCUser) {
                        Log.d("TAG", "onNext: 注册成功")
                        Toast.makeText(activity,"注册成功",Toast.LENGTH_SHORT).show()
                        //登录代码
                        LCUser.logIn(name,passwd).subscribe(object :Observer<LCUser>{
                            override fun onSubscribe(d: Disposable) {}
                            override fun onNext(t: LCUser) {
                                val navController=Navigation.findNavController(it)
                                navController.navigate(R.id.action_loginSignFragment_to_wordFragment)
                            }
                            override fun onError(e: Throwable) {
                                Toast.makeText(activity,e.message,Toast.LENGTH_SHORT).show()
                            }
                            override fun onComplete() {}

                        })
                    }

                    override fun onError(e: Throwable) {
                        Log.d("TAG", "onError: "+e)
                        Toast.makeText(activity,"${e.message}",Toast.LENGTH_SHORT).show()
                    }

                    override fun onComplete() {
                    }

                })
            }
        }
    }
}