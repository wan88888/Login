package com.example.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginState(
    val username: String = "",
    val password: String = "",
    val errorMessage: String = "",
    val isLoggedIn: Boolean = false
)

data class HomeState(
    val clickCount: Int = 0
)

class LoginViewModel : ViewModel() {
    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()
    
    private val _homeState = MutableStateFlow(HomeState())
    val homeState: StateFlow<HomeState> = _homeState.asStateFlow()
    
    fun updateUsername(username: String) {
        _loginState.value = _loginState.value.copy(
            username = username,
            errorMessage = ""
        )
    }
    
    fun updatePassword(password: String) {
        _loginState.value = _loginState.value.copy(
            password = password,
            errorMessage = ""
        )
    }
    
    fun login() {
        val username = _loginState.value.username
        val password = _loginState.value.password
        
        val errorMessage = validateInput(username, password)
        
        if (errorMessage.isNotEmpty()) {
            _loginState.value = _loginState.value.copy(errorMessage = errorMessage)
        } else if (isValidCredentials(username, password)) {
            _loginState.value = _loginState.value.copy(
                isLoggedIn = true,
                errorMessage = ""
            )
        } else {
            _loginState.value = _loginState.value.copy(
                errorMessage = "用户名或密码错误"
            )
        }
    }
    
    fun logout() {
        _loginState.value = LoginState()
        _homeState.value = HomeState()
    }
    
    fun incrementClick() {
        _homeState.value = _homeState.value.copy(
            clickCount = _homeState.value.clickCount + 1
        )
    }
    
    fun resetCount() {
        _homeState.value = _homeState.value.copy(clickCount = 0)
    }
    
    private fun validateInput(username: String, password: String): String {
        return when {
            username.isEmpty() && password.isEmpty() -> "用户名和密码不能为空"
            username.isEmpty() -> "用户名不能为空"
            password.isEmpty() -> "密码不能为空"
            else -> ""
        }
    }
    
    private fun isValidCredentials(username: String, password: String): Boolean {
        return username == "test" && password == "123"
    }
} 