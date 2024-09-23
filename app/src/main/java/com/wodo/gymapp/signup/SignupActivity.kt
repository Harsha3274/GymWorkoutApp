package com.wodo.gymapp.signup

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.wodo.gymapp.R
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

        // Set onClick listener for login text
        binding.LoginText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Set OnClickListener for Register button
        binding.button.setOnClickListener {
            val username = binding.usernameETSignup.text.toString().trim()
            val age = binding.ageETSignup.text.toString().trim()
            val gender = binding.genderETSignup.text.toString().trim()
            val email = binding.emailETSignup.text.toString().trim()
            val password = binding.passwordETSignup.text.toString().trim()
            val confirmPassword = binding.ConfirmPasswordETSignup.text.toString().trim()

            // Validate input fields
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
            if (password != confirmPassword) {
                binding.ConfirmPasswordETSignup.error = "Passwords do not match"
                return@setOnClickListener
            }

            // Show ProgressBar
            binding.signupProgressBar.visibility = View.VISIBLE

            // Register user with Firebase Authentication
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // User registered successfully, store additional user data in Firestore
                        val userId = auth.currentUser?.uid
                        val user = User(username, age.toInt(), gender, email, password)

                        if (userId != null) {
                            db.collection("users").document(userId).set(user)
                                .addOnSuccessListener {
                                    binding.signupProgressBar.visibility = View.GONE
                                    viewModel.setSignupSuccess(true)
                                }
                                .addOnFailureListener { e ->
                                    binding.signupProgressBar.visibility = View.GONE
                                    viewModel.setSignupSuccess(false)
                                    Toast.makeText(this, "Failed to store user data: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    } else {
                        binding.signupProgressBar.visibility = View.GONE
                        viewModel.setSignupSuccess(false)
                        Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        // Set OnTouchListener for Password field to toggle visibility
        binding.passwordETSignup.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                // Check if the touch is on the drawableEnd (eye icon)
                if (event.rawX >= (binding.passwordETSignup.right - binding.passwordETSignup.compoundDrawables[2].bounds.width())) {
                    togglePasswordVisibility()
                    return@setOnTouchListener true
                }
            }
            // Handle click accessibility by calling performClick explicitly
            v.performClick()
            false
        }

        // Set OnTouchListener for Confirm Password field to toggle visibility
        binding.ConfirmPasswordETSignup.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                // Check if the touch is on the drawableEnd (eye icon)
                if (event.rawX >= (binding.ConfirmPasswordETSignup.right - binding.ConfirmPasswordETSignup.compoundDrawables[2].bounds.width())) {
                    toggleConfirmPasswordVisibility()
                    return@setOnTouchListener true
                }
            }
            // Handle click accessibility by calling performClick explicitly
            v.performClick()
            false
        }
    }

    // Toggle visibility for password field
    private fun togglePasswordVisibility() {
        if (binding.passwordETSignup.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            binding.passwordETSignup.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            binding.passwordETSignup.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock, 0, R.drawable.closed_eye, 0)
        } else {
            binding.passwordETSignup.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.passwordETSignup.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock, 0, R.drawable.open_eye, 0)
        }
        binding.passwordETSignup.setSelection(binding.passwordETSignup.text.length)
    }

    // Toggle visibility for confirm password field
    private fun toggleConfirmPasswordVisibility() {
        if (binding.ConfirmPasswordETSignup.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            binding.ConfirmPasswordETSignup.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            binding.ConfirmPasswordETSignup.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock, 0, R.drawable.closed_eye, 0)
        } else {
            binding.ConfirmPasswordETSignup.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.ConfirmPasswordETSignup.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock, 0, R.drawable.open_eye, 0)
        }
        binding.ConfirmPasswordETSignup.setSelection(binding.ConfirmPasswordETSignup.text.length)
    }
}
