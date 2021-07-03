package com.example.leanclouddemo

import android.app.Application
import cn.leancloud.LeanCloud


class MyApplication :Application(){
    override fun onCreate() {
        super.onCreate()
        LeanCloud.initialize(this,
            "jNNquH6AHOVN0fuQnBxj7jd6-gzGzoHsz",
            "EY7JnEeqg0IN9jLY2nkg1l15",
            "https://jnnquh6a.lc-cn-n1-shared.com"
        )
    }
}