package com.cornellappdev.firebasedemo1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.cornellappdev.firebasedemo1.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        val signInIntent = AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(
            arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())
        ).build()

        val signInLauncher =
            registerForActivityResult(FirebaseAuthUIActivityResultContract()) { res ->
                if (res.resultCode == RESULT_OK) {
                    // Signed in successfully
                    val user = auth.currentUser
                    Glide.with(this).load(user!!.photoUrl.toString()).override(100, 100)
                        .into(binding.profileImage)
                }
            }

        binding.fab.setOnClickListener {
            signInLauncher.launch(signInIntent)
        }

        binding.button.setOnClickListener {
            startActivity(Intent(this, SecondaryActivity::class.java))
        }

        if (auth.currentUser != null) {
            updateUI()
        }
    }

    private fun updateUI() {
        val user = auth.currentUser
        Glide.with(this).load(user!!.photoUrl.toString()).override(100, 100)
            .into(binding.profileImage)

        val db = Firebase.firestore
        val docRef = db.collection("profiles").document(auth.currentUser?.uid.toString())
        docRef.get().addOnSuccessListener { document ->
            if (document != null) {
                val profile = document.toObject<Profile>()
                binding.collegeText.text = profile?.college
            }
        }
    }

    companion object {
        const val TAG = "MainActivity"
    }
}
