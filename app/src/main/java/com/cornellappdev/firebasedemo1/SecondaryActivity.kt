package com.cornellappdev.firebasedemo1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cornellappdev.firebasedemo1.databinding.ActivitySecondaryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class SecondaryActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondaryBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        auth.currentUser

        val db = Firebase.firestore

        binding.button.setOnClickListener {
            val newProfile = Profile(
                binding.favoriteFood.text.toString(),
                binding.collegeNameField.text.toString()
            )
            db.collection("profiles").document(auth.currentUser?.uid.toString()).set(newProfile)
                .addOnCompleteListener {
                    startActivity(Intent(this, MainActivity::class.java))
                }
        }
    }
}
