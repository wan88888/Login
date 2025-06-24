package com.example.login

import org.junit.Assert.*
import org.junit.Test

class LoginValidationTest {
    
    /**
     * 测试输入验证逻辑的辅助类
     */
    class LoginValidator {
        fun validateInput(username: String, password: String): String {
            return when {
                username.isEmpty() && password.isEmpty() -> "用户名和密码不能为空"
                username.isEmpty() -> "用户名不能为空"
                password.isEmpty() -> "密码不能为空"
                else -> ""
            }
        }
        
        fun isValidCredentials(username: String, password: String): Boolean {
            return username == "test" && password == "123"
        }
    }
    
    private val validator = LoginValidator()
    
    @Test
    fun `验证空用户名和空密码`() {
        val result = validator.validateInput("", "")
        assertEquals("用户名和密码不能为空", result)
    }
    
    @Test
    fun `验证空用户名但有密码`() {
        val result = validator.validateInput("", "123")
        assertEquals("用户名不能为空", result)
    }
    
    @Test
    fun `验证有用户名但空密码`() {
        val result = validator.validateInput("test", "")
        assertEquals("密码不能为空", result)
    }
    
    @Test
    fun `验证有用户名和密码时无错误`() {
        val result = validator.validateInput("test", "123")
        assertEquals("", result)
    }
    
    @Test
    fun `验证正确的登录凭据`() {
        assertTrue(validator.isValidCredentials("test", "123"))
    }
    
    @Test
    fun `验证错误的用户名`() {
        assertFalse(validator.isValidCredentials("wrong", "123"))
    }
    
    @Test
    fun `验证错误的密码`() {
        assertFalse(validator.isValidCredentials("test", "wrong"))
    }
    
    @Test
    fun `验证都错误的凭据`() {
        assertFalse(validator.isValidCredentials("wrong", "wrong"))
    }
    
    @Test
    fun `验证空凭据不应该通过认证`() {
        assertFalse(validator.isValidCredentials("", ""))
    }
    
    @Test
    fun `验证大小写敏感性 - 用户名`() {
        assertFalse(validator.isValidCredentials("Test", "123"))
        assertFalse(validator.isValidCredentials("TEST", "123"))
    }
    
    @Test
    fun `验证大小写敏感性 - 密码`() {
        assertFalse(validator.isValidCredentials("test", "123A"))
        assertFalse(validator.isValidCredentials("test", "ABC"))
    }
    
    @Test
    fun `验证特殊字符在用户名中`() {
        assertFalse(validator.isValidCredentials("test@", "123"))
        assertFalse(validator.isValidCredentials("test!", "123"))
    }
    
    @Test
    fun `验证特殊字符在密码中`() {
        assertFalse(validator.isValidCredentials("test", "123@"))
        assertFalse(validator.isValidCredentials("test", "123!"))
    }
    
    @Test
    fun `验证空格字符不应该通过认证`() {
        assertFalse(validator.isValidCredentials(" test", "123"))
        assertFalse(validator.isValidCredentials("test ", "123"))
        assertFalse(validator.isValidCredentials("test", " 123"))
        assertFalse(validator.isValidCredentials("test", "123 "))
    }
} 