package com.nexflare.expensemanager

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var firebaseDataBase:FirebaseDatabase
    private lateinit var databaseReference:DatabaseReference
    private lateinit var userArrayList:ArrayList<User>
    private lateinit var adapter:UserAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firebaseDataBase= FirebaseDatabase.getInstance()
        firebaseDataBase.setPersistenceEnabled(true)
        databaseReference=firebaseDataBase.getReference("users")
        userArrayList=ArrayList()
        adapter= UserAdapter(this,userArrayList)
        userRv.layoutManager=LinearLayoutManager(this)
        userRv.adapter=adapter
        getUserData()
        fab.setOnClickListener {
            checkForPermission()
        }

    }

    private fun getUserData() {
        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {
                showToast(p0?.toString()?:"Some error occured")
            }

            override fun onDataChange(p0: DataSnapshot?) {
                userArrayList.clear()
                for (user:DataSnapshot in p0?.children!!){
                    userArrayList.add(User(user.child("name").value.toString(),
                            user.child("phone").value.toString(),
                            user.child("total").value as Long))
                }
                adapter.updateArrayList(userArrayList)
            }

        })
    }

    private fun checkForPermission() {
        if(ActivityCompat.checkSelfPermission(this@MainActivity,Manifest.permission.READ_CONTACTS)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.READ_CONTACTS),1234)
        }
        else{
            getContact()
        }
    }

    private fun getContact() {
        val intent= Intent(Intent.ACTION_PICK)
        intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
        startActivityForResult(intent,1243)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==1243){
            if(resultCode== Activity.RESULT_OK){
                val contactData=data?.data
                val cursor: Cursor =managedQuery(contactData,null,null,null,null)
                cursor.moveToFirst()
                val number = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val user=User(name,number)
                val key=databaseReference.push().key
                databaseReference.child(key).setValue(user)
                databaseReference.addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onCancelled(p0: DatabaseError?) {
                        showToast(p0?.message?:"Some error occurred")
                    }

                    override fun onDataChange(p0: DataSnapshot?) {
                        for(contact:DataSnapshot in p0?.children!!){
                            Log.d("TAGGER",contact.key+": "+contact.child("total").value)
                        }

                    }

                })



            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==1234){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getContact()
            }
        }
    }
    /*inline fun View.snack(message: String, length: Int = Snackbar.LENGTH_SHORT, f: Snackbar.() -> Unit) {
        val snack = Snackbar.make(this, message, length)
        snack.f()
        snack.show()
    }*/
    private fun Context.showToast(message:String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }
}
