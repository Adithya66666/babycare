package com.example.midwivesapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.midwivesapp.databinding.ActivityAddMotherMedBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDate

class AddMotherMed : AppCompatActivity() {
    private lateinit var binding: ActivityAddMotherMedBinding
    private lateinit var user: FirebaseAuth

    private var counter:Int = 0
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddMotherMedBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        user = FirebaseAuth.getInstance()

        var pregnancyId = intent.getStringExtra("pregnancyId")

        binding.statusText.text = "Add Medicine for this mother"

        binding.btnConfirm.setOnClickListener{
            addMed(pregnancyId.toString())
        }

        binding.btnBack.setOnClickListener{
            var intent = Intent(this,MotherMed::class.java).also {
                it.putExtra("pregnancyId",pregnancyId)
            }
            startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addMed(pregnancyId:String){
        counter = 0

        var medName = binding.med.text.toString()
        if(medName.isNotEmpty()){
            FirebaseDatabase.getInstance().getReference("MotherMed").get().addOnSuccessListener {
                if(it.exists()){
                    FirebaseDatabase.getInstance().getReference("MotherMed").addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.exists()){
                                for(fineSnapshot in snapshot.children){
                                    if(fineSnapshot.child("pregnancyId").value.toString() == pregnancyId){
                                        counter++
                                    }
                                }
                                addMedConfirm(counter,pregnancyId,medName)
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {

                        }
                    })

                }else{
                    addMedConfirm(0,pregnancyId,medName)
                }
            }
        }else{
            Toast.makeText(this, "Medicine name can not be empty", Toast.LENGTH_SHORT).show()
        }

    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun addMedConfirm(counter:Int,pregnancyId:String,medName:String){

        var bmiId = counter + 1
        var createdDate = LocalDate.now().toString()

        var medicine = MotherMedItem(pregnancyId,createdDate,medName,user.uid.toString())

        FirebaseDatabase.getInstance().getReference("MotherMed").child(bmiId.toString()).setValue(medicine).addOnSuccessListener {
            var intent = Intent(this,MotherMed::class.java).also {
                it.putExtra("pregnancyId",pregnancyId)
            }
            startActivity(intent)
            finish()
        }

    }

}