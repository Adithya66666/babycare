package com.example.midwivesapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.midwivesapp.databinding.ActivityViewMotherClinicBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.util.*

class ViewMotherClinic : AppCompatActivity() {
    private lateinit var binding: ActivityViewMotherClinicBinding
    private lateinit var user: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityViewMotherClinicBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var status = intent.getStringExtra("status")
        var clinicId = intent.getStringExtra("clinicId")
        var pregnancyId = intent.getStringExtra("pregnancyId")

        if(status.toString() == "pending"){
            showForm()
        }else if(status.toString() == "completed"){
            readData(clinicId.toString())
        }

        binding.btnAdd.setOnClickListener{
            addClinic(clinicId.toString(),pregnancyId.toString())
        }
    }

    private fun showForm(){
        binding.form.visibility = View.VISIBLE
        binding.view.visibility = View.GONE
    }

    private fun readData(clinicId:String){
        binding.form.visibility = View.GONE
        binding.view.visibility = View.VISIBLE
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addClinic(clinicId: String,pregnancyId:String){
        var week = binding.pregnancyWeek.text.toString()
        var weight = binding.motherWeight.text.toString()
        var swelling = binding.swelling.text.toString()
        var pressure = binding.bloodPressure.text.toString()
        var position = binding.position.text.toString()
        var heartBeat = binding.heartBeat.text.toString()
        var powder = binding.powderCount.text.toString()
        var createdDate = LocalDate.now().toString()
        var nurseId = user.uid.toString()
        var conservationId = UUID.randomUUID().toString()

        if(week.isNotEmpty() && weight.isNotEmpty() && swelling.isNotEmpty() && pressure.isNotEmpty() && position.isNotEmpty() && heartBeat.isNotEmpty() &&
                createdDate.isNotEmpty() && nurseId.isNotEmpty()){
            if(powder.isEmpty()){
                powder = "0"
            }
            var conservationItem = MotherConservationItem(createdDate,nurseId,clinicId,week,weight,swelling,pressure,position,heartBeat,powder)

            FirebaseDatabase.getInstance().getReference("MotherConservations").child(conservationId).setValue(conservationItem).addOnSuccessListener {
                FirebaseDatabase.getInstance().getReference("MotherClinic").child("$clinicId/status").setValue("completed")
                var intent = Intent(this,MotherClinic::class.java).also {
                    it.putExtra("pregnancyId",pregnancyId)
                }
                startActivity(intent)
                finish()
            }

        }
    }
}