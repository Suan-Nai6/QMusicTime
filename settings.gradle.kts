/*
 * 项目昵称：QMusicTime
 * 项目作者：酸奶
 * 模块昵称：QMusicTime
 * 最后修改时间：2023/8/24 下午12:51
 * Copyright© (c) 2017-2023 酸奶 https://github.com/Suan-Nai6
 */

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "QMusicTime"
include(":app")
