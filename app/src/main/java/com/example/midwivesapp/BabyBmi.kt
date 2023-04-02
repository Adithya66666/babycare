package com.example.midwivesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.midwivesapp.databinding.ActivityBabyBmiBinding
import com.google.firebase.auth.FirebaseAuth

class BabyBmi : AppCompatActivity() {

    private lateinit var binding:ActivityBabyBmiBinding
    private lateinit var user:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBabyBmiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = FirebaseAuth.getInstance()

        var babyId = intent.getStringExtra("babyId")

    }
}