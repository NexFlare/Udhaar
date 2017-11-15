package com.nexflare.expensemanager

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_expense.*


class ExpenseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense)
        fabExpense.setOnClickListener{
            val dateFragment=DatePickerFragment.newInstance(object: DatePickerInterface{
                override fun getDate(year: Int, month: Int, day: Int) {
                    Log.d("TAGGER","$year $month $day")
                }

            })
            dateFragment.show(fragmentManager,"datePicker")
        }
    }

}
