package com.example.midwivesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.midwivesapp.databinding.ActivityMotherBmiBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MotherBmi : AppCompatActivity() {
    private lateinit var binding: ActivityMotherBmiBinding
    private lateinit var user: FirebaseAuth

    private lateinit var bmiArrayList : ArrayList<Bmi>
    private lateinit var bmiRecyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMotherBmiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var pregnancyId = intent.getStringExtra("pregnancyId")

        bmiRecyclerView = binding.bmiList
        bmiRecyclerView.layoutManager = LinearLayoutManager(this)
        bmiRecyclerView.setHasFixedSize(true)
        bmiArrayList = arrayListOf<Bmi>()
        readData(pregnancyId.toString())

        binding.btnAddBmi.setOnClickListener{
            var intent = Intent(this,AddMotherBmi   ::class.java).also {
                it.putExtra("pregnancyId",pregnancyId)
            }
            startActivity(intent)
            finish()
        }

    }

    private fun readData(pregnancyId:String){
        FirebaseDatabase.getInstance().getReference("Bmi").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(fineSnapshot in snapshot.children){
                        if(fineSnapshot.child("pregnancyId").value.toString() == pregnancyId){
                            val bmiItem =  fineSnapshot.getValue(Bmi::class.java)
                            bmiArrayList.add(bmiItem!!)
                        }
                    }
                    bmiRecyclerView.adapter = BmiAdapter(bmiArrayList,this@MotherBmi)
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun onItemClick(position: Int) {

    }
}