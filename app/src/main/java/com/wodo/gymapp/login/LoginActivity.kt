package com.wodo.gymapp.login

import android.content.Intent
import android.os.Bundle
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
import com.wodo.gymapp.HomeActivity
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

        // Set up the Activity Result Launcher for Google Sign-In
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

        // Observe user LiveData
        viewModel.userLiveData.observe(this, Observer { user ->
            if (user != null) {
                Toast.makeText(this, "Login Successful, Welcome ${user.username}", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish() // Close LoginActivity
            }
        })

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditTextLogin.text.toString().trim()
            val password = binding.passwordEditTextLogin.text.toString().trim()

            // Validation checks
            if (!ValidationUtils.isEmailValid(email)) {
                binding.emailEditTextLogin.error = "Invalid email"
                return@setOnClickListener
            }
            if (!ValidationUtils.isPasswordValid(password)) {
                binding.passwordEditTextLogin.error = "Invalid password"
                return@setOnClickListener
            }

            // Login user and fetch user data
            viewModel.loginUser(email, password, onSuccess = {
                // onSuccess is handled by LiveData observer
            }, onFailure = { exception ->
                Toast.makeText(this, "Login Failed: ${exception.message}", Toast.LENGTH_SHORT).show()
            })
        }

        // Navigate to SignupActivity when clicking "Sign Up"
        binding.SignupText.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
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
                val email = account.email ?: "No Email"
                val username = account.displayName ?: "No Name"

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
