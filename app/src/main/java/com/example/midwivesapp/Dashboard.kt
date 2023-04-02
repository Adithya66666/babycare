package com.example.midwivesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.midwivesapp.databinding.ActivityDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Dashboard : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var user: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = FirebaseAuth.getInstance()

        readData()

        binding.btnLogout.setOnClickListener{
            signOutUser()
        }
        binding.btnCreateMotherAccount.setOnClickListener{
            startActivity(Intent(this,addMotherForm::class.java))
        }
        binding.qrScanner.setOnClickListener{
            startActivity(Intent(this,MotherAddComplete::class.java))
        }
        binding.viewMotherList.setOnClickListener{
            startActivity(Intent(this,MotherList::class.java))
        }
    }

    private fun readData(){
        binding.txtWelcome.text = user.uid.toString()
        FirebaseDatabase.getInstance().getReference("User").child(user.uid.toString()).get().addOnSuccessListener {
            if(it.exists()){
                val firstName = it.child("firstName").value
                val lastName = it.child("lastName").value

                binding.txtWelcome.text = user.uid.toString() /*"Welcome! $firstName $lastName"*/
            }
        }
    }


    private fun signOutUser(){
        /*
        if(ConnectionCheck.checkForInternet(this)){
            user.signOut()
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
        else{
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
        }*/

        user.signOut()
        startActivity(Intent(this,Login::class.java))
        finish()
    }
}