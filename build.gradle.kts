/*
 * 项目昵称：QMusicTime
 * 项目作者：酸奶
 * 模块昵称：QMusicTime
 * 最后修改时间：2023/8/30 下午9:13
 * Copyright© (c) 2017-2023 酸奶 https://github.com/Suan-Nai6
 */

// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
}
true // Needed to make the Suppress annotation work for the plugins block

buildscript {
    dependencies {
        // https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp
    }
}