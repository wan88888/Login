package com.example.login

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 端到端完整流程的Espresso自动化测试
 * 测试用户完整的使用场景
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class EndToEndEspressoTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testCompleteUserJourney() {
        // 完整的用户使用流程测试
        
        // 1. 应用启动，显示登录页面
        composeTestRule.onNodeWithTag("login_screen").assertIsDisplayed()
        composeTestRule.onNodeWithTag("login_title")
            .assertTextEquals("欢迎登录")
        
        // 2. 尝试空字段登录
        composeTestRule.onNodeWithTag("login_button").performClick()
        composeTestRule.onNodeWithTag("error_message")
            .assertTextEquals("用户名和密码不能为空")
        
        // 3. 尝试错误凭据
        composeTestRule.onNodeWithTag("username_field")
            .performTextInput("wronguser")
        composeTestRule.onNodeWithTag("password_field")
            .performTextInput("wrongpass")
        composeTestRule.onNodeWithTag("login_button").performClick()
        composeTestRule.onNodeWithTag("error_message")
            .assertTextEquals("用户名或密码错误")
        
        // 4. 清空字段并输入正确凭据
        composeTestRule.onNodeWithTag("username_field").performTextClearance()
        composeTestRule.onNodeWithTag("password_field").performTextClearance()
        
        composeTestRule.onNodeWithTag("username_field")
            .performTextInput("test")
        composeTestRule.onNodeWithTag("password_field")
            .performTextInput("123")
        composeTestRule.onNodeWithTag("login_button").performClick()
        
        // 5. 成功登录，进入首页
        composeTestRule.onNodeWithTag("home_screen").assertIsDisplayed()
        composeTestRule.onNodeWithTag("home_title")
            .assertTextEquals("首页")
        composeTestRule.onNodeWithTag("click_count_text")
            .assertTextEquals("点击次数: 0")
        
        // 6. 使用计数功能
        repeat(3) {
            composeTestRule.onNodeWithTag("click_button").performClick()
        }
        composeTestRule.onNodeWithTag("click_count_text")
            .assertTextEquals("点击次数: 3")
        
        // 7. 重置计数
        composeTestRule.onNodeWithTag("reset_button").performClick()
        composeTestRule.onNodeWithTag("click_count_text")
            .assertTextEquals("点击次数: 0")
        
        // 8. 再次使用计数功能
        repeat(5) {
            composeTestRule.onNodeWithTag("click_button").performClick()
        }
        composeTestRule.onNodeWithTag("click_count_text")
            .assertTextEquals("点击次数: 5")
        
        // 9. 退出登录
        composeTestRule.onNodeWithTag("logout_button").performClick()
        
        // 10. 回到登录页面
        composeTestRule.onNodeWithTag("login_screen").assertIsDisplayed()
        composeTestRule.onNodeWithTag("login_title").assertIsDisplayed()
    }

    @Test
    fun testMultipleLoginLogoutCycles() {
        // 测试多次登录退出循环
        
        repeat(3) { cycle ->
            // 登录
            composeTestRule.onNodeWithTag("username_field")
                .performTextInput("test")
            composeTestRule.onNodeWithTag("password_field")
                .performTextInput("123")
            composeTestRule.onNodeWithTag("login_button").performClick()
            
            // 验证首页
            composeTestRule.onNodeWithTag("home_screen").assertIsDisplayed()
            
            // 进行一些操作
            repeat(cycle + 1) {
                composeTestRule.onNodeWithTag("click_button").performClick()
            }
            
            // 验证计数
            composeTestRule.onNodeWithTag("click_count_text")
                .assertTextEquals("点击次数: ${cycle + 1}")
            
            // 退出登录
            composeTestRule.onNodeWithTag("logout_button").performClick()
            
            // 验证回到登录页面
            composeTestRule.onNodeWithTag("login_screen").assertIsDisplayed()
            
            // 清空字段准备下一轮
            if (cycle < 2) { // 不是最后一轮
                composeTestRule.onNodeWithTag("username_field").performTextClearance()
                composeTestRule.onNodeWithTag("password_field").performTextClearance()
            }
        }
    }

    @Test
    fun testErrorRecoveryScenarios() {
        // 测试各种错误恢复场景
        
        // 场景1：输入错误后修正
        composeTestRule.onNodeWithTag("username_field")
            .performTextInput("wrong")
        composeTestRule.onNodeWithTag("password_field")
            .performTextInput("wrong")
        composeTestRule.onNodeWithTag("login_button").performClick()
        
        // 显示错误
        composeTestRule.onNodeWithTag("error_message").assertIsDisplayed()
        
        // 修正用户名
        composeTestRule.onNodeWithTag("username_field").performTextClearance()
        composeTestRule.onNodeWithTag("username_field")
            .performTextInput("test")
        
        // 错误消失
        composeTestRule.onNodeWithTag("error_message").assertDoesNotExist()
        
        // 修正密码
        composeTestRule.onNodeWithTag("password_field").performTextClearance()
        composeTestRule.onNodeWithTag("password_field")
            .performTextInput("123")
        
        // 成功登录
        composeTestRule.onNodeWithTag("login_button").performClick()
        composeTestRule.onNodeWithTag("home_screen").assertIsDisplayed()
        
        // 场景2：在首页进行操作后退出再重新登录
        repeat(10) {
            composeTestRule.onNodeWithTag("click_button").performClick()
        }
        
        composeTestRule.onNodeWithTag("logout_button").performClick()
        
        // 重新登录
        composeTestRule.onNodeWithTag("username_field")
            .performTextInput("test")
        composeTestRule.onNodeWithTag("password_field")
            .performTextInput("123")
        composeTestRule.onNodeWithTag("login_button").performClick()
        
        // 验证状态已重置
        composeTestRule.onNodeWithTag("click_count_text")
            .assertTextEquals("点击次数: 0")
    }

    @Test
    fun testUserInputValidation() {
        // 测试各种输入验证场景
        
        val testCases = listOf(
            Triple("", "", "用户名和密码不能为空"),
            Triple("test", "", "密码不能为空"),
            Triple("", "123", "用户名不能为空"),
            Triple("wrong", "wrong", "用户名或密码错误"),
            Triple("Test", "123", "用户名或密码错误"), // 大小写敏感
            Triple("test", "Wrong", "用户名或密码错误") // 大小写敏感
        )
        
        testCases.forEach { (username, password, expectedError) ->
            // 清空字段
            if (username.isNotEmpty() || password.isNotEmpty()) {
                composeTestRule.onNodeWithTag("username_field").performTextClearance()
                composeTestRule.onNodeWithTag("password_field").performTextClearance()
            }
            
            // 输入测试数据
            if (username.isNotEmpty()) {
                composeTestRule.onNodeWithTag("username_field")
                    .performTextInput(username)
            }
            if (password.isNotEmpty()) {
                composeTestRule.onNodeWithTag("password_field")
                    .performTextInput(password)
            }
            
            // 点击登录
            composeTestRule.onNodeWithTag("login_button").performClick()
            
            // 验证错误信息
            composeTestRule.onNodeWithTag("error_message")
                .assertIsDisplayed()
                .assertTextEquals(expectedError)
        }
        
        // 最后测试成功登录
        composeTestRule.onNodeWithTag("username_field").performTextClearance()
        composeTestRule.onNodeWithTag("password_field").performTextClearance()
        
        composeTestRule.onNodeWithTag("username_field")
            .performTextInput("test")
        composeTestRule.onNodeWithTag("password_field")
            .performTextInput("123")
        composeTestRule.onNodeWithTag("login_button").performClick()
        
        // 验证成功登录
        composeTestRule.onNodeWithTag("home_screen").assertIsDisplayed()
    }

    @Test
    fun testCounterFunctionalityExtensive() {
        // 先登录
        composeTestRule.onNodeWithTag("username_field")
            .performTextInput("test")
        composeTestRule.onNodeWithTag("password_field")
            .performTextInput("123")
        composeTestRule.onNodeWithTag("login_button").performClick()
        
        // 验证首页
        composeTestRule.onNodeWithTag("home_screen").assertIsDisplayed()
        
        // 测试各种计数场景
        val testScenarios = listOf(1, 5, 10, 15)
        
        testScenarios.forEach { targetCount ->
            // 重置计数器
            composeTestRule.onNodeWithTag("reset_button").performClick()
            composeTestRule.onNodeWithTag("click_count_text")
                .assertTextEquals("点击次数: 0")
            
            // 点击指定次数
            repeat(targetCount) {
                composeTestRule.onNodeWithTag("click_button").performClick()
            }
            
            // 验证计数
            composeTestRule.onNodeWithTag("click_count_text")
                .assertTextEquals("点击次数: $targetCount")
        }
        
        // 测试连续重置
        repeat(3) {
            composeTestRule.onNodeWithTag("reset_button").performClick()
            composeTestRule.onNodeWithTag("click_count_text")
                .assertTextEquals("点击次数: 0")
        }
    }

    @Test
    fun testApplicationStateConsistency() {
        // 测试应用状态一致性
        
        // 1. 登录
        composeTestRule.onNodeWithTag("username_field")
            .performTextInput("test")
        composeTestRule.onNodeWithTag("password_field")
            .performTextInput("123")
        composeTestRule.onNodeWithTag("login_button").performClick()
        
        // 2. 在首页进行操作
        val clickCount = 7
        repeat(clickCount) {
            composeTestRule.onNodeWithTag("click_button").performClick()
        }
        
        // 3. 验证状态
        composeTestRule.onNodeWithTag("click_count_text")
            .assertTextEquals("点击次数: $clickCount")
        
        // 4. 退出登录（这应该重置所有状态）
        composeTestRule.onNodeWithTag("logout_button").performClick()
        
        // 5. 重新登录
        composeTestRule.onNodeWithTag("username_field")
            .performTextInput("test")
        composeTestRule.onNodeWithTag("password_field")
            .performTextInput("123")
        composeTestRule.onNodeWithTag("login_button").performClick()
        
        // 6. 验证状态已完全重置
        composeTestRule.onNodeWithTag("click_count_text")
            .assertTextEquals("点击次数: 0")
        
        // 7. 验证功能仍然正常工作
        composeTestRule.onNodeWithTag("click_button").performClick()
        composeTestRule.onNodeWithTag("click_count_text")
            .assertTextEquals("点击次数: 1")
        
        composeTestRule.onNodeWithTag("reset_button").performClick()
        composeTestRule.onNodeWithTag("click_count_text")
            .assertTextEquals("点击次数: 0")
    }
} 