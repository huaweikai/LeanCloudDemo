package com.example.leanclouddemo

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import cn.leancloud.LCUser
import kotlinx.android.synthetic.main.fragment_word.*
import java.util.*

class WordFragment : Fragment() {
    private lateinit var myViewModel: MyViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_word, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        myViewModel=ViewModelProvider(requireActivity(),ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(MyViewModel::class.java)
        Log.d("TAG", "onActivityCreated: "+LCUser.getCurrentUser().username)
        myViewModel.LoginUser(requireActivity().application)
        val myadapter=MyAdapter()
        recycler.apply {
            adapter=myadapter
            addItemDecoration(DividerItemDecoration(requireContext(),DividerItemDecoration.VERTICAL))
            layoutManager=LinearLayoutManager(requireContext())
        }
        myViewModel.datalistlive.observe(requireActivity(), {
            Log.d("TAG", "onActivityCreated: "+it)
            myadapter.updatedatalist(it)
            myadapter.notifyDataSetChanged()
        })
        floatingActionButton.setOnClickListener {
            val v=LayoutInflater.from(requireContext()).inflate(R.layout.dialog,null)
            AlertDialog.Builder(requireContext())
                .setTitle("添加单词")
                .setView(v)
                .setPositiveButton("确定"){_,_ ->
                    val newWord=v.findViewById<EditText>(R.id.addword).text.toString()
                    myViewModel.addWord(newWord)
                }
                .setNegativeButton("取消"){dialog,_ ->dialog.dismiss()
                }
                .create()
                .show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.exit){
            LCUser.logOut()
            val navController:NavController=Navigation.findNavController(requireView())
            navController.navigate(R.id.action_wordFragment_to_loginSignFragment)
        }
        return super.onOptionsItemSelected(item)
    }
}