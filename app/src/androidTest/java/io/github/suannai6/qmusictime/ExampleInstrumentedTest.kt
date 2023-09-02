/*
 * 项目昵称：QMusicTime
 * 项目作者：酸奶
 * 模块昵称：QMusicTime.app.androidTest
 * 最后修改时间：2023/8/24 下午12:42
 * Copyright© (c) 2017-2023 酸奶 https://github.com/Suan-Nai6
 */

package io.github.suannai6.qmusictime

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

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
        assertEquals("io.github.suannai6.qmusictime", appContext.packageName)
    }
}