package com.example.tagihin

import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*


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
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val outputFormat = SimpleDateFormat("dd/MM/yyyy")
        val date: Date = inputFormat.parse("2019-11-28T04:00:00.000+07:00")
        val formattedDate: String = outputFormat.format(date)

        assertEquals(formattedDate,"28/11/2019")
    }
}
