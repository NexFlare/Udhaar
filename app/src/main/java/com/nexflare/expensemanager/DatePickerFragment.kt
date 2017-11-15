package com.nexflare.expensemanager

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import java.util.*

/**
 * Created by nexflare on 15/11/17.
 */
class DatePickerFragment:DialogFragment(),DatePickerDialog.OnDateSetListener{
    lateinit var datePickerInterface:DatePickerInterface
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c=Calendar.getInstance()
        val year=c.get(Calendar.YEAR)
        val month=c.get(Calendar.MONTH)
        val day=c.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(activity,this,year,month,day)
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        datePickerInterface.getDate(p1,p2,p3)

    }
    companion object {
        fun newInstance(datePickerInterface: DatePickerInterface):DatePickerFragment{
            val datePickerFragment=DatePickerFragment()
            datePickerFragment.datePickerInterface=datePickerInterface
            return datePickerFragment
        }
    }

}