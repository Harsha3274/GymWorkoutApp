package com.wodo.gymapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.wodo.gymapp.model.User

class UserRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    // LiveData to expose user data
    val userLiveData = MutableLiveData<User?>()

    // Method to handle user registration
    fun registerUser(user: User, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                    db.collection("users").document(userId).set(user)
                        .addOnSuccessListener { onSuccess() }
                        .addOnFailureListener { exception -> onFailure(exception) }
                } else {
                    onFailure(task.exception ?: Exception("Registration failed"))
                }
            }
    }

    // Method to handle user login
    fun loginUser(email: String, password: String, onSuccess: (User?) -> Unit, onFailure: (Exception) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        db.collection("users").document(userId).get()
                            .addOnSuccessListener { document ->
                                if (document.exists()) {
                                    val user = document.toObject(User::class.java)
                                    onSuccess(user)
                                } else {
                                    onFailure(Exception("User data not found"))
                                }
                            }
                            .addOnFailureListener { exception -> onFailure(exception) }
                    } else {
                        onFailure(Exception("User ID not found"))
                    }
                } else {
                    onFailure(task.exception ?: Exception("Login failed"))
                }
            }
    }
}
