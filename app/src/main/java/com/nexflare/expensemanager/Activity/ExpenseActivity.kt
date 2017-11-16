package com.nexflare.expensemanager.Activity

import android.app.Dialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.nexflare.expensemanager.DatePickerFragment
import com.nexflare.expensemanager.DatePickerInterface
import com.nexflare.expensemanager.Expense
import com.nexflare.expensemanager.R
import kotlinx.android.synthetic.main.activity_expense.*
import kotlinx.android.synthetic.main.layout_expense_dailog.*


class ExpenseActivity : AppCompatActivity() {
    lateinit var firebaseDatabase:FirebaseDatabase
    lateinit var databaseReference:DatabaseReference
    lateinit var phone:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense)
        firebaseDatabase= FirebaseDatabase.getInstance()
        databaseReference=firebaseDatabase.getReference("expense")
        phone=intent.getStringExtra("phone")
        fabExpense.setOnClickListener{

            val dialog=Dialog(this@ExpenseActivity)
            dialog.setContentView(R.layout.layout_expense_dailog)
            val amountEt=dialog.findViewById<EditText>(R.id.amountEt)
            val reason=dialog.findViewById<EditText>(R.id.reasonEt)
            val okTv=dialog.findViewById<TextView>(R.id.okTv)
            val dateTil=dialog.findViewById<TextInputLayout>(R.id.dateTil)
            val dateEt=dialog.findViewById<EditText>(R.id.dateEt)
            val calenderIv=dialog.findViewById<ImageView>(R.id.calenderIv)
            calenderIv.setOnClickListener{
                Log.d("TAGGER","Clicked")
                val dateFragment= DatePickerFragment.newInstance(object : DatePickerInterface {
                    override fun getDate(year: Int, month: Int, day: Int) {
                        dateEt.setText("$day-$month-$year")
                    }

                })
                dateFragment.show(fragmentManager,"datePicker")
            }
            okTv.setOnClickListener{
                val key=databaseReference.child(phone).push().key
                databaseReference.child(phone).child(key).setValue(Expense(amountEt.text.toString().toLong(),
                        reason.text.toString(),
                        dateEt.text.toString()))
                dialog.dismiss()
            }
            dialog.show()
        }
    }

}
