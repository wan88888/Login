package com.example.login

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CounterTest {
    
    /**
     * 计数器逻辑的辅助类
     */
    class Counter {
        private var count = 0
        
        fun increment(): Int {
            return ++count
        }
        
        fun reset(): Int {
            count = 0
            return count
        }
        
        fun getCount(): Int = count
        
        fun setCount(value: Int) {
            count = value
        }
    }
    
    private lateinit var counter: Counter
    
    @Before
    fun setup() {
        counter = Counter()
    }
    
    @Test
    fun `初始计数应该为0`() {
        assertEquals(0, counter.getCount())
    }
    
    @Test
    fun `单次增加计数`() {
        val result = counter.increment()
        assertEquals(1, result)
        assertEquals(1, counter.getCount())
    }
    
    @Test
    fun `多次增加计数`() {
        counter.increment()
        counter.increment()
        counter.increment()
        assertEquals(3, counter.getCount())
    }
    
    @Test
    fun `重置计数应该返回0`() {
        // 先增加一些计数
        counter.increment()
        counter.increment()
        counter.increment()
        assertEquals(3, counter.getCount())
        
        // 重置
        val result = counter.reset()
        assertEquals(0, result)
        assertEquals(0, counter.getCount())
    }
    
    @Test
    fun `重置后再次增加计数`() {
        // 增加计数
        counter.increment()
        counter.increment()
        assertEquals(2, counter.getCount())
        
        // 重置
        counter.reset()
        assertEquals(0, counter.getCount())
        
        // 再次增加
        counter.increment()
        assertEquals(1, counter.getCount())
    }
    
    @Test
    fun `连续增加大量计数`() {
        val targetCount = 1000
        for (i in 1..targetCount) {
            counter.increment()
        }
        assertEquals(targetCount, counter.getCount())
    }
    
    @Test
    fun `多次重置应该保持为0`() {
        counter.increment()
        counter.increment()
        
        counter.reset()
        assertEquals(0, counter.getCount())
        
        counter.reset()
        assertEquals(0, counter.getCount())
        
        counter.reset()
        assertEquals(0, counter.getCount())
    }
    
    @Test
    fun `增加计数返回值应该等于当前计数`() {
        assertEquals(1, counter.increment())
        assertEquals(2, counter.increment())
        assertEquals(3, counter.increment())
    }
    
    @Test
    fun `设置特定计数值`() {
        counter.setCount(50)
        assertEquals(50, counter.getCount())
        
        // 从设定值继续增加
        counter.increment()
        assertEquals(51, counter.getCount())
    }
    
    @Test
    fun `设置负数计数值`() {
        counter.setCount(-5)
        assertEquals(-5, counter.getCount())
        
        // 从负数增加
        counter.increment()
        assertEquals(-4, counter.getCount())
    }
} 