package com.example.onlinechatapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.onlinechatapplication.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {
    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference()

        binding.btnSignUp.setOnClickListener {
            val email = binding.editEmail.text.toString()
            val password = binding.editPasswordSignup.text.toString()
            val name = binding.name.text.toString()

            signUp(name,email,password)
        }
    }

    private fun signUp(name: String,email: String,password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    addUserToDatabase(name,email,mAuth.uid!!)
                    startActivity(Intent(this,MainActivity::class.java))
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "Could not sign up", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun addUserToDatabase(name: String, email: String, uid: String) {
        databaseReference.child("User").child(uid).setValue(User(name,email,uid))
    }
}