package com.example.login

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 首页功能的Espresso自动化测试
 * 测试计数器和退出登录功能
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class HomeScreenEspressoTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    /**
     * 辅助方法：执行登录操作
     */
    private fun performLogin() {
        composeTestRule.onNodeWithTag("username_field")
            .performTextInput("test")
        
        composeTestRule.onNodeWithTag("password_field")
            .performTextInput("123")
        
        composeTestRule.onNodeWithTag("login_button").performClick()
        
        // 等待首页加载
        composeTestRule.onNodeWithTag("home_screen").assertIsDisplayed()
    }

    @Test
    fun testHomeScreenElementsDisplayed() {
        // 先登录
        performLogin()
        
        // 验证首页所有元素都显示
        composeTestRule.onNodeWithTag("home_screen").assertIsDisplayed()
        composeTestRule.onNodeWithTag("home_title").assertIsDisplayed()
        composeTestRule.onNodeWithTag("click_count_text").assertIsDisplayed()
        composeTestRule.onNodeWithTag("click_button").assertIsDisplayed()
        composeTestRule.onNodeWithTag("reset_button").assertIsDisplayed()
        composeTestRule.onNodeWithTag("logout_button").assertIsDisplayed()
        
        // 验证标题文本
        composeTestRule.onNodeWithTag("home_title")
            .assertTextEquals("首页")
        
        // 验证初始计数
        composeTestRule.onNodeWithTag("click_count_text")
            .assertTextEquals("点击次数: 0")
    }

    @Test
    fun testClickCounterIncrement() {
        // 先登录
        performLogin()
        
        // 验证初始计数为0
        composeTestRule.onNodeWithTag("click_count_text")
            .assertTextEquals("点击次数: 0")
        
        // 点击一次
        composeTestRule.onNodeWithTag("click_button").performClick()
        composeTestRule.onNodeWithTag("click_count_text")
            .assertTextEquals("点击次数: 1")
        
        // 点击第二次
        composeTestRule.onNodeWithTag("click_button").performClick()
        composeTestRule.onNodeWithTag("click_count_text")
            .assertTextEquals("点击次数: 2")
        
        // 点击第三次
        composeTestRule.onNodeWithTag("click_button").performClick()
        composeTestRule.onNodeWithTag("click_count_text")
            .assertTextEquals("点击次数: 3")
    }

    @Test
    fun testResetCounterFunction() {
        // 先登录
        performLogin()
        
        // 先增加一些计数
        repeat(5) {
            composeTestRule.onNodeWithTag("click_button").performClick()
        }
        
        // 验证计数为5
        composeTestRule.onNodeWithTag("click_count_text")
            .assertTextEquals("点击次数: 5")
        
        // 点击重置按钮
        composeTestRule.onNodeWithTag("reset_button").performClick()
        
        // 验证计数重置为0
        composeTestRule.onNodeWithTag("click_count_text")
            .assertTextEquals("点击次数: 0")
    }

    @Test
    fun testMultipleClicksAndReset() {
        // 先登录
        performLogin()
        
        // 测试多次点击和重置的循环
        for (round in 1..3) {
            // 点击多次
            repeat(round * 2) {
                composeTestRule.onNodeWithTag("click_button").performClick()
            }
            
            // 验证计数
            composeTestRule.onNodeWithTag("click_count_text")
                .assertTextEquals("点击次数: ${round * 2}")
            
            // 重置
            composeTestRule.onNodeWithTag("reset_button").performClick()
            
            // 验证重置后为0
            composeTestRule.onNodeWithTag("click_count_text")
                .assertTextEquals("点击次数: 0")
        }
    }

    @Test
    fun testLogoutFunctionality() {
        // 先登录
        performLogin()
        
        // 验证在首页
        composeTestRule.onNodeWithTag("home_screen").assertIsDisplayed()
        
        // 点击退出登录
        composeTestRule.onNodeWithTag("logout_button").performClick()
        
        // 验证回到登录页面
        composeTestRule.onNodeWithTag("login_screen").assertIsDisplayed()
        composeTestRule.onNodeWithTag("login_title").assertIsDisplayed()
    }

    @Test
    fun testLogoutResetsState() {
        // 先登录
        performLogin()
        
        // 增加一些计数
        repeat(7) {
            composeTestRule.onNodeWithTag("click_button").performClick()
        }
        
        // 验证计数
        composeTestRule.onNodeWithTag("click_count_text")
            .assertTextEquals("点击次数: 7")
        
        // 退出登录
        composeTestRule.onNodeWithTag("logout_button").performClick()
        
        // 重新登录
        performLogin()
        
        // 验证计数重置为0（状态已重置）
        composeTestRule.onNodeWithTag("click_count_text")
            .assertTextEquals("点击次数: 0")
    }

    @Test
    fun testButtonsAreClickable() {
        // 先登录
        performLogin()
        
        // 验证所有按钮都可点击
        composeTestRule.onNodeWithTag("click_button")
            .assertIsDisplayed()
            .assertHasClickAction()
        
        composeTestRule.onNodeWithTag("reset_button")
            .assertIsDisplayed()
            .assertHasClickAction()
        
        composeTestRule.onNodeWithTag("logout_button")
            .assertIsDisplayed()
            .assertHasClickAction()
    }

    @Test
    fun testRapidClicking() {
        // 先登录
        performLogin()
        
        // 快速连续点击
        val clickCount = 10
        repeat(clickCount) {
            composeTestRule.onNodeWithTag("click_button").performClick()
        }
        
        // 验证计数正确
        composeTestRule.onNodeWithTag("click_count_text")
            .assertTextEquals("点击次数: $clickCount")
        
        // 快速重置
        composeTestRule.onNodeWithTag("reset_button").performClick()
        
        // 验证重置成功
        composeTestRule.onNodeWithTag("click_count_text")
            .assertTextEquals("点击次数: 0")
    }

    @Test
    fun testLargeNumberClicks() {
        // 先登录
        performLogin()
        
        // 测试大量点击
        val largeClickCount = 50
        repeat(largeClickCount) {
            composeTestRule.onNodeWithTag("click_button").performClick()
        }
        
        // 验证大数值显示正确
        composeTestRule.onNodeWithTag("click_count_text")
            .assertTextEquals("点击次数: $largeClickCount")
        
        // 重置
        composeTestRule.onNodeWithTag("reset_button").performClick()
        
        // 验证重置成功
        composeTestRule.onNodeWithTag("click_count_text")
            .assertTextEquals("点击次数: 0")
    }

    @Test
    fun testButtonTextContent() {
        // 先登录
        performLogin()
        
        // 验证按钮文本内容
        composeTestRule.onNodeWithTag("click_button")
            .assertTextContains("点击")
        
        composeTestRule.onNodeWithTag("reset_button")
            .assertTextContains("重置")
        
        composeTestRule.onNodeWithTag("logout_button")
            .assertTextContains("退出登录")
    }
} 