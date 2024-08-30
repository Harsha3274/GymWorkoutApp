package com.wodo.gymapp.signup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.wodo.gymapp.databinding.ActivitySignupBinding
import com.wodo.gymapp.login.LoginActivity
import com.wodo.gymapp.model.User
import com.wodo.gymapp.utils.ValidationUtils

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val viewModel: SignupViewModel by viewModels()
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        viewModel.signupSuccess.observe(this, Observer { isSuccess ->
            if (isSuccess) {
                Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show()
            }
        })

        binding.LoginText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.button.setOnClickListener {
            val username = binding.usernameETSignup.text.toString().trim()
            val age = binding.ageETSignup.text.toString().trim()
            val gender = binding.genderETSignup.text.toString().trim()
            val email = binding.emailETSignup.text.toString().trim()
            val password = binding.passwordETSignup.text.toString().trim()

            // Validation checks
            if (!ValidationUtils.isUsernameValid(username)) {
                binding.usernameETSignup.error = "Invalid username"
                return@setOnClickListener
            }
            if (!ValidationUtils.isAgeValid(age)) {
                binding.ageETSignup.error = "Invalid age"
                return@setOnClickListener
            }
            if (!ValidationUtils.isEmailValid(email)) {
                binding.emailETSignup.error = "Invalid email"
                return@setOnClickListener
            }
            if (!ValidationUtils.isPasswordValid(password)) {
                binding.passwordETSignup.error = "Invalid password"
                return@setOnClickListener
            }

            // Create user with Firebase Authentication
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // User registered successfully, store additional user data in Firestore
                        val userId = auth.currentUser?.uid
                        val user = User(username, age.toInt(), gender, email, password)

                        if (userId != null) {
                            db.collection("users").document(userId).set(user)
                                .addOnSuccessListener {
                                    viewModel.setSignupSuccess(true)
                                }
                                .addOnFailureListener { e ->
                                    viewModel.setSignupSuccess(false)
                                    Toast.makeText(this, "Failed to store user data: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    } else {
                        viewModel.setSignupSuccess(false)
                        Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}

