package com.wodo.gymapp.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wodo.gymapp.model.User
import com.wodo.gymapp.repository.UserRepository

class SignupViewModel : ViewModel() {
    private val userRepository = UserRepository()

    private val _signupSuccess = MutableLiveData<Boolean>()
    val signupSuccess: LiveData<Boolean> = _signupSuccess

    fun registerUser(user: User, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        userRepository.registerUser(user, onSuccess, onFailure)
    }

    fun setSignupSuccess(success: Boolean) {
        _signupSuccess.value = success
    }
}
