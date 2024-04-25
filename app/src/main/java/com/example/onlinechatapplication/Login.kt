package com.example.onlinechatapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.onlinechatapplication.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

//    private lateinit var editEmail: EditText
//    private lateinit var editPassword: EditText
//    private lateinit var btnLogin: Button
//    private lateinit var btnSign: Button

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()

        binding.signUp.setOnClickListener {
            startActivity(Intent(this,SignUp::class.java))
        }

        binding.login.setOnClickListener {
            val email = binding.editMail.text.toString()
            val password = binding.editPassword.text.toString()

            login(email, password)
        }
    }
    private fun login(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this,MainActivity::class.java))
                    // Sign in success, update UI with the signed-in user's information
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "Could not login", Toast.LENGTH_LONG).show()
                }
            }

    }
}