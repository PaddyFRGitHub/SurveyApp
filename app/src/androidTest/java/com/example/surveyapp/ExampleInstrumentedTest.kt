package com.example.surveyapp

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.surveyapp.Model.DataBaseHelper
import com.example.surveyapp.Model.User
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.surveyapp", appContext.packageName)
    }

    @Test
    fun register(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val dbhelper = DataBaseHelper(appContext)
        val input = User(0,"TestUser","TestPassword",1)
        val expected = User(15,"TestUser","TestPassword",1)
        dbhelper.addUser(input)
        assertEquals(expected,dbhelper.getUser(input.userName))
    }

}