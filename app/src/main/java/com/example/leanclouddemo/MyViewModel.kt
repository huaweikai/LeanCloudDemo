package com.example.leanclouddemo

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cn.leancloud.LCException
import cn.leancloud.LCObject
import cn.leancloud.LCQuery
import cn.leancloud.LCUser
import cn.leancloud.livequery.LCLiveQuery
import cn.leancloud.livequery.LCLiveQueryEventHandler
import cn.leancloud.livequery.LCLiveQuerySubscribeCallback
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.util.*

class MyViewModel(application: Application) : AndroidViewModel(application) {
    private val _datalistlive= MutableLiveData<List<LCObject>>()
    val datalistlive:LiveData<List<LCObject>> = _datalistlive
    fun LoginUser(application: Application){
        val query=LCQuery<LCObject>("Word")
        query.whereEqualTo("user",LCUser.getCurrentUser())
        query.findInBackground().subscribe(object :Observer<List<LCObject>>{
            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: List<LCObject>) {
                _datalistlive.value=t
            }

            override fun onError(e: Throwable) {
                Toast.makeText(application,e.message,Toast.LENGTH_SHORT).show()
            }

            override fun onComplete() {
            }
        })
        val livequery=LCLiveQuery.initWithQuery(query)
        livequery.subscribeInBackground(object :LCLiveQuerySubscribeCallback(){
            override fun done(e: LCException?) {
            }
        })
        livequery.setEventHandler(object :LCLiveQueryEventHandler(){
            override fun onObjectCreated(LCObject: LCObject) {
                super.onObjectCreated(LCObject)
                val t =_datalistlive.value?.toMutableList()
                t?.add(LCObject)
                _datalistlive.value=t!!
            }

            override fun onObjectDeleted(objectId: String?) {
                super.onObjectDeleted(objectId)
                val t=_datalistlive.value?.toMutableList()
                val ob=t?.find{
                    it.get("objectId")==objectId
                }
                t?.remove(ob)
                _datalistlive.value=t!!
            }

            override fun onObjectUpdated(LCObject: LCObject, updateKeyList: MutableList<String>) {
                super.onObjectUpdated(LCObject, updateKeyList)
                val ob=_datalistlive.value?.find{
                    it.get("objectId")==LCObject.get("objectId")
                }
                updateKeyList.forEach{
                    ob?.put(it,LCObject.get(it))
                }
                _datalistlive.value=_datalistlive.value
            }
        })
    }
    init{

    }
    fun addWord(newWord:String){
        LCObject("Word").apply {
            put("word",newWord)
            put("user",LCUser.getCurrentUser())
            saveInBackground().safeSubscribe(object :Observer<LCObject>{
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: LCObject) {
                    Toast.makeText(getApplication(),"添加成功",Toast.LENGTH_SHORT).show()
                }

                override fun onError(e: Throwable) {
                    Toast.makeText(getApplication(),"添加失败",Toast.LENGTH_SHORT).show()
                }

                override fun onComplete() {
                }

            })
        }
    }
    override fun onCleared() {
        super.onCleared()
    }
}