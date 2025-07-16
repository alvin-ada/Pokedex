/*
 * Constant.kt - 基准测试常量定义
 * 
 * 🎯 作用：定义基准测试中使用的常量
 * 📱 模块：benchmark - 性能测试模块
 * 🔗 功能：提供测试目标应用的包名等常量
 */

package com.github.skydoves.benchmark

// 🔥 应用包名常量
// 📌 internal：模块内部可见
// 📌 const：编译时常量
// 📌 作用：指定要测试的目标应用包名
internal const val PACKAGE_NAME: String = "com.skydoves.pokedex"

// 💡 用法：在基准测试中用于指定测试目标应用
// 📌 启动测试：measureRepeated(packageName = PACKAGE_NAME, ...)
// 📌 配置文件：baselineProfileRule.collect(packageName = PACKAGE_NAME, ...)
// 📌 UI 测试：By.res(PACKAGE_NAME, "resourceId")
