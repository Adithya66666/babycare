package com.example.midwivesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.midwivesapp.databinding.ActivityBabyMedBinding
import com.google.firebase.auth.FirebaseAuth

class BabyMed : AppCompatActivity() {
    private lateinit var binding:ActivityBabyMedBinding
    private lateinit var user:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBabyMedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var babyId = intent.getStringExtra("babyId")
    }
}