/*
 * MainRepository.kt - 主页面数据仓库接口
 * 
 * 🎯 作用：定义主页面数据获取的抽象接口
 * 📱 模块：core-data - 数据层
 * 🔗 功能：获取 Pokemon 列表数据的规范
 */

package com.skydoves.pokedex.core.repository

import androidx.annotation.WorkerThread
import com.skydoves.pokedex.core.model.Pokemon
import kotlinx.coroutines.flow.Flow

// 🔥 Repository 接口详解
// 📌 作用：定义数据获取的抽象方法
// 📌 设计模式：Repository 模式，分离数据层和业务层
// 📌 好处：便于测试和维护，可以轻松切换数据源
interface MainRepository {

  // 🔥 @WorkerThread 注解详解
  // 📌 作用：标记这个方法应该在工作线程中执行
  // 📌 原因：可能包含网络请求和数据库操作
  // 📌 好处：提醒开发者不要在主线程中调用
  @WorkerThread
  fun fetchPokemonList(
    // 🔥 方法参数详解
    page: Int,                    // 📌 页码：用于分页加载
    onStart: () -> Unit,          // 📌 开始回调：显示加载状态
    onComplete: () -> Unit,       // 📌 完成回调：隐藏加载状态
    onError: (String?) -> Unit,   // 📌 错误回调：显示错误信息
  ): Flow<List<Pokemon>>          // 📌 返回值：Pokemon 列表的 Flow
  // 💡 用法：ViewModel 调用此方法获取 Pokemon 数据
}
