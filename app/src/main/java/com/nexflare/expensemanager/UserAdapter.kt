package com.nexflare.expensemanager

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by nexflare on 15/11/17.
 */
class UserAdapter(val context:Context,var arrayList: ArrayList<User>?):RecyclerView.Adapter<UserAdapter.UserViewHolder>(){
    override fun onBindViewHolder(holder: UserViewHolder?, position: Int) {
        holder?.bindData(position)
    }

    override fun getItemCount(): Int {
        return arrayList?.size?:0
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): UserViewHolder {
        val view=LayoutInflater.from(context).inflate(R.layout.layout_user,parent,false)
        return UserViewHolder(view)
    }


    inner class UserViewHolder(itemView:View?):RecyclerView.ViewHolder(itemView){
        private val nameTv=itemView?.findViewById<TextView>(R.id.nameTv)
        private val amountTv=itemView?.findViewById<TextView>(R.id.amountTv)

        fun bindData(position:Int){
            val user= arrayList?.get(position)
            nameTv?.text=user?.name
            amountTv?.text="\u20B9 "+user?.total
            itemView.setOnClickListener({
                val intent= Intent(context,ExpenseActivity::class.java)
                context.startActivity(intent)
            })
        }
    }

    fun updateArrayList(arrayList:ArrayList<User>?){
        this.arrayList=arrayList
        notifyDataSetChanged()
    }
}