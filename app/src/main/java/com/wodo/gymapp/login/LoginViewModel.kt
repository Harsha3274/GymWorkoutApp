package com.wodo.gymapp.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wodo.gymapp.model.User
import com.wodo.gymapp.repository.UserRepository

class LoginViewModel : ViewModel() {
    private val userRepository = UserRepository()

    // LiveData to observe user data
    private val _userLiveData = MutableLiveData<User?>()
    val userLiveData: LiveData<User?> = _userLiveData

    // Method to handle user login
    fun loginUser(email: String, password: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        userRepository.loginUser(email, password, { user ->
            _userLiveData.value = user
            onSuccess()
        }, onFailure)
    }

    // Method to manually set the user data
    fun setUser(user: User?) {
        _userLiveData.value = user
    }
}
