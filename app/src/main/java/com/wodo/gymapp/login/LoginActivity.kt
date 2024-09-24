package com.wodo.gymapp.login

import com.wodo.gymapp.HomeActivity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.wodo.gymapp.R
import com.wodo.gymapp.databinding.ActivityLoginBinding
import com.wodo.gymapp.signup.SignupActivity
import com.wodo.gymapp.utils.ValidationUtils

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>
    private lateinit var firebaseAuth: FirebaseAuth
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        FirebaseApp.initializeApp(this)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        firebaseAuth = FirebaseAuth.getInstance()

        googleSignInLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResult(task)
        }

        binding.googleImageView.setOnClickListener {
            Toast.makeText(this, "Logging In", Toast.LENGTH_SHORT).show()
            signInGoogle()
        }

        // Observe the LiveData for login success
        viewModel.userLiveData.observe(this, Observer { user ->
            if (user != null) {
                val username = user.username  // Assuming 'user' has a 'username' field

                // Save the username in SharedPreferences
                val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("username", username)
                editor.apply()

                Toast.makeText(this, "Login Successful, Welcome $username", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        })

        // Handle password visibility toggle
        binding.passwordEditTextLogin.setOnTouchListener { _, event ->
            val drawableEnd = 2  // index for drawableEnd
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (binding.passwordEditTextLogin.right - binding.passwordEditTextLogin.compoundDrawables[drawableEnd].bounds.width())) {
                    togglePasswordVisibility()
                    return@setOnTouchListener true
                }
            }
            false
        }

        // Handle login button click
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditTextLogin.text.toString().trim()
            val password = binding.passwordEditTextLogin.text.toString().trim()

            if (!ValidationUtils.isEmailValid(email)) {
                binding.emailEditTextLogin.error = "Invalid email"
                return@setOnClickListener
            }
            if (!ValidationUtils.isPasswordValid(password)) {
                binding.passwordEditTextLogin.error = "Invalid password"
                return@setOnClickListener
            }

            binding.loginProgressBar.visibility = View.VISIBLE  // Show progress bar

            // Login user
            viewModel.loginUser(email, password, onSuccess = {
                binding.loginProgressBar.visibility = View.GONE  // Hide progress bar
                // Success logic will be handled by the LiveData observer
            }, onFailure = { exception ->
                binding.loginProgressBar.visibility = View.GONE  // Hide progress bar
                Toast.makeText(this, "Login Failed: ${exception.message}", Toast.LENGTH_SHORT).show()
            })
        }

        binding.SignupText.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Hide password
            binding.passwordEditTextLogin.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            binding.passwordEditTextLogin.setCompoundDrawablesWithIntrinsicBounds(R.drawable.email, 0, R.drawable.closed_eye, 0)
        } else {
            // Show password
            binding.passwordEditTextLogin.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.passwordEditTextLogin.setCompoundDrawablesWithIntrinsicBounds(R.drawable.email, 0, R.drawable.open_eye, 0)
        }
        isPasswordVisible = !isPasswordVisible
        // Move the cursor to the end of the text
        binding.passwordEditTextLogin.setSelection(binding.passwordEditTextLogin.text.length)
    }

    private fun signInGoogle() {
        val signInIntent: Intent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                updateUI(account)
            }
        } catch (e: ApiException) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val username = account.displayName ?: "No Name"

                // Save username to SharedPreferences
                val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("username", username)
                editor.apply()

                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Google Sign-In failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }
}
