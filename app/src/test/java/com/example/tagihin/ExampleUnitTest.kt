package com.example.tagihin

import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil
import kotlin.math.roundToInt


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun dateTest(){
        assertEquals(floatArrayOf(100.toFloat(),65.toFloat(),35.toFloat()), floatArrayOf(100.toFloat(),35.toFloat(),65.toFloat()).sortedArrayDescending())
    }
}
