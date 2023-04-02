package com.example.midwivesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.midwivesapp.databinding.ActivityBabyClinicBinding
import com.google.firebase.auth.FirebaseAuth

class BabyClinic : AppCompatActivity() {
    private lateinit var binding:ActivityBabyClinicBinding
    private lateinit var user:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBabyClinicBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = FirebaseAuth.getInstance()

        var babyId = intent.getStringExtra("babyId")

    }
}