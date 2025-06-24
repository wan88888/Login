package com.example.login

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 登录应用的Espresso自动化测试
 * 测试完整的用户交互流程
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginEspressoTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testLoginScreenDisplayed() {
        // 验证登录页面的主要元素是否显示
        composeTestRule.onNodeWithTag("login_screen").assertIsDisplayed()
        composeTestRule.onNodeWithTag("login_title").assertIsDisplayed()
        composeTestRule.onNodeWithTag("username_field").assertIsDisplayed()
        composeTestRule.onNodeWithTag("password_field").assertIsDisplayed()
        composeTestRule.onNodeWithTag("login_button").assertIsDisplayed()
        
        // 验证标题文本
        composeTestRule.onNodeWithTag("login_title")
            .assertTextEquals("欢迎登录")
        
        // 验证登录按钮文本
        composeTestRule.onNodeWithTag("login_button")
            .assertTextContains("登录")
    }

    @Test
    fun testEmptyFieldsValidation() {
        // 测试空字段验证
        composeTestRule.onNodeWithTag("login_button").performClick()
        
        // 验证错误信息显示
        composeTestRule.onNodeWithTag("error_message")
            .assertIsDisplayed()
            .assertTextEquals("用户名和密码不能为空")
    }

    @Test
    fun testEmptyUsernameValidation() {
        // 只输入密码，用户名为空
        composeTestRule.onNodeWithTag("password_field")
            .performTextInput("123")
        
        composeTestRule.onNodeWithTag("login_button").performClick()
        
        // 验证错误信息
        composeTestRule.onNodeWithTag("error_message")
            .assertIsDisplayed()
            .assertTextEquals("用户名不能为空")
    }

    @Test
    fun testEmptyPasswordValidation() {
        // 只输入用户名，密码为空
        composeTestRule.onNodeWithTag("username_field")
            .performTextInput("test")
        
        composeTestRule.onNodeWithTag("login_button").performClick()
        
        // 验证错误信息
        composeTestRule.onNodeWithTag("error_message")
            .assertIsDisplayed()
            .assertTextEquals("密码不能为空")
    }

    @Test
    fun testInvalidCredentials() {
        // 输入错误的用户名和密码
        composeTestRule.onNodeWithTag("username_field")
            .performTextInput("wrong")
        
        composeTestRule.onNodeWithTag("password_field")
            .performTextInput("wrong")
        
        composeTestRule.onNodeWithTag("login_button").performClick()
        
        // 验证错误信息
        composeTestRule.onNodeWithTag("error_message")
            .assertIsDisplayed()
            .assertTextEquals("用户名或密码错误")
    }

    @Test
    fun testSuccessfulLogin() {
        // 输入正确的用户名和密码
        composeTestRule.onNodeWithTag("username_field")
            .performTextInput("test")
        
        composeTestRule.onNodeWithTag("password_field")
            .performTextInput("123")
        
        composeTestRule.onNodeWithTag("login_button").performClick()
        
        // 验证跳转到首页
        composeTestRule.onNodeWithTag("home_screen").assertIsDisplayed()
        composeTestRule.onNodeWithTag("home_title").assertIsDisplayed()
        composeTestRule.onNodeWithTag("home_title")
            .assertTextEquals("首页")
    }

    @Test
    fun testErrorMessageClearOnInput() {
        // 先触发错误信息
        composeTestRule.onNodeWithTag("login_button").performClick()
        composeTestRule.onNodeWithTag("error_message").assertIsDisplayed()
        
        // 在用户名字段输入内容
        composeTestRule.onNodeWithTag("username_field")
            .performTextInput("test")
        
        // 验证错误信息消失
        composeTestRule.onNodeWithTag("error_message").assertDoesNotExist()
        
        // 再次触发错误（只有用户名没有密码）
        composeTestRule.onNodeWithTag("login_button").performClick()
        composeTestRule.onNodeWithTag("error_message").assertIsDisplayed()
        
        // 在密码字段输入内容
        composeTestRule.onNodeWithTag("password_field")
            .performTextInput("123")
        
        // 验证错误信息消失
        composeTestRule.onNodeWithTag("error_message").assertDoesNotExist()
    }

    @Test
    fun testFullLoginAndLogoutFlow() {
        // 完整的登录和退出流程测试
        
        // 1. 登录
        composeTestRule.onNodeWithTag("username_field")
            .performTextInput("test")
        
        composeTestRule.onNodeWithTag("password_field")
            .performTextInput("123")
        
        composeTestRule.onNodeWithTag("login_button").performClick()
        
        // 2. 验证首页显示
        composeTestRule.onNodeWithTag("home_screen").assertIsDisplayed()
        
        // 3. 退出登录
        composeTestRule.onNodeWithTag("logout_button").performClick()
        
        // 4. 验证返回登录页面
        composeTestRule.onNodeWithTag("login_screen").assertIsDisplayed()
        composeTestRule.onNodeWithTag("login_title").assertIsDisplayed()
    }

    @Test
    fun testInputFieldsAcceptText() {
        // 测试输入字段能正确接受文本输入
        val testUsername = "testuser"
        val testPassword = "testpass"
        
        // 输入用户名
        composeTestRule.onNodeWithTag("username_field")
            .performTextInput(testUsername)
        
        // 输入密码
        composeTestRule.onNodeWithTag("password_field")
            .performTextInput(testPassword)
        
        // 验证用户名字段包含输入的文本（由于OutlinedTextField的特性，我们检查EditableText部分）
        composeTestRule.onNodeWithTag("username_field")
            .assert(hasText(testUsername, substring = true))
        
        // 注意：密码字段因为PasswordVisualTransformation不能直接验证显示的文本
        // 但我们可以验证它确实接受了输入（通过后续的登录测试来间接验证）
    }

    @Test
    fun testLoginButtonClickable() {
        // 验证登录按钮可点击
        composeTestRule.onNodeWithTag("login_button")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()
        
        // 验证点击后有反应（显示错误信息）
        composeTestRule.onNodeWithTag("error_message").assertIsDisplayed()
    }

    @Test
    fun testMultipleInvalidLoginAttempts() {
        // 测试多次无效登录尝试
        
        // 第一次尝试
        composeTestRule.onNodeWithTag("username_field")
            .performTextInput("wrong1")
        composeTestRule.onNodeWithTag("password_field")
            .performTextInput("wrong1")
        composeTestRule.onNodeWithTag("login_button").performClick()
        composeTestRule.onNodeWithTag("error_message").assertIsDisplayed()
        
        // 清空字段
        composeTestRule.onNodeWithTag("username_field").performTextClearance()
        composeTestRule.onNodeWithTag("password_field").performTextClearance()
        
        // 第二次尝试
        composeTestRule.onNodeWithTag("username_field")
            .performTextInput("wrong2")
        composeTestRule.onNodeWithTag("password_field")
            .performTextInput("wrong2")
        composeTestRule.onNodeWithTag("login_button").performClick()
        composeTestRule.onNodeWithTag("error_message").assertIsDisplayed()
        
        // 清空字段
        composeTestRule.onNodeWithTag("username_field").performTextClearance()
        composeTestRule.onNodeWithTag("password_field").performTextClearance()
        
        // 第三次尝试 - 正确凭据
        composeTestRule.onNodeWithTag("username_field")
            .performTextInput("test")
        composeTestRule.onNodeWithTag("password_field")
            .performTextInput("123")
        composeTestRule.onNodeWithTag("login_button").performClick()
        
        // 验证成功登录
        composeTestRule.onNodeWithTag("home_screen").assertIsDisplayed()
    }
} 