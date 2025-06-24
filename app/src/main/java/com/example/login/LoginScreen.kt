package com.example.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.login.ui.theme.LoginTheme

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
            .testTag("login_screen"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 页面标题
        Text(
            text = stringResource(R.string.login_title),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(bottom = 48.dp)
                .testTag("login_title")
        )
        
        // 用户名输入框
        OutlinedTextField(
            value = username,
            onValueChange = { 
                username = it
                errorMessage = ""
            },
            label = { Text(stringResource(R.string.username_hint)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .testTag("username_field"),
            singleLine = true
        )
        
        // 密码输入框
        OutlinedTextField(
            value = password,
            onValueChange = { 
                password = it
                errorMessage = ""
            },
            label = { Text(stringResource(R.string.password_hint)) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .testTag("password_field"),
            singleLine = true
        )
        
        // 错误信息
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .testTag("error_message")
            )
        }
        
        // 登录按钮
        Button(
            onClick = {
                when {
                    username.isEmpty() && password.isEmpty() -> {
                        errorMessage = "用户名和密码不能为空"
                    }
                    username.isEmpty() -> {
                        errorMessage = "用户名不能为空"
                    }
                    password.isEmpty() -> {
                        errorMessage = "密码不能为空"
                    }
                    username == "test" && password == "123" -> {
                        onLoginSuccess()
                    }
                    else -> {
                        errorMessage = "用户名或密码错误"
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("login_button")
        ) {
            Text(
                text = stringResource(R.string.login_button),
                fontSize = 16.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginTheme {
        LoginScreen(onLoginSuccess = {})
    }
} 