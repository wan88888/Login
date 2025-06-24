package com.example.login

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class LoginViewModelTest {
    
    private lateinit var viewModel: LoginViewModel
    
    @Before
    fun setup() {
        viewModel = LoginViewModel()
    }
    
    @Test
    fun `初始状态应该是未登录状态`() {
        val state = viewModel.loginState.value
        assertEquals("", state.username)
        assertEquals("", state.password)
        assertEquals("", state.errorMessage)
        assertFalse(state.isLoggedIn)
    }
    
    @Test
    fun `更新用户名应该清除错误信息`() {
        // 先设置一个错误状态
        viewModel.updateUsername("")
        viewModel.updatePassword("")
        viewModel.login()
        assertTrue(viewModel.loginState.value.errorMessage.isNotEmpty())
        
        // 更新用户名应该清除错误
        viewModel.updateUsername("test")
        assertEquals("", viewModel.loginState.value.errorMessage)
        assertEquals("test", viewModel.loginState.value.username)
    }
    
    @Test
    fun `更新密码应该清除错误信息`() {
        // 先设置一个错误状态
        viewModel.updateUsername("")
        viewModel.updatePassword("")
        viewModel.login()
        assertTrue(viewModel.loginState.value.errorMessage.isNotEmpty())
        
        // 更新密码应该清除错误
        viewModel.updatePassword("123")
        assertEquals("", viewModel.loginState.value.errorMessage)
        assertEquals("123", viewModel.loginState.value.password)
    }
    
    @Test
    fun `用户名和密码都为空时应该显示对应错误信息`() {
        viewModel.updateUsername("")
        viewModel.updatePassword("")
        viewModel.login()
        
        assertEquals("用户名和密码不能为空", viewModel.loginState.value.errorMessage)
        assertFalse(viewModel.loginState.value.isLoggedIn)
    }
    
    @Test
    fun `仅用户名为空时应该显示对应错误信息`() {
        viewModel.updateUsername("")
        viewModel.updatePassword("123")
        viewModel.login()
        
        assertEquals("用户名不能为空", viewModel.loginState.value.errorMessage)
        assertFalse(viewModel.loginState.value.isLoggedIn)
    }
    
    @Test
    fun `仅密码为空时应该显示对应错误信息`() {
        viewModel.updateUsername("test")
        viewModel.updatePassword("")
        viewModel.login()
        
        assertEquals("密码不能为空", viewModel.loginState.value.errorMessage)
        assertFalse(viewModel.loginState.value.isLoggedIn)
    }
    
    @Test
    fun `正确的用户名和密码应该登录成功`() {
        viewModel.updateUsername("test")
        viewModel.updatePassword("123")
        viewModel.login()
        
        assertEquals("", viewModel.loginState.value.errorMessage)
        assertTrue(viewModel.loginState.value.isLoggedIn)
    }
    
    @Test
    fun `错误的用户名和密码应该显示错误信息`() {
        viewModel.updateUsername("wrong")
        viewModel.updatePassword("wrong")
        viewModel.login()
        
        assertEquals("用户名或密码错误", viewModel.loginState.value.errorMessage)
        assertFalse(viewModel.loginState.value.isLoggedIn)
    }
    
    @Test
    fun `退出登录应该重置所有状态`() {
        // 先登录并增加计数
        viewModel.updateUsername("test")
        viewModel.updatePassword("123")
        viewModel.login()
        viewModel.incrementClick()
        viewModel.incrementClick()
        
        // 验证状态
        assertTrue(viewModel.loginState.value.isLoggedIn)
        assertEquals(2, viewModel.homeState.value.clickCount)
        
        // 退出登录
        viewModel.logout()
        
        // 验证状态被重置
        assertFalse(viewModel.loginState.value.isLoggedIn)
        assertEquals("", viewModel.loginState.value.username)
        assertEquals("", viewModel.loginState.value.password)
        assertEquals("", viewModel.loginState.value.errorMessage)
        assertEquals(0, viewModel.homeState.value.clickCount)
    }
    
    @Test
    fun `点击计数应该正确增加`() {
        assertEquals(0, viewModel.homeState.value.clickCount)
        
        viewModel.incrementClick()
        assertEquals(1, viewModel.homeState.value.clickCount)
        
        viewModel.incrementClick()
        assertEquals(2, viewModel.homeState.value.clickCount)
        
        viewModel.incrementClick()
        assertEquals(3, viewModel.homeState.value.clickCount)
    }
    
    @Test
    fun `重置计数应该将计数清零`() {
        // 先增加一些计数
        viewModel.incrementClick()
        viewModel.incrementClick()
        viewModel.incrementClick()
        assertEquals(3, viewModel.homeState.value.clickCount)
        
        // 重置计数
        viewModel.resetCount()
        assertEquals(0, viewModel.homeState.value.clickCount)
    }
    
    @Test
    fun `初始的首页状态应该是零计数`() {
        val homeState = viewModel.homeState.value
        assertEquals(0, homeState.clickCount)
    }
} 