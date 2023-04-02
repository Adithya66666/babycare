package com.example.midwivesapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.midwivesapp.databinding.ActivityAddMotherBmiBinding
import com.example.midwivesapp.databinding.ActivityMotherBmiBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDate
import java.util.*

class AddMotherBmi : AppCompatActivity() {
    private lateinit var binding: ActivityAddMotherBmiBinding
    private lateinit var user: FirebaseAuth

    private var counter:Int = 0

    private lateinit var bmiStatus:String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMotherBmiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.statusText.text = "Add BMI Data for this mother"

        var pregnancyId = intent.getStringExtra("pregnancyId")

        binding.btnConfirm.setOnClickListener{
            addBmi(pregnancyId.toString())
        }

        binding.btnBack.setOnClickListener{
            var intent = Intent(this,MotherBmi::class.java).also {
                it.putExtra("pregnancyId",pregnancyId)
            }
            startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addBmi(pregnancyId:String){
        counter = 0

        FirebaseDatabase.getInstance().getReference("Bmi").get().addOnSuccessListener {
            if(it.exists()){
                FirebaseDatabase.getInstance().getReference("Bmi").addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()){
                            for(fineSnapshot in snapshot.children){
                                counter++
                            }
                            addBmiConfirm(counter,pregnancyId)
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {

                    }
                })

            }else{
                binding.statusText.text = "null"
                addBmiConfirm(0,pregnancyId)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addBmiConfirm(counter:Int,pregnancyId: String){

        var weight = binding.weight.text.toString()
        var height = binding.height.text.toString()

        if(weight.isNotEmpty() && height.isNotEmpty()){
            //var bmiId = UUID.randomUUID().toString()
            var bmiId = counter + 1
            var createdDate = LocalDate.now().toString()

            var bmiVal = (weight.toFloat() / (height.toFloat() * height.toFloat()))*10000

            bmiStatus = if(bmiVal < 18.5){
                "Underweight"
            }else if(bmiVal in 18.5..24.9){
                "Healthy weight"
            }else if(bmiVal in 25.0..29.9){
                "Overweight"
            }else if(bmiVal in 30.0..39.9){
                "obesity"
            }else{
                "Class 3 obesity"
            }

            binding.statusText.text = bmiVal.toString()
            var bmi = Bmi(pregnancyId,weight,height,bmiVal,createdDate,bmiStatus)

            FirebaseDatabase.getInstance().getReference("Bmi").child(bmiId.toString()).setValue(bmi).addOnSuccessListener {
                var intent = Intent(this,MotherBmi::class.java).also {
                    it.putExtra("pregnancyId",pregnancyId)
                }
                startActivity(intent)
                finish()
            }
        }else{
            Toast.makeText(this, "Fill all the weight and height inputs", Toast.LENGTH_SHORT).show()
        }


    }
}