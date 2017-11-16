package com.nexflare.expensemanager.Adapter

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.nexflare.expensemanager.Expense
import com.nexflare.expensemanager.R
import com.nexflare.expensemanager.User

/**
 * Created by nexflare on 16/11/17.
 */
class ExpenseAdapter(val context:Context,var arrayList: ArrayList<Expense>?): RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {
    override fun getItemCount(): Int = arrayList?.size?:0

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ExpenseViewHolder {
        val view=LayoutInflater.from(context).inflate(R.layout.layout_user,parent,false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder?, position: Int) {
        holder?.bindView(position)
    }


    inner class ExpenseViewHolder(itemView: View?):RecyclerView.ViewHolder(itemView){
        private val nameTv=itemView?.findViewById<TextView>(R.id.nameTv)
        private val amountTv=itemView?.findViewById<TextView>(R.id.amountTv)
        private val dateTv=itemView?.findViewById<TextView>(R.id.dateTv)
        private val colorView=itemView?.findViewById<View>(R.id.colorView)
        fun bindView(position:Int){
            val expense=arrayList?.get(position)
            nameTv?.text=expense?.reason?:"Just for fun"
            amountTv?.text=expense?.amount.toString()
            dateTv?.text=expense?.time
            if((expense?.amount?:0)<0){
                colorView?.setBackgroundColor(Color.parseColor("#E93324"))
            }
            else{
                colorView?.setBackgroundColor(Color.parseColor("#65e761"))
            }
        }
    }

    fun updateArrayList(userArrayList: ArrayList<Expense>) {
        arrayList=userArrayList
        notifyDataSetChanged()
    }
}