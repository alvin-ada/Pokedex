/*
 * PokedexAppDispatchers.kt - 应用协程调度器
 * 
 * 🎯 作用：定义应用中使用的协程调度器类型
 * 📱 模块：core-network - 网络层
 * 🔗 功能：为依赖注入提供调度器标识
 */

package com.skydoves.pokedex.core.network

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

// 🔥 依赖注入限定符
// 📌 @Qualifier：Hilt 限定符，用于区分不同的依赖
// 📌 @Retention：运行时保留注解信息
// 📌 作用：在注入时指定需要哪种调度器
@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val pokedexAppDispatchers: PokedexAppDispatchers)

// 🔥 调度器类型枚举
// 📌 作用：定义应用中使用的协程调度器类型
// 📌 当前：只定义了 IO 调度器
// 📌 扩展：可以添加更多调度器类型（如 MAIN、DEFAULT）
enum class PokedexAppDispatchers {
  IO,  // 📌 IO 调度器：用于网络请求和数据库操作
}
// 💡 用法：@Dispatcher(PokedexAppDispatchers.IO) 注入IO调度器
