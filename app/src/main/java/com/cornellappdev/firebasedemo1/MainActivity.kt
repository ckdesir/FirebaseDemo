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
    }

    companion object {
        const val TAG = "MainActivity"
    }
}
