package com.example.login

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite

/**
 * 登录应用测试套件
 * 包含所有的单元测试类
 */
@ExperimentalCoroutinesApi
@RunWith(Suite::class)
@Suite.SuiteClasses(
    LoginViewModelTest::class,
    LoginValidationTest::class,
    CounterTest::class
)
class LoginTestSuite 