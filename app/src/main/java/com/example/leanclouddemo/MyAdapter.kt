package com.example.leanclouddemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.leancloud.LCObject

class MyAdapter: RecyclerView.Adapter<MyAdapter.VHolder>() {
    private var _datalist= listOf<LCObject>()
    fun updatedatalist(newlist :List<LCObject>){
        _datalist=newlist
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder {
        val v=LayoutInflater.from(parent.context).inflate(R.layout.rec_item,parent, false)
        return VHolder(v)
    }

    override fun onBindViewHolder(holder: VHolder, position: Int) {
        val ob=_datalist[position]
        holder.wordstring.text=ob.get("word").toString()
        holder.worddate.text=ob.get("createdAt").toString()
    }

    override fun getItemCount()=_datalist.size
    class VHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val wordstring:TextView=itemView.findViewById(R.id.wordstring)
        val worddate:TextView=itemView.findViewById(R.id.worddate)
    }
}