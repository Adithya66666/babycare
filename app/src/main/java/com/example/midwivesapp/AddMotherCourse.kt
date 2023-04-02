package com.example.midwivesapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.midwivesapp.databinding.ActivityAddMotherCourseBinding
import com.example.midwivesapp.databinding.ActivityAddMotherMedBinding
import com.example.midwivesapp.databinding.ActivityMotherCoursesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDate

class AddMotherCourse : AppCompatActivity() {
    private lateinit var binding: ActivityAddMotherCourseBinding
    private lateinit var user:FirebaseAuth

    private var counter:Int = 0
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddMotherCourseBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.statusText.text = "Add Injection Course for this mother"

        var pregnancyId = intent.getStringExtra("pregnancyId")

        binding.btnConfirm.setOnClickListener{
            addCourse(pregnancyId.toString())
        }

        binding.btnBack.setOnClickListener{
            var intent = Intent(this,MotherCourses::class.java).also {
                it.putExtra("pregnancyId",pregnancyId)
            }
            startActivity(intent)
            finish()
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun addCourse(pregnancyId:String){
        counter = 0

        var courseName = binding.course.text.toString()
        if(courseName.isNotEmpty()){
            FirebaseDatabase.getInstance().getReference("MotherCourse").get().addOnSuccessListener {
                if(it.exists()){
                    FirebaseDatabase.getInstance().getReference("MotherCourse").addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.exists()){
                                for(fineSnapshot in snapshot.children){
                                    counter++
                                }
                                addCourseConfirm(counter,pregnancyId,courseName)
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {

                        }
                    })

                }else{
                    addCourseConfirm(0,pregnancyId,courseName)
                }
            }
        }else{
            Toast.makeText(this, "Medicine name can not be empty", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addCourseConfirm(counter:Int,pregnancyId:String,courseName:String){

        var bmiId = counter + 1
        var createdDate = LocalDate.now().toString()

        var motherCourseItem = MotherCourseItem(pregnancyId,createdDate,courseName,user.uid.toString())

        FirebaseDatabase.getInstance().getReference("MotherCourse").child(bmiId.toString()).setValue(motherCourseItem).addOnSuccessListener {
            var intent = Intent(this,MotherCourses::class.java).also {
                it.putExtra("pregnancyId",pregnancyId)
            }
            startActivity(intent)
            finish()
        }

    }
}