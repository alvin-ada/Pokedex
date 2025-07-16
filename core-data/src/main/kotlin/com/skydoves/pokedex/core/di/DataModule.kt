/*
 * DataModule.kt - 数据层模块的依赖注入配置
 * 
 * 🎯 作用：配置 Repository 相关的依赖注入
 * 📱 模块：core-data - 数据层
 * 🔗 功能：将 Repository 接口绑定到具体实现
 */

package com.skydoves.pokedex.core.di

import com.skydoves.pokedex.core.repository.DetailRepository
import com.skydoves.pokedex.core.repository.DetailRepositoryImpl
import com.skydoves.pokedex.core.repository.MainRepository
import com.skydoves.pokedex.core.repository.MainRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

// 🔥 数据层 Hilt 模块
// 📌 @Module：标记这是一个 Hilt 模块
// 📌 @InstallIn：安装到应用级别的单例组件
// 📌 interface：抽象模块，用于绑定接口和实现
@Module
@InstallIn(SingletonComponent::class)
internal interface DataModule {

  // 🔥 @Binds 注解详解
  // 📌 作用：绑定接口到具体实现
  // 📌 用途：告诉 Hilt 当需要 MainRepository 时，提供 MainRepositoryImpl
  // 📌 好处：支持接口编程，便于测试和维护
  @Binds
  fun bindsMainRepository(mainRepositoryImpl: MainRepositoryImpl): MainRepository
  // 💡 用法：在 ViewModel 中注入 MainRepository，实际得到 MainRepositoryImpl

  // 🔥 绑定详情页面 Repository
  // 📌 作用：绑定详情页面的数据仓库接口和实现
  // 📌 单例：整个应用共享一个实例
  @Binds
  fun bindsDetailRepository(detailRepositoryImpl: DetailRepositoryImpl): DetailRepository
  // 💡 用法：在详情页面 ViewModel 中注入 DetailRepository
}
