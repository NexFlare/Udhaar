package com.nexflare.expensemanager.Activity

import android.app.Dialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.*
import com.google.firebase.database.*
import com.nexflare.expensemanager.*
import com.nexflare.expensemanager.Adapter.ExpenseAdapter
import com.nexflare.expensemanager.R
import kotlinx.android.synthetic.main.activity_expense.*


class ExpenseActivity : AppCompatActivity() {
    private lateinit var firebaseDatabase:FirebaseDatabase
    private lateinit var databaseReference:DatabaseReference
    private lateinit var expenseAdapter: ExpenseAdapter
    lateinit var phone:String
    lateinit var name:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense)
        firebaseDatabase= FirebaseDatabase.getInstance()
        databaseReference=firebaseDatabase.getReference("expense")
        phone=intent.getStringExtra("phone")
        name=intent.getStringExtra("name")
        expenseAdapter = ExpenseAdapter(this, ArrayList())
        expenseRv.layoutManager=LinearLayoutManager(this)
        expenseRv.adapter=expenseAdapter
        getUserData()
        fabExpense.setOnClickListener{

            val dialog=Dialog(this@ExpenseActivity)
            dialog.setContentView(R.layout.layout_expense_dailog)
            val amountEt=dialog.findViewById<EditText>(R.id.amountEt)
            val reason=dialog.findViewById<EditText>(R.id.reasonEt)
            val okTv=dialog.findViewById<TextView>(R.id.okTv)
            val radioGroup=dialog.findViewById<RadioGroup>(R.id.radioGroup)
            val radioUser=dialog.findViewById<RadioButton>(R.id.user)
            val dateTil=dialog.findViewById<TextInputLayout>(R.id.dateTil)
            val dateEt=dialog.findViewById<EditText>(R.id.dateEt)
            val calenderIv=dialog.findViewById<ImageView>(R.id.calenderIv)
            if(name.contains(" "))
                radioUser.setText(name.toCharArray(),0,name.indexOf(" "))
            else
                radioUser.setText(name.toCharArray(),0,name.length)
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
                var amount=amountEt.text.toString().toLong()
                if(radioGroup.checkedRadioButtonId == R.id.user){
                    amount=amountEt.text.toString().toLong()*-1
                }
                databaseReference.child(phone).child(key).setValue(Expense(amount,
                        reason.text.toString(),
                        dateEt.text.toString()))
                firebaseDatabase.getReference("users").orderByChild("phone").equalTo(phone).addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onCancelled(p0: DatabaseError?) {
                        Log.d("TAGGER",p0?.message?:"Some error occurred")
                    }

                    override fun onDataChange(p0: DataSnapshot?) {
                        Log.d("TAGGER",p0?.hasChildren().toString())
                        for(userSnapshot:DataSnapshot in p0!!.children)
                            //Log.d("TAGGER",userSnapshot.child("total").value.toString())
                            userSnapshot.ref?.child("total")?.setValue(amount.plus( userSnapshot.child("total")?.value as Long))
                    }

                })
                dialog.dismiss()
            }
            dialog.show()
        }
    }
    private fun getUserData() {
        databaseReference.child(phone).addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {
                Log.d("TAGGER",p0?.toString()?:"Some error occured")
            }

            override fun onDataChange(p0: DataSnapshot?) {
                val arrayList=ArrayList<Expense>()
                for (user:DataSnapshot in p0?.children!!){
                    Log.d("TAGGER",user.value.toString())
                    arrayList.add(Expense(user.child("amount").value.toString().toLong(),
                            user.child("reason").value.toString(),
                            user.child("time").value.toString()))
                }
                expenseAdapter.updateArrayList(arrayList)
            }

        })
    }

}

