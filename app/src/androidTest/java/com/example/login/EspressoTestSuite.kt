package com.example.login

import org.junit.runner.RunWith
import org.junit.runners.Suite

/**
 * Espresso自动化测试套件
 * 包含所有的UI自动化测试类
 */
@RunWith(Suite::class)
@Suite.SuiteClasses(
    LoginEspressoTest::class,
    HomeScreenEspressoTest::class,
    EndToEndEspressoTest::class
)
class EspressoTestSuite 