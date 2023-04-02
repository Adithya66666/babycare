package com.example.midwivesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.midwivesapp.databinding.ActivityBabyCoursesBinding
import com.google.firebase.auth.FirebaseAuth

class BabyCourses : AppCompatActivity() {
    private lateinit var binding:ActivityBabyCoursesBinding
    private lateinit var user:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBabyCoursesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var babyId = intent.getStringExtra("babyId")

    }
}